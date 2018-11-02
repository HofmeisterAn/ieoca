package com.ieoca.mvc.controller;

import com.ieoca.components.algorithm.Algorithm;
import com.ieoca.components.algorithm.AlgorithmView;
import com.ieoca.components.problem.Problem;
import com.ieoca.components.problem.ProblemView;
import com.ieoca.loader.JarLoader;
import com.ieoca.mvc.model.MainModel;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

/**
 * todo: Maybe don't use a complex data structure for the models? todo: Remove 'cid'. Use start of
 * class name.
 */
public class MainController extends AbstractController {
  private final String[] cid = new String[] {"com.ieoca.algorithm", "com.ieoca.problem"};

  private final JarLoader jcl = new JarLoader(new URL[] {});

  private MainModel model;

  public MainController() {
    this.jcl.setupCertificate();
  }

  public void loadNewComponent(File file) {
    try {
      String className = this.jcl.addComponent(file);

      Class c = this.jcl.loadClass(className);

      if (className.startsWith(this.cid[0])) {
        Class p = c.asSubclass(Algorithm.class);
        this.model.addAlgorithm((Algorithm<AlgorithmView>) p.newInstance());
      } else if (className.startsWith(this.cid[1])) {
        Class p = c.asSubclass(Problem.class);
        this.model.addProblem((Problem<ProblemView>) p.newInstance());
      }
      /**
       * Could not recognize if the component is a problem or an algorithm. Maybe there is a better
       * way to verify the component! Anyway, the component will not load.
       */
    } catch (NullPointerException
        | ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | MalformedURLException
        | CertificateException
        | IllegalArgumentException e) {
      /**
       * Probably the manifest attribute Class-Name does not exist. The attribute contains the name
       * of the class and is necessary to create an instance of a component. The class has to
       * derived from Problem<View> or Algorithm<View>.
       */
      e.printStackTrace();
    }
  }

  public void unloadAll() {
    this.model.unloadAll();
  }

  public void unloadComponent(Problem problem) {
    this.model.unloadComponent(problem);
  }

  public void start() {
    ArrayList<Runnable> runnables = new ArrayList<>();

    ExecutorService executor = Executors.newCachedThreadPool();

    for (int i = 0; i < this.model.getAlgorithms().length; i++) {
      Runnable runnable =
          MainController.getRunnable(this.model.getAlgorithm(i), this.model.getProblem(i));
      runnables.add(runnable);
    }

    for (Runnable runnable : runnables) {
      executor.execute(runnable);
    }
  }

  public void singleStep() {
    System.out.println("Single-Step called!");
  }

  public void stop() {
    for (int i = 0; i < this.model.getAlgorithms().length; i++) {
      Algorithm algorithm = this.model.getAlgorithm(i);
      algorithm.setInterrupt(true);
    }
  }

  public void reset() {
    for (int i = 0; i < this.model.getProblems().length; i++) {
      Problem problem = this.model.getProblem(i);
      problem.reset();
    }
  }

  @Override
  protected void setModelProperty(Class modelClass, String propertyName, Object newValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void updateModel() {
    this.model = (MainModel) this.getModel();
  }

  private static Runnable getRunnable(final Algorithm algorithm, final Problem problem) {
    if (algorithm == null) throw new NullPointerException();

    if (problem == null) throw new NullPointerException();

    Error error = problem.isStartable();

    if (error != null) {
      JOptionPane.showMessageDialog(null, error.getMessage());
      return null;
    }

    algorithm.setProperties(problem);
    algorithm.setInterrupt(false);

    return algorithm::run;
  }
}
