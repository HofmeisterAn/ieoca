package com.ieoca.mvc.model;

import com.ieoca.components.algorithm.Algorithm;
import com.ieoca.components.problem.Problem;
import com.ieoca.components.problem.ProblemView;
import com.ieoca.components.problem.listener.ProblemEvent;
import com.ieoca.components.problem.listener.ProblemListener;
import com.ieoca.mvc.Action;
import java.util.ArrayList;

public class MainModel extends AbstractModel {
  private static class FakeFactory {
    static Problem getProblem(Problem<ProblemView> problem) {
      try {
        return problem.getClass().newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
        return null;
      }
    }
  }

  private class ComplainingProblemListener implements ProblemListener {
    @Override
    public void update(Problem source, ProblemEvent e) {
      for (Problem<ProblemView> p : problems) {
        p.setProperties(source);
      }
    }
  }

  private static final int NUMBERS = 3;

  private final ArrayList<Problem> problems = new ArrayList<>();

  private final ArrayList<Algorithm> algorithms = new ArrayList<>();

  public MainModel() {
    init();
  }

  @Override
  public void init() {}

  public void addProblem(Problem problem) {
    if (problem == null) return;

    if (this.problems.size() == 0 || (this.problems.size() < this.algorithms.size())) {
      if (this.problems.size() > 0) {
        problem = FakeFactory.getProblem(problem);
      }

      problem.addProblemListener(new ComplainingProblemListener());

      this.problems.add(problem);
      this.firePropertyChange(Action.ADD_PROBLEM.toString(), null, problem);

      if (this.problems.size() > 0) {
        this.problems.get(0).notify(new ProblemEvent((this.problems.get(0))));
      }

      this.addProblem(this.problems.get(0));
    }

    if ((this.problems.size() > 0) && (this.algorithms.size()) > 0) {
      this.firePropertyChange(Action.IS_ENABLE.toString(), null, true);
    }
  }

  public void addAlgorithm(Algorithm algorithm) {
    if (algorithm == null) return;

    if (this.algorithms.size() >= NUMBERS) return;

    this.algorithms.add(algorithm);
    this.firePropertyChange(Action.ADD_ALGORITHM.toString(), null, algorithm);

    if (this.problems.size() > 0) {
      this.addProblem(this.problems.get(0));
    }
  }

  public void unloadAll() {
    for (int i = this.problems.size() - 1; i >= 0; i--) {
      this.firePropertyChange(Action.REMOVE_PROBLEM.toString(), null, this.problems.get(i));
      this.problems.remove(i);
    }

    for (int i = this.algorithms.size() - 1; i >= 0; i--) {
      this.firePropertyChange(Action.REMOVE_ALGORITHM.toString(), null, this.algorithms.get(i));
      this.algorithms.remove(i);
    }

    this.firePropertyChange(Action.IS_ENABLE.toString(), null, false);
  }

  public void unloadComponent(Problem problem) {
    for (int i = 0; i < this.problems.size(); i++) {
      if (this.problems.get(i) != problem) continue;

      this.firePropertyChange(Action.REMOVE_PROBLEM.toString(), null, this.problems.get(i));
      this.problems.remove(i);

      if (this.algorithms.size() <= i) continue;

      this.firePropertyChange(Action.REMOVE_ALGORITHM.toString(), null, this.algorithms.get(i));
      this.algorithms.remove(i);
    }

    if ((this.problems.size() == 0) || (this.algorithms.size() == 0)) {
      this.firePropertyChange(Action.IS_ENABLE.toString(), null, false);
    }
  }

  public Algorithm getAlgorithm(int index) {
    return this.algorithms.get(index);
  }

  public Problem getProblem(int index) {
    return this.problems.get(index);
  }

  public Algorithm[] getAlgorithms() {
    return this.algorithms.toArray(new Algorithm[0]);
  }

  public Problem[] getProblems() {
    return this.problems.toArray(new Problem[0]);
  }
}
