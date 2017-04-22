package trabalho2;
/*
 * 	PUC-Rio
 * 	INF1416 - Seguran�a da Informa��o - 2016.1
 * 	Trabalho 2 
 * 	Yang Miranda	-	1212206
 * 	Tiago Santana	-	1011154
 * 
 */

import java.io.*;
import java.security.*;
import java.util.*;

public class DigestCalculator {

	private static boolean existeDigest(LinkedHashMap<String, LinkedHashMap<String, String>> digestMap, String digest, String tipoDigest)
	{
		for (Map.Entry<String, LinkedHashMap<String, String>> entry : digestMap.entrySet())
		{
			Map<String, String> hash = entry.getValue();
			if(hash.containsKey(tipoDigest) && hash.get(tipoDigest).equals(digest))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		// javac -classpath . DigestCalculator.java
		// java -classpath . DigestCalculator listaDigest.txt md5 arq1.txt arq2.txt
		if (args.length < 3) {
			System.err.println("Usage: java DigestCalculator \"Caminho_ArqListaDigest\" \"Tipo_Digest\" \"Caminho_Arq1\" ... \"Caminho_ArqN\"");
			System.exit(1);
		}

		/*
		 * Argumento 1: \"Caminho_ArqListaDigest\"
		 * Argumento 2: \"Tipo_Digest\"
		 * Argumento 3_N: \"Caminho_Arq1\" ... \"Caminho_ArqN\"
		 */


		List<String> argsLista  = Arrays.asList(args);
		String listaDigests = argsLista.get(0);
		String tipoDigest = argsLista.get(1);
		List<String> arquivos = argsLista.subList(2, argsLista.size());

		//Checa Diretorio de Digests
		File arquivo = new File(listaDigests);
		if(!arquivo.isFile()){
			System.err.println("\"Caminho_ArqListaDigest\" nao eh um caminho valido!");
			System.exit(1);
		}
		Boolean boolMD5 = !tipoDigest.equals("MD5") && !tipoDigest.equals("md5") ;
		Boolean boolSHA1 = !tipoDigest.equals("SHA1") && !tipoDigest.equals("sha1");
		if(  boolMD5 && boolSHA1  ){
			System.err.println("\"Tipo_Digest\" nao eh um tipo de digest valido. (Entradas Validas: MD5 | md5 | SHA1 | sha1");
			System.exit(1);
		}

		System.out.println(" "+argsLista.size()+" argumentos lidos. \n \"Caminho_ArqListaDigest\": "+listaDigests + "\n \"Tipo_Digest\": " + tipoDigest);

		//Checa todos os caminhos para os arquivos
		int i = 0;
		for(String arquivoCaminho : arquivos) {
			i++;
			arquivo = new File(arquivoCaminho);

			//			System.out.println(arquivo);

			if(!arquivo.isFile()) {
				System.err.println(arquivoCaminho+" (\"Caminho_Arq"+i+"\") nao eh um caminho valido!");
				System.exit(1);
			}
			System.out.println(" \"Caminho_Arq"+i+"\": " +arquivoCaminho);
		}

		// mapa para verificacao de digests
		LinkedHashMap<String, LinkedHashMap<String, String>> digestMap = buildDigestMap(listaDigests);

		// verifica todos os arquivos
		for(String arquivoCaminho : arquivos) {
			try {
				byte[] bytesArquivo = getBytesArquivo(new File(arquivoCaminho));
				byte[] digest;
				String digestHex;
				String nomeArquivo;
				// inicia o messageDigest com tipoDigest (MD5|SHA1)
				MessageDigest messageDigest = MessageDigest.getInstance(tipoDigest);
				messageDigest.update(bytesArquivo, 0, bytesArquivo.length);
				digest = messageDigest.digest();
				digestHex = convertHexaDecimal(digest);
				nomeArquivo = getNomeArquivo(arquivoCaminho);
				
				if(digestMap.containsKey(nomeArquivo) && digestMap.get(nomeArquivo).containsKey(tipoDigest) )
				{
					Map<String, String> digestTypeMap = digestMap.get(nomeArquivo);
					// ok
					if(digestTypeMap.get(tipoDigest).equals(digestHex))
					{
						System.out.println(nomeArquivo +" "+ tipoDigest +" "+
								digestHex +" (OK)");
					}
					//not ok
					else
					{
						System.out.println(nomeArquivo +" "+ tipoDigest +" "+
								digestHex +" (NOT OK)");
					}
				}
				//colisao
				else if(existeDigest(digestMap, digestHex, tipoDigest))
				{
					System.out.println(nomeArquivo +" "+ tipoDigest +" "+
							digestHex +" (COLISION)");
				}
				// not found
				else
				{
					System.out.println(nomeArquivo +" "+ tipoDigest +" "+ digestHex
					+" (NOT FOUND)");
					
					if(digestMap.containsKey(nomeArquivo))
					{
						digestMap.get(nomeArquivo).put(tipoDigest, digestHex);
					}
					else
					{						
						LinkedHashMap<String, String> newDigest = new LinkedHashMap<String, String>();
						newDigest.put(tipoDigest, digestHex);
						digestMap.put(nomeArquivo,newDigest);
					}

					
					System.out.println(digestMap);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		appendDigest(listaDigests, digestMap);

	}

	private static void appendDigest(String listaDigests, LinkedHashMap<String, LinkedHashMap<String, String>> digestMap){
		try{
			FileWriter fw = new FileWriter(listaDigests, false);
			BufferedWriter out = new BufferedWriter(fw);
			
			for (Map.Entry<String, LinkedHashMap<String, String>> map : digestMap.entrySet()){
				String linha = map.getKey();
				Map<String, String> digest = map.getValue();
				for (Map.Entry<String, String> aux : digest.entrySet() ){
					linha = linha+" "+aux.getKey()+" "+aux.getValue();
				}
				out.write(linha+'\n');
			}
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public static String convertHexaDecimal(byte[] data){
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0x0100 + (data[i] & 0x00FF)).substring(1);
			buf.append((hex.length() < 2 ? "0" : "") + hex);
		}
		return buf.toString();
	}

	private static String getNomeArquivo(String arquivo) {
		String[] partesArquivo = arquivo.split("\\\\");
		String nomeArquivo = partesArquivo[partesArquivo.length-1];
		return nomeArquivo;
	}

	private static byte[] getBytesArquivo(File arquivo) throws IOException {
		// check file
		if (!arquivo.exists()) {
			return null;
		}
		// verify file size
		long length = arquivo.length();
		int maxLength = Integer.MAX_VALUE;
		if (length > maxLength)
			throw new IOException(String.format("The file %s is too large",
					arquivo.getName()));
		int len = (int) length;
		byte[] bytes = new byte[len];
		InputStream in = new FileInputStream(arquivo);
		// read bytes
		int offset = 0, n = 0;
		while (offset < len && n >= 0) {
			n = in.read(bytes, offset, len - offset);
			offset += n;
		}
		in.close();
		if (offset < len)
			throw new IOException("Falha ao ler o conteudo completo de: " +	arquivo.getName());
		return bytes;
	}

//	private static Map<String, String> getDigestArquivo(String nomeArquivo){
//		if(digestMap.containsKey(nomeArquivo)) {
//			return digestMap.get(nomeArquivo);
//		}
//		return null;
//	}
	
	private static LinkedHashMap<String, LinkedHashMap<String, String>> buildDigestMap(String listaDigest) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(listaDigest));
			LinkedHashMap<String, LinkedHashMap<String, String>> digestMap = new LinkedHashMap<String,
					LinkedHashMap<String,String>>();
			String buffer;
			while ((buffer = in.readLine()) != null) {
				String[] linha = buffer.split(" ");
				LinkedHashMap<String, String> digestTipoMap;
				if(digestMap.containsKey(linha[0])) {
					digestTipoMap = digestMap.get(linha[0]);
				} else {
					digestTipoMap = new LinkedHashMap<String, String>();
				}
				digestTipoMap.put(linha[1], linha[2]);
				if(linha.length >3)
					digestTipoMap.put(linha[3], linha[4]);
				digestMap.put(linha[0], digestTipoMap);
			}
			in.close();
			return digestMap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}

