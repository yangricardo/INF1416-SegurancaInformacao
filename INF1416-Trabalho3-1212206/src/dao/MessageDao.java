package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Message;
import utils.Util;

public class MessageDao {


	/* Local variables */

	private static Connection con;
	private static Statement comando;
	
	/* Private Methods */

	private static void openConnection() {
		try {
			con = ConnectionFactory.getConnection();
			comando = con.createStatement();
		} catch (ClassNotFoundException e) {
			Util.printError("Erro ao carregar o driver", null);
		} catch (SQLException e) {
			Util.printError("Erro ao conectar", e);
		}
	}

	private static void closeConnection() {
		try {
			comando.close();
			con.close();
		} catch (SQLException e) {
			Util.printError("Erro ao fechar conexão", e);
		}
	}

	public static Message getMessage(int idMessage) {
		openConnection();
		String query = "SELECT * FROM Mensagens WHERE idmessage ='" + idMessage + "' ;";
		Message resultados = null;
		try {
			ResultSet rs = comando.executeQuery(query);
			resultados = new Message(rs.getInt("idmessage"),rs.getString("message"));
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
			return null;
		}finally {
			closeConnection();
		}
		return resultados;
	}
	
	public static ArrayList<String> getAllMessages() {
		openConnection();
		String query = "SELECT * FROM Mensagens ;";
		ArrayList<String> resultados = new ArrayList<String>();
		try {
			ResultSet rs = comando.executeQuery(query);
			while(rs.next()){
				resultados.add(rs.getString("idmessage")+" - "+rs.getString("message"));
			}
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
			return null;
		}finally {
			closeConnection();
		}
		return resultados;
	}

	/*public static void main (String[] args){
		Message message = MessageDao.getMessage(1002);
		System.out.println(message.getIdMessage()+": "+message.getMessage());
	}*/
}
