package com.ieoca.problem.world;

import java.util.Random;

public class Position {
  private final int x;

  private final int y;

  public Position(int x, int y, Boolean b) {
    this.x = x;
    this.y = y;
  }

  public Position(int x, int y) {
    this.x = (int) (((x / Map.GRIDWIDTH) + 0.5) * Map.GRIDWIDTH);
    this.y = (int) (((y / Map.GRIDWIDTH) + 0.5) * Map.GRIDWIDTH);
  }

  public Position() {
    Random r = new Random();
    int tx = r.nextInt(Map.PANELWIDTH);
    int ty = r.nextInt(Map.PANELHEIGHT);
    this.x = (int) (((tx / Map.GRIDWIDTH) + 0.5) * Map.GRIDWIDTH);
    this.y = (int) (((ty / Map.GRIDWIDTH) + 0.5) * Map.GRIDWIDTH);
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Position)) return false;

    Position position = (Position) o;

    if (x != position.x) return false;
    if (y != position.y) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    return result;
  }
}
