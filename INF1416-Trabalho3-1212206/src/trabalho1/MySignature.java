package trabalho1;
/*
 * 	PUC-Rio
 * 	INF1416 - Segurança da Informação - 2016.1
 * 	Trabalho 1 
 * 	Yang Miranda	-	1212206
 * 	Tiago Santana	-	1011154
 * 
 */

import java.security.*;
import javax.crypto.*;
public class MySignature {
	
	private static MySignature msInstance;
	private MessageDigest msMessageDigest; 	//Instancia do Message Digest
	private PrivateKey msPrivateKey;
	private PublicKey msPublicKey;
 	private static String algorithmDigest = "MD5";
 	private static String algorithmEncription = "RSA";
 	
	
	
	//MySignature Singleton Implementation
    private MySignature(String algorithm) throws NoSuchAlgorithmException{
    	String array[] = new String[2];
    	
    	array = algorithm.split("With");

    	algorithmDigest = array[0];
    	algorithmEncription = array[1];
    	
    	msMessageDigest = MessageDigest.getInstance(algorithmDigest);  //TODO <digest>with<encryption> ->>Gambiarra
    } 
    
    public static MySignature getInstance(String algorithm)
            throws NoSuchAlgorithmException {
        if(msInstance == null){
        	msInstance = new MySignature(algorithm);
        }
        return msInstance;
    }


	/*Adiciona a chave privada que será utilizada para cifragem */
	public final void initSign(PrivateKey privateKey)
            throws InvalidKeyException{
		try {
			msPrivateKey = privateKey;
		} catch (ClassCastException cce) {
			throw new InvalidKeyException("Wrong private key type");
		}
	}
	/*Adiciona o texto plano que será utilizado no Digest*/
	public final void update(byte[] data)
	         throws SignatureException{
		try {
			msMessageDigest.update(data);
		} catch (NullPointerException npe) {
			throw new SignatureException("No Type digest found");
		}
	}
	
	
	public final byte[] sign()
	        throws SignatureException{
		byte messageDigestPrivate[] = null;
		try {
			/*
			Faz o Digest do Texto plano
			Requer previamente o initSign(PrivateKey) e update(plainText)
			*/
			messageDigestPrivate = msMessageDigest.digest();
			System.out.println("\nDigest do texto plano");
			printHexaDecimal(messageDigestPrivate);
		} catch (NullPointerException npe) {
			throw new SignatureException("No SHA digest found");
		}


		/*
		Faz cifragem do digest com chave privada
		*/
		byte[] cipherText = null;
		
		try {
			Cipher cipher = Cipher.getInstance(algorithmEncription+"/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, msPrivateKey);
			cipherText = cipher.doFinal(messageDigestPrivate);
			System.out.println("\nAssinatura com Chave Privada");
			printHexaDecimal(cipherText);
		} catch (Exception e){
			throw new SignatureException("Criptografia Falhada");
		}
		return cipherText;
	}


	/*
	/*Adiciona a chave publica que será utilizada para decifragem da assinatura digital recebida */

	public final void initVerify(PublicKey publicKey)
	        throws InvalidKeyException{
		try {
			msPublicKey = publicKey;
		} catch (ClassCastException cce) {
			throw new InvalidKeyException("Wrong public key type");
		}
	}
	
	
	public final boolean verify(byte[] signaturePrivate)
			throws SignatureException{
		/*
		Faz o digest do texto plano inicial pós initVerify(PublicKey) e update(plainText)
		*/		
		byte messageDigestPublic[] = null;
		try {
			messageDigestPublic = msMessageDigest.digest();
			System.out.println("\nDigest do texto plano");
			printHexaDecimal(messageDigestPublic);
		} catch (NullPointerException npe) {
			throw new SignatureException("No SHA digest found");
		}
		/*Decifra com a chave publica a assinatura digital recebida*/
		byte[] messageDigestPrivate = null;
		try {
			Cipher cipher = Cipher.getInstance(algorithmEncription+"/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, msPublicKey);
			messageDigestPrivate = cipher.doFinal(signaturePrivate);
			System.out.println("\nMessage Digest da Chave Privada");
			printHexaDecimal(messageDigestPrivate);
		} catch (Exception e)
		{
			System.err.println("Decriptacao chave publica falhou");
		}	
		return MessageDigest.isEqual(messageDigestPrivate, messageDigestPublic);
	}
	
	public static void printHexaDecimal(byte[] data){
		StringBuffer buf = new StringBuffer();
	    for(int i = 0; i < data.length; i++) {
	       String hex = Integer.toHexString(0x0100 + (data[i] & 0x00FF)).substring(1);
	       buf.append((hex.length() < 2 ? "0" : "") + hex);
	    }
	    System.out.println( buf.toString() );
	}
	
	
	public static void main (String[] args) throws Exception {
		//
	    // verifica args e recebe o texto plano
	    if (args.length !=1) {
	      System.err.println("Usage: java MySignature text");
	      System.exit(1);
	    }
	    byte[] plainText = args[0].getBytes("UTF8");
	    //
	    // gera o par de chaves RSA
	    System.out.println( "\nStart generating RSA key" );
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithmEncription);
	    keyGen.initialize(1024);
	    KeyPair key = keyGen.generateKeyPair();
	    System.out.println( "Finish generating RSA key" );
	    //
	    // define um objeto signature para utilizar MD5 e RSA
	    // e assina o texto plano com a chave privada,
	    // o provider utilizado tambem eh impresso
	    MySignature sig = MySignature.getInstance(algorithmDigest+"With"+algorithmEncription);
	    sig.initSign(key.getPrivate());										 //<<<<<<<<<<<<<<
	    sig.update(plainText);												 //<<<<<<<<<<<<<<
	    byte[] signature = sig.sign();										 //<<<<<<<<<<<<<<
	    //System.out.println( sig.getProvider().getInfo() );
	    System.out.println( "\nSignature:" );
	    
	    //
	    // verifica a assinatura com a chave publica
	    System.out.println( "\nStart signature verification" );
	    sig.initVerify(key.getPublic());									 //<<<<<<<<<<<<<<
	    sig.update(plainText);												 //<<<<<<<<<<<<<<
	    try {
	      if (sig.verify(signature)) {										 //<<<<<<<<<<<<<<
	        System.out.println( "Signature verified" );
	      } else System.out.println( "Signature failed" );
	    } catch (SignatureException se) {
	      System.out.println( "Singature failed" );
	    }
		
	}
	
}
