package com.ieoca.problem;

import com.ieoca.problem.world.City;
import com.ieoca.problem.world.Map;
import com.ieoca.problem.world.Position;
import java.util.ArrayList;

class Controller {
  private TSP tsp;

  public Controller(TSP tsp) {
    this.tsp = tsp;
  }

  public void setCities(Integer count) {
    this.tsp.setCities(count);
  }

  public void setBestTrail(Boolean clear) {
    this.tsp.setBestTrail(clear, null);
  }

  public void connectCities() {
    this.tsp.connectCities();
  }

  public void addCity(Position position) {
    this.tsp.addCity(position);
  }

  public void removeCity(City city) {
    this.tsp.removeCity(city);
  }

  public Boolean isOccupied(Position position, ArrayList<City> cities) {
    if (position == null) {
      throw new UnsupportedOperationException();
    }

    for (City city : cities) {
      if (city.getPosition().equals(position)) {
        return true;
      }
    }

    return false;
  }

  public Integer getDistance(City a, City b) {
    int x = Math.abs(a.getPosition().x() - b.getPosition().x());
    int y = Math.abs(a.getPosition().y() - b.getPosition().y());
    double dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    return (int) Math.round(dist / Map.UNIT);
  }

  public City getMarked(Integer x, Integer y, City[] cities) {
    for (int i = cities.length - 1; i >= 0; i--) {
      if (cities[i].isMarked(x, y)) {
        return cities[i];
      }
    }

    return null;
  }

  public Position getPointOnLine(City city1, City city2, double position) {
    Position position1 = city1.getPosition();
    Position position2 = city2.getPosition();
    int x = (int) (position * (position2.x() - position1.x()) + position1.x());
    int y = (int) (position * (position2.y() - position1.y()) + position1.y());

    return new Position(x, y, true);
  }

  public boolean isStartable(ArrayList<City> cities) {
    boolean value = true;

    for (City city : cities) {
      if (city.getConnections().size() == 0) value = false;
    }

    return ((cities.size() != 0) && (value));
  }
}
