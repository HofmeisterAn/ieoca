package com.ieoca;

import com.ieoca.mvc.controller.MainController;
import com.ieoca.mvc.model.MainModel;
import com.ieoca.mvc.view.MainView;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          MainModel mainModel = new MainModel();
          MainController mainController = new MainController();
          MainView mainView = new MainView(mainController);

          mainController.addModel(mainModel);
          mainController.addView(mainView);

          createAndShowGUI(mainController, mainView);
        });
  }

  private static void createAndShowGUI(final MainController mainController, JPanel jPanel) {
    JFrame jFrame = new JFrame("Intelligent Evaluation Of Complex Algorithm");

    jFrame.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            mainController.unloadAll();
            System.exit(0);
          }
        });

    jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    jFrame.add(jPanel);
    jFrame.setVisible(true);
    jFrame.requestFocusInWindow();
    jFrame.setResizable(false);
    jFrame.pack();
  }
}
