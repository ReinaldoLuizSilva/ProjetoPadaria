package com.example.projetopadaria;

import com.example.projetopadaria.database.ConexaoDatabase;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProdutoController {

    @FXML
    private AnchorPane produtobox;
    @FXML
    private TextField nomeText;
    @FXML
    private TextField precoText;
    @FXML
    private TextField validadeText;
    @FXML
    private TextField quantidadeText;
    @FXML
    private Button botaoVoltar;
    @FXML
    private Button sairModoEdicaoButton;
    @FXML
    private TableView<Produto> tabelaProduto;
    @FXML
    private TableColumn<Produto, Integer> colunaId;
    @FXML
    private TableColumn<Produto, String> colunaNome;
    @FXML
    private TableColumn<Produto, Double> colunaPreco;
    @FXML
    private TableColumn<Produto, Date> colunaValidade;
    @FXML
    private TableColumn<Produto, Integer> colunaQuantidade;
    @FXML
    private TextField pesquisaPorIdText;
    @FXML
    private Label modoLabel;
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
    private boolean modoEdicao;
    private Produto produtoEmEdicao;
    private Stage stage;

    @FXML
    protected void onSalvarClienteButtonClick() throws Exception {
        int quantidadeModificacoes = 0;
        String nome = nomeText.getText();
        double preco = 0.0;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Date validade = dateFormatter.parse(validadeText.getText());
        java.sql.Date dataValidade = new java.sql.Date(validade.getTime());
        int quantidade = 0;

        try {
            preco = Double.parseDouble(precoText.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, insira um valor numérico para o preço.");
            alert.showAndWait();
            return;
        }

        try{
            quantidade = Integer.parseInt(quantidadeText.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, insira um valor inteiro para a quantidade.");
            alert.showAndWait();
            return;
        }

        if (modoEdicao) {
            produtoEmEdicao.setNome(nome);
            produtoEmEdicao.setPreco(preco);
            produtoEmEdicao.setQuantidade(quantidade);
            produtoEmEdicao.setValidade(dataValidade);
            quantidadeModificacoes = ProdutoDAO.atualiza(produtoEmEdicao);
        } else {
            Produto produto = new Produto(nome, preco, dataValidade, quantidade);
            ProdutoDAO.inserirProduto(produto);
            limpaCampos();
            mostrarTodosProdutos();
        }

        if (quantidadeModificacoes > 0) {
            mostrarTodosProdutos();
            setModoInsercao();
            limpaCampos();
        }
    }


    @FXML
    protected void onPesquisaTodosButtonClick() throws Exception {
        mostrarTodosProdutos();
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

            Produto produto = ProdutoDAO.obterPorId(idBusca);
            if(produto != null){
                List<Produto> produtos = new ArrayList<>();
                produtos.add(produto);
                carregaTabela(produtos);
                limpaCampos();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação");
                alert.setHeaderText(null);
                alert.setContentText("Nenhum produto encontrado com o ID informado.");
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


    //botao para fechar tudo
    @FXML
    protected void onSairButtonClick(){
        fechartudo();
    }
    private void fechartudo(){
        stage = (Stage) produtobox.getScene().getWindow();
        stage.close();
    }

    //botao para voltar para pagina inicial
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

    private void limpaCampos(){
        nomeText.setText("");
        precoText.setText("");
        quantidadeText.setText("");
        validadeText.setText("");
    }

    @FXML
    protected void initialize() throws Exception {
        colunaId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colunaNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colunaValidade.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getValidade().toString()));
        colunaPreco.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPreco()).asObject());
        colunaQuantidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject());
        setModoInsercao();
        mostrarTodosProdutos();
    }

    @FXML
    protected  void onPesquisaVencimentoButtonClick() throws Exception{
        List<Produto> produtos = ProdutoDAO.obterConsultaVencimento();
        if (produtos != null){
            carregaTabela(produtos);
            limpaCampos();
        }
    }

    @FXML
    protected void onPesquisaCarosButtonClick() throws Exception{
        List<Produto> produtos = ProdutoDAO.obterProdutosCaros();
        carregaTabela(produtos);
        limpaCampos();
    }

    @FXML
    protected void onEditarButtonClick(){
        Produto produto = tabelaProduto.getSelectionModel().getSelectedItem();
        if(produto != null){
            setModoEdicao(produto);
        }
    }

    //carrega a tabela e insere os dados
    private void carregaTabela(List<Produto> produtos) throws Exception {
        ObservableList<Produto> ProdutoList = FXCollections.observableArrayList();
        ProdutoList.addAll(produtos);
        tabelaProduto.setItems(ProdutoList);
    }

    private void mostrarTodosProdutos() throws Exception {
        List<Produto> produtos = ProdutoDAO.selecionarProdutos();
        carregaTabela(produtos);
    }

    @FXML
    protected void onExcluirButtonClick() throws Exception {
        Produto produto = tabelaProduto.getSelectionModel().getSelectedItem();
        if(produto != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exclusão");
            alert.setHeaderText("Você solicitou a exclusão do aluno de id " + produto.getId() + " (" + produto.getNome() + ")");
            alert.setContentText("Confirma a exclusão?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                int quantidadeModificacoes = ProdutoDAO.exclui(produto);
                if(quantidadeModificacoes > 0){
                    mostrarTodosProdutos();
                    setModoInsercao();
                }
            }
        }
    }

    private void setModoEdicao(Produto produto){
        modoEdicao = true;
        modoLabel.setText("Modo: edição");
        nomeText.setText(produto.getNome());
        precoText.setText(String.valueOf(produto.getPreco()));
        quantidadeText.setText(String.valueOf(produto.getQuantidade()));
        java.sql.Date validade = produto.getValidade();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = dateFormatter.format(validade);
        validadeText.setText(dataFormatada);

        produtoEmEdicao = produto;
        sairModoEdicaoButton.setVisible(true);
    }

    private void setModoInsercao(){
        modoEdicao = false;
        modoLabel.setText("Modo: inserção");
        sairModoEdicaoButton.setVisible(false);
    }
}
