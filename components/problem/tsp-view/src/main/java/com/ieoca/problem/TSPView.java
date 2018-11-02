package com.ieoca.problem;

import com.ieoca.components.problem.ProblemView;
import com.ieoca.problem.world.City;
import com.ieoca.problem.world.Map;
import com.ieoca.problem.world.Position;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class TSPView extends ProblemView {
  private Mediator mediator;

  private Map map;

  private ControlPanel controlPanel;

  private InfoPanel infoPanel;

  private JPanel jPanel;

  private Controller controller;

  public TSPView() {
    init();
  }

  public void register(Controller controller) {
    if (this.controller == null) {
      this.controller = controller;
    }
  }

  public void setCities(Integer count) {
    this.controller.setCities(count);
  }

  public void addCity(Position position) {
    this.controller.addCity(position);
  }

  public void removeCity(City city) {
    this.controller.removeCity(city);
  }

  public void updateCities(City[] cities) {
    this.map.setCities(cities);
  }

  public void setBestTrail(Boolean clear) {
    this.controller.setBestTrail(clear);
  }

  public void updateBestTrail(ArrayList<City> bestTrail) {
    this.map.setBestTrail(bestTrail);
  }

  public void connectCities() {
    this.controller.connectCities();
  }

  public void notifyProblem() {
    this.firePropertyChange(null, null, null);
  }

  public Boolean isOccupied(Position position, ArrayList<City> cities) {
    return this.controller.isOccupied(position, cities);
  }

  public Integer getDistance(City a, City b) {
    return this.controller.getDistance(a, b);
  }

  public City getMarked(Integer x, Integer y, City[] cities) {
    return this.controller.getMarked(x, y, cities);
  }

  public Position getPointOnLine(City city1, City city2, double position) {
    return this.controller.getPointOnLine(city1, city2, position);
  }

  public void setEventsEnabled(Boolean b) {
    this.map.setEventsEnabled(b);
    this.controlPanel.setEventsEnabled(b);
  }

  public JProgressBar getProgressBar() {
    return this.mediator.getProgressBar();
  }

  public void addLable(String text) {
    this.mediator.addLable(text);
  }

  @Override
  public void setProperties(Object object) {}

  @Override
  public JPanel getView() {
    return this.jPanel;
  }

  private void init() {
    this.mediator = new Mediator();

    this.mediator.register(this);

    this.map = new Map(mediator);
    this.controlPanel = new ControlPanel(mediator);
    this.infoPanel = new InfoPanel(mediator);

    this.jPanel = new JPanel();
    this.jPanel.setPreferredSize(new Dimension(450, 600));
    this.jPanel.add(this.map, BorderLayout.NORTH);
    this.jPanel.add(this.controlPanel, BorderLayout.CENTER);
    this.jPanel.add(this.infoPanel, BorderLayout.SOUTH);
  }
}
