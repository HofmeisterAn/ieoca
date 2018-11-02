package com.ieoca.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CertificateCheckX509 implements ICertificateCheck {
  private final Certificate certificate;
  private final ArrayList<JarEntry> entries = new ArrayList<>();

  public CertificateCheckX509(Certificate certificate) {
    this.certificate = certificate;
  }

  @Override
  public final boolean validate(File file) {
    if (file == null) return false;

    if (!file.exists()) return false;

    this.entries.clear();

    return this.verifySignatures(file) && this.verifyCertification();
  }

  private boolean verifySignatures(File file) {
    if (file == null) {
      throw new NullPointerException("file");
    }

    if (!file.exists()) {
      throw new IllegalArgumentException("File does not exist.");
    }

    byte[] buffer = new byte[4096];

    JarFile jarFile = null;

    InputStream is = null;

    Enumeration enumeration = null;

    try {
      jarFile = new JarFile(file);

      if (jarFile.getManifest() == null) return false;

      enumeration = jarFile.entries();

      while (enumeration.hasMoreElements()) {
        JarEntry jarEntry = (JarEntry) enumeration.nextElement();

        if (jarEntry.isDirectory()) continue;

        is = jarFile.getInputStream(jarEntry);

        while (is.read(buffer, 0, buffer.length) != -1) {}

        is.close();

        this.entries.add(jarEntry);
      }
    } catch (SecurityException | IOException e) {
      return false;
    } finally {
      try {
        if (jarFile != null) {
          jarFile.close();
        }

        if (is != null) {
          is.close();
        }
      } catch (IOException ignored) {
      }
    }
    return true;
  }

  private boolean verifyCertification() {
    if (this.entries.size() == 0) return false;

    if (this.certificate == null) return false;

    Certificate[] certificates;
    Certificate[] certificatesChain;

    for (JarEntry je : entries) {
      certificates = je.getCertificates();

      if ((certificates == null) || (certificates.length == 0)) {
        if (!je.getName().startsWith("META-INF")) {
          return false;
        }
      } else {
        int index = 0;

        while ((certificatesChain = this.getChain(certificates, index)) != null) {
          if (certificatesChain[0].equals(certificate)) {
            return true;
          }
          index += certificatesChain.length;
        }
      }
    }
    return false;
  }

  private Certificate[] getChain(Certificate[] certificates, int index) {
    if ((certificates == null) || (certificates.length == 0)) return null;

    if (index < 0) return null;

    if (index > certificates.length - 1) return null;

    int i = 0;

    for (i = index; i < certificates.length - 1; i++) {
      Principal principal0 = ((X509Certificate) certificates[i]).getIssuerDN();
      Principal principal1 = ((X509Certificate) certificates[i + 1]).getSubjectDN();

      if (!principal0.equals(principal1)) {
        break;
      }
    }

    i -= index;

    X509Certificate[] result = new X509Certificate[i + 1];

    for (; i >= 0; i--) {
      result[i] = (X509Certificate) certificates[index + i];
    }

    return result;
  }
}
