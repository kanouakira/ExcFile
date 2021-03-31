package com.kanouakira.client.javafx;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author KanouAkira
 * @date 2021/3/29 14:11
 */
public class Controller {
    @FXML
    private ListView taskList;
    @FXML
    private Label chosenFileName;
    @FXML
    private TextField hostText;
    @FXML
    private TextField portText;

    private File uploadFile = null;

    final ExecutorService exec = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    /**
     * 初始化函数，自动在构造函数后调用
     */
    public void initialize() {
        System.out.println("Controller initialize");
        hostText.setText("127.0.0.1");
        portText.setText("10888");

        // 设置ListView的CellFactory
        taskList.setCellFactory((Callback<ListView<TaskCell>, ListCell<TaskCell>>) list -> {
            final ListCell<TaskCell> cell = new ListCell<>();
            final ChangeListener<Worker.State> stateListener = (observable, oldValue, newValue) -> cell.setGraphic(updateGraphicFromState(cell.getItem(), exec));
            cell.itemProperty().addListener((observable, oldTask, newTask) -> {
                if (oldTask != null) {
                    oldTask.stateProperty().removeListener(stateListener);
                }
                if (newTask != null) {
                    cell.setGraphic(updateGraphicFromState(newTask, exec));
                    newTask.stateProperty().addListener(stateListener);
                }
            });
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return cell;
        });
    }

    /**
     * 选择文件按钮点击
     */
    @FXML
    private void chooseFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        uploadFile = fileChooser.showOpenDialog(new Stage());
        chosenFileName.setText(uploadFile.getName());
    }

    @FXML
    private void clickListView(){
        if (hostText.getText() == null || "".equals(hostText.getText())){
            Main.showError("主机地址不合法！");
            return;
        }
        String host = hostText.getText();
        if (portText.getText() == null || "".equals(portText.getText())){
            Main.showError("端口不合法！");
            return;
        }
        int port = Integer.valueOf(portText.getText());
        if (uploadFile == null) {
            Main.showError("请选择需要上传的文件！");
            return;
        }
        taskList.getItems().add(new TaskCell(host, port, uploadFile));
        chosenFileName.setText("");
        uploadFile = null;
    }


    private Node updateGraphicFromState(final TaskCell task, final ExecutorService exec) {
        if (task == null) { // shouldn't happen...
            throw new IllegalArgumentException("Null task");
        }
        // 设置任务名
        final Label taskName = new Label();
        taskName.setPrefWidth(80);
        taskName.setText(task.fileName());
        // 绑定进度条
        final ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(180);
        progressBar.progressProperty().bind(task.progressProperty());
        // 按钮
        Button pauseButton = null;
        Button button = new Button();
        // 获取任务不同状态展示不同的内容
        final Worker.State state = task.getState();
        if (state == Worker.State.READY) {
//            exec.submit(task);
            button.setText("开始");
            button.setOnAction(event -> exec.submit(task));
        } else if (state == Worker.State.SCHEDULED) {
            return new Label(task.fileName() + " Waiting to run");
        } else if (state == Worker.State.RUNNING) {
            pauseButton = new Button();
            pauseButton.textProperty().bind(task.getBtnText());
            pauseButton.setOnAction(event -> {
                task.pauseTask(!task.isPause());
            });
            button.setText("取消");
            button.setOnAction(event -> task.cancel());
        } else if (state == Worker.State.FAILED) {
            return new Label(task.fileName() + " Error");
        } else if (state == Worker.State.CANCELLED) {
            return new Label(task.fileName() + " Cancelled");
        } else if (state == Worker.State.SUCCEEDED) {
            return new Label(task.fileName() + " Finished");
        } else {
            System.out.println("Unexpected state: " + state);
            return null;
        }
        final HBox hbox = new HBox(10);
        if (pauseButton != null){
            hbox.getChildren().addAll(taskName, progressBar, pauseButton, button);
        }else {
            hbox.getChildren().addAll(taskName, progressBar, button);
        }
        return hbox;
    }
}
