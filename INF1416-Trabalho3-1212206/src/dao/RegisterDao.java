package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Register;
import utils.Util;

public class RegisterDao {

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
	
	public static void registerLog(Register register) {
		openConnection();
		String query = "INSERT INTO Registros (iduser,idmessage,message,timeregister)"
				+ "VALUES('"+register.getIdUser()+"','"+register.getIdMessage()
				+"','"+register.getMessage()+"','"+register.getTimeRegister()+"');";
		System.out.println("===>Log Register("+register.getIdMessage()+") = TimeStamp: "+register.getTimeRegister()+" Message: "+register.getMessage());
		try {
			comando.executeUpdate(query);
		} catch (SQLException e) {
			Util.printError("Erro ao inserir Usuario", e);
		} finally {
			closeConnection();
		}
	}
	
	public static ArrayList<Register> getUserLogs(String idUser,String idmessage) {
		openConnection();
		String query = (idmessage.equals("TODOS")
				? "SELECT * FROM Registros WHERE iduser = '" + idUser + "' ORDER BY idregister ;"
				: "SELECT * FROM Registros WHERE iduser = '" + idUser +
				  "' AND idmessage = "+idmessage+" ORDER BY idregister ; "
			);
		ArrayList<Register> resultados = new ArrayList<Register>();
		try {
			ResultSet rs = comando.executeQuery(query);
			while(rs.next()){
				resultados.add(new Register(rs.getInt("idregister"), rs.getInt("idmessage"), rs.getInt("iduser")
						, rs.getString("message"), rs.getString("timeregister")));
			}
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
			return null;
		}finally {
			closeConnection();
		}
		return resultados;
	}
	
	public static ArrayList<Register> getLogs(String idmessage) {
		openConnection();
		String query = (idmessage.equals("TODOS")
						? "SELECT * FROM Registros ORDER BY idregister;"
						:"SELECT * FROM Registros WHERE idmessage = "+idmessage+" ORDER BY idregister ;"
					);
		ArrayList<Register> resultados = new ArrayList<Register>();
		try {
			ResultSet rs = comando.executeQuery(query);
			while(rs.next()){
				resultados.add(new Register(rs.getInt("idregister"), rs.getInt("idmessage"), rs.getInt("iduser")
						, rs.getString("message"), rs.getString("timeregister")));
			}
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
			return null;
		}finally {
			closeConnection();
		}
		return resultados;
	}
	/*public static void main(String[] args) {
		//RegisterDao.registerLog(new Register(1001));
		ArrayList<Register> logs = RegisterDao.getLogs();
		for(Register l : logs){
			System.out.println(l.getIdRegister()+" "+ l.getIdUser() + " "+ l.getIdMessage()+" "+ l.getMessage());
		}
	}*/
}
