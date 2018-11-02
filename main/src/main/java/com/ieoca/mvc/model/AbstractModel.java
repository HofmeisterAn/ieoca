package com.ieoca.mvc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel {
  private final PropertyChangeSupport propertyChangeSupport;

  AbstractModel() {
    this.propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public abstract void init();

  public final void addPropertyChangeListener(PropertyChangeListener l) {
    this.propertyChangeSupport.addPropertyChangeListener(l);
  }

  public final void removePropertyChangeListener(PropertyChangeListener l) {
    this.propertyChangeSupport.removePropertyChangeListener(l);
  }

  final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }
}
