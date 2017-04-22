package controller;

import javax.security.cert.X509Certificate;
import dao.GroupDao;
import dao.UserDao;
import model.User;
import security.AuthenticationUserFiles;

public class ListKeysController {

	private static User userAdm = UserDao.getUserInfo("admin");
	private static String pemPrivateKey = null; 
	private static X509Certificate userCert =  null;
	
	public static void setLoggedUser(User userAdm, String pemPrivateKey){
		ListKeysController.userAdm = userAdm;
		ListKeysController.pemPrivateKey = pemPrivateKey;
		ListKeysController.userCert = AuthenticationUserFiles.getUserCert(userAdm.getDigitalcertificate());
		UserDao.setUserSearch(userAdm.getLoginname());	
	}

	public static void setOpenListKeys(){
		RegisterController.RegisterLog(7001, userAdm.getLoginname());
	}
	
	public static void setBackToMainMenu(){
		RegisterController.RegisterLog(7002, userAdm.getLoginname());
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
	
	public static int getNumberOfSearches(){
		return UserDao.getNumberOfKeysSearch(userAdm.getLoginname());
	}
	
	public static String getPem64EncodedPrivateKey(){
		return pemPrivateKey;
	}
	
	public static String getUserDigitalCertificateInfo(){ 	
		return AuthenticationUserFiles.getUserCertInfo(userCert);
	}
	
}
