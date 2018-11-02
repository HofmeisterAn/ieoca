package com.ieoca.algorithm.aco;

import com.ieoca.components.problem.Problem;
import com.ieoca.problem.TSP;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AntColonyOptimization extends ACO {
  private final Logger logger = LogManager.getLogger(AntColonyOptimization.class);

  private TSP tsp;

  private AntColonyOptimization() {
    super(new AntColonyOptimizationView());

    this.logger.info(String.format("%s was loaded.", AntColonyOptimization.class));
  }

  @Override
  public void run() {
    this.solve(this.tsp);
  }

  @Override
  public void solve(Problem problem) {
    this.tsp.setEventsEnabled(false);

    this.distances = this.tsp.getDistances();
    this.cities = this.distances.length;

    this.logger.info(AntColonyOptimization.printDistance(this.distances));

    this.ants = this.initAnts(super.getAnts(), cities);

    this.trail = this.bestTrail();
    this.tsp.setBestTrail(false, this.trail);
    this.trailLength = this.length(this.trail);

    this.logger.info(
        String.format(
            "Best initial trail length %s. Initializing pheromones on trails.", this.trailLength));

    this.pheromones = initPheromones();

    for (int i = 0; i < super.getIteration(); i++) {
      if (this.isInterrupt()) {
        this.progressBar.setValue(0);
        return;
      }

      this.progressBar.setValue(i + 1);
      int percent = ((i + 1) * 100) / super.getIteration();
      this.progressBar.setString(Integer.toString(percent) + "%");
      this.updateAnts();
      this.updatePheromones();

      int[] currBestTrail = this.bestTrail();
      int currBestLength = this.length(currBestTrail);

      if (currBestLength < this.trailLength) {
        this.trailLength = currBestLength;
        this.trail = currBestTrail;
        this.tsp.setBestTrail(false, this.trail);

        this.logger.info(String.format("New best length of %d found at %d", this.trailLength, i));
      }
    }

    this.logger.warn(this.progressBar.isMaximumSizeSet());

    this.logger.info("Time complete");
    this.logger.info(String.format("Best trail found %s", Arrays.toString(this.trail)));
    this.logger.info(String.format("Length of best trail found %d", this.trailLength));
  }

  @Override
  public void setProperties(Problem problem) {
    this.tsp = (TSP) problem;
    this.progressBar = tsp.getProgressBar();
    this.progressBar.setMinimum(0);
    this.progressBar.setMaximum(super.getIteration());
    this.progressBar.setValue(0);
  }

  @Override
  protected int distance(int cityX, int cityY, int[][] dists) {
    return dists[cityX][cityY];
  }

  @Override
  protected double[] moveProbs(int k, int cityX, Boolean[] visited) {
    double sum = 0.0;

    double[] taueta = new double[this.cities];

    double[] probs = new double[this.cities];

    for (int i = 0; i < taueta.length; ++i) {
      if (i == cityX) {
        taueta[i] = 0.0;
      } else if (visited[i]) {
        taueta[i] = 0.0;
      } else {
        taueta[i] =
            Math.pow(this.pheromones[cityX][i], super.getAlpha())
                * Math.pow((1.0 / distance(cityX, i, this.distances)), super.getBeta());

        if (taueta[i] < 0.0001) {
          taueta[i] = 0.0001;
        }

        if (taueta[i] > (Double.MAX_VALUE / (this.cities * 100))) {
          taueta[i] = Double.MAX_VALUE / (this.cities * 100);
        }
      }
      sum += taueta[i];
    }

    for (int i = 0; i < probs.length; ++i) {
      probs[i] = taueta[i] / sum;
    }

    return probs;
  }

  private static String printDistance(int[][] distances) {
    StringBuilder sb = new StringBuilder();
    for (int[] distance : distances) {
      sb.append("\n");
      for (int j = 0; j < distances.length; j++) {
        sb.append(String.format("%3s", distance[j]));
      }
    }
    return sb.toString();
  }
}
