package com.example.projetopadaria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SobreController {
    @FXML
    private AnchorPane sobreBox;
    @FXML
    private Button botaoVoltar;
    @FXML
    protected void produtoClick(ActionEvent e){
        HelloApplication.janelas(1);
    }
    @FXML
    protected void pedidoClick(ActionEvent e){
        HelloApplication.janelas(2);
    }
    @FXML
    protected void funcionarioClick(ActionEvent e){
        HelloApplication.janelas(3);
    }

    private Stage stage;
    @FXML
    protected void onSairButtonClick(){
        fechartudo();
    }
    private void fechartudo(){
        stage = (Stage) sobreBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onVoltarButtonClick(){
        Stage stage = (Stage) botaoVoltar.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicio.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
