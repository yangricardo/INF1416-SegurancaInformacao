package view;

import controller.RegisterController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.RegisterLogBean;

public class LogView extends Application {

	private AnchorPane pane;
	private ComboBox<String> cbUsers;
	private ComboBox<String> cbMessages;
	private TableView<RegisterLogBean> tbLogList;
	private TableColumn<RegisterLogBean, String> timeRegisterColumn;
	private TableColumn<RegisterLogBean, String> idMessageColumn;
	private TableColumn<RegisterLogBean, String> messageColumn;
	private Button btSearchLog;
	private static Stage stage;
		
	public static void main(String[] args) {
		launch(args);
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
		stage.setTitle("LogView - INF1416 - Trabalho 3 - 1212206");
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				stage.close();
				System.exit(0);
			}
		});		
		LogView.stage = stage;
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		pane = new AnchorPane();
		pane.setPrefSize(600, 400);
		cbMessages = new ComboBox<String>();
		cbMessages.getItems().addAll(RegisterController.getMessages());
		cbMessages.setValue(cbMessages.getItems().get(0));
		cbMessages.setLayoutX(50);	cbMessages.setLayoutY(15);
		cbMessages.setPrefSize(82, 25);
		cbUsers = new ComboBox<String>();
		cbUsers.getItems().addAll(RegisterController.getUsers());
		cbUsers.setValue(cbUsers.getItems().get(0));
		cbUsers.setLayoutX(160);	cbUsers.setLayoutY(15);
		cbUsers.setPrefSize(265, 25);
		btSearchLog = new Button("Listar Registros");
		btSearchLog.setPrefWidth(117);
		btSearchLog.setLayoutX(433);	btSearchLog.setLayoutY(15);
		tbLogList = new TableView<RegisterLogBean>();
		tbLogList.setPrefSize(500, 300);
		tbLogList.setLayoutX(50); tbLogList.setLayoutY(55);
		timeRegisterColumn = new TableColumn<RegisterLogBean,String>("Data/Hora");
		timeRegisterColumn.setCellValueFactory(new PropertyValueFactory<>("timeRegister"));
		timeRegisterColumn.setPrefWidth(110); timeRegisterColumn.setMaxWidth(110);
		idMessageColumn = new TableColumn<RegisterLogBean,String>("Tipo");
		idMessageColumn.setCellValueFactory(new PropertyValueFactory<>("idMessage"));
		idMessageColumn.setPrefWidth(40); idMessageColumn.setMaxWidth(40);
		messageColumn = new TableColumn<RegisterLogBean,String>("Messagem");
		messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		messageColumn.setPrefWidth(350);messageColumn.setMaxWidth(600);
		tbLogList.getColumns().addAll(timeRegisterColumn,idMessageColumn,messageColumn);
		pane.getChildren().addAll(cbMessages,cbUsers,btSearchLog,tbLogList);
		
	}

	private void initListeners() {
		btSearchLog.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				ObservableList<RegisterLogBean> temp = FXCollections.observableArrayList(
						RegisterController.getRegisters(
								cbUsers.getSelectionModel().getSelectedItem(), 
								cbMessages.getSelectionModel().getSelectedItem()
				));
				tbLogList.setItems(temp);
			}
		});
		
	}

}
