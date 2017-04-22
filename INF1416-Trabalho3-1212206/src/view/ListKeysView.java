package view;

import controller.ListKeysController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Util;

public class ListKeysView extends Application {

	private AnchorPane pane;
	private Label lbLoginName;
	private Label lbGroupName;
	private Label lbUserDescription;
	private Label lbUserTotalAccess;
	private Label lbPrivateKey;
	private TextArea taPrivateKey;
	private Label lbUserDigitalCertificate;
	private TextArea taUserDigitalCertificate;
	private Button btBackMainMenu;
	private static Stage stage;
	
	@Override
	public void start(Stage stage) throws Exception {
		initComponents();
		initListeners();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Listagem de Chaves - INF1416 - Trabalho 3 - 1212206");
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
		ListKeysView.stage = stage;
		ListKeysController.setOpenListKeys();
	}

	private void initComponents() {
		pane = new AnchorPane();
		pane.setPrefSize(600, 400);
		lbLoginName = new Label("Login: "+ListKeysController.getLoggedUserLoginname());
		lbGroupName = new Label("Grupo: "+ListKeysController.getLoggedUserGroup());
		lbUserDescription = new Label("Descrição: "+ListKeysController.getLoggedUserUsername());
		lbUserTotalAccess = new Label("Total de acessos do usuário: "+ListKeysController.getNumberOfSearches());
		lbPrivateKey = new Label("Chave Privada: ");
		taPrivateKey = new TextArea(ListKeysController.getPem64EncodedPrivateKey());
		lbUserDigitalCertificate = new Label("Certificado Digital:");
		taUserDigitalCertificate = new TextArea(ListKeysController.getUserDigitalCertificateInfo());
		btBackMainMenu = new Button("Voltar ao Menu Principal");
		pane.getChildren().addAll(lbLoginName,lbGroupName,lbUserDescription,
				lbUserTotalAccess,lbPrivateKey,taPrivateKey,lbUserDigitalCertificate,taUserDigitalCertificate,
				btBackMainMenu);
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
		lbPrivateKey.setLayoutX(50);
		lbPrivateKey.setLayoutY(135);
		taPrivateKey.setLayoutX(50);
		taPrivateKey.setLayoutY(155);
		taPrivateKey.setPrefSize(500,65);
		taPrivateKey.setEditable(false);
		taPrivateKey.setMaxWidth(500);
		taPrivateKey.setWrapText(true);
		lbUserDigitalCertificate.setLayoutX(50);
		lbUserDigitalCertificate.setLayoutY(225);
		taUserDigitalCertificate.setLayoutX(50);
		taUserDigitalCertificate.setLayoutY(244);
		taUserDigitalCertificate.setPrefSize(500,90);
		taUserDigitalCertificate.setEditable(false);
		taUserDigitalCertificate.setWrapText(true);
		taPrivateKey.setMaxWidth(500);
		btBackMainMenu.setLayoutX(50);
		btBackMainMenu.setLayoutY(340);
		btBackMainMenu.setPrefWidth(500);
	}

	private void initListeners() {
		btBackMainMenu.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				ListKeysController.setBackToMainMenu();
				try {
					new MainView().start(new Stage());
					ListKeysView.getStage().close();
				} catch (Exception e) {
					Util.printError("Erro ao voltar para menu principal", e);
				}
			}
		});
		
	}

	protected static Stage getStage() {
		return ListKeysView.stage;
	}
}
