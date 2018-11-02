package com.ieoca.mvc;

public enum Action {
  ADD_PROBLEM("ADDPROBLEM"),
  ADD_ALGORITHM("ADDALGORITHM"),
  REMOVE_PROBLEM("REMOVEPROBLEM"),
  REMOVE_ALGORITHM("REMOVEALGORITHM"),
  IS_ENABLE("ENABLESTARTBUTTON");

  private final String id;

  private Action(final String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return this.id;
  }
}
