package org.cyland.cydraw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static org.cyland.cydraw.contract.Contract.VIEW_SLUG;

final public class Launch extends Application {

  public static Stage stage;

  private final static String APPLICATION_NAME = "CyDraw";
  private final static String VIEW_NAME = "/main_stage.fxml";

  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;

    Parent root = FXMLLoader.load(getClass().getResource(VIEW_SLUG + VIEW_NAME));

    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setTitle(APPLICATION_NAME);
    primaryStage.setScene(new Scene(root));

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
