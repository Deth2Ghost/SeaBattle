package ua.deth.mb.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBconnect {
	private Connection conn;
	private Statement statmt;
	private ResultSet resSet;

	public void Conn() throws ClassNotFoundException, SQLException {
		conn = null;
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:src/ua/deth/mb/server/db.db");

		System.out.println("База Подключена!");
	}

	// --------Выполняем регистрацию--------
	public int register(String login, String password, String email) throws SQLException, ClassNotFoundException {
		Conn();
		statmt = conn.createStatement();
		boolean loginInUse = false;
		resSet = statmt.executeQuery("SELECT login FROM login");

		while (resSet.next()) {
			loginInUse = resSet.getString("login").equals(login) ? true : false;

		}

		if (!loginInUse) {
			statmt.execute("INSERT INTO 'login' ('login', 'password', 'email') VALUES ('" + login + "', '" + password
					+ "', '" + email + "'); ");
			System.out.println("Таблица заполнена");
			
			return 0;
		} else {
			System.out.println("Такое имя уже есть");
			
			return 1;
		}
		

	}

	// -------- Вход в игру--------
	public int login(String login, String password) throws ClassNotFoundException, SQLException {
		Conn();
		statmt = conn.createStatement();
		resSet = statmt.executeQuery("SELECT login, password, isOnline FROM login");
		boolean notFound = true;
		while (resSet.next()) {
			if (resSet.getString("login").equals(login)) {
				if (resSet.getString("password").equals(password)) {
					System.out.println("Вход выполнен");
					statmt.execute("update login set isOnline=1 where login='"+login+"'");
					
					notFound = false;
				}
			}
		}
		
		if (notFound) {
			System.out.println("Check the values");
			return 1;
		} else {
			return 0;
		}

	}
	
	// Выход из игры
	public void exit(String name) throws ClassNotFoundException, SQLException{
		
		Conn();
		statmt = conn.createStatement();
		statmt.execute("update login set isOnline=0 where login='"+name+"'");
		System.out.println("Игрок" + name + " вышел.");
	}

	// --------Получаем список пользователей--------
	public ArrayList CloseDB() throws ClassNotFoundException, SQLException {
		Conn();
		ArrayList loginOnline = new ArrayList<>();
		statmt = conn.createStatement();
		resSet = statmt.executeQuery("SELECT login, isOnline FROM login");
		while(resSet.next()){
			if(resSet.getInt("isOnline") == 1) loginOnline.add(resSet.getString("login"));
			
		}
		if (loginOnline.isEmpty()) loginOnline.add(new String("Игроков нет"));
		System.out.println("Список игроков получен");
		return loginOnline;
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new DBconnect().exit("Deth");
	}


}
