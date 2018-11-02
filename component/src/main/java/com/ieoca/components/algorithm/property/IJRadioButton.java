package com.ieoca.components.algorithm.property;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 * @author Andre Hofmeister
 * @version 1.0
 */
public final class IJRadioButton extends IJProperty {
  public static final class Builder extends IJProperty.Builder<Builder> {
    ArrayList<String> elements = new ArrayList<>();

    public Builder(String name, String desc) {
      super(name, desc);
    }

    public Builder elements(String[] elements) {
      this.elements = new ArrayList<>(Arrays.asList(elements));
      return getThis();
    }

    @Override
    public Builder getThis() {
      return this;
    }
  }

  private final ArrayList<String> elements;

  public IJRadioButton(Builder builder) {
    super(builder);
    this.elements = builder.elements;
  }

  private String[] getElements() {
    return this.elements.toArray(new String[0]);
  }

  @Override
  public Type getType() {
    return Type.RADIOBUTTON;
  }

  @Override
  public JComponent create(final JPanel jPanel) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> create(jPanel));
    }

    ButtonGroup buttonGroup = new ButtonGroup();

    JPanel radioButtonGroup = new JPanel();

    JLabel jLabel = new JLabel();
    jLabel.setText(this.getName());

    for (String element : this.getElements()) {
      JRadioButton jRadioButton = new JRadioButton(element);
      buttonGroup.add(jRadioButton);
      radioButtonGroup.add(jRadioButton);
    }

    jPanel.add(jLabel);
    jPanel.add(radioButtonGroup);

    if (this.getValue() == null) return radioButtonGroup;

    for (Component component : radioButtonGroup.getComponents()) {
      if (!(component instanceof JRadioButton)) continue;

      JRadioButton jRadioButton = (JRadioButton) component;

      if (!(jRadioButton.getText().equals(this.getValue().toString()))) continue;

      jRadioButton.setSelected(true);
    }

    return radioButtonGroup;
  }
}
