package com.example.projetopadaria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Scene scenePedido, sceneProduto, sceneFuncionario, sceneSobre, sceneLoader;
    private static Stage primeiroStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inicio.fxml"));
        FXMLLoader fxmlpedido = new FXMLLoader(getClass().getResource("pedido-view.fxml"));
        FXMLLoader fxmlproduto = new FXMLLoader(getClass().getResource("produto-view.fxml"));
        FXMLLoader fxmlfuncionario = new FXMLLoader(getClass().getResource("funcionario-view.fxml"));
        FXMLLoader fxmlSobre = new FXMLLoader(getClass().getResource("sobre.fxml"));

        primeiroStage = stage;
        primeiroStage.setTitle("Inicio");

        Parent parentLoader = fxmlLoader.load();
        Parent parentPedido = fxmlpedido.load();
        Parent parentProduto = fxmlproduto.load();
        Parent parentFuncionario = fxmlfuncionario.load();
        Parent parentSobre = fxmlSobre.load();

        sceneLoader = new Scene(parentLoader, 600, 400);
        sceneFuncionario = new Scene(parentFuncionario, 1000 ,500);
        scenePedido = new Scene(parentPedido, 1000 ,500);
        sceneProduto = new Scene(parentProduto, 1000 ,500);
        sceneSobre = new Scene(parentSobre, 600 ,500);

        stage.setScene(sceneLoader);
        stage.show();
    }
    public static void janelas(int opcao){
        switch (opcao) {
            case 1 :
                primeiroStage.setScene(sceneProduto);
                primeiroStage.setTitle("Produto");
                break;
            case 2:
                primeiroStage.setScene(scenePedido);
                primeiroStage.setTitle("Pedido");
                break;
            case 3:
                primeiroStage.setScene(sceneFuncionario);
                primeiroStage.setTitle("Funcionario");
                break;
            case 4:
                primeiroStage.setScene(sceneSobre);
                primeiroStage.setTitle("Sobre");
                break;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}