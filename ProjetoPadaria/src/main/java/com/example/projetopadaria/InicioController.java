package com.example.projetopadaria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InicioController {

    @FXML
    private AnchorPane root;
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
    @FXML
    protected void sobreClick(ActionEvent e){HelloApplication.janelas(4);}

    private Stage stage;
    @FXML
    protected void onSairButtonClick(){
        fechartudo();
    }
    private void fechartudo(){
        stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
