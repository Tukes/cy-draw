package org.cyland.cydraw.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.cyland.cydraw.Launch;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.cyland.cydraw.contract.Contract.VIEW_SLUG;

public class MainStageController implements Initializable {

  @FXML
  private AnchorPane parent;

  @FXML
  private HBox header;

  @FXML
  public VBox leftPanel;

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

    addBasicSerialCommunicationTab();
    addBasicCommandsTab();

    makeStageDraggable();
    fixBugWithMaximizingAfterMinimizing();
  }

  public static void setContent(Node... elements){

    Launch.getController().content.getChildren().setAll(elements);
  }

  private void addBasicSerialCommunicationTab() {

    Pane tab;
    try {
      tab = FXMLLoader.load(getClass().getResource(VIEW_SLUG + "/basic_communication_tab_icon.fxml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    leftPanel.getChildren().add(tab);
  }

  private void addBasicCommandsTab() {

    Pane tab;
    try {
      tab = FXMLLoader.load(getClass().getResource(VIEW_SLUG + "/basic_commands_tab_icon.fxml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    leftPanel.getChildren().add(tab);
  }

  private void fixBugWithMaximizingAfterMinimizing() {

    Launch.getStage().iconifiedProperty().addListener((observable, oldValue, newValue) -> {
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
      Launch.getStage().setOpacity(0.8);
      Launch.getStage().setX(event.getScreenX() - xOffset);
      Launch.getStage().setY(event.getScreenY() - yOffset);
    });

    draggableBar.setOnDragDone(event -> Launch.getStage().setOpacity(1.0));
    draggableBar.setOnMouseReleased(event -> Launch.getStage().setOpacity(1.0));
  }

  @FXML
  void closeApplication(MouseEvent event) {

    Platform.exit();
  }

  @FXML
  void minimizeStage(MouseEvent event) {

    Launch.getStage().setIconified(true);
  }
}
