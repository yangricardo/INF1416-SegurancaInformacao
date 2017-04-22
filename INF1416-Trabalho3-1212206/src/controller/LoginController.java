package controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import dao.MessageDao;
import dao.UserDao;
import model.User;
import model.UserStatus;
import security.AuthenticationPassword;
import security.AuthenticationUserFiles;
import utils.Util;

public class LoginController {

	private static User user;
	private static String pemPrivateKey = null;
	private static int contWrongPassword = 0;
	private static boolean blockStep2 = false;
	private static int contWrongPrivateKey = 0;
	private static boolean blockStep3 = false;
	private static DateTimeFormatter dataFormat = DateTimeFormat.forPattern("yyyy-M-dd HH:mm:ss");
	
	
	public static void setLogStartedSystem(){
		RegisterController.RegisterLog(1001);
		RegisterController.RegisterLog(2001);
	}
	
	public static void setLogClosingSystem(){
		RegisterController.RegisterLog(1002);
	}
	
	public static void setLogin(){
		MainMenuController.setLoggedUser(LoginController.user, LoginController.pemPrivateKey);
	}
	
	//USER DAO
	
	private static boolean verifyBlockTime(String blockedSince){
		 DateTime userTimeBlocked = dataFormat.parseDateTime(blockedSince);
		 DateTime now = dataFormat.parseDateTime(DateTime.now().toString(dataFormat));
		 boolean blocked = Minutes.TWO.isGreaterThan(Minutes.minutesBetween(userTimeBlocked, now ));
		 System.out.println("Agora: "+DateTime.now().toString(dataFormat)+" millisegundos: "+ DateTime.now().getMillis());
		 System.out.println("Tempo Bloqueio: "+userTimeBlocked.toString(dataFormat)+" millisegundos: "+ userTimeBlocked.getMillis());
		 System.out.println("Usuário bloqueado? : "+blocked+" Minutos entre agora e o tempo de bloqueio: "+Minutes.minutesBetween(userTimeBlocked, now ).getMinutes());
		 return  blocked;
	 }
	
	public static UserStatus searchUser(String loginname){
		LoginController.user = UserDao.getUserInfo(loginname);
		UserStatus userStatus;
		if(user!=null){
			System.out.println(
					"--------------------- Informações do Usuário-------------------\n"
					+"LoginName: "+user.getLoginname()+"| UserName: "+user.getUsername()+" | isBlocked: "+user.isBlocked()+" | salt: "+user.getSalt()
					+" | hexPasswordDigest: "+user.getPassword()+ "\n | pathDigitalCertificate: "+user.getDigitalcertificate()
					+"\n-----------------------------------------------------------");
			if(user.isBlocked()){
				if(!verifyBlockTime(user.getBlockedSince())){
					setLogFoundLoginname(user.getLoginname());
					UserDao.setUserAvailable(user.getLoginname());
					userStatus =  UserStatus.FOUND;
				} else{
					setLogFoundBlockLoginname(user.getLoginname());
					userStatus =  UserStatus.BLOCKED;
				}
			} else{ 
				setLogFoundLoginname(user.getLoginname());
				userStatus =  UserStatus.FOUND;
			}
		} else{
			setLogNotFoundLoginname(loginname);
			userStatus =  UserStatus.NOTFOUND;
		}
		return userStatus;
	}
	
	private static void setLogFoundLoginname(String loginname){
		RegisterController.RegisterLog(2003,loginname);
		RegisterController.RegisterLog(2002,loginname);
		RegisterController.RegisterLog(3001,loginname);
	}
	
	private static void setLogNotFoundLoginname(String loginname){
		System.out.println("Usuário não Encontrado: "+loginname);
		RegisterController.RegisterLog(2005, loginname);
	}
	
	private static void setLogFoundBlockLoginname(String loginname){
		RegisterController.RegisterLog(2004, loginname);
	}
	
	//AUTHENTICATION PASSWORD KEY

	public static void addPasswordDigit(String digit){
		AuthenticationPassword.addDigit(digit);
	}
	
	public static int verifyPassword(){
		boolean passwordStatus = false;
		System.out.println("salt: "+user.getSalt()+" hexmd5passwordsalt: "+user.getPassword());
		passwordStatus = AuthenticationPassword.verifyPassword(user.getSalt(),user.getPassword());
		if(passwordStatus){
			setLogRightPassword(user.getLoginname());	
		}else{
			setLogWrongPassword(user.getLoginname());
			if(contWrongPassword==3){
				contWrongPassword = 0;
				return 3;
			}
		}
		return contWrongPassword;
	}
	
