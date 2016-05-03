package ua.deth.mb.logik;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import ua.deth.mb.server.DBconnect;
import ua.deth.mb.server.LoginI;
import ua.deth.mb.server.Server;

public class Login extends UnicastRemoteObject implements LoginI{
	private DBconnect connect;
	public Login() throws RemoteException {
		super();
		connect = new DBconnect();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int login(String login, String passwd)throws RemoteException, ClassNotFoundException, SQLException{
		int i = 5;
		i = connect.login(login, passwd);
		return i;
	}
	public int register(String login, String password, String email)throws RemoteException, ClassNotFoundException{
		int i = 5;
		try {
			i = connect.register(login, password, email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	
}
