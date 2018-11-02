package com.ieoca.components.algorithm;

import com.ieoca.components.algorithm.property.IJCheckBox;
import com.ieoca.components.algorithm.property.IJComboBox;
import com.ieoca.components.algorithm.property.IJProperty;
import com.ieoca.components.algorithm.property.IJRadioButton;
import com.ieoca.components.algorithm.property.IJSlider;
import com.ieoca.components.algorithm.property.IJTextField;
import com.ieoca.components.algorithm.property.annotation.ICheckBox;
import com.ieoca.components.algorithm.property.annotation.IComboBox;
import com.ieoca.components.algorithm.property.annotation.IProperty;
import com.ieoca.components.algorithm.property.annotation.IRadioButton;
import com.ieoca.components.algorithm.property.annotation.ISlider;
import com.ieoca.components.algorithm.property.annotation.ITextField;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * The implementation of this abstract class produces a view for an algorithm component. This view
 * is the interface to the user and provides the input fields for different parameters.
 *
 * <p>Those input fields could easily created with annotations. The following example creates a text
 * field with the default value zero:
 *
 * <p>
 *
 * <pre>
 * {@literal @}IProperty
 * {@literal @}ITextField(name = "Name")
 * private Integer name = 0;
 * </pre>
 *
 * <p>Following annotations exist: {@link
 * com.ieoca.components.algorithm.property.annotation.ICheckBox}, {@link
 * com.ieoca.components.algorithm.property.annotation.IComboBox}, {@link
 * com.ieoca.components.algorithm.property.annotation.IRadioButton}, {@link
 * com.ieoca.components.algorithm.property.annotation.ISlider}, {@link
 * com.ieoca.components.algorithm.property.annotation.ITextField}
 *
 * <p>
 *
 * <p>If a property has changed, this class will call the update method. See {@link #update(String,
 * boolean)}, {@link #update(String, int)} or {@link
 * com.ieoca.components.algorithm.AlgorithmView#update(String, String)}.
 *
 * @author Andre Hofmeister
 * @version 1.0
 */
public abstract class AlgorithmView {
  private final JFrame jFrame;

  private final JPanel jPanel;

  private final JMenuItem jMenuItem;

  private final HashMap<IJProperty, JComponent> components = new HashMap<>();

  /**
   * Notify the implementation that a text field or radio button has changed.
   *
   * @param name property name.
   * @param value new value.
   */
  protected abstract void update(String name, boolean value);

  /**
   * Notify the implementation that a combo box or slider has changes.
   *
   * @param name property name.
   * @param value new value.
   */
  protected abstract void update(String name, int value);

  /**
   * Notify the implementation that a check box has changes.
   *
   * @param name property name.
   * @param value new value.
   */
  protected abstract void update(String name, String value);

  protected AlgorithmView() {
    this.jFrame = new JFrame();
    this.jPanel = new JPanel();
    this.jMenuItem = new JMenuItem();

    this.init();
  }

  /**
   * Change the name of the menue item.
   *
   * @param menuItemName name of the menue item.
   */
  public void setMenuItemName(final String menuItemName) {
    if (menuItemName == null) {
      throw new UnsupportedOperationException();
    }

    if (menuItemName.length() == 0) {
      throw new UnsupportedOperationException();
    }

    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> setMenuItemName(menuItemName));
    }

    this.jMenuItem.setText(menuItemName);
  }

  /**
   * Return the menue item.
   *
   * @return menue item.
   */
  public JMenuItem getMenu() {
    return this.jMenuItem;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AlgorithmView)) return false;

    AlgorithmView that = (AlgorithmView) o;

    if (!components.equals(that.components)) return false;
    if (!jFrame.equals(that.jFrame)) return false;
    if (!jMenuItem.equals(that.jMenuItem)) return false;
    if (!jPanel.equals(that.jPanel)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = jFrame.hashCode();
    result = 31 * result + jPanel.hashCode();
    result = 31 * result + jMenuItem.hashCode();
    result = 31 * result + components.hashCode();
    return result;
  }

  /**
   * This method searches all property annotations and will create them. It should be called from
   * the constructor of the specific implementation of this class.
   */
  protected final void findProperties() {
    ArrayList<Field> fields = new ArrayList<>(AlgorithmView.getAllFields(this.getClass()));

    for (Field field : fields) {
      if (!field.isAnnotationPresent(IProperty.class)) continue;

      Annotation[] annotations = field.getDeclaredAnnotations();

      if (annotations == null) continue;

      if (annotations.length < 2) continue;

      Object object = null;

      field.setAccessible(true);

      try {
        object = field.get(this);
      } catch (IllegalAccessException ignored) {
      }

      field.setAccessible(false);

      IJProperty property = null;

      if (annotations[1] instanceof ITextField) {
        property = AlgorithmView.createIJTextField((ITextField) annotations[1], object);
      } else if (annotations[1] instanceof ICheckBox) {
        property = AlgorithmView.createIJCheckBox((ICheckBox) annotations[1], object);
      } else if (annotations[1] instanceof IComboBox) {
        property = AlgorithmView.createIJComboBox((IComboBox) annotations[1], object);
      } else if (annotations[1] instanceof ISlider) {
        property = AlgorithmView.createIJSlider((ISlider) annotations[1], object);
      } else if (annotations[1] instanceof IRadioButton) {
        property = AlgorithmView.createIJRadioButton((IRadioButton) annotations[1], object);
      }

      if (property == null) return;

      if (this.components.containsKey(property)) return;

      this.append(property);
    }
  }

  private static List<Field> getAllFields(Class<?> type) {
    List<Field> fields = new ArrayList<>();

    for (Class<?> c = type; c != null; c = c.getSuperclass()) {
      fields.addAll(Arrays.asList(c.getDeclaredFields()));
    }

    return fields;
  }

  private static IJProperty createIJTextField(ITextField textField, Object object) {
    IJTextField.Builder textFieldBuilder =
        new IJTextField.Builder(textField.name(), textField.desc());
    textFieldBuilder.value(object);

    return new IJTextField(textFieldBuilder);
  }

  private static IJProperty createIJCheckBox(ICheckBox iCheckBox, Object object) {
    IJCheckBox.Builder checkBoxBuilder = new IJCheckBox.Builder(iCheckBox.name(), iCheckBox.desc());
    checkBoxBuilder.value(object);

    return new IJCheckBox(checkBoxBuilder);
  }

  private static IJProperty createIJComboBox(IComboBox IComboBox, Object object) {
    IJComboBox.Builder comboBoxBuilder = new IJComboBox.Builder(IComboBox.name(), IComboBox.desc());
    comboBoxBuilder.value(object);
    comboBoxBuilder.elements(IComboBox.elements());

    return new IJComboBox(comboBoxBuilder);
  }

  private static IJProperty createIJSlider(ISlider ISlider, Object object) {
    IJSlider.Builder sliderBuilder = new IJSlider.Builder(ISlider.name(), ISlider.desc());
    sliderBuilder.value(object);
    sliderBuilder.min(ISlider.min());
    sliderBuilder.max(ISlider.max());
    sliderBuilder.spaceing(ISlider.spaceing());

    return new IJSlider(sliderBuilder);
  }

  private static IJProperty createIJRadioButton(IRadioButton IRadioButton, Object object) {
    IJRadioButton.Builder radioButtonBuilder =
        new IJRadioButton.Builder(IRadioButton.name(), IRadioButton.desc());
    radioButtonBuilder.value(object);
    radioButtonBuilder.elements(IRadioButton.elements());

    return new IJRadioButton(radioButtonBuilder);
  }

  private void append(final IJProperty property) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> append(property));
    }

    JComponent jComponent = property.create(this.jPanel);
    this.components.put(property, jComponent);

    SpringUtilities.makeCompactGrid(this.jPanel, this.components.size(), 2, 6, 6, 6, 6);
  }

  private void init() {
    this.jFrame.setResizable(false);
    this.jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.jFrame.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            for (Map.Entry<IJProperty, JComponent> entry : components.entrySet()) {
              IJProperty key = entry.getKey();
              update(key.getType(), key.getName(), entry.getValue());
            }
          }
        });

    this.jPanel.setLayout(new SpringLayout());
    this.jPanel.setBorder(new EmptyBorder(3, 3, 3, 3));

    this.jMenuItem.setText("Default");
    this.jMenuItem.addActionListener(e -> show());

    this.jFrame.add(this.jPanel);
  }

  private void show() {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(this::show);
    }

    this.jFrame.setVisible(true);
    this.jFrame.requestFocusInWindow();
    this.jFrame.pack();
  }

  private void update(IJProperty.Type type, String name, JComponent jComponent) {
    switch (type) {
      case TEXTFIELD:
        this.update(name, ((JTextField) jComponent).getText());
        break;
      case CHECKBOX:
        this.update(name, ((JCheckBox) jComponent).isSelected());
        break;
      case COMBOBOX:
        this.update(name, ((JComboBox) jComponent).getSelectedIndex());
        break;
      case SLIDER:
        this.update(name, ((JSlider) jComponent).getValue());
        break;
      case RADIOBUTTON:
        for (Component component : jComponent.getComponents()) {
          if (((JRadioButton) component).isSelected()) {
            this.update(name, ((JRadioButton) component).getActionCommand());
          }
        }
        break;
    }
  }
}
