package controller;

import javax.security.cert.X509Certificate;
import java.util.ArrayList;
import dao.GroupDao;
import dao.UserDao;
import model.Group;
import model.User;
import security.AuthenticationUserFiles;
import security.GenerationPassword;

public class RegisterUserController {

	private static User userAdm = UserDao.getUserInfo("admin");
	private static String userCertASCII = null;
	
	public static void setLoggedUser(User userAdm){
		RegisterUserController.userAdm = userAdm;
	}
	
	public static void setOpenRegisterUser(){
		RegisterController.RegisterLog(6001, userAdm.getLoginname());
	}
	
	public static void setBackToMainMenu(){
		RegisterController.RegisterLog(6006, userAdm.getLoginname());
	}

	public static String getLoggedUserLoginname(){
		return userAdm.getLoginname();
	}
	
	public static String getLoggedUserGroup(){
		return GroupDao.getGroupName(userAdm.getIdUserGroup());
	}
	
	public static String getLoggedUserUsername(){
		return userAdm.getUsername();
	}
	
	public static int getNumberOfUsers(){
		return UserDao.getNumberOfUsers();
	}
	
	public static ArrayList<String> getGroups(){
		ArrayList<String> nameGroup = new ArrayList<String>();
		ArrayList<Group> groups = GroupDao.getGroups();
		for(Group g: groups){
			nameGroup.add(g.getIdGroup()+" - "+g.getGroupname());
		}
		return nameGroup;
	}
	
	public static void setNewUser(String loginname, String username, int idgroup, String password, String pathUserDigitalCertificate){
		String salt = null;
		String hexMd5PasswordSalt = null;
		salt = GenerationPassword.getSalt();
		hexMd5PasswordSalt = GenerationPassword.generateSafePassword(password, salt);
		User newUser = new User(loginname, username, hexMd5PasswordSalt, salt, idgroup, 
				AuthenticationUserFiles.getASCIIUserCert(pathUserDigitalCertificate));
		UserDao.createUser(newUser);
		RegisterController.RegisterLog(6004, userAdm.getLoginname());
	}
	
	public static void requestResgistration(){
		RegisterController.RegisterLog(6002, userAdm.getLoginname());
	}
	
	public static void refuseDataUserCert(){
		RegisterController.RegisterLog(6005, userAdm.getLoginname());
	}
	
	public static void setWrongPathUserDigitalCertificate(){
		RegisterController.RegisterLog(6003, userAdm.getLoginname());
	}
	
	public static String verifyUserDigitalCertificate(String pathUserCert){
		userCertASCII = AuthenticationUserFiles.getASCIIUserCert(pathUserCert);
		X509Certificate userCert = AuthenticationUserFiles.getUserCert(userCertASCII);
		if(userCert.equals(null))
			setWrongPathUserDigitalCertificate();
		String userCertInfo = AuthenticationUserFiles.getUserCertInfo(userCert);
		return userCertInfo;
	}
	
	public static boolean verifyLoginName(String loginname){
		if(UserDao.getUserInfo(loginname)!=null){
			System.out.println("Usuario encontrado");
			return false;
		}
		return true;
	}
	
	public static boolean verifyPassword(String password){
		return GenerationPassword.checkPassword(password);
	}
	public static boolean verifyPassword(String password,String confirmPassword){
		System.out.println(password.equals(confirmPassword)+" "+GenerationPassword.checkPassword(password)+" "+GenerationPassword.checkPassword(confirmPassword) );
		return password.equals(confirmPassword)
				&& GenerationPassword.checkPassword(password)
				&& GenerationPassword.checkPassword(confirmPassword);
	}

}
