package com.ieoca.algorithm.aco;

import com.ieoca.components.algorithm.AlgorithmView;
import com.ieoca.components.algorithm.property.annotation.IComboBox;
import com.ieoca.components.algorithm.property.annotation.IProperty;
import com.ieoca.components.algorithm.property.annotation.ISlider;
import com.ieoca.components.algorithm.property.annotation.ITextField;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class ACOView extends AlgorithmView {
  private final String ANTS = "Ants";

  private final String ITERATION = "Iteration";

  private final String ALPHA = "Alpha";

  private final String BETA = "Beta";

  private final String Q = "Q";

  private final String RHO = "Rho";

  @IProperty
  @ITextField(name = ANTS)
  private int ants = 10;

  @IProperty
  @ITextField(name = ITERATION)
  private int iteration = 1000;

  @IProperty
  @IComboBox(
    name = ALPHA,
    elements = {"1", "2", "3"}
  )
  private int alpha = 3;

  @IProperty
  @IComboBox(
    name = BETA,
    elements = {"1", "2", "3"}
  )
  private int beta = 2;

  @IProperty
  @ISlider(name = Q, min = 20, max = 30)
  private double q = 2.0;

  @IProperty
  @ISlider(name = RHO, min = 1, max = 10)
  private double rho = 0.01;

  public int getAnts() {
    return this.ants;
  }

  public int getIteration() {
    return this.iteration;
  }

  public int getAlpha() {
    return this.alpha;
  }

  public int getBeta() {
    return this.beta;
  }

  public double getQ() {
    return this.q;
  }

  public double getRho() {
    return this.rho;
  }

  @Override
  protected void update(String name, boolean value) {
    throw new NotImplementedException();
  }

  @Override
  protected void update(String name, String value) {
    if (name.equals(ANTS)) {
      this.ants = Integer.parseInt(value);
    }

    if (name.equals(ITERATION)) {
      this.iteration = Integer.parseInt(value);
    }
  }

  @Override
  protected void update(String name, int value) {
    if (name.equals(ALPHA)) {
      this.alpha = value + 1;
    }

    if (name.equals(BETA)) {
      this.beta = value + 1;
    }

    if (name.equals(Q)) {
      this.q = value / 10;
    }

    if (name.equals(RHO)) {
      this.rho = value / 100;
    }
  }
}
