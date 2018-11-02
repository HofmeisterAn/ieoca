package com.ieoca.components.problem;

import com.ieoca.components.problem.listener.ProblemEvent;
import com.ieoca.components.problem.listener.ProblemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.EventListenerList;

/**
 * This abstract class represent the implementation of an problem. It contains all necessary
 * information about the problem. Every problem has his own view, which is the interface to the
 * user.
 *
 * @param <T> view of the problem. See {@link com.ieoca.components.problem.ProblemView}.
 * @author Andre Hofmeister
 * @version 1.0
 */
public abstract class Problem<T extends ProblemView> implements PropertyChangeListener {
  private final T view;

  private final EventListenerList listeners = new EventListenerList();

  public abstract void propertyChange(PropertyChangeEvent evt);

  public abstract Error isStartable();

  public abstract void reset();

  public abstract void setEventsEnabled(Boolean b);

  public Problem(T view) {
    this.view = view;
    this.view.addPropertyChangeListener(this);
  }

  public final JPanel getPanel() {
    return this.view.getView();
  }

  public final T getView() {
    return this.view;
  }

  public final void addProblemListener(ProblemListener listener) {
    this.listeners.add(ProblemListener.class, listener);
  }

  public final void removeProblemListener(ProblemListener listener) {
    this.listeners.remove(ProblemListener.class, listener);
  }

  public final void notify(ProblemEvent event) {
    for (ProblemListener listener : this.listeners.getListeners(ProblemListener.class)) {
      listener.update(this, event);
    }
  }

  public void setProperties(Object object) {
    this.view.setProperties(object);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Problem problem = (Problem) o;

    return !(this.view != null ? !this.view.equals(problem.view) : problem.view != null);
  }

  @Override
  public int hashCode() {
    return view.hashCode();
  }
}
