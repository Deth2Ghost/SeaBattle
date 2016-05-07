package ua.deth.mb.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface LoginI extends Remote{
	public int login(String login, String passwd)throws RemoteException, ClassNotFoundException, SQLException;
	public int register(String login, String password, String email)throws RemoteException, ClassNotFoundException;
	public void exit(String login)throws RemoteException, ClassNotFoundException;
}
