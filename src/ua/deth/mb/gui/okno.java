package ua.deth.mb.gui;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ua.deth.mb.server.gameI;

public class okno extends JFrame {
	
	private gameI myGame;

	// Конструктор класса
	public okno(String name) throws MalformedURLException {
		
		// Создание объекта панели и подключения ее к окну
		pole pan = new pole(name);
		
		Container cont = getContentPane();
		cont.add(pan);
		// Заголовок окна
		setTitle("Игра \"Морской бой\"");
		// Границы окна: расположение и размеры
		setBounds(0, 0, 1200, 600);
		// Операция при закрытии окна - завершение приложения
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		WindowListener exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(
		             null, "Вы уверены, что хотите выйти?", 
		             "Подвердите выход", JOptionPane.YES_NO_OPTION, 
		             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) {
		        	try {
						myGame = (gameI)Naming.lookup("MBGame");
					} catch (MalformedURLException | RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        	try {
						myGame.exit(name);
					} catch (RemoteException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		           System.exit(0);
		        }
		    }
		};
		addWindowListener(exitListener);
		// Запрет изменения размеров окна
		setResizable(false);
		// Отображение (показ) окна
		setVisible(true);
	}
}