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
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicCommandsTabController implements Initializable {

  @FXML
  private TextField desiredX;

  @FXML
  private TextField desiredY;

  private int currentX;
  private int currentY;

  @FXML
  private Button sendButton;

  @FXML
  private Label responseField;

  @FXML
  private Button disconnectButton;

  @FXML
  private ComboBox<SerialPort> devicesList;

  @FXML
  private Button connectButton;

  private SerialPort openedPort;

  private final ExecutorService executorService;

  public BasicCommandsTabController() {
    this.executorService = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r);
      t.setDaemon(true);
      return t;
    });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  @FXML
  void connectToDevice(MouseEvent event) {

    devicesList.setDisable(true);
    connectButton.setDisable(true);
    SerialPort comPort = devicesList.getValue();
    if (comPort.openPort()) {
      openedPort = comPort;
      openedPort.setBaudRate(115200);

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
    } else {
      responseField.setText("Something went wrong");
      devicesList.setDisable(false);
      connectButton.setDisable(false);
    }
  }

  void sendCommand(String commandToSend) {

    String command = commandToSend + "\r";

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
      responseField.setText("Disconnected successfully");
    } else {
      disconnectButton.setDisable(false);
      responseField.setText("Something went wrong while closing port");
    }
  }

  public void gotoXY(MouseEvent mouseEvent) {

      int gotoX = Integer.parseInt(desiredX.getText());
      int gotoY = Integer.parseInt(desiredY.getText());

      int dx = gotoX - currentX;
      int dy = gotoY - currentY;

      String command = String.format("G91 G1 F5000 X %d Y %d", dx, dy);
      sendCommand(command);

      currentX = currentX + dx;
      currentY = currentY + dy;
  }
}
