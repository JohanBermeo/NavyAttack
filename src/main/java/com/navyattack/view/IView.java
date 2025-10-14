package com.navyattack.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface IView {
   void start(Stage stage);
   Scene getScene();
}