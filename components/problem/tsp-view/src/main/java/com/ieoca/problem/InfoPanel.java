package com.ieoca.problem;

import java.awt.*;
import javax.swing.*;

class InfoPanel extends JPanel {

  public InfoPanel(Mediator mediator) {
    this.init();

    mediator.register(this);
  }

  public void addLable(String text) {
    this.add(new JLabel(text));
  }

  private void init() {
    this.setLayout(new GridLayout(0, 2));
  }
}
