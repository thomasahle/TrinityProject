package com.github.thomasahle.trainbox.trainbox.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;

public class TrainBoxActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/github/thomasahle/trainbox/trainbox/resources");
    PlayN.run(new TrainBox());
  }
}
