package com.ieoca.algorithm.ga;

import com.ieoca.components.algorithm.Algorithm;
import com.ieoca.components.algorithm.AlgorithmView;

abstract class GA extends Algorithm<AlgorithmView> {

  int cities;

  int[][] distances;

  private final GAView gaView;

  GA(AlgorithmView algorithmView) {
    super(algorithmView);

    this.gaView = (GAView) algorithmView;
  }

  int getIteration() {
    return 1000;
  }

  protected int[] bestTrail() {
    return new int[this.cities];
  }
}
