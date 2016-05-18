package io.github.liuzm.crawler.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.Statement;



public class DerbyUtil {
	
	private static final Log log = LogFactory.getLog(DerbyUtil.class);
	
	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver"; 
	private static final String PROTOCOL = "jdbc:derby:";
	
	public static final String DERBY_DB_NAME = "crawler_db_info";
	
	public static Connection getConnection(String dbName){
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return DriverManager.getConnection(PROTOCOL + dbName + ";create=true");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static PreparedStatement getPreparedStatement(Connection conn,String sql){
		try {
			return  conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
