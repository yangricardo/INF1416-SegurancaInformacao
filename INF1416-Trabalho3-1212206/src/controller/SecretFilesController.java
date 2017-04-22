package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.cert.X509Certificate;
import dao.GroupDao;
import dao.UserDao;
import model.FileData;
import model.FileStatus;
import model.SecretFileBean;
import model.User;
import security.AuthenticationUserFiles;
import utils.Util;

public class SecretFilesController {
	
	private static User userAdm = UserDao.getUserInfo("admin");
	private static PrivateKey userPrivateKey;
	private static X509Certificate userCert; 
	private static String secretDirectory;
	private static String[] indexSecretFiles;
	private static LinkedHashMap<String, FileData> userSecretFiles;
	
	public static void setLoggedUser(User userAdm, String pemPrivateKey){
		SecretFilesController.userAdm = userAdm;
		SecretFilesController.userPrivateKey = AuthenticationUserFiles.getPrivateKey(pemPrivateKey);
		SecretFilesController.userCert = AuthenticationUserFiles.getUserCert(userAdm.getDigitalcertificate());
	}
	
	public static void setOpenSecretFilesView(){
		RegisterController.RegisterLog(8001, userAdm.getLoginname());
	}

	public static void setBackToMainMenu(){
		RegisterController.RegisterLog(8002, userAdm.getLoginname());
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
		return UserDao.getNumberOfFilesSearch(userAdm.getLoginname());
	}
	
	public static FileData processFile(String fileName,String directoryPath){
		String pathFile = directoryPath+"\\"+fileName;
		byte[] envSeed = null;
		byte[] encContent = null;
		String asdStatus = null;
		FileData file =  new FileData();
		try {
			envSeed = AuthenticationUserFiles.AsymmetricDecription(pathFile+".env", userPrivateKey);
			RegisterController.RegisterLog(8009, userAdm.getLoginname(), fileName+".env");
			file.setSeed(envSeed);
			try {
				encContent = AuthenticationUserFiles.SymmetricDecription(pathFile+".enc", Util.convertToUTF8(envSeed));
				RegisterController.RegisterLog(8009, userAdm.getLoginname(), fileName+".enc");
				file.setContent(encContent);
				try {
					asdStatus = AuthenticationUserFiles.verifyFileSignature(userCert.getPublicKey(), encContent, pathFile+".asd");
					if(asdStatus.equals("OK")){
						RegisterController.RegisterLog(8010, userAdm.getLoginname(),  fileName+".env");
						file.setStatus(asdStatus);
						file.setFileStatus(FileStatus.FILEENVASDOK);
					}else{
						RegisterController.RegisterLog(8012, userAdm.getLoginname(), fileName+".enc");
						file.setStatus("NOT OK");
						file.setFileStatus(FileStatus.FILEENVASDNOTOK);
					}
				} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | IOException e) {
					RegisterController.RegisterLog(8012, userAdm.getLoginname(), fileName+".enc");
					file.setStatus("NOT OK");
					file.setFileStatus(FileStatus.FILEENVASDNOTOK);
				}
				file.setSignature(Util.convertToHex(Files.readAllBytes(new File(pathFile+".asd").toPath())));
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException | IOException e) {
				RegisterController.RegisterLog(8011, userAdm.getLoginname(), fileName+".enc");
				file.setFileStatus(FileStatus.FILEENCNOTFOUND);
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			RegisterController.RegisterLog(8011, userAdm.getLoginname(), fileName+".env");
			file.setFileStatus(FileStatus.FILEENVNOTFOUND);
		}
		return file;
	}
	
	
	public static FileStatus setSecretDirectoryPath(String directoryPath){
		FileData index = processFile("index", directoryPath);
		if(index.getFileStatus().equals(FileStatus.FILEENVASDOK)||index.getFileStatus().equals(FileStatus.FILEENVASDNOTOK)){
			SecretFilesController.secretDirectory = directoryPath;
			indexSecretFiles = Util.convertToUTF8(index.getContent()).split("\n");
			userSecretFiles = new LinkedHashMap<String,FileData>();
		} else if(index.getFileStatus().equals(FileStatus.FILEENVNOTFOUND)||index.getFileStatus().equals(FileStatus.FILEENCNOTFOUND)){
			RegisterController.RegisterLog(8006, userAdm.getLoginname());
		}
		System.out.println(index.getFileStatus());
		return index.getFileStatus();
	}
	
	public static void setWrongDirectoryPathLength(){
		RegisterController.RegisterLog(8006, userAdm.getLoginname());
	}
	
	public static ArrayList<SecretFileBean> getSecretFiles(){
		ArrayList<SecretFileBean> secretFiles = new ArrayList<SecretFileBean>();
		for(String s : indexSecretFiles){
			String secretName = s.split(" ")[0];
			String codeName = s.split(" ")[1];
			FileData secretFile = processFile(codeName, secretDirectory);
			System.out.println(secretName+" | "+codeName+" | "+secretFile.getSeedHEX()+"("+secretFile.getSeedUTF8()+") | "+secretFile.getStatus()+" | "+secretFile.getSignature());
			if(secretFile.getFileStatus().equals(FileStatus.FILEENVASDOK)
					||secretFile.getFileStatus().equals(FileStatus.FILEENVASDNOTOK)){
				//Se o arquivo secreto foi decriptado com status ok ou not ok
				secretFiles.add(new SecretFileBean(secretName, codeName, secretFile.getSignature(), 
						secretFile.getSeedHEX(), secretFile.getStatus())
						);
				userSecretFiles.put(codeName, secretFile);
			}
		}
		return secretFiles;
	}

	public static void setSecretFilesListShowed(){
		RegisterController.RegisterLog(8007, userAdm.getLoginname());
	}
	
	public static void setListPressed() {
		UserDao.setUserFilesSearch(userAdm.getLoginname());
		RegisterController.RegisterLog(8003, userAdm.getLoginname());
	}

	public static byte[] getDecodedSecretFile(String codeName) {
		RegisterController.RegisterLog(8008, userAdm.getLoginname(), codeName);
		return userSecretFiles.get(codeName).getContent();
	}
}
