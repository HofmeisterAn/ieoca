package com.ieoca.algorithm.aco;

import com.ieoca.components.algorithm.Algorithm;
import com.ieoca.components.algorithm.AlgorithmView;
import java.util.Random;

abstract class ACO extends Algorithm<AlgorithmView> {
  int cities;

  int trailLength;

  int[] trail;

  int[][] ants;

  int[][] distances;

  double[][] pheromones;

  private final ACOView acoView;

  private final Random random = new Random();

  protected abstract int distance(int cityX, int cityY, int[][] dists);

  protected abstract double[] moveProbs(int k, int cityX, Boolean[] visited);

  ACO(AlgorithmView algorithmView) {
    super(algorithmView);

    this.acoView = (ACOView) algorithmView;
  }

  /* Optionen */

  int getAnts() {
    return this.acoView.getAnts();
  }

  int getIteration() {
    return this.acoView.getIteration();
  }

  int getAlpha() {
    return this.acoView.getAlpha();
  }

  int getBeta() {
    return this.acoView.getBeta();
  }

  private double getQ() {
    return this.acoView.getQ();
  }

  private double getRho() {
    return this.acoView.getRho();
  }

  /* Methods */

  void updateAnts() {
    for (int k = 0; k < this.ants.length; ++k) {
      int start = random.nextInt(this.cities);
      this.ants[k] = buildTrail(k, start);
    }
  }

  void updatePheromones() {
    for (int i = 0; i < this.pheromones.length; ++i) {
      for (int j = i + 1; j < this.pheromones[i].length; ++j) {
        for (int k = 0; k < this.ants.length; ++k) {
          int length = length(this.ants[k]); // length of ant k trail
          double decrease = (1.0 - this.getRho()) * this.pheromones[i][j];
          double increase = 0.0;
          if (edgeInTrail(i, j, this.ants[k])) increase = (this.getQ() / length);

          this.pheromones[i][j] = decrease + increase;

          if (this.pheromones[i][j] < 0.0001) this.pheromones[i][j] = 0.0001;
          else if (this.pheromones[i][j] > 100000.0) this.pheromones[i][j] = 100000.0;

          this.pheromones[j][i] = this.pheromones[i][j];
        }
      }
    }
  }

  int length(int[] trail) {
    int result = 0;

    for (int i = 0; i < trail.length - 1; ++i) {
      result += this.distance(trail[i], trail[i + 1], this.distances);
    }
    result += this.distance(trail[trail.length - 1], trail[0], this.distances);

    return result;
  }

  private int indexOfTarget(int[] trail, int target) {
    for (int i = 0; i < trail.length; ++i) {
      if (trail[i] == target) return i;
    }

    return 0;
  }

  private int[] randomTrail(int start, int numCities) {
    int[] trail = new int[numCities];
    for (int i = 0; i < numCities; ++i) {
      trail[i] = i;
    }

    for (int i = 0; i < numCities; ++i) {
      int r = random.nextInt(numCities - i) + i;
      int tmp = trail[r];
      trail[r] = trail[i];
      trail[i] = tmp;
    }

    int idx = indexOfTarget(trail, start);
    int temp = trail[0];
    trail[0] = trail[idx];
    trail[idx] = temp;

    return trail;
  }

  private int[] buildTrail(int k, int start) {
    int[] trail = new int[this.cities];
    Boolean[] visited = new Boolean[this.cities];
    for (int i = 0; i < this.cities; i++) {
      visited[i] = false;
    }
    trail[0] = start;
    visited[start] = true;
    for (int i = 0; i < this.cities - 1; ++i) {
      int cityX = trail[i];
      int next = nextCity(k, cityX, visited);
      trail[i + 1] = next;
      visited[next] = true;
    }
    return trail;
  }

  int[] bestTrail() {
    int idxBestLength = 0;

    int bestLength = length(this.ants[0]);

    for (int k = 1; k < this.ants.length; ++k) {
      int len = length(this.ants[k]);
      if (len < bestLength) {
        bestLength = len;
        idxBestLength = k;
      }
    }

    return this.ants[idxBestLength].clone();
  }

  int[][] initAnts(int numAnts, int numCities) {
    int[][] ants = new int[numAnts][];
    for (int k = 0; k < numAnts; ++k) {
      int start = random.nextInt(numCities);
      ants[k] = randomTrail(start, numCities);
    }
    return ants;
  }

  double[][] initPheromones() {
    double[][] pheromones = new double[this.cities][];
    for (int i = 0; i < this.cities; ++i) pheromones[i] = new double[this.cities];
    for (int i = 0; i < pheromones.length; ++i)
      for (int j = 0; j < pheromones[i].length; ++j) pheromones[i][j] = 0.01;
    return pheromones;
  }

  private Boolean edgeInTrail(int cityX, int cityY, int[] trail) {
    int lastIndex = trail.length - 1;
    int idx = indexOfTarget(trail, cityX);

    if (idx == 0 && trail[1] == cityY) return true;
    else if (idx == 0 && trail[lastIndex] == cityY) return true;
    else if (idx == 0) return false;
    else if (idx == lastIndex && trail[lastIndex - 1] == cityY) return true;
    else if (idx == lastIndex && trail[0] == cityY) return true;
    else if (idx == lastIndex) return false;
    else if (trail[idx - 1] == cityY) return true;
    else return trail[idx + 1] == cityY;
  }

  private int nextCity(int k, int cityX, Boolean[] visited) {
    double[] probs = moveProbs(k, cityX, visited);

    double[] cumul = new double[probs.length + 1];
    cumul[0] = 0.0;
    for (int i = 0; i < probs.length; ++i) cumul[i + 1] = cumul[i] + probs[i];

    double p = random.nextDouble();

    for (int i = 0; i < cumul.length - 1; ++i) if (p >= cumul[i] && p < cumul[i + 1]) return i;
    return 0;
  }
}
