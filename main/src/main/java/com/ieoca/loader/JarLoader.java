package com.ieoca.loader;

import com.ieoca.error.Error;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** todo: Split Class-Path result? Check if it is a JAR file? */
public final class JarLoader extends URLClassLoader {
  private final Logger logger = LogManager.getLogger(JarLoader.class.getName());

  private ICertificateCheck certificateCheck = null;

  public JarLoader(URL[] urls) {
    super(urls, JarLoader.getSystemClassLoader());

    this.certificateCheck = new CertificateCheckNull();
  }

  public void setupCertificate() {
    File file = new File("dhbw-mosbach.cer");

    try {
      this.certificateCheck = this.loadX509Certificate(file);
    } catch (CertificateException e) {
      this.logger.warn(com.ieoca.error.Error.CERTIFICATE_NOT_LOADED);
      this.certificateCheck = new CertificateCheckNull();
    }
  }

  public String addComponent(File file) throws MalformedURLException, CertificateException {
    if (file == null) {
      throw new NullPointerException("file");
    }

    if (!file.exists()) {
      throw new IllegalArgumentException(Error.FILE_NOT_EXIST.toString());
    }

    String dependency = this.getManifestProperty(file, "Class-Path");

    if (dependency != null) {
      this.addComponent(new File(file.getParent(), dependency));
    }

    if (certificateCheck.validate(file)) {
      super.addURL(file.toURI().toURL());
      return this.getManifestProperty(file, "Class-Name");
    } else {
      throw new CertificateException(Error.COMPONENT_INVALID.toString());
    }
  }

  private ICertificateCheck loadX509Certificate(File file) throws CertificateException {
    if (file == null) {
      throw new NullPointerException("file");
    }

    if (!file.exists()) {
      throw new IllegalArgumentException(Error.FILE_NOT_EXIST.toString());
    }

    InputStream is = null;

    try {
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      is = new FileInputStream(file);
      return new CertificateCheckX509(cf.generateCertificate(is));
    } catch (CertificateException e) {
      throw new CertificateException(Error.CERTIFICATE_CREATION.toString(), e);
    } catch (FileNotFoundException e) {
      throw new CertificateException(Error.CERTIFICATE_NOT_FOUND.toString(), e);
    } catch (SecurityException e) {
      throw new CertificateException(Error.CERTIFICATE_NOT_ACCESSIBLE.toString(), e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException ignored) {
        }
      }
    }
  }

  private String getManifestProperty(File file, String name) {
    if (file == null) {
      throw new NullPointerException("file");
    }

    if (!file.exists()) {
      throw new IllegalArgumentException("File does not exist.");
    }

    String property = null;
    try (JarFile jarFile = new JarFile(file)) {

      Manifest manifest = jarFile.getManifest();
      if (manifest != null) {
        property = manifest.getMainAttributes().getValue(name);
      }
      return property;
    } catch (IOException e) {
      return property;
    }
  }
}
