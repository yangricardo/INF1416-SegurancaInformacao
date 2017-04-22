package controller;

import dao.GroupDao;
import dao.UserDao;
import model.User;

public class MainMenuController {

	private static User userAdm = UserDao.getUserInfo("admin");
	private static String pemPrivateKey;
		
	protected static void setLoggedUser(User user,String pemPrivateKey){
		MainMenuController.userAdm = user;
		MainMenuController.pemPrivateKey = pemPrivateKey;
		UserDao.setUserAccess(userAdm.getLoginname());
	}
	
	public static void setOpenMainMenu(){
		RegisterController.RegisterLog(5001, userAdm.getLoginname());
	}
	
	public static String getLoggedUser(){
		return userAdm.getLoginname();
	}
	
	public static String getGroupLoggedUser(){
		return GroupDao.getGroupName(userAdm.getIdUserGroup());
	}

	public static String getUserDescription(){
		return userAdm.getUsername();
	}
	
	public static void setOpenRegisterUserView(){
		RegisterController.RegisterLog(5002, userAdm.getLoginname());
		RegisterUserController.setLoggedUser(userAdm);
	}
	
	public static void setOpenListKeysView(){
		RegisterController.RegisterLog(5003, userAdm.getLoginname());
		ListKeysController.setLoggedUser(userAdm,pemPrivateKey);
	}

	public static int getUserNumberOfAccess() {
		return UserDao.getNumberOfAccess(userAdm.getLoginname());
	}

	public static void setOpenSecretFileView() {
		RegisterController.RegisterLog(5004, userAdm.getLoginname());
		SecretFilesController.setLoggedUser(userAdm, pemPrivateKey);
		
	}
	
	public static void setCloseView(){
		RegisterController.RegisterLog(5005, userAdm.getLoginname());
	}
		
	public static void setOpenCloseView(){
		RegisterController.RegisterLog(9001, userAdm.getLoginname());
	}

	public static void setCloseViewBackToMainMenu() {
		RegisterController.RegisterLog(9003, userAdm.getLoginname());
	}
	
	public static void setCloseViewExit() {
		RegisterController.RegisterLog(9002, userAdm.getLoginname());
		RegisterController.RegisterLog(1002);
	}
}
