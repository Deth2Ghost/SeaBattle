package ua.deth.mb.gui;

import java.awt.Container;

import javax.swing.JFrame;

public class okno extends JFrame {
	
	// Конструктор класса
	public okno(String name) {
		
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