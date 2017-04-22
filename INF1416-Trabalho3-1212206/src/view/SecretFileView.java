package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Optional;
import controller.SecretFilesController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.FileStatus;
import model.SecretFileBean;
import utils.Util;

public class SecretFileView extends Application {

	private AnchorPane pane;
	private Label lbLoginName;
	private Label lbGroupName;
	private Label lbUserDescription;
	private Label lbUserTotalSearches;
	private Button btAddPathSecretFiles;
	private TextField txPathSecretFiles;
	private Button btListSecretFiles;
	private TableView<SecretFileBean> tbSecretFilesList;
	private TableColumn<SecretFileBean, String> secretNameColumn;
	private TableColumn<SecretFileBean, String> codeNameColumn;
	private TableColumn<SecretFileBean, String> digitalSignatureColumn;
	private TableColumn<SecretFileBean, String> digitalEnvelopeColumn;
	private TableColumn<SecretFileBean, String> statusColumn;
	private ObservableList<SecretFileBean> secretFiles =  FXCollections.observableArrayList();
	private Button btBackMainMenu;
	private static Stage stage;

	protected static Stage getStage() {
		return SecretFileView.stage;
	}

	@Override
	public void start(Stage stage) throws Exception {
		initComponents();
		initListeners();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Consultar Pasta de Arquivos Secretos do Usuário - INF1416 - Trabalho 3 - 1212206");
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				try {
					new CloseView().start(new Stage());
					stage.close();
				} catch (Exception e) {
					Util.printError("Erro ao prosseguir para menu principal", e);
				}
			}
		});
		initLayout();
		SecretFileView.stage = stage;
		SecretFilesController.setOpenSecretFilesView();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		pane = new AnchorPane();
		pane.setPrefSize(600, 400);
		lbLoginName = new Label("Login: "+SecretFilesController.getLoggedUserLoginname());
		lbGroupName = new Label("Grupo: "+SecretFilesController.getLoggedUserGroup());
		lbUserDescription = new Label("Descrição: "+SecretFilesController.getLoggedUserUsername());
		lbUserTotalSearches = new Label("Total de acessos do usuário: "+SecretFilesController.getNumberOfSearches());
		btAddPathSecretFiles = new Button("Caminho para pasta de Arquivos Secretos");
		txPathSecretFiles = new TextField();
		txPathSecretFiles.setEditable(false);
		txPathSecretFiles.setDisable(true);
		btListSecretFiles = new Button("Listar Arquivos Secretos");
		btListSecretFiles.setDisable(true);
		tbSecretFilesList = new TableView<SecretFileBean>();
		tbSecretFilesList.setDisable(true);
		secretNameColumn = new TableColumn<SecretFileBean,String>("Nome Secreto");
		secretNameColumn.setCellValueFactory(new PropertyValueFactory<>("secretName"));
		codeNameColumn = new TableColumn<SecretFileBean,String>("Nome Codigo");
		codeNameColumn.setCellValueFactory(new PropertyValueFactory<>("codeName"));
		digitalSignatureColumn = new TableColumn<SecretFileBean,String>("Assinatura Digital");
		digitalSignatureColumn.setCellValueFactory(new PropertyValueFactory<>("digitalSignature"));
		digitalEnvelopeColumn = new TableColumn<SecretFileBean,String>("Envelope Digital");
		digitalEnvelopeColumn.setCellValueFactory(new PropertyValueFactory<>("digitalEnvelope"));
		statusColumn = new TableColumn<SecretFileBean,String>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		tbSecretFilesList.getColumns().addAll(secretNameColumn,codeNameColumn,
				digitalEnvelopeColumn,digitalSignatureColumn,statusColumn);
		tbSecretFilesList.setItems(secretFiles);
		btBackMainMenu = new Button("Voltar ao Menu Principal");
		pane.getChildren().addAll(lbLoginName,lbGroupName,lbUserDescription,
				lbUserTotalSearches,btAddPathSecretFiles,txPathSecretFiles,
				btListSecretFiles,tbSecretFilesList,btBackMainMenu);
	}

	private void initLayout() {
		lbLoginName.setLayoutX(50);
		lbLoginName.setLayoutY(15);
		lbGroupName.setLayoutX(50);
		lbGroupName.setLayoutY(40);
		lbUserDescription.setLayoutX(50);
		lbUserDescription.setLayoutY(65);
		lbUserTotalSearches.setLayoutX(50);
		lbUserTotalSearches.setLayoutY(100);
		btAddPathSecretFiles.setLayoutX(50);
		btAddPathSecretFiles.setLayoutY(135);
		txPathSecretFiles.setLayoutX(290);
		txPathSecretFiles.setLayoutY(135);
		txPathSecretFiles.setPrefWidth(261);
		btListSecretFiles.setLayoutX(50);
		btListSecretFiles.setLayoutY(165);
		btListSecretFiles.setPrefWidth(500);
		tbSecretFilesList.setLayoutX(50);
		tbSecretFilesList.setLayoutY(195);
		tbSecretFilesList.setPrefSize(500, 165);
		secretNameColumn.setMinWidth(90);
		secretNameColumn.setMaxWidth(90);
		codeNameColumn.setMinWidth(90);
		codeNameColumn.setMaxWidth(90);
		digitalEnvelopeColumn.setMinWidth(135);
		digitalEnvelopeColumn.setMaxWidth(135);
		digitalSignatureColumn.setMinWidth(135);
		digitalSignatureColumn.setMaxWidth(135);
		statusColumn.setMinWidth(50);
		statusColumn.setMaxWidth(50);
		btBackMainMenu.setLayoutX(50);
		btBackMainMenu.setLayoutY(365);
		btBackMainMenu.setPrefWidth(500);

	}

	private static void configureDirectoryChooser( final DirectoryChooser directoryChooser) {      
		directoryChooser.setTitle("Escolha o Diretório de Arquivos Secreto");
		directoryChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
				);      
	}

	private void initListeners() {

		btBackMainMenu.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				SecretFilesController.setBackToMainMenu();
				try {
					new MainView().start(new Stage());
					SecretFileView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao voltar para menu principal", e);
				}				
			}
		});

		btAddPathSecretFiles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				configureDirectoryChooser(directoryChooser);
				File file = directoryChooser.showDialog(stage);
				if (file != null) {
					if(file.getAbsolutePath().toString().length()>255){
						txPathSecretFiles.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						SecretFilesController.setWrongDirectoryPathLength();
						tbSecretFilesList.setDisable(true);
						btListSecretFiles.setDisable(true);
					}
					else {
						txPathSecretFiles.setText(file.getAbsolutePath().toString());
						FileStatus directoryStatus = SecretFilesController.setSecretDirectoryPath(txPathSecretFiles.getText());
						if(directoryStatus.equals(FileStatus.FILEENVASDOK) || directoryStatus.equals(FileStatus.FILEENVASDNOTOK)){
							txPathSecretFiles.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
							btListSecretFiles.setDisable(false);
							tbSecretFilesList.setItems(FXCollections.observableArrayList());
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Arquivo index.env e index.enc Encontrados");
							alert.getDialogPane().setContentText((directoryStatus.equals(FileStatus.FILEENVASDOK)?"Assinatura de index.enc: OK":"Assinatura de index.enc: NOT OK"));
							alert.showAndWait();
						} else if(directoryStatus.equals(FileStatus.FILEENVNOTFOUND) || directoryStatus.equals(FileStatus.FILEENCNOTFOUND)){
							txPathSecretFiles.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
							SecretFilesController.setWrongDirectoryPathLength();
							tbSecretFilesList.setDisable(true);
							btListSecretFiles.setDisable(true);
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Falha ao encontrar o index");
							alert.getDialogPane().setContentText("Os arquivos index.env e index.enc devem existir na pasta selecionada\nSua chave também precisa deve ser compatível com o index e posteriormente com os arquivos");
							alert.showAndWait();
						}			
					}
					System.out.println(file.getAbsolutePath().length()+": "+file.getAbsolutePath().toString());
				}	
			}
		});	

		btListSecretFiles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SecretFilesController.setListPressed();
				ArrayList<SecretFileBean> secretFilesTemp = SecretFilesController.getSecretFiles();
				if(!secretFilesTemp.isEmpty()){
					tbSecretFilesList.setDisable(false);
					secretFiles = FXCollections.observableArrayList(secretFilesTemp);
					tbSecretFilesList.setItems(secretFiles);
					lbUserTotalSearches.setText("Total de acessos do usuário: "+
							SecretFilesController.getNumberOfSearches());
					SecretFilesController.setSecretFilesListShowed();
				} else{
					txPathSecretFiles.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				}
			}
		});	

		tbSecretFilesList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (tbSecretFilesList.getSelectionModel().getSelectedItem() != null) {
				TextArea secretFile = new TextArea("Nome Secreto: "+newValue.getSecretName()
				+"\nNome Código: "+newValue.getCodeName()
				+"\nStatus: "+newValue.getStatus()
				+"\nEnvelope Digital: "+newValue.getDigitalEnvelope()
				+"\nAssinatura Digital: "+newValue.getDigitalSignature());
				secretFile.setEditable(false);
				secretFile.setWrapText(true);
				secretFile.setMaxSize(500, 500);
				System.out.println(secretFile.getText());
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Verificação de Arquivo Secreto");
				alert.getDialogPane().setContent(secretFile);
				Optional<ButtonType> resultAlert = alert.showAndWait();
				if (resultAlert.get() == ButtonType.OK){
					FileChooser fileChooser = new FileChooser();
					String fileFormat = newValue.getSecretName().substring(
							newValue.getSecretName().length()-4, newValue.getSecretName().length());
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
							fileFormat.substring(1, fileFormat.length()).toUpperCase()+" files", fileFormat);
					fileChooser.getExtensionFilters().add(extFilter);
					fileChooser.setInitialFileName(newValue.getSecretName());
					File file = fileChooser.showSaveDialog(getStage());
					OutputStream outputStream = null;
					if(file != null){
						try {
							FileWriter fileWriter = null;
							fileWriter = new FileWriter(file);
							fileWriter.write("");
							fileWriter.close();
							outputStream = new FileOutputStream(file);
							byte[] bytesSecretFile = SecretFilesController.getDecodedSecretFile(newValue.getCodeName());
							outputStream.write(bytesSecretFile);
							outputStream.close();
						} catch (IOException e) {
							Util.printError("Erro ao salvar arquivo!", e);
						}
					}
				} else if(resultAlert.get() == ButtonType.CANCEL){

				}
			}
		});
	}
}
