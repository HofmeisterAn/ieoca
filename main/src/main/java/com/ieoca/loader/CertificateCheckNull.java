package com.ieoca.loader;

import java.io.File;

public class CertificateCheckNull implements ICertificateCheck {
  @Override
  public final boolean validate(File file) {
    return true;
  }
}
