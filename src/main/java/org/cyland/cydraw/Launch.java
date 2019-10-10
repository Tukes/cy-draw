package org.cyland.cydraw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cyland.cydraw.controller.MainStageController;

import static org.cyland.cydraw.contract.Contract.VIEW_SLUG;

final public class Launch extends Application {

  private static Stage stage;

  private static MainStageController controller;
  private final static String APPLICATION_NAME = "CyDraw";

  private final static String VIEW_NAME = "/main_stage.fxml";
  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;

    FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_SLUG + VIEW_NAME));
    Pane root = loader.load();
    controller = loader.getController();

    primaryStage.setTitle(APPLICATION_NAME);
    primaryStage.setScene(new Scene(root));
    primaryStage.initStyle(StageStyle.TRANSPARENT);
    primaryStage.getScene().setFill(Color.TRANSPARENT);

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  private void setStage(Stage stage) {
    Launch.stage = stage;
  }

  private void setController(MainStageController controller) {
    Launch.controller = controller;
  }

  public static Stage getStage() {
    return stage;
  }

  public static MainStageController getController() {
    return controller;
  }
}
