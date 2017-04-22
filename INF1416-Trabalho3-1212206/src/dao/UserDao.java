package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.User;
import utils.Util;;

public class UserDao {

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

	public static User getUserInfo(String loginname) {
		openConnection();
		String query = "SELECT * FROM Usuarios WHERE loginname = '" + loginname + "' ;";
		User resultados = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if(rs.next()){
				resultados = new User(rs.getInt("iduser"),rs.getString("loginname") ,rs.getString("username")
						,rs.getString("password"), rs.getString("salt"),rs.getInt("idgroup")
						,rs.getString("digitalcertificate"),rs.getBoolean("isblocked"),rs.getString("blockedsince")
						,rs.getInt("contloginerror"), rs.getInt("numberofaccess"), rs.getInt("numberofsearcheskeys"), 
						rs.getInt("numberofsearchesfiles"));
			} 
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
		} 
		closeConnection();
		//Se rs não encontra retorna null
		return resultados;
	}
	
	public static void setUserBlocked(String loginname, String blockedSince, int contLoginError) {
		openConnection();
		String query = "UPDATE Usuarios SET isblocked = '"+1+"', contloginerror = '"+contLoginError+"',"
				+ " blockedsince = '"+blockedSince+"' WHERE loginname = '"+loginname+"' ;";
		try {
			comando.executeUpdate(query);
		} catch (SQLException e) {
			Util.printError("Erro ao bloquear Usuario", e);
		} finally {
			closeConnection();
		}
	}
	
	public static void setUserAvailable(String loginname) {
		openConnection();
		String query = "UPDATE Usuarios SET isblocked = '"+0+"', contloginerror = '"+0+"',"
				+ " blockedsince = '' WHERE loginname = '"+loginname+"' ;";
		try {
			comando.executeUpdate(query);
		} catch (SQLException e) {
			Util.printError("Erro ao habilitar Usuario", e);
		} finally {
			closeConnection();
		}
	}
	
	
	
	public static void createUser(User user) {
		openConnection();
		String query = "INSERT INTO Usuarios (loginname,password,salt,idgroup,username,digitalcertificate)"
				+ "VALUES('"+user.getLoginname()+"','"+user.getPassword()
				+"','"+user.getSalt()+"','"+user.getIdUserGroup()+"','"+user.getUsername()+"','"
				+user.getDigitalcertificate()+"');";
		try {
			comando.executeUpdate(query);
		} catch (SQLException e) {
			Util.printError("Erro ao inserir Usuario", e);
		} finally {
			closeConnection();
		}
	}
	
	public static void setUserAccess(String loginname){
		int oldNumOfAccess = getNumberOfAccess(loginname) + 1;
		openConnection();
		String query = "UPDATE Usuarios SET numberofaccess = "+oldNumOfAccess+" WHERE loginname = '"+
				loginname+"';";
		try {
			comando.executeUpdate(query);
		} catch (SQLException e) {
			Util.printError("Erro ao atualizar numero de acesso do usuario Usuario", e);
		} finally {
			closeConnection();
		}
	}
	
	public static int getNumberOfAccess(String loginname){
		openConnection();
		String query = "SELECT numberofaccess FROM Usuarios WHERE loginname = '"+loginname+"';";
		int numberOfUserAccess = 0;
		try{
			ResultSet rs = con.createStatement().executeQuery(query);
			numberOfUserAccess = rs.getInt(1);
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
		}
		closeConnection();
		return numberOfUserAccess; 
	}
	
	public static void setUserSearch(String loginname){
		int oldNumOfSearchs = getNumberOfKeysSearch(loginname) + 1;
		openConnection();
		String query = "UPDATE Usuarios SET numberofsearcheskeys = "+oldNumOfSearchs+" WHERE loginname = '"+
				loginname+"';";
		try {
			comando.executeUpdate(query);
		} catch (SQLException e) {
			Util.printError("Erro ao atualizar numero de acesso do usuario Usuario", e);
		} finally {
			closeConnection();
		}
	}
	
	public static int getNumberOfKeysSearch(String loginname){
		openConnection();
		String query = "SELECT numberofsearcheskeys FROM Usuarios WHERE loginname = '"+loginname+"';";
		int numberOfUserAccess = 0;
		try{
			ResultSet rs = con.createStatement().executeQuery(query);
			numberOfUserAccess = rs.getInt(1);
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
		}
		closeConnection();
		return numberOfUserAccess; 
	}
	
	public static void setUserFilesSearch(String loginname){
		int oldNumOfSearchs = getNumberOfFilesSearch(loginname) + 1;
		openConnection();
		String query = "UPDATE Usuarios SET numberofsearchesfiles = "+oldNumOfSearchs+" WHERE loginname = '"+
				loginname+"';";
		try {
			comando.executeUpdate(query);
		} catch (SQLException e) {
			Util.printError("Erro ao atualizar numero de acesso do usuario Usuario", e);
		} finally {
			closeConnection();
		}
	}
	
	public static int getNumberOfFilesSearch(String loginname){
		openConnection();
		String query = "SELECT numberofsearchesfiles FROM Usuarios WHERE loginname = '"+loginname+"';";
		int numberOfUserAccess = 0;
		try{
			ResultSet rs = con.createStatement().executeQuery(query);
			numberOfUserAccess = rs.getInt(1);
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
		}
		closeConnection();
		return numberOfUserAccess; 
	}
	
	public static int getNumberOfUsers(){
		openConnection();
		String query = "SELECT COUNT(iduser) FROM Usuarios";
		int numberOfUsers = 0;
		try{
			ResultSet rs = con.createStatement().executeQuery(query);
			numberOfUsers = rs.getInt(1);
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
		}
		closeConnection();
		return numberOfUsers; 
	}
	
	public static ArrayList<String> getAllUsers(){
		openConnection();
		String query = "SELECT * FROM Usuarios;";
		ArrayList<String> resultados = new ArrayList<String>();
		try {
			ResultSet rs = comando.executeQuery(query);
			while(rs.next()){
				resultados.add(rs.getString("iduser")+" - "+rs.getString("loginname"));
			}
		} catch (SQLException e) {
			Util.printError("User Inexistente ou ja Existente", e);
			return null;
		}
		closeConnection();
		return resultados;
	}
	
	
	/*public static void main (String[] args){
		//UserDao.createUser(new User("admin", "Administrador", "7cce55243be4085afc17f42ef04d8d40", "176745866", 1, "C:\\INF1416-Trabalho3\\INF1416-Trabalho3-1011154-1212206\\Pacote-T3\\Keys"));
		//String horabloqueio = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date());
		
		
		
		UserDao.setUserBlocked("admin", horabloqueio);
		User user = UserDao.getUserInfo("admin");
		if(user.isBlocked()==true)
			System.out.println(user.getLoginname()+" bloqueado desde "+user.getBlockedSince());
		UserDao.setUserAvailable("admin");
		user = UserDao.getUserInfo("admin");
		if(user.isBlocked()==false)
			System.out.println(user.getLoginname()+" disponivel");
		User user = UserDao.getUserInfo("admin");
		System.out.println(user.getUsername() +" - "+ user.getPassword());
		System.out.println(user.getPassword());
		//UserDao.createUser(new User("userTiago","7cce55243be4085afc17f42ef04d8d40","176745866",1,"Tiago","teste"));
		user = UserDao.getUserInfo("userYang");
		System.out.println(user.getUsername() +" - "+ user.getPassword());
		user = UserDao.getUserInfo("userTiago");
		System.out.println(user.getUsername() +" - "+ user.getPassword());
		user = UserDao.getUserInfo("a");
		System.out.println(user==null);
		//System.out.println(user.getUsername() +" - "+ user.getPassword());
	}*/

}