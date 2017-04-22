package view;

import java.io.File;
import java.util.Optional;
import controller.RegisterUserController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Util;

public class RegisterUserView  extends Application{


	private AnchorPane pane;
	private Label lbLoginName;
	private Label lbGroupName;
	private Label lbUserDescription;
	private Label lbUserTotalNumUsers;
	private Label lbRegisterForm;
	private Label lbLoginUsername;
	private Label lbUsername;
	private Label lbGrupo;
	private Label lbPassword;
	private Label lbConfirmPassword;
	private Button btPathUserDigitalCertificate;
	private TextField txLoginUsername;
	private TextField txUsername;
	private ComboBox<String> cbGrupo;
	private PasswordField txPassword;
	private PasswordField txConfirmPassword;
	private TextField txPathUserDigitalCertificate;
	private Button btRegisterUser;
	private Button btBackMainMenu;
	private static Stage stage;
	
	public static Stage getStage(){
		return stage;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		initComponents();
		initListeners();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Cadastro de Usuário - INF1416 - Trabalho 3 - 1212206");
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
		RegisterUserView.stage = stage;
		RegisterUserController.setOpenRegisterUser();
	}

	private void initComponents() {
		pane = new AnchorPane();
		pane.setPrefSize(600, 400);
		lbLoginName = new Label("Login: "+RegisterUserController.getLoggedUserLoginname());
		lbGroupName = new Label("Grupo: "+RegisterUserController.getLoggedUserGroup());
		lbUserDescription = new Label("Descrição: "+ RegisterUserController.getLoggedUserUsername());
		lbUserTotalNumUsers = new Label("Total de usuários do sistema: "+ RegisterUserController.getNumberOfUsers());
		lbRegisterForm = new Label("Formulário de Cadastro:");
		lbUsername = new Label("– Nome do usuário:");
		lbLoginUsername = new Label("– Login name:");
		lbGrupo = new Label("– Grupo:");
		lbPassword = new Label("– Senha pessoal:");
		lbConfirmPassword = new Label("– Confirmação da senha pessoal:");
		btPathUserDigitalCertificate = new Button("- Caminho para o Certificado Digital:");
		txLoginUsername = new TextField();
		txUsername = new TextField();
		cbGrupo = new ComboBox<String>();
		cbGrupo.getItems().addAll(RegisterUserController.getGroups());
		cbGrupo.setValue(cbGrupo.getItems().get(1));
		txPassword = new PasswordField();
		txConfirmPassword = new PasswordField();
		txPathUserDigitalCertificate = new TextField();
		txPathUserDigitalCertificate.setEditable(false);
		txPathUserDigitalCertificate.setDisable(true);
		btRegisterUser = new Button("Cadastrar Usuário");
		btBackMainMenu = new Button("Voltar ao Menu Principal");
		
		pane.getChildren().addAll(lbLoginName,lbGroupName,lbUserDescription,
				lbUserTotalNumUsers,lbRegisterForm,lbUsername,lbLoginUsername,
				lbGrupo,lbPassword,lbConfirmPassword,btPathUserDigitalCertificate,
				txUsername,txLoginUsername,cbGrupo,txPassword,txConfirmPassword,
				txPathUserDigitalCertificate,btRegisterUser,btBackMainMenu);
	}
	
	private void initLayout() {
		lbLoginName.setLayoutX(50);						lbLoginName.setLayoutY(15);
		lbGroupName.setLayoutX(50);						lbGroupName.setLayoutY(40);
		lbUserDescription.setLayoutX(50);				lbUserDescription.setLayoutY(65);
		lbUserTotalNumUsers.setLayoutX(50);				lbUserTotalNumUsers.setLayoutY(100);
		lbRegisterForm.setLayoutX(50);					lbRegisterForm.setLayoutY(135);
		lbUsername.setLayoutX(50);						lbUsername.setLayoutY(164);
		lbLoginUsername.setLayoutX(50);					lbLoginUsername.setLayoutY(194);
		lbGrupo.setLayoutX(50);							lbGrupo.setLayoutY(224);
		lbPassword.setLayoutX(50);						lbPassword.setLayoutY(254);
		lbConfirmPassword.setLayoutX(50);				lbConfirmPassword.setLayoutY(284);
		btPathUserDigitalCertificate.setLayoutX(50);	btPathUserDigitalCertificate.setLayoutY(310);
		txUsername.setLayoutX(290);		txUsername.setLayoutY(160);		txUsername.setPrefWidth(260);
		txLoginUsername.setLayoutX(290);				txLoginUsername.setLayoutY(190);
		txLoginUsername.setPrefWidth(260);
		cbGrupo.setLayoutX(290);		cbGrupo.setLayoutY(220);		cbGrupo.setPrefWidth(260);						
		txPassword.setLayoutX(290);		txPassword.setLayoutY(250);		txPassword.setPrefWidth(260);
		txConfirmPassword.setLayoutX(290);		txConfirmPassword.setLayoutY(280);		txConfirmPassword.setPrefWidth(260);
		txPathUserDigitalCertificate.setLayoutX(290);
		txPathUserDigitalCertificate.setLayoutY(310);
		txPathUserDigitalCertificate.setPrefWidth(260);
		btRegisterUser.setLayoutX(50);		btRegisterUser.setLayoutY(350);
		btBackMainMenu.setLayoutX(402);		btBackMainMenu.setLayoutY(350);
	}
	
	private void setBlankFields(){
		txUsername.clear();
		txUsername.setStyle(null);
		txLoginUsername.clear();
		txLoginUsername.setStyle(null);
		txPassword.clear();
		txPassword.setStyle(null);
		txConfirmPassword.clear();
		txConfirmPassword.setStyle(null);
		txPathUserDigitalCertificate.clear();
		txPathUserDigitalCertificate.setStyle(null);
	}
	
