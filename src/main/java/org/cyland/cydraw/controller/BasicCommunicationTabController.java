package org.cyland.cydraw.controller;

import com.fazecast.jSerialComm.SerialPort;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicCommunicationTabController implements Initializable {

  @FXML
  private Button disconnectButton;

  @FXML
  private ComboBox<SerialPort> devicesList;

  @FXML
  private Button connectButton;

  @FXML
  private TextField commandToSend;

  @FXML
  private Button sendButton;

  @FXML
  private Label responseField;

  private SerialPort openedPort;

  private final ExecutorService executorService;

  public BasicCommunicationTabController() {
    this.executorService = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r);
      t.setDaemon(true);
      return t;
    });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    commandToSend.setDisable(true);
    sendButton.setDisable(true);
  }

  @FXML
  void connectToDevice(MouseEvent event) {

    devicesList.setDisable(true);
    connectButton.setDisable(true);
    SerialPort comPort = devicesList.getValue();
    if (comPort.openPort()) {
      openedPort = comPort;
      openedPort.setBaudRate(115200);
      commandToSend.setDisable(false);
      sendButton.setDisable(false);

      Task<String> readTask = new Task<String>() {
        @Override
        protected String call() throws Exception {
          byte[] readBuffer = new byte[1024];
          int bytesRead = openedPort.readBytes(readBuffer, readBuffer.length);
          return new String(readBuffer, StandardCharsets.US_ASCII);
        }
      };

      responseField.setText("Connected successfully! ");
      readTask.setOnSucceeded(event1 -> responseField.setText("Connected successfully: " + readTask.getValue()));
      executorService.submit(readTask);
      disconnectButton.setDisable(false);
      disconnectButton.setVisible(true);
    }
    else {
      responseField.setText("Something went wrong");
      devicesList.setDisable(false);
      connectButton.setDisable(false);
    }
  }

  @FXML
  void sendCommand(MouseEvent event) {

    String command = commandToSend.getText() + "\r";

    Task<Void> sendTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        byte[] commandBytes = command.getBytes(StandardCharsets.US_ASCII);
        openedPort.writeBytes(commandBytes, commandBytes.length);
        return null;
      }
    };
    sendTask.setOnSucceeded(event1 -> responseField.setText("Command sent"));

    Task<String> readTask = new Task<String>() {
      @Override
      protected String call() throws Exception {
        byte[] readBuffer = new byte[1024];
        int bytesRead = openedPort.readBytes(readBuffer, readBuffer.length);
        return new String(readBuffer, StandardCharsets.US_ASCII);
      }
    };
    readTask.setOnSucceeded(event1 -> responseField.setText("response: " + readTask.getValue()));

    executorService.submit(sendTask);
    executorService.submit(readTask);
  }

  @FXML
  void updateAvailableDevices(MouseEvent event) {

    devicesList.getItems().setAll(SerialPort.getCommPorts());
  }

  @FXML
  void disconnectFromDevice(MouseEvent mouseEvent) {

    disconnectButton.setDisable(true);
    if (openedPort.closePort()) {
      disconnectButton.setVisible(false);
      devicesList.setDisable(false);
      connectButton.setDisable(false);
      commandToSend.setDisable(true);
      sendButton.setDisable(true);
      responseField.setText("Disconnected successfully");
    }
    else {
      disconnectButton.setDisable(false);
      responseField.setText("Something went wrong while closing port");
    }
  }
}
