package security;

import java.security.MessageDigest;
import java.util.ArrayList;

public class AuthenticationPassword {

	private static ArrayList<char[]> digits;

	public static void cleanDigits(){
		digits.clear();
		digits = null;
	}
	
	public static void addDigit(String digit){
		if(digits == null)
			digits = new ArrayList<char[]>();
		char[] opDigit = {digit.charAt(0),digit.charAt(4)};
		digits.add(opDigit);
	}

	public static boolean verifyPassword(String salt,String digestPassword){
		
        int countArray[] = new int[digits.size()];
        //define maximo de permutacoes por digito
        int totalPermutations = (int) Math.pow(2,digits.size());		
        //gera lista de possiveis senhas
        StringBuilder possiblePassword;
        //Enquanto houver permutacao para calcular
        for (int count = totalPermutations; count > 0; --count)
        {
        	possiblePassword = new StringBuilder();
            for (int i = 0; i < digits.size(); ++i){
            	possiblePassword.append(digits.get(i)[countArray[i]]);
            }
            String digestPossiblePassword = null;
			digestPossiblePassword = GenerationPassword.generateSafePassword(possiblePassword.toString(), salt);
			//System.out.println(possiblePassword.toString());
			if(MessageDigest.isEqual(digestPossiblePassword.getBytes(),digestPassword.getBytes()) ){
				System.out.println("PlainPassword: "+possiblePassword.toString()+" Salt: "+salt+" HEX(MD5(PlainPassword+Salt)): "+digestPossiblePassword);
				return true;
			}
            for (int nindex = digits.size() - 1; nindex >= 0; --nindex){
                if (countArray[nindex] + 1 < 2){
                    ++countArray[nindex];
                    break;
                }
                countArray[nindex] = 0;
            }
        }
        return false;
    }
	
	/*public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		addDigit("1 - 2"); 		addDigit("2 - 3");
		System.out.println("Array limpo");
		cleanDigits();
		addDigit("3 - 9");		addDigit("3 - 9");
		addDigit("7 - 2");		addDigit("1 - 5");
		addDigit("6 - 8");		addDigit("4 - 0");
		addDigit("6 - 8");		addDigit("7 - 2");
		addDigit("4 - 0");		addDigit("1 - 5");
		generateDigitsPermutations();
		printPossiblePasswords();
		String salt = GenerationPassword.getSalt();
		String password = "3971846205";
		String digestPassword = GenerationPassword.generateSafePassword(password, salt);
		System.out.println("hex(md5("+password+salt+"): "+digestPassword);
		System.out.println(verifyPassword(salt, digestPassword));	
	}*/
}
