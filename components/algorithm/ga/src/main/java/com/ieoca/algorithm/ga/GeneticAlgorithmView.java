package com.ieoca.algorithm.ga;

public class GeneticAlgorithmView extends GAView {
  public GeneticAlgorithmView() {
    this.setMenuItemName("Genetic Algorithm");
    this.findProperties();
  }

  @Override
  protected void update(String name, boolean value) {}

  @Override
  protected void update(String name, int value) {}

  @Override
  protected void update(String name, String value) {}
}
