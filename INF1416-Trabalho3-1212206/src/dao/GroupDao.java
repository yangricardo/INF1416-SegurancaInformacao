package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Group;
import utils.Util;

public class GroupDao {

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

	public static String getGroupName(int idgroup) {
		openConnection();
		String query = "SELECT * FROM Grupos WHERE idgroup = '" + idgroup + "' ;";
		String resultados = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if(rs.next()){
				resultados = rs.getString("namegroup");	
			} 
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
		} 
		closeConnection();
		//Se rs não encontra retorna null
		return resultados;
	}
	
	public static ArrayList<Group> getGroups(){
		openConnection();
		String query = "SELECT * FROM Grupos;";
		ArrayList<Group> resultados = new ArrayList<Group>();
		try{
			ResultSet rs = con.createStatement().executeQuery(query);
			while(rs.next()){
				resultados.add(new Group(rs.getInt("idgroup"),rs.getString("namegroup")));
			}
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
		} 
		
		closeConnection();
		return resultados;
	}
}
