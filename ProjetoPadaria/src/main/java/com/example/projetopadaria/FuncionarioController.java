package com.example.projetopadaria;

import com.example.projetopadaria.database.FuncionarioDAO;
import com.example.projetopadaria.database.entidades.Funcionario;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionarioController {

    @FXML
    private AnchorPane funcionariobox;
    @FXML
    private TextField nomeText;
    @FXML
    private TextField cargoText;
    @FXML
    private TextField salarioText;
    @FXML
    private TextField telefoneText;
    @FXML
    private Button botaoVoltar;
    @FXML
    private TableView<Funcionario> tabelaFuncionario;
    @FXML
    private Button sairModoEdicaoButton;
    @FXML
    private TableColumn<Funcionario, Integer> colunaId;
    @FXML
    private TableColumn<Funcionario, String> colunaNome;
    @FXML
    private TableColumn<Funcionario, String> colunaCargo;
    @FXML
    private TableColumn<Funcionario, Double> colunaSalario;
    @FXML
    private TableColumn<Funcionario, String>colunaTelefone;
    @FXML
    private Label modoLabel;
    @FXML
    private TextField pesquisaPorIdText;
    @FXML
    protected void produtoClick(ActionEvent e){
        HelloApplication.janelas(1);
    }
    @FXML
    protected void pedidoClick(ActionEvent e){
        HelloApplication.janelas(2);
    }
    @FXML
    protected void sobreClick(ActionEvent e){HelloApplication.janelas(4);}
    private boolean modoEdicao;
    private Stage stage;
    private Funcionario funcionarioEmEdicao;

    @FXML
    protected void initialize() throws Exception {
        colunaId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdFuncionario()).asObject());
        colunaNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colunaCargo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCargo()));
        colunaTelefone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefone()));
        colunaSalario.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalario()).asObject());
        setModoInsercao();
        mostrarTodosFuncionarios();
    }

    @FXML
    protected  void onPesquisaTodosButtonClick() throws Exception{
        mostrarTodosFuncionarios();
    }

    @FXML
    protected void onPesquisaPorIdButtonClick() {
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

            Funcionario funcionario = FuncionarioDAO.obterPorId(idBusca);
            if (funcionario != null) {
                List<Funcionario> funcionarios = new ArrayList<>();
                funcionarios.add(funcionario);
                carregaTabela(funcionarios);
                limpaCampos();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação");
                alert.setHeaderText(null);
                alert.setContentText("Nenhum funcionário encontrado com o ID informado.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, insira um número inteiro para o ID.");
            alert.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    protected void onSalvarFuncionarioButtonClick() throws Exception {
        int quantidadeModificacoes = 0;
        String nome = nomeText.getText();
        String cargo = cargoText.getText();
        double salario = 0.0;
        String telefone = telefoneText.getText();

        try {
            salario = Double.parseDouble(salarioText.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, insira um valor numérico para o salario.");
            alert.showAndWait();
            return;
        }

        if(modoEdicao){
            funcionarioEmEdicao.setNome(nome);
            funcionarioEmEdicao.setCargo(cargo);
            funcionarioEmEdicao.setTelefone(telefone);
            funcionarioEmEdicao.setSalario(salario);
            quantidadeModificacoes = FuncionarioDAO.atualiza(funcionarioEmEdicao);
        }else{
            Funcionario funcionario = new Funcionario(nome, cargo, salario, telefone);
            FuncionarioDAO.inserirFuncionario(funcionario);
            limpaCampos();
            mostrarTodosFuncionarios();
        }

        if(quantidadeModificacoes > 0){
            mostrarTodosFuncionarios();
            setModoInsercao();
            limpaCampos();
        }
    }

    @FXML
    protected void onPesquisaGanhaMaisButtonClick()throws Exception{
        Funcionario funcionario = FuncionarioDAO.ganhaMais();
        if(funcionario != null){
            List<Funcionario> funcionarios = new ArrayList<>();
            funcionarios.add(funcionario);
            carregaTabela(funcionarios);
            limpaCampos();
        }
    }

    @FXML
    protected void onPesquisaGanhaMenosButtonClick() throws Exception{
        Funcionario funcionario = FuncionarioDAO.ganhaMenos();
        if(funcionario != null){
            List<Funcionario> funcionarios = new ArrayList<>();
            funcionarios.add(funcionario);
            carregaTabela(funcionarios);
            limpaCampos();
        }
    }

    @FXML
    protected void onSairButtonClick(){
        fechartudo();
    }
    private void fechartudo(){
        stage = (Stage) funcionariobox.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onVoltarButtonClick() {
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
    protected void onEditarButtonClick(){
        Funcionario funcionario = tabelaFuncionario.getSelectionModel().getSelectedItem();
        if(funcionario != null){
            setModoEdicao(funcionario);
        }
    }

    @FXML
    protected void onExcluirButtonClick() throws Exception {
        Funcionario funcionario = tabelaFuncionario.getSelectionModel().getSelectedItem();
        if(funcionario != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exclusão");
            alert.setHeaderText("Você solicitou a exclusão do aluno de id " + funcionario.getIdFuncionario() +
                    " (" + funcionario.getNome() + ")");
            alert.setContentText("Confirma a exclusão?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                int quantidadeModificacoes = FuncionarioDAO.exclui(funcionario);
                if(quantidadeModificacoes > 0){
                    mostrarTodosFuncionarios();
                    setModoInsercao();
                }
            }
        }
    }

    @FXML
    protected void onSairModoEdicaoButtonClick(){
        setModoInsercao();
        limpaCampos();
    }

    private void carregaTabela(List<Funcionario> funcionarios) throws Exception {
        ObservableList<Funcionario> funcionariosList = FXCollections.observableArrayList();
        funcionariosList.addAll(funcionarios);
        tabelaFuncionario.setItems(funcionariosList);
    }

    private void mostrarTodosFuncionarios() throws Exception {
        List<Funcionario> funcionarios = FuncionarioDAO.selecionarTudo();
        carregaTabela(funcionarios);
    }

    private void setModoEdicao(Funcionario funcionario){
        modoEdicao = true;
        modoLabel.setText("Modo: edição");
        nomeText.setText(funcionario.getNome());
        cargoText.setText(funcionario.getCargo());
        salarioText.setText(String.valueOf(funcionario.getSalario()));
        telefoneText.setText(funcionario.getTelefone());
        funcionarioEmEdicao = funcionario;
        sairModoEdicaoButton.setVisible(true);
    }

    private void setModoInsercao(){
        modoEdicao = false;
        modoLabel.setText("Modo: inserção");
        sairModoEdicaoButton.setVisible(false);
    }

    private void limpaCampos(){
        nomeText.setText("");
        cargoText.setText("");
        salarioText.setText("");
        telefoneText.setText("");
    }
}
