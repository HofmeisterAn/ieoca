package com.ieoca.algorithm.ga;

import com.ieoca.components.problem.Problem;
import com.ieoca.problem.TSP;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GeneticAlgorithm extends GA {

  private final Logger logger = LogManager.getLogger(GeneticAlgorithm.class);

  private TSP tsp;

  private final double mutationRate = 0.015;
  private final int tournamentSize = 5;
  private final int populationSize = 50;
  private final int iterations = 1000;
  private final boolean elitism = true;

  private Tour fittest;

  private GeneticAlgorithm() {
    super(new GeneticAlgorithmView());

    this.logger.info(String.format("%s was loaded.", GeneticAlgorithm.class));
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

    this.logger.info(GeneticAlgorithm.printDistance(this.distances));

    Population pop = new Population(this.populationSize, true, this.distances);
    this.fittest = pop.getFittest();
    this.tsp.setBestTrail(false, this.fittest.getBestTrail());
    System.out.println("Initial distance: " + this.fittest.getDistance());

    pop = this.evolvePopulation(pop);
    for (int i = 0; i < this.iterations; i++) {
      if (this.isInterrupt()) {
        this.progressBar.setValue(0);
        return;
      }

      this.progressBar.setValue(i + 1);
      int percent = ((i + 1) * 100) / this.iterations;
      this.progressBar.setString(Integer.toString(percent) + "%");
      pop = this.evolvePopulation(pop);
      if (pop.getFittest().getDistance() < this.fittest.getDistance()) {
        this.fittest = pop.getFittest();
        this.tsp.setBestTrail(false, this.fittest.getBestTrail());

        this.logger.info(
            String.format("New best length of %d found at %d", this.fittest.getDistance(), i));
      }
    }

    this.logger.warn(this.progressBar.isMaximumSizeSet());

    this.logger.info("Time complete");
    this.logger.info(
        String.format("Best trail found %s", Arrays.toString(this.fittest.getBestTrail())));
    this.logger.info(String.format("Length of best trail found %d", this.fittest.getDistance()));

    System.out.println("Finished");
    System.out.println("Final distance: " + this.fittest.getDistance());
    System.out.println("Solution");
    System.out.println(this.fittest);
  }

  private Population evolvePopulation(Population pop) {
    Population newPopulation = new Population(pop.populationSize(), false, this.distances);

    // Keep our best individual if elitism is enabled
    int elitismOffset = 0;
    if (elitism) {
      newPopulation.saveTour(0, pop.getFittest());
      elitismOffset = 1;
    }

    // Crossover population
    // Loop over the new population's size and create individuals from
    // Current population
    for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
      // Select parents
      Tour parent1 = this.tournamentSelection(pop);
      Tour parent2 = this.tournamentSelection(pop);
      // Crossover parents
      Tour child = this.crossover(parent1, parent2);
      newPopulation.saveTour(i, child);
    }

    // Mutate the new population a bit to add some new genetic material
    for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
      this.mutate(newPopulation.getTour(i));
    }
    return newPopulation;
  }

  private Tour crossover(Tour parent1, Tour parent2) {
    // Create new child tour
    Tour child = new Tour(this.distances);

    // Get start and end sub tour positions for parent1's tour
    int startPos = (int) (Math.random() * parent1.tourSize());
    int endPos = (int) (Math.random() * parent1.tourSize());

    // Loop and add the sub tour from parent1 to our child
    for (int i = 0; i < child.tourSize(); i++) {
      // If our start position is less than the end position
      if (startPos < endPos && i > startPos && i < endPos) {
        child.setCity(i, parent1.getCity(i));
      } // if our start position is larger
      else if (startPos > endPos) {
        if (!(i < startPos && i > endPos)) {
          child.setCity(i, parent1.getCity(i));
        }
      }
    }

    // Loop through parent2's city tour
    for (int i = 0; i < parent2.tourSize(); i++) {
      // If child doesn't have the city add it
      if (!child.containsCity(parent2.getCity(i))) {
        // Loop to find a spare position in the child's tour
        for (int k = 0; k < child.tourSize(); k++) {
          // Spare position found, add city
          if (child.getCity(k) == null) {
            child.setCity(k, parent2.getCity(i));
            break;
          }
        }
      }
    }
    return child;
  }

  private void mutate(Tour tour) {
    // Loop through tour cities
    for (int tourPos1 = 0; tourPos1 < tour.tourSize(); tourPos1++) {
      // Apply mutation rate
      if (Math.random() < mutationRate) {
        // Get a second random position in the tour
        int tourPos2 = (int) (tour.tourSize() * Math.random());

        // Get the cities at target position in tour
        int city1 = tour.getCity(tourPos1);
        int city2 = tour.getCity(tourPos2);

        // Swap them around
        tour.setCity(tourPos2, city1);
        tour.setCity(tourPos1, city2);
      }
    }
  }

  private Tour tournamentSelection(Population pop) {
    // Create a tournament population
    Population tournament = new Population(this.tournamentSize, false, this.distances);
    // For each place in the tournament get a random candidate tour and
    // add it
    for (int i = 0; i < this.tournamentSize; i++) {
      int randomId = (int) (Math.random() * pop.populationSize());
      tournament.saveTour(i, pop.getTour(randomId));
    }
    // Get the fittest tour
    return tournament.getFittest();
  }

  @Override
  public void setProperties(Problem problem) {
    this.tsp = (TSP) problem;
    this.progressBar = tsp.getProgressBar();
    this.progressBar.setMinimum(0);
    this.progressBar.setMaximum(super.getIteration());
    this.progressBar.setValue(0);
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
