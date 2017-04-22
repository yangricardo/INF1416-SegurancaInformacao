package security;

import java.io.File;
/*import java.io.FileOutputStream;
import java.io.FileWriter;*/
import java.io.IOException;
/*import java.io.OutputStream;*/
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import controller.RegisterController;
import model.FileData;
import model.FileStatus;
import utils.Util;

public class AuthenticationUserFiles {
	/*
	 * Terceira Etapa
	 * Decriptar chave privada usando o formato DES/ECB/PKCS5Padding
	 */

	public static byte[] AsymmetricDecription(String pathFile, PrivateKey userPrivateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		byte[] bytesDecriptedFile = null;
		byte[] bytesEncriptedFile = Files.readAllBytes(new File(pathFile).toPath());
		Cipher cipherDecription = Cipher.getInstance("RSA");
		cipherDecription.init(Cipher.DECRYPT_MODE, userPrivateKey);
		bytesDecriptedFile = cipherDecription.doFinal(bytesEncriptedFile);
		return bytesDecriptedFile;
	}

	public static byte[] AsymmetricDecription(String pathFile, PublicKey userPublicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		System.out.println(pathFile);
		byte[] bytesDecriptedFile = null;
		byte[] bytesEncriptedFile = Files.readAllBytes(new File(pathFile).toPath());
		Cipher cipherDecription = Cipher.getInstance("RSA");
		cipherDecription.init(Cipher.DECRYPT_MODE, userPublicKey);
		bytesDecriptedFile = cipherDecription.doFinal(bytesEncriptedFile);
		return bytesDecriptedFile;
	}

	public static byte[] SymmetricDecription(String pathfile,String seed) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		byte[] bytesDecriptedFile = null;

