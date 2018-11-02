package com.ieoca.problem.world;

import java.awt.*;
import java.util.ArrayList;

public class City {

  public static final int WIDTH = (int) (0.75 * Map.GRIDWIDTH);

  private final Color color = Color.BLACK;

  private final ArrayList<City> connections = new ArrayList<>();

  private int id;

  private Position position;

  public City(int id) {
    this.id = id;
    this.position = new Position();
  }

  public City(int id, int x, int y) {
    this.id = id;
    this.position = new Position(x, y);
  }

  public boolean isMarked(int x, int y) {
    return (this.distanceFromCenter(x, y) < City.WIDTH / 2);
  }

  private double distanceFromCenter(int x, int y) {
    int dx = Math.abs(this.position.x() - x);
    int dy = Math.abs(this.position.y() - y);

    return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
  }

  public int getRimX() {
    return this.position.x() - City.WIDTH / 2;
  }

  public int getRimY() {
    return this.position.y() - City.WIDTH / 2;
  }

  public void addConnection(City city) {
    if (!this.connections.contains(city)) {
      this.connections.add(city);
    }
  }

  public void removeConnection(City city) {
    this.connections.remove(city);
  }

  public boolean contains(City city) {
    return this.connections.contains(city);
  }

  public Position getPosition() {

    return this.position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public int getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Color getColor() {
    return this.color;
  }

  public ArrayList<City> getConnections() {
    return this.connections;
  }

  @Override
  public String toString() {
    return String.valueOf(this.getId());
  }
}
