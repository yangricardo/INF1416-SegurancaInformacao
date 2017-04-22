package model;

import javafx.beans.property.SimpleStringProperty;

public class RegisterLogBean {
	
	private final SimpleStringProperty timeRegister;
	private final SimpleStringProperty idMessage;
	private final SimpleStringProperty message;
	public RegisterLogBean(String timeRegister, int idMessage,
			String message) {
		super();
		this.timeRegister = new SimpleStringProperty(timeRegister);
		this.idMessage = new SimpleStringProperty(String.valueOf(idMessage));
		this.message = new SimpleStringProperty(message);
	}
	public String getTimeRegister() {
		return timeRegister.get();
	}
	public String getIdMessage() {
		return idMessage.get();
	}
	public String getMessage() {
		return message.get();
	}
}
