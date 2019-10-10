package org.cyland.cydraw.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.cyland.cydraw.contract.Contract.VIEW_SLUG;

public class BasicCommandsTabIconController implements Initializable {

  @FXML
  private Label basicCommandsTab;

  private Pane content;

  public void openBasicCommandsTab(MouseEvent mouseEvent) {

    MainStageController.setContent(content);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    try {
      content = FXMLLoader.load(getClass().getResource(VIEW_SLUG + "/basic_commands_tab.fxml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
