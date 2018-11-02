package com.ieoca.components.algorithm.property;

import com.ieoca.components.utility.TypeConverter;
import javax.swing.*;

/**
 * @author Andre Hofmeister
 * @version 1.0
 */
public final class IJCheckBox extends IJProperty {
  public static final class Builder extends IJProperty.Builder<Builder> {
    public Builder(String name, String desc) {
      super(name, desc);
    }

    @Override
    public Builder getThis() {
      return this;
    }
  }

  public IJCheckBox(Builder builder) {
    super(builder);
  }

  @Override
  public Type getType() {
    return Type.CHECKBOX;
  }

  @Override
  public JComponent create(final JPanel jPanel) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> create(jPanel));
    }

    JLabel jLabel = new JLabel();
    jLabel.setText(this.getName());

    JCheckBox jCheckBox = new JCheckBox();

    jPanel.add(jLabel);
    jPanel.add(jCheckBox);

    if (this.getValue() == null) return jCheckBox;

    Boolean value = TypeConverter.toBoolean(this.getValue());

    jCheckBox.setSelected(value);

    return jCheckBox;
  }
}
