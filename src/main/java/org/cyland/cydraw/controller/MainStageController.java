package org.cyland.cydraw.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.cyland.cydraw.Launch;

import java.net.URL;
import java.util.ResourceBundle;

public class MainStageController implements Initializable {

  @FXML
  private AnchorPane parent;

  @FXML
  private HBox header;

  @FXML
  private Pane content;

  @FXML
  private Pane draggableBar;

  @FXML
  public Label minimizeLabel;

  private double xOffset;
  private double yOffset;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    makeStageDraggable();
    fixBugWithMaximizingAfterMinimizing();
  }

  private void fixBugWithMaximizingAfterMinimizing() {
    Launch.stage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue){
        minimizeLabel.setVisible(false);
        minimizeLabel.setVisible(true);
      }
    });
  }

  private void makeStageDraggable() {

    draggableBar.setOnMousePressed(event -> {
      xOffset = event.getSceneX();
      yOffset = event.getSceneY();
    });

    draggableBar.setOnMouseDragged(event -> {
      Launch.stage.setOpacity(0.8);
      Launch.stage.setX(event.getScreenX() - xOffset);
      Launch.stage.setY(event.getScreenY() - yOffset);
    });

    draggableBar.setOnDragDone(event -> Launch.stage.setOpacity(1.0));
    draggableBar.setOnMouseReleased(event -> Launch.stage.setOpacity(1.0));
  }

  @FXML
  void closeApplication(MouseEvent event) {

    Platform.exit();
  }

  @FXML
  void minimizeStage(MouseEvent event) {

    Launch.stage.setIconified(true);
  }
}
