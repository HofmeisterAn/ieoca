package com.ieoca.components.algorithm.property;

import javax.swing.*;

/**
 * @author Andre Hofmeister
 * @version 1.0
 */
public final class IJTextField extends IJProperty {
  public static final class Builder extends IJProperty.Builder<Builder> {
    public Builder(String name, String desc) {
      super(name, desc);
    }

    @Override
    public Builder getThis() {
      return this;
    }
  }

  public IJTextField(Builder builder) {
    super(builder);
  }

  @Override
  public Type getType() {
    return Type.TEXTFIELD;
  }

  @Override
  public JComponent create(final JPanel jPanel) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> create(jPanel));
    }

    JLabel jLabel = new JLabel();
    jLabel.setText(this.getName());

    JTextField jTextField = new JTextField();

    jPanel.add(jLabel);
    jPanel.add(jTextField);

    if (this.getValue() == null) return jTextField;

    jTextField.setText(this.getValue().toString());

    return jTextField;
  }
}
