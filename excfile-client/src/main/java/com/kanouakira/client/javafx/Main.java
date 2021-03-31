package com.kanouakira.client.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author KanouAkira
 * @date 2021/3/29 14:11
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Main.class.getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("ExcFile");
        primaryStage.setScene(new Scene(root));
        // 退出时结束子程序
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }

    /**
     * Creates a popup containing error without any pretty things for a graphical way to show errors
     * @param error Error string to show
     */
    public static void showError(String error) {
        if (Platform.isFxApplicationThread()) {
            initError(error);
        } else {
            Platform.runLater(() -> {
                initError(error);
            });
        }
    }

    private static void initError(String error) {
        Stage s = new Stage();
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        v.getChildren().add(new Label("Error: " + error));
        Scene sc = new Scene(v, 200 ,100);
        s.setTitle("Fail");
        s.setScene(sc);
        s.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}