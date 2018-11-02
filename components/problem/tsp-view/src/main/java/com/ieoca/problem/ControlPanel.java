package com.ieoca.problem;

import java.awt.*;
import javax.swing.*;

class ControlPanel extends JPanel {
  private final Mediator mediator;

  private JComboBox<Integer> jComboBox;

  private JButton generateCities;

  private JButton connectAll;

  private JCheckBox isConnecting;

  private JCheckBox isACO;

  private JLabel progressLabel = new JLabel("Progress");

  private JProgressBar progressBar;

  // Wird noch rausgeschmissen
  private JLabel fillingLable = new JLabel();

  public ControlPanel(Mediator mediator) {
    this.mediator = mediator;
    this.mediator.register(this);

    this.init();
  }

  private void init() {
    this.setLayout(new GridLayout(0, 2, 10, 10));

    this.jComboBox = new JComboBox<>(new Integer[] {5, 25, 50});

    this.generateCities = new JButton();
    this.generateCities.setText("Generate Cities");
    this.generateCities.addActionListener(e -> doGenerateCities());

    this.connectAll = new JButton();
    this.connectAll.setText("Connect All");
    this.connectAll.addActionListener(e -> doConnectAll());

    this.isConnecting = new JCheckBox();
    this.isConnecting.setText("Connect");
    this.isConnecting.addActionListener(e -> doConnecting());

    this.isACO = new JCheckBox();
    this.isACO.setText("ACO information");
    this.isACO.addActionListener(e -> doACO());
    this.progressBar = new JProgressBar();
    this.progressBar.setStringPainted(true);
    this.add(this.generateCities);
    this.add(this.jComboBox);
    this.add(this.connectAll);
    this.add(this.isConnecting);
    this.add(this.isACO);
    // wird noch rausgeschmissen
    this.add(this.fillingLable);
    this.add(this.progressLabel);
    this.add(this.progressBar);
  }

  private void doGenerateCities() {
    this.mediator.setCities((Integer) this.jComboBox.getSelectedItem());
    this.mediator.setBestTrail(true);
    this.mediator.notifyProblem();
  }

  private void doConnectAll() {
    this.mediator.connectCities();
    this.mediator.notifyProblem();
  }

  private void doConnecting() {
    this.mediator.isConnecting(this.isConnecting.isSelected());
  }

  private void doACO() {
    this.mediator.isACO(this.isACO.isSelected());
  }

  public void setEventsEnabled(Boolean b) {
    this.generateCities.setEnabled(b);
    this.connectAll.setEnabled(b);
    this.isConnecting.setEnabled(b);
    this.isACO.setEnabled(b);
  }

  public JProgressBar getProgressBar() {
    return this.progressBar;
  }
}
