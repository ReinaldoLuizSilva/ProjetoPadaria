package com.example.projetopadaria;

import com.example.projetopadaria.database.PedidoDAO;
import com.example.projetopadaria.database.ProdutoDAO;
import com.example.projetopadaria.database.entidades.Pedido;
import com.example.projetopadaria.database.entidades.Produto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PedidoController {

    @FXML
    private AnchorPane pedidobox;
    @FXML
    private TextField nomeText;
    @FXML
    private TextField telefoneText;
    @FXML
    private TextField totalText;
    @FXML
    private Button botaoVoltar;
    @FXML
    private TableView<Pedido> tabelaPedido;
    @FXML
    private TableColumn<Pedido, Integer> colunaId;
    @FXML
    private TableColumn<Pedido, String> colunaNome;
    @FXML
    private TableColumn<Pedido, String> colunaTelefone;
    @FXML
    private TableColumn<Pedido, Double> colunaTotal;
    @FXML
    private TableColumn<Pedido, Date> colunaData;
    @FXML
    private Label modoLabel;
    @FXML
    private Button sairModoEdicaoButton;
    @FXML
    private TextField pesquisaPorIdText;
    @FXML
    private TextField pesquisaPorNomeText;
    @FXML
    protected void produtoClick(ActionEvent e){
        HelloApplication.janelas(1);
    }
    @FXML
    protected void funcionarioClick(ActionEvent e){
        HelloApplication.janelas(3);
    }
    @FXML
    protected void sobreClick(ActionEvent e){HelloApplication.janelas(4);}
    private boolean modoEdicao;
    private Pedido pedidoEmEdicao;
    private Stage stage;

    @FXML
    protected void initialize() throws Exception{
        colunaId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colunaNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colunaTotal.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());
        colunaTelefone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefone()));
        colunaData.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getData().toString()));
        setModoInsercao();
        mostrarTodosPedido();
    }

    @FXML
    protected void onPesquisaRecenteButtonClick() throws Exception{
        Pedido pedido = PedidoDAO.consultaPedidoRecente();
        if(pedido != null){
            List<Pedido> pedidos = new ArrayList<>();
            pedidos.add(pedido);
            carregaTabela(pedidos);
            limpaCampos();
        }
    }

    @FXML
    protected void onPesquisaNomeButtonClick()throws Exception{
        String nomeBusca = pesquisaPorNomeText.getText();
        List<Pedido> pedidos = PedidoDAO.consultaNome(nomeBusca);
        if (!pedidos.isEmpty()) {
            carregaTabela(pedidos);
            limpaCampos();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText(null);
            alert.setContentText("Nenhum pedido encontrado com o nome informado.");
            alert.showAndWait();
        }
    }


    @FXML
    protected void onPesquisaTodosButtonClick() throws Exception {
        mostrarTodosPedido();
    }

    @FXML
    protected void onPesquisaPorIdButtonClick() throws Exception {
        try {
            int idBusca = Integer.parseInt(pesquisaPorIdText.getText());
            if (idBusca <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, insira um ID válido (maior que zero).");
                alert.showAndWait();
                return;
            }
            Pedido pedido = PedidoDAO.obterPorId(idBusca);
            if (pedido != null) {
                List<Pedido> pedidos = new ArrayList<>();
                pedidos.add(pedido);
                carregaTabela(pedidos);
                limpaCampos();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação");
                alert.setHeaderText(null);
                alert.setContentText("Nenhum pedido encontrado com o ID informado.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, insira um número inteiro para o ID.");
            alert.showAndWait();
        }
    }


    @FXML
    protected void onExcluirButtonClick() throws Exception {
        Pedido pedido = tabelaPedido.getSelectionModel().getSelectedItem();
        if(pedido != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exclusão");
            alert.setHeaderText("Você solicitou a exclusão do aluno de id " + pedido.getId() + " (" + pedido.getNome() + ")");
            alert.setContentText("Confirma a exclusão?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                int quantidadeModificacoes = PedidoDAO.exclui(pedido);
                if(quantidadeModificacoes > 0){
                    mostrarTodosPedido();
                    setModoInsercao();
                }
            }
        }
    }

    @FXML
    protected void onEditarButtonClick(){
        Pedido pedido = tabelaPedido.getSelectionModel().getSelectedItem();
        if(pedido != null){
            setModoEdicao(pedido);
        }
    }

    @FXML
    protected void onSalvarClienteButtonClick() throws Exception {
        int quantidadeModificacoes = 0;
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        double total = 0.0;

        try {
            total = Double.parseDouble(totalText.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, insira um valor numérico para o total.");
            alert.showAndWait();
            return;
        }

        if(modoEdicao){
            pedidoEmEdicao.setNome(nome);
            pedidoEmEdicao.setTelefone(telefone);
            pedidoEmEdicao.setTotal(total);
            quantidadeModificacoes = PedidoDAO.atualiza(pedidoEmEdicao);
        }else{
            Pedido pedido = new Pedido(nome,telefone,total);
            PedidoDAO.inserirPedido(pedido);
            limpaCampos();
            mostrarTodosPedido();
        }

        if(quantidadeModificacoes > 0){
            mostrarTodosPedido();
            setModoInsercao();
            limpaCampos();
        }

    }
    @FXML
    protected void onSairButtonClick(){
        fechartudo();
    }
    private void fechartudo(){
        stage = (Stage) pedidobox.getScene().getWindow();
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

    @FXML
    protected void onSairModoEdicaoButtonClick(){
        setModoInsercao();
        limpaCampos();
    }

    private void carregaTabela(List<Pedido> pedidos) throws Exception {
        ObservableList<Pedido> PedidoList = FXCollections.observableArrayList();
        PedidoList.addAll(pedidos);
        tabelaPedido.setItems(PedidoList);
    }

    private void mostrarTodosPedido() throws Exception {
        List<Pedido> pedidos = PedidoDAO.selecionarTudo();
        carregaTabela(pedidos);
    }

    private void limpaCampos(){
        nomeText.setText("");
        totalText.setText("");
        telefoneText.setText("");
    }

    private void setModoInsercao(){
        modoEdicao = false;
        modoLabel.setText("Modo: inserção");
        sairModoEdicaoButton.setVisible(false);
    }

    private void setModoEdicao(Pedido pedido){
        modoEdicao = true;
        modoLabel.setText("Modo: edição");
        nomeText.setText(pedido.getNome());
        telefoneText.setText(pedido.getTelefone());
        totalText.setText(String.valueOf(pedido.getTotal()));
        pedidoEmEdicao = pedido;
        sairModoEdicaoButton.setVisible(true);
    }
}
