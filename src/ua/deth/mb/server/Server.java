package ua.deth.mb.server;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ua.deth.mb.logik.Login;
import ua.deth.mb.logik.game;

public class Server {
	private static Registry r;
	private static game server;
	private Login login;
	public Server() {
		try {
			r = LocateRegistry.createRegistry(1099);
			server = new game();
			login = new Login();
			// RMI логина
			r.bind("MB", login);
			// RMI игры
			r.bind("MBGame", server);
			for (int i = 0; i < r.list().length; i++) {
				System.out.println(r.list()[i]+" Сервер запущен");
				
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		Server start = new Server();
		
	}

}
