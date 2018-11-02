package com.ieoca.problem.world;

import com.ieoca.problem.Mediator;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class Map extends JPanel implements MouseListener, MouseMotionListener {
  public static final int UNIT = 11;

  public static final int GRIDWIDTH = 3 * UNIT;

  public static final int PANELWIDTH = 12 * GRIDWIDTH;

  public static final int PANELHEIGHT = 12 * GRIDWIDTH;

  private final Mediator mediator;

  private ArrayList<City> cities = new ArrayList<>();

  private ArrayList<City> bestTrail = new ArrayList<>();

  private boolean isConnecting;

  private boolean isACO;

  private boolean isEventsEnabled = true;

  private City city;

  private boolean move;

  private int pressX;

  private int pressY;

  private Graphics2D g2;

  public Map(Mediator mediator) {
    this.mediator = mediator;
    this.mediator.register(this);

    this.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));
    this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }

  public void setCities(City[] cities) {
    this.cities.clear();

    Collections.addAll(this.cities, cities);
    this.repaint();
  }

  public void setBestTrail(ArrayList<City> bestTrail) {
    this.bestTrail = bestTrail;
    this.repaint();
  }

  public void isConnecting(Boolean b) {
    this.isConnecting = b;
  }

  public void isACO(Boolean b) {
    this.isACO = b;
    this.repaint();
  }

  public void setEventsEnabled(Boolean b) {
    this.isEventsEnabled = b;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (!this.isEventsEnabled) return;

    if (SwingUtilities.isRightMouseButton(e)) {
      Position position = new Position(e.getX(), e.getY());

      City c = this.mediator.getMarked(position.x(), position.y(), this.getCities());

      if (c == null) {
        this.mediator.addCity(position);
        this.mediator.setBestTrail(true);
      } else {
        this.mediator.removeCity(c);
        this.mediator.setBestTrail(true);
      }

      this.repaint();
      this.mediator.notifyProblem();
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (!this.isEventsEnabled) return;

    Position position = new Position(e.getX(), e.getY());
    this.city = this.mediator.getMarked(position.x(), position.y(), this.getCities());
    if (this.city == null) return;
    this.move = true;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (!this.isEventsEnabled) return;

    if (this.isConnecting) {
      City city = this.mediator.getMarked(e.getX(), e.getY(), this.getCities());
      if (city != null) {
        city.addConnection(this.city);
        this.city.addConnection(city);
      }
    }

    this.move = false;

    this.repaint();
    this.mediator.notifyProblem();
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    if (!this.isEventsEnabled) return;

    if (this.isConnecting) {
      this.pressX = e.getX();
      this.pressY = e.getY();
    } else {
      int maxHeight = Map.PANELHEIGHT - City.WIDTH / 2;
      int minHeight = City.WIDTH / 2;
      int maxWidth = Map.PANELWIDTH - City.WIDTH / 2;
      int minWidth = Map.WIDTH / 2;

      if (e.getX() > maxWidth || e.getX() < minWidth) return;

      if (e.getY() > maxHeight || e.getY() < minHeight) return;

      if (this.mediator.isOccupied(new Position(e.getX(), e.getY()), this.cities)) return;

      if (!move) return;

      this.city.setPosition(new Position(e.getX(), e.getY()));
    }

    this.repaint();
    this.mediator.notifyProblem();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    if (!this.isEventsEnabled) return;

    if (this.move || this.isConnecting) return;

    City city = this.mediator.getMarked(e.getX(), e.getY(), this.getCities());

    if (this.city != city) {
      this.city = city;
      this.repaint();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    this.g2 = (Graphics2D) g;
    // this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // this.g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    // RenderingHints.VALUE_INTERPOLATION_BICUBIC);

    this.paintGrid();
    this.paintConnections();
    this.paintBestTrail();
    this.paintCities();

    if (this.isConnecting && this.move) {
      this.g2.setColor(Color.GRAY);
      this.g2.drawLine(
          this.city.getPosition().x(), this.city.getPosition().y(), this.pressX, this.pressY);
    }
  }

  private void paintGrid() {
    this.g2.setColor(Color.LIGHT_GRAY);

    for (int i = 0; i < PANELWIDTH; i += GRIDWIDTH) {
      this.g2.drawLine(i, 0, i, PANELHEIGHT);
      this.g2.drawLine(0, i, PANELHEIGHT, i);
    }
  }

  private void paintCities() {
    this.g2.setColor(Color.BLUE);

    for (City city : this.cities) {
      this.paintEdge(city);
      this.paintCity(city);
    }

    if (this.city != null) {
      this.paintEdge(this.city);
    }
  }

  private City[] getCities() {
    return this.cities.toArray(new City[0]);
  }

  private void paintCity(City city) {
    int rimX = city.getRimX();
    int rimY = city.getRimY();
    int x = city.getPosition().x();
    int y = city.getPosition().y();

    this.g2.setColor(city.getColor());

    this.box(rimX, rimY, City.WIDTH, true);

    this.write(String.valueOf(city.getId()), x, y);

    if (this.city == city) {
      this.g2.setColor(Color.GREEN);
    } else {
      this.g2.setColor(Color.WHITE);
    }

    this.box(rimX, rimY, City.WIDTH, false);
  }

  private void paintConnections() {
    for (int i = 0; i < this.cities.size(); i++) {
      for (int k = i + 1; k < this.cities.size(); k++) {
        City city1 = this.cities.get(i);
        City city2 = this.cities.get(k);

        if (city1.contains(city2)) {
          Position position = this.mediator.getPointOnLine(city1, city2, 0.5);
          Position pos1 = this.mediator.getPointOnLine(city1, city2, 0.4);
          Position pos2 = this.mediator.getPointOnLine(city1, city2, 0.6);

          String dist = String.valueOf(this.mediator.getDistance(city1, city2));
          FontMetrics fm = this.g2.getFontMetrics();
          int strWidth = SwingUtilities.computeStringWidth(fm, dist);

          this.g2.setColor(Color.GRAY);
          this.g2.drawLine(
              city1.getPosition().x(),
              city1.getPosition().y(),
              city2.getPosition().x(),
              city2.getPosition().y());
          // this.g2.drawLine(city1.getPosition().x(), city1.getPosition().y(), pos1.x(), pos1.y());
          // this.g2.drawLine(city2.getPosition().x(), city2.getPosition().y(), pos2.x(), pos2.y());

          this.g2.drawString(dist, position.x() - strWidth / 2, position.y());

          if (this.isACO) {
            String pheromon = "0.1";
            FontMetrics fmetric = this.g2.getFontMetrics();
            int stringWidth = SwingUtilities.computeStringWidth(fmetric, pheromon);

            this.g2.drawString(
                pheromon, position.x() - stringWidth / 2, position.y() + City.WIDTH / 2);
          }
        }
      }
    }
  }

  private void paintBestTrail() {
    this.g2.setColor(Color.RED);
    for (int i = 0; i < this.bestTrail.size(); i++) {
      City a = this.bestTrail.get(i);
      City b;
      if (i + 1 == this.bestTrail.size()) b = this.bestTrail.get(0);
      else b = this.bestTrail.get(i + 1);
      this.g2.drawLine(
          a.getPosition().x(), a.getPosition().y(), b.getPosition().x(), b.getPosition().y());
    }
  }

  private void paintEdge(City city) {
    this.g2.setColor(Color.CYAN);

    if (city == this.city) {
      this.g2.setColor(Color.YELLOW);
    }
  }

  private void box(int x, int y, int width, boolean value) {
    if (value) {
      this.g2.fillOval(x, y, City.WIDTH, City.WIDTH);
    } else {
      this.g2.drawOval(x, y, City.WIDTH, City.WIDTH);
    }
  }

  private void write(String name, int x, int y) {
    this.g2.setColor(Color.WHITE);

    FontMetrics fm = this.g2.getFontMetrics();
    int strWidth = SwingUtilities.computeStringWidth(fm, name);

    this.g2.drawString(name, (x - strWidth / 2), (y + fm.getMaxAscent() / 2));
  }
}
