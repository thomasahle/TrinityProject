package com.github.thomasahle.trainbox.trainbox.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;

public class TrainBoxJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/github/thomasahle/trainbox/trainbox/resources");
    PlayN.run(new TrainBox());
  }
}