	private void initListeners() {
		txUsername.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	        	if (txUsername.getText().length() > 50) {
	                txUsername.setText(txUsername.getText().substring(0, 50));
	            }
	        }
	    });
		
		txLoginUsername.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	        	if (txLoginUsername.getText().length() > 20) { 
	                txLoginUsername.setText(txLoginUsername.getText().substring(0, 20));
	            }
	        }
	    });
		
		txPassword.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	        	if(!txPassword.getText().isEmpty()){
	        		if(!txPassword.getText().trim().matches("[0-9]+")){
	        			Alert alert = new Alert(AlertType.WARNING);
	        			alert.setTitle("Erro ao inserir nova senha");
	        			alert.setHeaderText("Somente caracteres númericos são permitidos: [0-9]");
	        			alert.setContentText("Erro: "+txPassword.getText());
	        			alert.showAndWait();
	        			String password = (txPassword.getLength()>0?txPassword.getText().substring(0, txPassword.getLength()-1):"");
	        			txPassword.setText(password);
	        		}
	        		if (txPassword.getText().length() > 10) {
	        			String s = txPassword.getText().substring(0, 10);
	        			txPassword.setText(s);
	        		}
	        		if(txPassword.getText().length()<8)
	        			txPassword.setStyle(null);
	        		if (txPassword.getText().length()>=8 && txPassword.getText().length()<=10){
	        			if(RegisterUserController.verifyPassword(txPassword.getText()))
	        				txPassword.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
	        			else
	        				txPassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
	        		}
	        	}
	        }
	    });
		
		txConfirmPassword.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	        	if(!txConfirmPassword.getText().isEmpty()){
	        		if(!txConfirmPassword.getText().trim().matches("[0-9]+")){
	        			Alert alert = new Alert(AlertType.WARNING);
	        			alert.setTitle("Erro ao confirmar nova senha");
	        			alert.setHeaderText("Somente caracteres númericos são permitidos: [0-9]");
	        			alert.setContentText("Erro: "+txConfirmPassword.getText());
	        			alert.showAndWait();
	        			String password = (txConfirmPassword.getLength()>0?txConfirmPassword.getText().substring(0, txConfirmPassword.getLength()-1):"");
	        			txConfirmPassword.setText(password);
	        		}
	        		if (txConfirmPassword.getText().length() > 10) {
	        			String s = txConfirmPassword.getText().substring(0, 10);
	        			txConfirmPassword.setText(s);
	        		}
	        		if(txConfirmPassword.getText().length()<8)
	        			txConfirmPassword.setStyle(null);
	        		if (txConfirmPassword.getText().length()>=8 && txConfirmPassword.getText().length()<=10){
	        			if(RegisterUserController.verifyPassword(txPassword.getText(),txConfirmPassword.getText()))
	        				txConfirmPassword.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
	        			else 
	        				txConfirmPassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
	        		}
	        	}
	        }
	    });
		
		btRegisterUser.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				RegisterUserController.requestResgistration();
				if(txLoginUsername.getText().length()<2 || !RegisterUserController.verifyLoginName(txLoginUsername.getText()))
					txLoginUsername.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				else if(txUsername.getText().length()<2)
					txUsername.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				else if(txPassword.getText().length()<8
						|| txConfirmPassword.getText().length()<8
						|| !RegisterUserController.verifyPassword(txPassword.getText(), txConfirmPassword.getText())){
					txPassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
					txConfirmPassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				}
				else if(txPathUserDigitalCertificate.getText().length()>255 ||txPathUserDigitalCertificate.getText().length() ==0 ){
					txPathUserDigitalCertificate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
					RegisterUserController.setWrongPathUserDigitalCertificate();
				}
				else{
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Verificação de Certificado Digital");
					String userCertInfo = RegisterUserController.verifyUserDigitalCertificate(txPathUserDigitalCertificate.getText());
					if(userCertInfo!=null){
						alert.setContentText(userCertInfo);
					} else{
						alert.setContentText("Certificado com problema");
					}
					Optional<ButtonType> resultAlert = alert.showAndWait();
					if (resultAlert.get() == ButtonType.OK){
						RegisterUserController.setNewUser(txLoginUsername.getText(), txUsername.getText(), 
								cbGrupo.getSelectionModel().getSelectedIndex()+1, txPassword.getText(), 
								txPathUserDigitalCertificate.getText());
						setBlankFields();
					} else if(resultAlert.get() == ButtonType.CANCEL){
						RegisterUserController.refuseDataUserCert();
					}
					

				}
			}
		});
		
		btBackMainMenu.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				RegisterUserController.setBackToMainMenu();
				try {
					new MainView().start(new Stage());
					RegisterUserView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao voltar para menu principal", e);
				}				
			}
		});
		
		btPathUserDigitalCertificate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				configureFileChooser(fileChooser);
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					if(file.getAbsolutePath().toString().length()>255){
						txPathUserDigitalCertificate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						RegisterUserController.setWrongPathUserDigitalCertificate();
					} else
						txPathUserDigitalCertificate.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
					txPathUserDigitalCertificate.setText(file.getAbsolutePath());
					System.out.println(file.getAbsolutePath().length());
				}	
			}
		});	
		
	}
	
	private static void configureFileChooser( final FileChooser fileChooser) {      
		fileChooser.setTitle("Selecione o Certificado Digital");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
				);                 
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("CRT", "*.crt")
				);
	}
		
}
