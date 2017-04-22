package view;

import controller.MainMenuController;
import controller.RegisterController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Util;

public class CloseView extends Application {

	private AnchorPane pane;
	private Label lbLoginName;
	private Label lbGroupName;
	private Label lbUserDescription;
	private Label lbUserTotalAccess;
	private Label lbClosingSystem;
	private Label lbCloseMessage;
	private Button btExit;
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
		stage.setTitle("Tela de Saída - INF1416 - Trabalho 3 - 1212206");
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
		CloseView.stage = stage;
		MainMenuController.setOpenCloseView();
	}

	private void initComponents() {
		pane = new AnchorPane();
		pane.setPrefSize(600, 400);
		lbLoginName = new Label("Login: "+MainMenuController.getLoggedUser());
		lbGroupName = new Label("Grupo: "+MainMenuController.getGroupLoggedUser());
		lbUserDescription = new Label("Descrição: "+MainMenuController.getUserDescription());
		lbUserTotalAccess = new Label("Total de acessos do usuário: "+MainMenuController.getUserNumberOfAccess());
		lbClosingSystem = new Label("Saída do Sistema:");
		lbCloseMessage = new Label("Pressione o botão Sair para confirmar.");
		btExit = new Button("Sair");
		btBackMainMenu = new Button("Voltar ao Menu Principal");
		pane.getChildren().addAll(lbLoginName,lbGroupName,lbUserDescription,lbUserTotalAccess,lbClosingSystem,
				lbCloseMessage,btExit,btBackMainMenu);
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
		lbClosingSystem.setLayoutX(50);
		lbClosingSystem.setLayoutY(160);
		lbClosingSystem.setPrefWidth(500);
		lbClosingSystem.setAlignment(Pos.CENTER);
		lbCloseMessage.setLayoutX(50);
		lbCloseMessage.setLayoutY(210);
		lbCloseMessage.setPrefWidth(500);
		lbCloseMessage.setAlignment(Pos.CENTER);
		btExit.setLayoutX(50);
		btExit.setLayoutY(290);
		btExit.setPrefWidth(245);
		btBackMainMenu.setLayoutX(305);
		btBackMainMenu.setLayoutY(290);
		btBackMainMenu.setPrefWidth(245);
		
		
	}

	
	private void initListeners() {
		btBackMainMenu.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				try {
					new MainView().start(new Stage());
					CloseView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao voltar para menu principal", e);
				}
				MainMenuController.setCloseViewBackToMainMenu();
			}
		});
		btExit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				MainMenuController.setCloseViewExit();
				stage.close();
				System.exit(0);
			}
		});
	}

	
}
