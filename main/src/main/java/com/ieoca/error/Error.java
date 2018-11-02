package com.ieoca.error;

public enum Error {
  FILE_NOT_EXIST(100, "File does not exist"),
  CERTIFICATE_CREATION(500, "Could not create certificate."),
  CERTIFICATE_NOT_FOUND(501, "Certificate file not found."),
  CERTIFICATE_NOT_ACCESSIBLE(502, "Cannot read certificate."),
  CERTIFICATE_NOT_LOADED(
      503, "No certificate is loaded! It's highly recommended to usinge a certificate."),
  COMPONENT_INVALID(504, "Cannot add invalid component.");

  private final int code;

  private final String description;

  private Error(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return String.format("%d: %s", code, description);
  }
}
