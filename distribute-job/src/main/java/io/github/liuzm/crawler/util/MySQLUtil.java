package io.github.liuzm.crawler.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLUtil {
	
	public static Connection getHiveConnection() throws Exception {
		Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
		return DriverManager.getConnection("jdbc:hive://hadoop:10000/default","","");
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(MySQLUtil.getHiveConnection()==null);
	}

}
