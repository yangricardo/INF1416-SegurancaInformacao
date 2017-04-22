package model;

public class Register {

	private int idRegister;
	private int idMessage;
	private int idUser;
	private String message;
	private String timeRegister;
	
	public Register(){
		
	}
	
	public Register(int idMessage,String message,String timeRegister){
		this.idMessage = idMessage;
		this.message = message;
		this.timeRegister = timeRegister;
	}
	
	public Register(int idRegister, int idMessage,int idUser, String message,String timeRegister) {
		this.idRegister = idRegister;
		this.idMessage = idMessage;
		this.idUser = idUser;
		this.message = message;
		this.timeRegister = timeRegister;
	}
	
	public Register(int idMessage,int idUser,String timeRegister) {
		this.idMessage = idMessage;
		this.idUser = idUser;
		this.timeRegister = timeRegister;
	}
	
	public Register(int idMessage,int idUser, String message,String timeRegister) {
		this.idMessage = idMessage;
		this.idUser = idUser;
		this.message = message;
		this.timeRegister = timeRegister;
	}

	public int getIdRegister() {
		return idRegister;
	}

	public void setIdRegister(int idRegister) {
		this.idRegister = idRegister;
	}

	public int getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeRegister() {
		return timeRegister;
	}

	public void setTimeRegister(String timeregister) {
		this.timeRegister = timeregister;
	}

}
