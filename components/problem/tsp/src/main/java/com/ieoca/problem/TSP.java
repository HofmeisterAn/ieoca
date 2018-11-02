package com.ieoca.problem;

import com.ieoca.components.problem.Problem;
import com.ieoca.components.problem.ProblemView;
import com.ieoca.components.problem.listener.ProblemEvent;
import com.ieoca.problem.world.City;
import com.ieoca.problem.world.Position;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class TSP extends Problem<ProblemView> {
  private Controller controller;

  private ArrayList<City> cities = new ArrayList<>();

  private ArrayList<City> bestTrail = new ArrayList<>();

  private JProgressBar progressBar;

  public TSP() {
    super(new TSPView());

    this.controller = new Controller(this);

    this.getProblemView().register(controller);
  }

  public void setCities(Integer count) {
    this.cities.clear();

    for (int i = 1; i <= count; i++) {
      this.addCity(new City(i));
    }

    this.getProblemView().updateCities(this.getCities());
  }

  private void addCity(City city) {
    if (this.controller.isOccupied(city.getPosition(), this.cities)) {
      city.setPosition(new Position());
      this.addCity(city);
    }

    this.cities.add(city);

    this.getProblemView().updateCities(this.getCities());
  }

  public void addCity(Position position) {
    if (position == null) {
      throw new UnsupportedOperationException();
    }

    City city = new City(this.cities.size() + 1, position.x(), position.y());

    this.cities.add(city);

    this.getProblemView().updateCities(this.getCities());
  }

  public void removeCity(City city) {
    if (city == null) {
      throw new UnsupportedOperationException();
    }

    for (City c : city.getConnections()) {
      c.removeConnection(city);
    }

    this.cities.remove(city);

    for (int i = 0; i < this.cities.size(); i++) {
      this.cities.get(i).setId(i + 1);
    }

    this.getProblemView().updateCities(this.getCities());
  }

  public void connectCities() {
    for (int i = 0; i < this.cities.size(); i++) {
      for (int k = i + 1; k < this.cities.size(); k++) {
        this.cities.get(i).addConnection(this.cities.get(k));
        this.cities.get(k).addConnection(this.cities.get(i));
      }
    }

    this.getProblemView().updateCities(this.getCities());
  }

  public void setBestTrail(Boolean clear, int[] trails) {
    this.bestTrail.clear();

    if (clear) return;

    for (int trail : trails) {
      City city = this.cities.get(trail);
      this.bestTrail.add(city);
    }

    this.getProblemView().updateBestTrail(this.bestTrail);
  }

  public int[][] getDistances() {
    int[][] distances = new int[this.cities.size()][this.cities.size()];
    for (int i = 0; i < distances.length; i++) {
      City cityI = this.cities.get(i);
      for (int k = 0; k < distances.length; k++) {
        City cityK = this.cities.get(k);
        if (cityI.contains(cityK)) {
          distances[i][k] = this.controller.getDistance(cityI, cityK);
          distances[k][i] = distances[i][k];
        }
      }
    }
    return distances;
  }

  private City[] getCities() {
    return this.cities.toArray(new City[0]);
  }

  private TSPView getProblemView() {
    return (TSPView) this.getView();
  }

  public JProgressBar getProgressBar() {
    return this.getProblemView().getProgressBar();
  }

  public void addLable(String text) {
    this.getProblemView().addLable(text);
  }

  @Override
  public void setProperties(Object object) {
    TSP problem = TSP.getTSP(object);

    if (this.getView() == problem.getView()) return;

    this.cities.clear();
    Collections.addAll(this.cities, problem.getCities());

    this.getProblemView().updateCities(this.getCities());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.notify(new ProblemEvent(this));
  }

  @Override
  public Error isStartable() {
    if (this.controller.isStartable(this.cities)) {
      return null;
    } else {
      return new Error("Cannot start. Missing connectiong between cities.");
    }
  }

  @Override
  public void reset() {
    this.setBestTrail(true, null);
    this.setCities(0);
    this.setEventsEnabled(true);
  }

  @Override
  public void setEventsEnabled(Boolean b) {
    this.getProblemView().setEventsEnabled(b);
  }

  private static TSP getTSP(Object object) {
    if (object instanceof TSP) {
      return (TSP) object;
    }

    throw new IllegalArgumentException();
  }
}
