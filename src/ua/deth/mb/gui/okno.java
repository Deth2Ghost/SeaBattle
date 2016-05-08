package ua.deth.mb.gui;

import java.awt.Container;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JFrame;

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
		setDefaultCloseOperation(1);
		
		// Запрет изменения размеров окна
		setResizable(false);
		// Отображение (показ) окна
		setVisible(true);
	}
}