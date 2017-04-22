package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import utils.Util;

public class GenerationPassword {

	private static MessageDigest messageDigest ;
	
	public static String getSalt(){
		SecureRandom numberRandom = null;
		try {
			numberRandom = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			Util.printError("Erro ao gerar SALT", e);
		}
		String salt = new Integer(numberRandom.nextInt(999999999)).toString();
		return salt;
	}
	
	public static String generateSafePassword(String password, String salt){
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			Util.printError("Erro ao gerar senha segura HEX(MD5(PASSWORD+SALT)", e);
		}	//instancia message digest MD5
		String passwordSalt = password+salt;
		messageDigest.update(passwordSalt.getBytes(), 0, passwordSalt.getBytes().length);
		byte[] digestPasswordSalt = messageDigest.digest();
		return Util.convertToHex(digestPasswordSalt);
	}

	public static Boolean checkPassword(String password){
		//System.out.println("Password length: "+ password.length());
		if(password.length()<8 || password.length()>10){
			System.out.println("Menor que 8 ou maior que 10");
			return false;
		}
		for(int i=1;i<password.length();i++){
			if(password.charAt(i-1) == password.charAt(i)){
				//System.out.println("sequencia de digitos iguais: "+password.charAt(i-1)+password.charAt(i));
				return false;
			}
			if((password.charAt(i-1)+1) == password.charAt(i)){
				//System.out.println("sequencia de digitos decrescente: "+password.charAt(i-1)+password.charAt(i));
				return false;
			}
			if((password.charAt(i-1)-1) == password.charAt(i)){
				//System.out.println("sequencia de digitos decrescente: "+password.charAt(i-1)+password.charAt(i));
				return false;
			}
		}
		return true; 
	}
	
	/*public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String salt = getSalt();
		String password = "3971846205";
		System.out.println("Senha plana: "+password+" salt: "+salt+" HEX(HASHMD5(PASSWORD+SALT): "+generateSafePassword(password,salt));
		String hexPassword1 = generateSafePassword(password,salt); 
		String hexPassword2 = generateSafePassword(password,salt);
		System.out.println(hexPassword1 + " = " + hexPassword2 + " :" + MessageDigest.isEqual(hexPassword1.getBytes(),hexPassword2.getBytes()));
		System.out.println(checkPassword("3971896205"));
	
	}*/

}
