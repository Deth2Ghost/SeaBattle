package ua.deth.mb.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


import ua.deth.mb.logik.Login;
import ua.deth.mb.logik.game;

public class Server extends Thread {
	private static Registry r;
	private static game server;
	private Login login;
	private Socket s;
	private int num;

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
				System.out.println(r.list()[i] + " Сервер запущен");

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
		try {
			int i = 0; // счётчик подключений

			// привинтить сокет на локалхост, порт 3128
			ServerSocket server = new ServerSocket(3128, 0, InetAddress.getByName("localhost"));

			System.out.println("server is started");

			// слушаем порт
			while (true) {
				// ждём нового подключения, после чего запускаем обработку
				// клиента
				// в новый вычислительный поток и увеличиваем счётчик на
				// единичку
				new Server(i, server.accept());
				i++;
			}
		} catch (Exception e) {
			System.out.println("init error: " + e);
		} // вывод исключений
	}
	 public Server(int num, Socket s)
	    {
	        // копируем данные
	        this.num = num;
	        this.s = s;

	        // и запускаем новый вычислительный поток (см. ф-ю run())
	        setDaemon(true);
	        setPriority(NORM_PRIORITY);
	        start();
	    }

	    public void run()
	    {
	        try
	        {
	            // из сокета клиента берём поток входящих данных
	            InputStream is = s.getInputStream();
	            // и оттуда же - поток данных от сервера к клиенту
	            OutputStream os = s.getOutputStream();

	            // буффер данных в 64 килобайта
	            byte buf[] = new byte[64*1024];
	            // читаем 64кб от клиента, результат - кол-во реально принятых данных
	            int r = is.read(buf);

	            // создаём строку, содержащую полученную от клиента информацию
	            String data = new String(buf, 0, r);

	            // добавляем данные об адресе сокета:
	            data = ""+num+": "+"\n"+data;

	            // выводим данные:
	            os.write(data.getBytes());

	            // завершаем соединение
	            s.close();
	        }
	        catch(Exception e)
	        {System.out.println("init error: "+e);} // вывод исключений
	    }
}
