package view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controller.LoginController;
import controller.RegisterController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.UserStatus;
import utils.Util;

public class LoginView  extends Application{

	private AnchorPane pane;
	private TextField txUsername;
	private PasswordField txPassword;
	private TextField txPrivateKeyPath;
	private Button btCheckUsername;
	private Button[] btOp;
	private Button btCheckPassword;
	private Button btOpenPrivateKey;
	private PasswordField txSecretText;
	private Button btCheckPrivateKey;
	private Label lbStatusVerification;
	private Button btLogin;
	private Button btClose;
	private static Stage stage;
	
	public static void main(String[] args) {
		launch(args);//método  javafx.application.Application.launch
	}

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
		stage.setTitle("Login - INF1416 - Trabalho 3 - 1212206");
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				RegisterController.RegisterLog(1002);
				stage.close();
				System.exit(0);
			}
		});
		initLayout();
		LoginView.stage = stage;
		LoginController.setLogStartedSystem();
	}

	private void initComponents(){
		pane = new AnchorPane();
		pane.setPrefSize(600, 400);
		txUsername = new TextField();
		txUsername.setPromptText("USERNAME");
		btCheckUsername = new Button("Check Username");
		btCheckUsername.setDisable(true);
		txPassword = new PasswordField();
		txPassword.setPromptText("PASSWORD");
		txPassword.setEditable(false);
		txPassword.setDisable(true);
		btCheckPassword = new Button("Check Password");
		btCheckPassword.setDisable(true);
		txPrivateKeyPath = new TextField();
		txPrivateKeyPath.setPromptText("PRIVATE KEY PATH");
		txPrivateKeyPath.setEditable(false);
		txPrivateKeyPath.setDisable(true);
		btOpenPrivateKey = new Button("Choose Private Key");
		btOpenPrivateKey.setDisable(true);
		txSecretText = new PasswordField();
		txSecretText.setPromptText("PRIVATE KEY SECRET TEXT");
		txSecretText.setDisable(true);
		btCheckPrivateKey = new Button("Check Private Key");
		btCheckPrivateKey.setDisable(true);
		lbStatusVerification = new Label(LoginController.getLogStatus(2001));
		btLogin = new Button("Login");
		btLogin.setDisable(true);
		btClose = new Button("Close");
		btClose.setDisable(false);
		pane.getChildren().addAll(txUsername,btCheckUsername,txPassword,btCheckPassword,
				txPrivateKeyPath,btOpenPrivateKey,txSecretText,btCheckPrivateKey
				,lbStatusVerification,btLogin,btClose);
		btOp = new Button[5];
		initButtonsArray(); //Cria os Buttons e adiciona no pane
			
	}
	
	private void initButtonsArray(){
		List<Integer> numbers = new ArrayList<Integer>();
		for(int i=0;i<=9;i++){
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for(int i = 0; i < btOp.length; i++) {
            btOp[i] = new Button(numbers.get(2*i)+" - "+numbers.get(2*i+1));
            btOp[i].setDisable(true);
            pane.getChildren().add(btOp[i]);
		}
	}
	
	private void initLayout(){
		txUsername.setLayoutX(50);
		txUsername.setLayoutY(50);
		txUsername.setPrefWidth(209);
		btCheckUsername.setLayoutX(340);
		btCheckUsername.setLayoutY(50);
		txPassword.setLayoutX(50);
		txPassword.setLayoutY(90);
		txPassword.setPrefWidth(209);
		btCheckPassword.setLayoutX(340);
		btCheckPassword.setLayoutY(130);
		txPrivateKeyPath.setLayoutX(50);
		txPrivateKeyPath.setLayoutY(170);
		txPrivateKeyPath.setPrefWidth(379);
		btOpenPrivateKey.setLayoutX(435);
		btOpenPrivateKey.setLayoutY(170);
		txSecretText.setLayoutX(50);
		txSecretText.setLayoutY(210);
		txSecretText.setPrefWidth(209);
		btCheckPrivateKey.setLayoutX(340);
		btCheckPrivateKey.setLayoutY(210);
		lbStatusVerification.setLayoutX(50);
		lbStatusVerification.setLayoutY(245);
		btLogin.setLayoutX(50);
		btLogin.setLayoutY(320);
		btLogin.setPrefWidth(250);
		btClose.setLayoutX(315);
		btClose.setLayoutY(320);
		btClose.setPrefWidth(250);
		Double value = 271.0;
		for(Button btn : btOp){
			btn.setLayoutX(value);
			btn.setLayoutY(90);
			value += 58.0;
		}
	}
	
	private void initListeners() {
		txUsername.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(txUsername.getText().length() > 0){
					btCheckUsername.setDisable(false);
				} else{
					btCheckUsername.setDisable(true);
				}
			}
		});
		
		btCheckUsername.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				UserStatus userVerified = LoginController.searchUser(txUsername.getText()); 
				if(userVerified !=UserStatus.NOTFOUND){
					if(userVerified == UserStatus.FOUND){
						txUsername.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
						txUsername.setDisable(true);
						btCheckUsername.setDisable(true);
						lbStatusVerification.setText(LoginController.getLogStatus(3001, txUsername.getText()));
						//Habilita os botoes que dao append no password
						for(int i = 0; i < btOp.length; i++) {
							btOp[i].setDisable(false);
						}
					} else{
						setBlockUser();
						lbStatusVerification.setText(LoginController.getLogStatus(2004, txUsername.getText()));
					}
				} else{
					//username nao encontrado
					txUsername.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
					lbStatusVerification.setText(LoginController.getLogStatus(2005, txUsername.getText()));
				}
				
			}
		});
		
		 txPassword.textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
		        	//Limita o tamnho da senha 10 digitos
		        	if (txPassword.getText().length() > 10) {
		                String s = txPassword.getText().substring(0, 10);
		                txPassword.setText(s);
		            }
		        	//a partir de 8 digitos o btCheckPassword eh desbloqueado
		        	if (txPassword.getText().length() >= 8) {
		                btCheckPassword.setDisable(false);
		            }
		        	if (txPassword.getText().length() == 10){
		        		for(Button btn : btOp)
		        			btn.setDisable(true);
		        	}
		        }
		    });
		
		 txSecretText.textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
		        	if (txSecretText.getText().length()>0)
		        		btCheckPrivateKey.setDisable(false);
		        }
		    });
		 
		 
		for(Button btn : btOp){ //para cada botao do digito
			btn.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					LoginController.addPasswordDigit(btn.getText());
					txPassword.appendText( String.valueOf(txPassword.getText().length()) );
					//System.out.println(txPassword.getText());
				}
			});
		}
		
		btCheckPassword.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				//Verifico a senha do username ja validado
				int blockCont = LoginController.verifyPassword(); 
				if (blockCont==0){
					txPassword.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
					btCheckPassword.setDisable(true);
					btOpenPrivateKey.setDisable(false);
					for(Button btn : btOp)
	        			btn.setDisable(true);
					lbStatusVerification.setText(LoginController.getLogStatus(4001, txUsername.getText()));
				} else{
					txPassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
					txPassword.clear();
					btCheckPassword.setDisable(true);
					for(Button btn : btOp)
	        			btn.setDisable(false);
					if(blockCont==1)
						lbStatusVerification.setText(LoginController.getLogStatus(3005, txUsername.getText()));
					else if(blockCont==2)
						lbStatusVerification.setText(LoginController.getLogStatus(3006, txUsername.getText()));
					else if(blockCont==3){
						setBlockUser();
						lbStatusVerification.setText(LoginController.getLogStatus(3007, txUsername.getText())
								+"\n"+LoginController.getLogStatus(3008, txUsername.getText()));
					}
				}
				
			}
		});
		
		btOpenPrivateKey.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				configureFileChooser(fileChooser);
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					if(file.getAbsolutePath().toString().length()>255){
						txPrivateKeyPath.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						lbStatusVerification.setText("Chave privada com caminho maior que 255 caracteres");
						LoginController.setWrongPathPrivateKey();
					}
					else{
						txPrivateKeyPath.setText(file.getAbsolutePath().toString());
						txPrivateKeyPath.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
						txSecretText.setDisable(false);
					}
					System.out.println("File Path Lenght"+file.getAbsolutePath().length());
					
				}	
			}
		});	
		
		btCheckPrivateKey.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int blockCont = LoginController.verifyPrivateKey(txPrivateKeyPath.getText(),txSecretText.getText());
				if(blockCont == 0){
					txSecretText.setDisable(true);
					btOpenPrivateKey.setDisable(true);
					btCheckPrivateKey.setDisable(true);
					txSecretText.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
					btLogin.setDisable(false);
					lbStatusVerification.setText(LoginController.getLogStatus(4003, txUsername.getText()));
				} else{
					txSecretText.clear();
					txSecretText.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
					txSecretText.setDisable(false);
					btCheckPrivateKey.setDisable(true);
					if(blockCont==1)
						lbStatusVerification.setText(LoginController.getLogStatus(4004, txUsername.getText()));
					else if(blockCont==2)
						lbStatusVerification.setText(LoginController.getLogStatus(4005, txUsername.getText()));
					else if(blockCont==3){
						setBlockUser();
						lbStatusVerification.setText(
								LoginController.getLogStatus(4006,  txUsername.getText())+"\n"+
								LoginController.getLogStatus(4007, txUsername.getText())
								+"\n ou \n"+
								LoginController.getLogStatus(4008, txUsername.getText())
								);
					}
				}
			}
		});
		
		btLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				LoginController.setLogin();
				try {
					new MainView().start(new Stage());
					LoginView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao prosseguir para menu principal", e);
				}
			}
		});
		
		btClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				RegisterController.RegisterLog(1002);
				stage.close();
				System.exit(0);
			}
		});
		
	}
	
	private void setBlockUser(){
		txUsername.clear();
		txUsername.setDisable(false);
		txUsername.setStyle(null);
		txPassword.clear();
		txPassword.setStyle(null);
		btCheckUsername.setDisable(true);
		for(Button btn : btOp)
			btn.setDisable(true);
		btCheckPassword.setDisable(true);
		txPrivateKeyPath.clear();
		txPrivateKeyPath.setStyle(null);
		btOpenPrivateKey.setDisable(true);
		txSecretText.clear();
		txSecretText.setStyle(null);
		txSecretText.setDisable(true);
		btCheckPrivateKey.setDisable(true);
		btLogin.setDisable(true);
	}
	
	private static void configureFileChooser( final FileChooser fileChooser) {      
		fileChooser.setTitle("Selecione a Private Key");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
				);                 
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("KEY", "*.key")
				);
	}
	
}
