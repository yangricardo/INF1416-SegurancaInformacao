package model;

public class Message {

	private String message;
	private int idMessage;
	
	
	public Message() {

	}

	public Message(int idMessage, String message) {
		this.idMessage = idMessage;
		this.message = message;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getIdMessage() {
		return idMessage;
	}


	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}

}
