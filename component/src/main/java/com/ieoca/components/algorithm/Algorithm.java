package com.ieoca.components.algorithm;

import com.ieoca.components.problem.Problem;
import javax.swing.*;

/**
 * This abstract class represent the implementation of an algorithm. Extend this class to implement
 * your own algorithm component. Every algorithm has his own view with all necessary parameters. The
 * implementation of this class, is used to solve a Problem. See {@link
 * com.ieoca.components.problem.Problem}.
 *
 * @param <T> view of the algorithm. See {@link com.ieoca.components.algorithm.AlgorithmView}.
 * @author Andre Hofmeister
 * @version 1.0
 */
public abstract class Algorithm<T extends AlgorithmView> {
  protected JProgressBar progressBar;

  private final T view;

  private boolean interrupt = false;

  private boolean pause = false;

  public abstract void run();

  public abstract void solve(Problem problem);

  public abstract void setProperties(Problem problem);

  /**
   * @param view view of the algorithm. See {@link com.ieoca.components.algorithm.AlgorithmView}.
   */
  public Algorithm(T view) {
    this.view = view;
  }

  /**
   * Return the view menu item.
   *
   * @return the view menu item.
   */
  public final JMenuItem getMenue() {
    return this.view.getMenu();
  }

  public boolean isInterrupt() {
    return this.interrupt;
  }

  public void setInterrupt(boolean interrupt) {
    this.interrupt = interrupt;
  }

  public boolean isPause() {
    return this.pause;
  }

  public void setPause(boolean pause) {
    this.pause = pause;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Algorithm algorithm = (Algorithm) o;

    return !(this.view != null ? !this.view.equals(algorithm.view) : algorithm.view != null);
  }

  @Override
  public int hashCode() {
    return view != null ? view.hashCode() : 0;
  }
}
