package com.assignment2.monali.bean;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public class Database {
	private final MysqlDataSource dataSource = new MysqlDataSource();

	private String hostname = systemEnvOrProp("SERVER_HOSTNAME");
	private int port = systemEnvOrPropInt("DATABASE_PORT");
	private String databaseName = systemEnvOrProp("DATABASE_NAME");
	private String username = systemEnvOrProp("DB_USER");
	private String password = systemEnvOrProp("DB_PASSWORD");
	private static Database DATABASE = new Database();

	public Database() {
		System.out.println("Connecting to MySQL database on "+hostname);
		dataSource.setDatabaseName(databaseName);
		dataSource.setServerName(hostname);
		dataSource.setPort(port);
		dataSource.setUser(username);
		dataSource.setPassword(password);
	}

	private String systemEnvOrProp(String key) {
		String value = System.getenv(key);
		if (value != null) {
			return value;
		}

		String valueFromProp = System.getProperty(key);
		if (valueFromProp != null) {
			return valueFromProp;
		}

		return null;
	}

	private int systemEnvOrPropInt(String key) {
		final String systemEnvOrProp = systemEnvOrProp(key);
		return Integer.valueOf(systemEnvOrProp);
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public static Database getInstance() {
		return DATABASE;
	}

	public static void closeSilently(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
		}
	}
}
