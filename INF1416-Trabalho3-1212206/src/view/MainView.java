package view;

import controller.MainMenuController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Util;

public class MainView extends Application{

	private AnchorPane pane;
	private Label lbLoginName;
	private Label lbGroupName;
	private Label lbUserDescription;
	private Label lbUserTotalAccess;
	private Label lbMainMenu;
	private Button btRegisterUser;
	private Button btListUserKeys;
	private Button btSearchSecretFiles;
	private Button btCloseSystem;
	private static Stage stage;
	
	public static Stage getStage(){
		return stage;
	}
	
	public void start(Stage stage) throws Exception {
		initComponents();
		initListeners();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Menu Principal - INF1416 - Trabalho 3 - 1011154 - 1212206");
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
		MainView.stage = stage;
		MainMenuController.setOpenMainMenu();
	}

	private void initComponents() {
		pane = new AnchorPane();
		pane.setPrefSize(600, 400);
		lbLoginName = new Label("Login: "+MainMenuController.getLoggedUser());
		lbGroupName = new Label("Grupo: "+MainMenuController.getGroupLoggedUser());
		lbUserDescription = new Label("Descrição: "+MainMenuController.getUserDescription());
		lbUserTotalAccess = new Label("Total de acessos do usuário: "+MainMenuController.getUserNumberOfAccess());
		lbMainMenu = new Label("Menu Principal:");
		btRegisterUser = new Button("1 – Cadastrar um novo usuário");
		btListUserKeys = new Button("2 – Listar chave privada e certificado digital");
		btSearchSecretFiles = new Button("3 – Consultar pasta de arquivos secretos do usuário");
		btCloseSystem = new Button("4 – Sair do Sistema");
		pane.getChildren().addAll(lbLoginName,lbGroupName,lbUserDescription,
				lbUserTotalAccess,lbMainMenu,btRegisterUser,btListUserKeys,
				btSearchSecretFiles,btCloseSystem);
	}
	
	private void initLayout() {
		lbLoginName.setLayoutX(50);
		lbLoginName.setLayoutY(15);
		lbGroupName.setLayoutX(50);
		lbGroupName.setLayoutY(40);
		lbUserDescription.setLayoutX(50);
		lbUserDescription.setLayoutY(65);
		lbUserTotalAccess.setLayoutX(50);
		lbUserTotalAccess.setLayoutY(100);
		lbMainMenu.setLayoutX(50);
		lbMainMenu.setLayoutY(135);
		btRegisterUser.setLayoutX(50);
		btRegisterUser.setLayoutY(170);
		btRegisterUser.setPrefWidth(500);
		if(MainMenuController.getGroupLoggedUser().equals("Usuário"))
			btRegisterUser.setDisable(true);
		btListUserKeys.setLayoutX(50);
		btListUserKeys.setLayoutY(205);
		btListUserKeys.setPrefWidth(500);
		btSearchSecretFiles.setLayoutX(50);
		btSearchSecretFiles.setLayoutY(240);
		btSearchSecretFiles.setPrefWidth(500);
		btCloseSystem.setLayoutX(50);
		btCloseSystem.setLayoutY(275);
		btCloseSystem.setPrefWidth(500);
	}
	
	private void initListeners() {
		btRegisterUser.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				MainMenuController.setOpenRegisterUserView();
				try {
					new RegisterUserView().start(new Stage());
					MainView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao ir para Formulário de Cadastro de Usuário", e);
				}
			}
		});
		
		btListUserKeys.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				MainMenuController.setOpenListKeysView();
				try {
					new ListKeysView().start(new Stage());
					MainView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao abrir tela de Listagem de Chaves", e);
				}
			}
		});
		
		btSearchSecretFiles.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				MainMenuController.setOpenSecretFileView();
				try {
					new SecretFileView().start(new Stage());
					MainView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao prosseguir para menu principal", e);
				}
			}
		});
		
		btCloseSystem.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				MainMenuController.setCloseView();
				try {
					new CloseView().start(new Stage());
					MainView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao prosseguir para menu principal", e);
				}
			}
		});
		
	}

}
