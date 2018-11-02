package com.ieoca.components.problem.listener;

import com.ieoca.components.problem.Problem;
import java.util.EventListener;

public interface ProblemListener extends EventListener {
  void update(Problem source, ProblemEvent e);
}
