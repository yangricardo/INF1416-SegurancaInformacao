package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utils.Util;
public class ConnectionFactory {
	private static Connection c = null;
	public static Connection getConnection() throws ClassNotFoundException,SQLException {
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:INF1416-Trabalho3-1212206.db");
	    } catch ( Exception e ) {
	      Util.printError("Erro ao conectar no banco de dados", e);
	      throw new RuntimeException(e);
	    }
		return c;
	}
}
