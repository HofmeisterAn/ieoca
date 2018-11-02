package com.ieoca.components.algorithm.property;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 * @author Andre Hofmeister
 * @version 1.0
 */
public final class IJComboBox extends IJProperty {
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

  public IJComboBox(Builder builder) {
    super(builder);
    this.elements = builder.elements;
  }

  private String[] getElements() {
    return this.elements.toArray(new String[0]);
  }

  @Override
  public Type getType() {
    return Type.COMBOBOX;
  }

  @Override
  public JComponent create(final JPanel jPanel) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> create(jPanel));
    }

    JLabel jLabel = new JLabel();
    jLabel.setText(this.getName());

    JComboBox<String> jComboBox = new JComboBox<>(this.getElements());

    jPanel.add(jLabel);
    jPanel.add(jComboBox);

    if (this.getValue() == null) return jComboBox;

    jComboBox.setSelectedIndex(this.elements.indexOf(this.getValue().toString()));

    return jComboBox;
  }
}