	private static void setLogRightPassword(String loginname){
		RegisterController.RegisterLog(3003, loginname);
		RegisterController.RegisterLog(3002, loginname);
		RegisterController.RegisterLog(4001, loginname);
		contWrongPassword = 0;
		UserDao.setUserAvailable(loginname);
	}
	
	private static boolean setLogWrongPassword(String loginname){
		AuthenticationPassword.cleanDigits();
		RegisterController.RegisterLog(3004, loginname);
		contWrongPassword++;
		if(contWrongPassword==1)
			RegisterController.RegisterLog(3005, loginname);
		else if(contWrongPassword==2)
			RegisterController.RegisterLog(3006, loginname);
		else if(contWrongPassword==3){
			RegisterController.RegisterLog(3007, loginname);
			blockStep2 = true;
			RegisterController.RegisterLog(3008, loginname);
			UserDao.setUserBlocked(loginname,DateTime.now().toString(dataFormat),contWrongPassword);
			RegisterController.RegisterLog(3002, loginname);
			RegisterController.RegisterLog(2001);
		}
		return blockStep2;
	}
	
	//AUTHENTICATION PRIVATE KEY
	
	public static int verifyPrivateKey(String pathPrivateKey,String secretText){
		System.out.println("Frase Secreta: "+ secretText);
		try {
			byte[] bytePemPrivateKey = AuthenticationUserFiles.SymmetricDecription(pathPrivateKey, secretText);
			pemPrivateKey = Util.convertToUTF8(bytePemPrivateKey);
			System.out.println(pemPrivateKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			//Util.printError("Erro ao recuperar chave", e);
			setLogWrongPrivateKey(user.getLoginname());
			System.out.println("Erros na private key: "+contWrongPrivateKey);
			System.out.println("Private Key block: "+blockStep3);
			if(contWrongPrivateKey==3){
				contWrongPrivateKey = 0;
				return 3;
			}
			return contWrongPrivateKey;
		}
		boolean isSigned = false;
		try {
			System.out.println("Certificado digital de "+user.getLoginname()+": "+user.getDigitalcertificate());
			isSigned = AuthenticationUserFiles.verifyPrivateKeySignature(
					AuthenticationUserFiles.getPrivateKey(pemPrivateKey), 
					AuthenticationUserFiles.getUserCert(user.getDigitalcertificate()));
		} catch (Exception e) {
			//Util.printError("Erro ao recuperar chave privada", e);
		}
		if(isSigned){
			setLogRightPrivateKey(user.getLoginname());
		} else{
			setLogWrongPrivateKey(user.getLoginname());
			System.out.println("Erros na private key: "+contWrongPrivateKey);
			System.out.println("Private Key block: "+blockStep3);
			if(contWrongPrivateKey==3){
				contWrongPrivateKey = 0;
				return 3;
			}
		}
		return contWrongPrivateKey;
	}
	
	private static void setLogRightPrivateKey(String loginname){
		RegisterController.RegisterLog(4003, loginname);
		RegisterController.RegisterLog(4002, loginname);
		contWrongPrivateKey = 0;
		blockStep3 = false;	
		UserDao.setUserAvailable(loginname);
	}
	
	public static boolean setLogWrongPrivateKey(String loginname){
		RegisterController.RegisterLog(4008, user.getLoginname());
		contWrongPrivateKey++;
		if(contWrongPrivateKey==1)
			RegisterController.RegisterLog(4004, loginname);
		else if(contWrongPrivateKey==2)
			RegisterController.RegisterLog(4005, loginname);
		else if(contWrongPrivateKey==3){
			RegisterController.RegisterLog(4006, loginname);
			blockStep3 = true;
			RegisterController.RegisterLog(4007, loginname);
			RegisterController.RegisterLog(4009, loginname);
			RegisterController.RegisterLog(4002, loginname);
			UserDao.setUserBlocked(loginname,DateTime.now().toString(dataFormat),contWrongPrivateKey);
			RegisterController.RegisterLog(2001);
		}
		return blockStep3;
	}
	
	public static void setWrongPathPrivateKey(){
		RegisterController.RegisterLog(4007, user.getLoginname());
	}

	public static String getLogStatus(int idMessage,String loginname){
		return MessageDao.getMessage(idMessage).getMessage().replaceAll("<login_name>", loginname);
	}
	public static String getLogStatus(int idMessage){
		return MessageDao.getMessage(idMessage).getMessage();
	}
	
}

/*
 * admin
 * 7cce55243be4085afc17f42ef04d8d40
 * 176745866
 * 1
 * Administrador
 */