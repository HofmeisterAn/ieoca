package com.ieoca.mvc.view;

import java.beans.PropertyChangeEvent;
import javax.swing.*;

public abstract class AbstractViewPanel extends JPanel {
  public abstract void init();

  public abstract void modelPropertyChange(PropertyChangeEvent evt);
}
