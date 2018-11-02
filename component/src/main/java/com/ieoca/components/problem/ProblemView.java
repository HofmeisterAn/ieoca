package com.ieoca.components.problem;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.*;

/**
 * The Class ProblemView is the root of each problem view. It represents the sight of a problem and
 * performs with the users. Views dispatch information to their associated problem.
 *
 * @author Andre Hofmeister
 * @version 1.0
 */
public abstract class ProblemView {
  private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public abstract JPanel getView();

  public abstract void setProperties(Object object);

  protected ProblemView() {}

  public final void addPropertyChangeListener(PropertyChangeListener listener) {
    this.propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public final void removePropertyChangeListener(PropertyChangeListener listener) {
    this.propertyChangeSupport.removePropertyChangeListener(listener);
  }

  @Override
  public boolean equals(Object o) {
    return (this == o) || !((o == null) || (getClass() != o.getClass()));
  }

  @Override
  public int hashCode() {
    return propertyChangeSupport.hashCode();
  }

  protected final void firePropertyChange(PropertyChangeEvent event) {
    this.propertyChangeSupport.firePropertyChange(event);
  }

  protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }
}
