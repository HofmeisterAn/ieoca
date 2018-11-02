package com.ieoca.mvc.view;

import com.ieoca.ComponentTitleBorder;
import com.ieoca.components.algorithm.Algorithm;
import com.ieoca.components.algorithm.AlgorithmView;
import com.ieoca.components.problem.Problem;
import com.ieoca.components.problem.ProblemView;
import com.ieoca.mvc.Action;
import com.ieoca.mvc.controller.MainController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainView extends AbstractViewPanel {
  private final MainController controller;

  private final JFileChooser fc = new JFileChooser();

  private JMenuBar jMenueBar;

  private JMenu jMenuComponent;

  private JMenu jMenuSystem;

  private JMenu jMenuConfiguration;

  private JMenu jMenuAlgorithm;

  private JPanel jPanel;

  public MainView(MainController controller) {
    this.controller = controller;
    init();
  }

  @Override
  public void init() {
    JMenuItem jMenuItem = new JMenuItem();

    this.setLayout(new BorderLayout());
    this.setBorder(new EmptyBorder(3, 3, 3, 3));

    this.fc.setAcceptAllFileFilterUsed(false);
    this.fc.setFileFilter(new FileNameExtensionFilter("JAR File", "jar"));

    this.jMenueBar = new JMenuBar();
    this.jMenuComponent = new JMenu("Component");
    this.jMenuSystem = new JMenu("System");
    this.jMenuConfiguration = new JMenu("Configuration");
    this.jMenuAlgorithm = new JMenu("Algorithm");

    this.jMenueBar.add(this.jMenuComponent);
    this.jMenueBar.add(this.jMenuSystem);
    this.jMenueBar.add(this.jMenuConfiguration);

    this.jPanel = new JPanel();
    this.jPanel.setLayout(new BoxLayout(this.jPanel, BoxLayout.X_AXIS));

    jMenuItem = this.addMenueItem(this.jMenuComponent, "Open...");
    jMenuItem.addActionListener(this::doLoadNewComponent);

    jMenuItem = this.addMenueItem(this.jMenuComponent, "Close...");
    jMenuItem.addActionListener(e -> doUnloadAll());

    jMenuItem = this.addMenueItem(this.jMenuSystem, "Start");
    jMenuItem.addActionListener(e -> doStart());
    jMenuItem.setEnabled(false);

    jMenuItem = this.addMenueItem(this.jMenuSystem, "Single-Step");
    jMenuItem.addActionListener(e -> doSingleStep());
    jMenuItem.setEnabled(false);

    jMenuItem = this.addMenueItem(this.jMenuSystem, "Stop");
    jMenuItem.addActionListener(e -> doStop());
    jMenuItem.setEnabled(false);

    jMenuItem = this.addMenueItem(this.jMenuSystem, "Reset");
    jMenuItem.addActionListener(e -> doReset());

    this.jMenuSystem.addSeparator();

    this.addMenueItem(this.jMenuSystem, "Export...");

    this.addMenueItem(this.jMenuConfiguration, "General Settings");
    this.jMenuConfiguration.add(this.jMenuAlgorithm);

    this.add(this.jMenueBar, BorderLayout.PAGE_START);
    this.add(this.jPanel, BorderLayout.CENTER);
  }

  @Override
  public void modelPropertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(Action.ADD_PROBLEM.toString())) {
      Problem<ProblemView> problem = (Problem<ProblemView>) evt.getNewValue();
      this.doSetProblemView(problem);
    } else if (evt.getPropertyName().equals(Action.ADD_ALGORITHM.toString())) {
      Algorithm<AlgorithmView> algorithm = (Algorithm<AlgorithmView>) evt.getNewValue();
      this.doSetAlgorithmMenu(algorithm);
    } else if (evt.getPropertyName().equals(Action.REMOVE_PROBLEM.toString())) {
      Problem<ProblemView> problem = (Problem<ProblemView>) evt.getNewValue();
      this.doRemoveProblemView(problem);
    } else if (evt.getPropertyName().equals(Action.REMOVE_ALGORITHM.toString())) {
      Algorithm<AlgorithmView> algorithm = (Algorithm<AlgorithmView>) evt.getNewValue();
      this.doRemoveAlgorithmMenu(algorithm);
    } else if (evt.getPropertyName().equals(Action.IS_ENABLE.toString())) {
      this.doIsEnable((Boolean) evt.getNewValue());
    }
  }

  private JMenuItem addMenueItem(JMenu jMenu, String name) {
    JMenuItem jMenuItem = new JMenuItem(name);
    jMenu.add(jMenuItem);
    return jMenuItem;
  }

  private void resize() {
    ((JFrame) this.getTopLevelAncestor()).pack();
  }

  private void doLoadNewComponent(ActionEvent e) {
    if (fc.showOpenDialog(MainView.this) == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      this.controller.loadNewComponent(file);
    }
  }

  private void doUnloadAll() {
    this.controller.unloadAll();
  }

  private void doUnloadComponent(Problem<ProblemView> problem) {
    this.controller.unloadComponent(problem);
  }

  private void doSetProblemView(final Problem<ProblemView> problem) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> doSetProblemView(problem));
    }

    JButton jButton = new JButton();
    jButton.setText("Remove");
    jButton.addActionListener(e -> doUnloadComponent(problem));

    ComponentTitleBorder componentTitleBorder =
        new ComponentTitleBorder(jButton, problem.getPanel(), BorderFactory.createEtchedBorder());

    problem.getPanel().setBorder(componentTitleBorder);

    this.jPanel.add(problem.getPanel());
    this.resize();
  }

  private void doSetAlgorithmMenu(final Algorithm<AlgorithmView> algorithm) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> doSetAlgorithmMenu(algorithm));
    }

    this.jMenuAlgorithm.add(algorithm.getMenue());
    this.repaint();
  }

  private void doRemoveProblemView(final Problem<ProblemView> problem) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> doRemoveProblemView(problem));
    }

    this.jPanel.remove(problem.getPanel());
    this.resize();
  }

  private void doRemoveAlgorithmMenu(final Algorithm<AlgorithmView> algorithm) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> doRemoveAlgorithmMenu(algorithm));
    }

    this.jMenuAlgorithm.remove(algorithm.getMenue());
    this.repaint();
  }

  private void doIsEnable(final Boolean value) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> doIsEnable(value));
    }

    this.jMenuSystem.getItem(0).setEnabled(value);
    this.jMenuSystem.getItem(1).setEnabled(value);
    this.jMenuSystem.getItem(2).setEnabled(value);
  }

  private void doStart() {
    this.controller.start();
  }

  private void doSingleStep() {
    this.controller.singleStep();
  }

  private void doStop() {
    this.controller.stop();
  }

  private void doReset() {
    this.controller.reset();
  }
}
