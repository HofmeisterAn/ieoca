package com.ieoca.mvc.controller;

import com.ieoca.mvc.model.AbstractModel;
import com.ieoca.mvc.model.MainModel;
import com.ieoca.mvc.view.AbstractViewPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public abstract class AbstractController implements PropertyChangeListener {
  private final ArrayList<AbstractViewPanel> views;
  private AbstractModel model;

  AbstractController() {
    this.views = new ArrayList<>();
    this.model = new MainModel();
  }

  protected abstract void setModelProperty(Class modelClass, String propertyName, Object newValue);

  protected abstract void updateModel();

  public final void addModel(AbstractModel model) {
    this.model = model;
    this.model.addPropertyChangeListener(this);

    this.updateModel();
  }

  public final void removeModel(AbstractModel model) {
    this.model = new MainModel();
    this.model.removePropertyChangeListener(this);
  }

  public final void addView(AbstractViewPanel view) {
    this.views.add(view);
  }

  public final void removeView(AbstractViewPanel view) {
    this.views.remove(view);
  }

  public final void propertyChange(PropertyChangeEvent evt) {
    for (AbstractViewPanel view : this.views) {
      view.modelPropertyChange(evt);
    }
  }

  final AbstractModel getModel() {
    return this.model;
  }
}
