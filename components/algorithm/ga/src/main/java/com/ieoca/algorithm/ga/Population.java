package com.ieoca.algorithm.ga;

class Population {

  private Tour[] tours;

  public Population(int populationSize, boolean initialise, int[][] distances) {
    this.tours = new Tour[populationSize];
    if (initialise) {
      for (int i = 0; i < this.populationSize(); i++) {
        Tour newTour = new Tour(distances);
        newTour.generateIndividual();
        this.tours[i] = newTour;
      }
    }
  }

  public void saveTour(int index, Tour tour) {
    tours[index] = tour;
  }

  public Tour getTour(int index) {
    return tours[index];
  }

  public Tour getFittest() {
    Tour fittest = tours[0];
    for (int i = 1; i < this.populationSize(); i++) {
      if (fittest.getFitness() <= this.tours[i].getFitness()) {
        fittest = this.tours[i];
      }
    }
    return fittest;
  }

  public int populationSize() {
    return tours.length;
  }
}
