package com.ieoca.problem;

import com.ieoca.problem.world.City;
import com.ieoca.problem.world.Map;
import com.ieoca.problem.world.Position;
import java.util.ArrayList;
import javax.swing.*;

public class Mediator {
  private Map map;

  private ControlPanel controlPanel;

  private InfoPanel infoPanel;

  private TSPView view;

  public String name = "Unknown";

  public Mediator() {}

  public void register(Map map) {
    this.map = map;
  }

  public void register(ControlPanel controlPanel) {
    this.controlPanel = controlPanel;
  }

  public void register(InfoPanel infoPanel) {
    this.infoPanel = infoPanel;
  }

  public void register(TSPView tspView) {
    this.view = tspView;
  }

  public void setCities(Integer count) {
    this.view.setCities(count);
  }

  public void setBestTrail(Boolean clear) {
    this.view.setBestTrail(clear);
  }

  public void addCity(Position position) {
    this.view.addCity(position);
  }

  public void removeCity(City city) {
    this.view.removeCity(city);
  }

  public Boolean isOccupied(Position position, ArrayList<City> cities) {
    return this.view.isOccupied(position, cities);
  }

  public Position getPointOnLine(City city1, City city2, double position) {
    return this.view.getPointOnLine(city1, city2, position);
  }

  public City getMarked(Integer x, Integer y, City[] cities) {
    return this.view.getMarked(x, y, cities);
  }

  public Integer getDistance(City a, City b) {
    return this.view.getDistance(a, b);
  }

  public void connectCities() {
    this.view.connectCities();
  }

  public void notifyProblem() {
    this.view.notifyProblem();
  }

  public void isConnecting(Boolean b) {
    this.map.isConnecting(b);
  }

  public void isACO(Boolean b) {
    this.map.isACO(b);
  }

  public JProgressBar getProgressBar() {
    return this.controlPanel.getProgressBar();
  }

  public void addLable(String text) {
    this.infoPanel.addLable(text);
  }
}
