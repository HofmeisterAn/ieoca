package com.ieoca.algorithm.ga;

import java.util.ArrayList;
import java.util.Collections;

public class Tour {

  private ArrayList<Integer> tour = new ArrayList<>();
  private double fitness = 0;
  private int distance = 0;
  private int cities;
  private int[][] distances;

  public Tour(int[][] distances) {
    this.distances = distances;
    this.cities = distances.length;
    for (int i = 0; i < this.cities; i++) {
      tour.add(null);
    }
  }

  public Tour(ArrayList<Integer> tour, int[][] distances) {
    this.distances = distances;
    this.cities = distances.length;
    this.tour = tour;
  }

  // Creates a random individual
  public void generateIndividual() {
    for (int cityIndex = 0; cityIndex < this.cities; cityIndex++) {
      this.setCity(cityIndex, cityIndex);
    }
    Collections.shuffle(tour);
  }

  // Gets a city from the tour
  public Integer getCity(int tourPosition) {
    return tour.get(tourPosition);
  }

  // Sets a city in a certain position within a tour
  public void setCity(int tourPosition, int city) {
    tour.set(tourPosition, city);
    this.fitness = 0;
    this.distance = 0;
  }

  public double getFitness() {
    if (fitness == 0) {
      fitness = 1 / (double) getDistance();
    }
    return fitness;
  }

  // Gets the total distance of the tour
  public int getDistance() {
    if (distance == 0) {
      int tourDistance = 0;
      for (int i = 0; i < this.cities; i++) {
        int fromCity = getCity(i);
        int destinationCity;

        if (i + 1 < this.cities) {
          destinationCity = getCity(i + 1);
        } else {
          destinationCity = getCity(0);
        }

        tourDistance += distances[fromCity][destinationCity];
      }
      this.distance = tourDistance;
    }
    return distance;
  }

  public int tourSize() {
    return tour.size();
  }

  public boolean containsCity(int city) {
    return tour.contains(city);
  }

  public int[] getBestTrail() {
    int[] bestTrail = new int[this.cities];
    for (int i = 0; i < this.cities; i++) {
      bestTrail[i] = tour.get(i);
    }
    return bestTrail;
  }

  @Override
  public String toString() {
    StringBuilder geneString = new StringBuilder("|");
    for (int i = 0; i < this.cities; i++) {
      geneString.append(getCity(i)).append("|");
    }
    return geneString.toString();
  }
}