		byte[] bytesEncriptedFile = Files.readAllBytes(new File(pathfile).toPath());
		//Determina o key generator com algoritmo DES
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		//Determina o secureRandom com SHA1PRNG
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		//passa os bytes da frase secreta (semente) que sera usada no secureRandom
		secureRandom.setSeed(seed.getBytes("UTF8"));
		//Utiliza os bytes da frase secreta para intancializar o secure random
		keyGen.init(56, secureRandom);
		//Gera a chave para decriptacao
		Key keyDecription = keyGen.generateKey();
		//Gera objeto Cipher para decriptação da chave privada com a chave simetrica gerada
		Cipher cipherDecription = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipherDecription.init(Cipher.DECRYPT_MODE, keyDecription);
		//Decripta a chava privada com cipherDecription
		bytesDecriptedFile = cipherDecription.doFinal(bytesEncriptedFile);
		return bytesDecriptedFile;
	}

	public static PrivateKey getPrivateKey(String pemFormatPrivateKey){
		//Limpa a string da chave privada
		pemFormatPrivateKey = pemFormatPrivateKey.replaceAll("-----BEGIN PRIVATE KEY-----","").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
		//base64
		byte[] encodedPrivateKey = Base64.getDecoder().decode(pemFormatPrivateKey);
		//Especifica para o padrão PKCS8, numa chave RSA
		PKCS8EncodedKeySpec pkcs8KeySpecPrivateKey = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey privateKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(pkcs8KeySpecPrivateKey);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			Util.printError("Erro ao gerar Private Key", e);
			return null;
		}
	}

	public static String getASCIIUserCert(String userCertFilename){
		byte[] bytesCertFile = null;
		try {
			bytesCertFile = Files.readAllBytes(new File(userCertFilename).toPath());
			System.out.println(Util.convertToASCII(bytesCertFile));
		} catch (IOException e) {
			Util.printError("Erro ao recuperar ASCII do certificado", e);
		}
		return Util.convertToASCII(bytesCertFile);
	}

	public static X509Certificate getUserCert(String userCertASCII) {
		try {
			X509Certificate userCert = X509Certificate.getInstance(userCertASCII.getBytes());
			return userCert;
		} catch (CertificateException e) {
			Util.printError("Erro ao gerar certificiado", e);
			return null;
		}
	}

	public static String getUserCertInfo(X509Certificate userCert){
		String info = null;
		info =  "Versão: "  + userCert.getVersion()+"\n";
		info += "Série: "   + userCert.getSerialNumber()+"\n";
		info += "Validade: "+ userCert.getNotBefore()+" até "+ userCert.getNotAfter()+"\n";
		info += "Tipo de Assinatura: "+ userCert.getSigAlgName()+"\n";
		info += "Emissor: " + userCert.getIssuerDN().getName()+"\n";
		info += "Sujeito: " + userCert.getSubjectDN().getName()+"\n";
		return info;
	}

	public static boolean verifyPrivateKeySignature(PrivateKey userPrivatekey, X509Certificate userCert) throws Exception{
		Signature sig = Signature.getInstance("SHA1withRSA");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		//É gerado o array aleatorio de 512 bytes
		byte[] randomArray = sr.generateSeed(512);
		//init Sign com a chave privada
		sig.initSign(userPrivatekey);
		sig.update(randomArray);
		//gera assinatura com chave privada
		byte[] privateSignature = sig.sign();	
		System.out.println("Assinatura Chave Privada em RandomArray[512]: \n"+Util.convertToHex(privateSignature));
		//init Verify com chave publica do certificado digital do usuario
		sig.initVerify(userCert.getPublicKey());
		sig.update(randomArray);
		boolean isSigned = false;
		try {
			if (sig.verify(privateSignature)) {
				isSigned = true;
			} else isSigned = false;
		} catch (SignatureException se) {
			isSigned = false;
		}
		return isSigned;
	}

	public static String verifyFileSignature(PublicKey userPublicKey,byte[] encContent,String asdFile) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException{
		System.out.println(asdFile);
		String verifyStatus = "NOT OK";
		byte[] asdBytes = null;
		asdBytes = Files.readAllBytes(new File(asdFile).toPath());
		Signature sig = Signature.getInstance("MD5withRSA");
		sig.initVerify(userPublicKey);
		sig.update(encContent);
		if(sig.verify(asdBytes)){
			verifyStatus = "OK";
			System.out.println(verifyStatus);
		}
		System.out.println(Util.convertToHex(asdBytes));
		return verifyStatus;
	}

	public static FileData processFile(String fileName,String directoryPath,String user,PrivateKey userPrivateKey, X509Certificate userCert){
		String pathFile = directoryPath+"\\"+fileName;
		byte[] envSeed = null;
		byte[] encContent = null;
		String asdStatus = null;
		FileData file =  new FileData();
		try {
			envSeed = AuthenticationUserFiles.AsymmetricDecription(pathFile+".env", userPrivateKey);
			RegisterController.RegisterLog(8009, user, fileName+".env");
			file.setSeed(envSeed);
			try {
				encContent = AuthenticationUserFiles.SymmetricDecription(pathFile+".enc", Util.convertToUTF8(envSeed));
				RegisterController.RegisterLog(8009, user, fileName+".enc");
				file.setContent(encContent);
				try {
					asdStatus = AuthenticationUserFiles.verifyFileSignature(userCert.getPublicKey(), encContent, pathFile+".asd");
					if(asdStatus.equals("OK")){
						RegisterController.RegisterLog(8010, user,  fileName+".env");
						file.setStatus(asdStatus);
						file.setFileStatus(FileStatus.FILEENVASDOK);
					}else{
						RegisterController.RegisterLog(8012, user, fileName+".enc");
						file.setStatus("NOT OK");
						file.setFileStatus(FileStatus.FILEENVASDNOTOK);
					}
				} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | IOException e) {
					RegisterController.RegisterLog(8012, user, fileName+".enc");
					file.setStatus("NOT OK");
					file.setFileStatus(FileStatus.FILEENVASDNOTOK);
				}
				file.setSignature(Util.convertToHex(Files.readAllBytes(new File(pathFile+".asd").toPath())));
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException | IOException e) {
				RegisterController.RegisterLog(8011, user, fileName+".enc");
				file.setFileStatus(FileStatus.FILEENCNOTFOUND);
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			RegisterController.RegisterLog(8006, user);
			RegisterController.RegisterLog(8011, user, fileName+".env");
			file.setFileStatus(FileStatus.FILEENVNOTFOUND);
		}
		return file;
	}

	public static void main(String[] args){
		System.out.println("------------------ADMINISTRADOR-----------------\n");
		PrivateKey pkadm = null;
		try {
			pkadm = AuthenticationUserFiles.getPrivateKey(
					Util.convertToUTF8(
							AuthenticationUserFiles.SymmetricDecription("Pacote-T3\\Keys\\userpriv-pkcs8-pem-des.key", "teste123")
							)
					);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			e.printStackTrace();
		}		
		X509Certificate ucadm = AuthenticationUserFiles.getUserCert(
				AuthenticationUserFiles.getASCIIUserCert("Pacote-T3\\Keys\\usercert-x509.crt")
				);
		try {
			System.out.println("Verificação de Assinatura das Chaves do Administrador : "+AuthenticationUserFiles.verifyPrivateKeySignature(pkadm, ucadm));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\n------------------USER01-----------------\n");
		PrivateKey pk1 = null;
		try {
			pk1 = AuthenticationUserFiles.getPrivateKey(
					Util.convertToUTF8(
							AuthenticationUserFiles.SymmetricDecription("Pacote-T3-Avaliação\\Keys\\user01-pkcs8-pem-des.key", "user01")
							)
					);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			e.printStackTrace();
		}		
		X509Certificate uc1 = AuthenticationUserFiles.getUserCert(
				AuthenticationUserFiles.getASCIIUserCert("Pacote-T3-Avaliação\\Keys\\user01-x509-pem.crt")
				);
		try {
			System.out.println("Verificação de Assinatura das Chaves do USER01 : "+AuthenticationUserFiles.verifyPrivateKeySignature(pk1, uc1));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\n------------------USER02-----------------\n");
		PrivateKey pk2 = null;
		try {
			pk2 = AuthenticationUserFiles.getPrivateKey(
					Util.convertToUTF8(
							AuthenticationUserFiles.SymmetricDecription("Pacote-T3-Avaliação\\Keys\\user02-pkcs8-pem-des.key", "user02")
							)
					);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			e.printStackTrace();
		}		
		X509Certificate uc2 = AuthenticationUserFiles.getUserCert(
				AuthenticationUserFiles.getASCIIUserCert("Pacote-T3-Avaliação\\Keys\\user02-x509-pem.crt")
				);
		try {
			System.out.println("Verificação de Assinatura das Chaves do USER02 : "+AuthenticationUserFiles.verifyPrivateKeySignature(pk2, uc2));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\n------------------USER03-----------------\n");
		PrivateKey pk3 = null;
		try {
			pk3 = AuthenticationUserFiles.getPrivateKey(
					Util.convertToUTF8(
							AuthenticationUserFiles.SymmetricDecription("Pacote-T3-Avaliação\\Keys\\user03-pkcs8-pem-des.key", "user03")
							)
					);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			e.printStackTrace();
		}		
		X509Certificate uc3 = AuthenticationUserFiles.getUserCert(
				AuthenticationUserFiles.getASCIIUserCert("Pacote-T3-Avaliação\\Keys\\user03-x509-pem.crt")
				);
		try {
			System.out.println("Verificação de Assinatura das Chaves do USER03 : "+AuthenticationUserFiles.verifyPrivateKeySignature(pk3, uc3));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\n---------------------Administrador");
		String directoryPath = "Pacote-T3\\Files";
		FileData index = processFile("index", directoryPath, "administrador", pkadm, ucadm);
		if(index.getFileStatus().equals(FileStatus.FILEENVASDOK) || index.getFileStatus().equals(FileStatus.FILEENVASDNOTOK)){
			String[] indexSecretFiles = Util.convertToUTF8(index.getContent()).split("\n");
			for(String s : indexSecretFiles){
				String secretName = s.split(" ")[0];
				String codeName = s.split(" ")[1];
				FileData secretFile = processFile(codeName, directoryPath, "administrador", pkadm, ucadm);
				System.out.println(secretName+" | "+codeName+" | "+secretFile.getSeedHEX()+"("+secretFile.getSeedUTF8()+") | "+secretFile.getStatus()+" | "+secretFile.getSignature());
			}
		}

		for(int i = 1; i<=3;i++){
			String user = "user0"+i;
			PrivateKey userPrivateKey = null;
			X509Certificate userCert = null;
			if(i==1){
				System.out.println("\n---------------------User0"+i);
				directoryPath = "Pacote-T3-Avaliação\\Files";
				userPrivateKey = pk1;
				userCert = uc1;
			}
			if(i==2){
				System.out.println("\n---------------------User0"+i);
				directoryPath = "Pacote-T3-Avaliação\\Files";
				userPrivateKey = pk2;
				userCert = uc2;
			}
			if(i==3){
				System.out.println("\n---------------------User0"+i);
				directoryPath = "Pacote-T3-Avaliação\\Files";
				userPrivateKey = pk3;
				userCert = uc3;
			}
			System.out.println(user);
			index = processFile("index", directoryPath, user, userPrivateKey, userCert);
			if(index.getFileStatus().equals(FileStatus.FILEENVASDOK) || index.getFileStatus().equals(FileStatus.FILEENVASDNOTOK)){
				String[] indexSecretFiles = Util.convertToUTF8(index.getContent()).split("\n");
				for(String s : indexSecretFiles){
					String secretName = s.split(" ")[0];
					String codeName = s.split(" ")[1];
					FileData secretFile = processFile(codeName, directoryPath, user, userPrivateKey, userCert);
					System.out.println(secretName+" | "+codeName+" | "+secretFile.getSeedHEX()+"("+secretFile.getSeedUTF8()+") | "+secretFile.getStatus()+" | "+secretFile.getSignature());
				}
			}
		}
		
		System.out.println("****************Força Bruta arquivos");
		for(int i=1;i<=3;i++){
			String user = "user0"+i;
			PrivateKey userPrivateKey = null;
			X509Certificate userCert = null;
			directoryPath = "Pacote-T3-Avaliação\\Files";
			String codeName = null;
			String secretName = null;
			if(i==1){
				System.out.println("\n---------------------User0"+i);
				userPrivateKey = pk1;
				userCert = uc1;
			}
			if(i==2){
				System.out.println("\n---------------------User0"+i);
				userPrivateKey = pk2;
				userCert = uc2;
			}
			if(i==3){
				System.out.println("\n---------------------User0"+i);
				userPrivateKey = pk3;
				userCert = uc3;
			}
			for(int j=1;j<=3;j++){
				if(j==1){
					codeName = "X1X1X1X1";
					secretName = "secreto1";
				} else if(j==2){
					codeName = "Y1Y1Y1Y1";
					secretName = "secreto2";
				}else if(j==3){
					codeName = "Z1Z1Z1Z1";
					secretName = "secreto3";
				}
				FileData secretFile = processFile(codeName, directoryPath, user, userPrivateKey, userCert);
				System.out.println(secretName+" | "+codeName+" | "+secretFile.getSeedHEX()+"("+secretFile.getSeedUTF8()+") | "+secretFile.getStatus()+" | "+secretFile.getSignature());		
				/*if(secretFile.getFileStatus().equals(FileStatus.FILEENVASDOK)||secretFile.getFileStatus().equals(FileStatus.FILEENVASDNOTOK)){
					try {
						File file = new File(directoryPath+"\\"+secretName);
						FileWriter fileWriter = new FileWriter(file);
						fileWriter.write("");
						fileWriter.close();
						OutputStream outputStream = new FileOutputStream(file);
						outputStream.write(secretFile.getContent());
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
			}
		}
	}

}

