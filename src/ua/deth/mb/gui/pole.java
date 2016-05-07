package ua.deth.mb.gui;


// Для обработки событий
import java.awt.event.*;
// Для работы с окнами
import javax.swing.*;

import ua.deth.mb.server.Rasstanovka;
import ua.deth.mb.server.gameI;

// Для работы с графикой
import java.awt.*;
// Для работы с изображениями
import javax.imageio.*;
// Для работы с файлами
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

//Класс панели игрового поля
public class pole extends JPanel  {
	String name;
	// Таймер отрисовки
	private Timer tmDraw;
	// Изображения, используемые в игре
	private Image fon, paluba, ubit, ranen, end1, end2, bomba;
	// Две кнопки
	private JButton btn1, btn2;
	// Переменная для реализации логики игры
	private gameI myGame;
	// Координаты курсора мыши
	private int mX, mY;
	private Rasstanovka lRasstanovka;
	private JLabel userList;
	private int[][] masPl;
	private int[][] masSop;

	public class myMouse1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
		}

		// При нажатии кнопки мыши
		public void mousePressed(MouseEvent e) {
			// Если сделано одиночное нажатие левой клавишей мыши
			if ((e.getButton() == 1) && (e.getClickCount() == 1)) {
				// Получаем текущие координаты курсора мыши
				mX = e.getX();
				mY = e.getY();
				// Если курсор мыши внутри игрового поля компьютера
				if ((mX > 100) && (mY > 100) && (mX < 400) && (mY < 400)) {
					// Если не конец игры и ход игрока
					try {
						if ((myGame.getEndg() == 0) && (myGame.isCompHod() == false)) {
							// Вычисляем номер строки в массиве
							int i = (mY - 100) / 30;
							// Вычисляем номер элемента в строке в массиве
							int j = (mX - 100) / 30;
							// Если ячейка подходит для выстрела
							if (myGame.getMasComp()[i][j] <= 4)
								// Производим выстрел
								myGame.vistrelPlay(i, j);
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	public class myMouse2 implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
		}

		// При перемещении курсора мыши
		public void mouseMoved(MouseEvent e) {
			// Получаем координаты курсора
			mX = e.getX();
			mY = e.getY();
			// Если курсор в области поля игрока
			if ((mX >= 100) && (mY >= 100) && (mX <= 400) && (mY <= 400))
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			else
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	// Конструктор класса
	public pole(String name) {
		// Создаем объект новой игры
				try {
					myGame = (gameI)Naming.lookup("MBGame");
				} catch (MalformedURLException | RemoteException | NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		this.name = name;
		// Подключаем обработчики события для мыши к панели
		masPl = new int[10][10];
		masSop = new int[10][10];
		lRasstanovka = new Rasstanovka();
		masPl = lRasstanovka.Rasstanovka(masPl);
		masSop = lRasstanovka.Rasstanovka(masSop);
		try {
			myGame.setMasPlay(masPl);
			myGame.setMasComp(masSop);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		addMouseListener(new myMouse1());
		addMouseMotionListener(new myMouse2());
		setFocusable(true); // Передаем фокус панели

		
		// Запускаем игру
		try {
			start();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Попытка загрузки всех изображений для игры
		try {
			fon = ImageIO.read(new File("res/fon.png"));
			paluba = ImageIO.read(new File("res/paluba.png"));
			ranen = ImageIO.read(new File("res/pr.png"));
			ubit = ImageIO.read(new File("res/pm.png"));
			end1 = ImageIO.read(new File("res/pobeda.png"));
			end2 = ImageIO.read(new File("res/porazhenie.png"));
			bomba = ImageIO.read(new File("res/b.png"));
		} catch (Exception ex) {
		}

		// Создаем, настраиваем и запускаем таймер
		// для отрисовки игрового поля
		tmDraw = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Вызываем перерисовку -paintComponent()
				repaint();
			}
		});
		tmDraw.start();

		// Включаем возможность произвольного размещения
		// элементов интерфейса на панели
		setLayout(null);

		// Создаем кнопку Новая игра
		btn1 = new JButton();
		btn1.setText("Новая игра");
		btn1.setForeground(Color.BLUE);
		btn1.setFont(new Font("serif", 0, 30));
		btn1.setBounds(130, 450, 200, 80);
		btn1.addActionListener(new ActionListener() {
			// Обработчик события при нажатии на кнопку Новая игра
			public void actionPerformed(ActionEvent arg0) {
				// Запуск - начало игры
				try {
					start();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		add(btn1);

		// Создаем кнопку Выход
		btn2 = new JButton();
		btn2.setText("Выход");
		btn2.setForeground(Color.RED);
		btn2.setFont(new Font("serif", 0, 30));
		btn2.setBounds(530, 450, 200, 80);
		btn2.addActionListener(new ActionListener() {
			// Обработчик события при нажатии на кнопку Новая игра
			public void actionPerformed(ActionEvent arg0) {
				// Выход их игры -завершение работы приложения
				System.exit(0);
				try {
					myGame.exit(name);
				} catch (RemoteException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		add(btn2);
		ArrayList u = new ArrayList<>();
		try {
			
			u = myGame.userOnline();
		} catch (RemoteException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userList = new JLabel("Список Игроков на сервере");
		userList.setBounds(950,50,220,50);
		add(userList);
		JList list = new JList(u.toArray());
		list.setBounds(950, 100, 240, 400);
		
		add(list);
	}

	// Метод отрисовки
	public void paintComponent(Graphics gr) {
		// Очищение игрового поля
		super.paintComponent(gr);

		// Отрисовка фона
		gr.drawImage(fon, 0, 0, 900, 600, null);

		// Установка шрифта
		gr.setFont(new Font("serif", 3, 40));
		// Установка цвета
		gr.setColor(Color.BLUE);

		// Выведение надписей
		gr.drawString("Компьютер", 190, 50);
		gr.drawString(name,550, 50);

		// Отрисовка игровых полей Компьютера
		// и Игрока на основании массивов
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

				// Игровое поле компьютера
				try {
					if (myGame.getMasComp()[i][j] != 0) {
						// Если это подбитая палуба корабля
						if ((myGame.getMasComp()[i][j] >= 8) && (myGame.getMasComp()[i][j] <= 11)) {
							gr.drawImage(ranen, 100 + j * 30, 100 + i * 30, 30, 30, null);
						}
						// Если это палуба полностью подбитого корабля
						else if (myGame.getMasComp()[i][j] >= 15) {
							gr.drawImage(ubit, 100 + j * 30, 100 + i * 30, 30, 30, null);
						}
						// Если был выстрел
						if (myGame.getMasComp()[i][j] >= 5) {
							gr.drawImage(bomba, 100 + j * 30, 100 + i * 30, 30, 30, null);
						}
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Игровое поле игрока
				try {
					if (myGame.getMasPlay()[i][j] != 0) {
						// Если это палуба корабля
						if ((myGame.getMasPlay()[i][j] >= 1) && (myGame.getMasPlay()[i][j] <= 4)) {
							gr.drawImage(paluba, 500 + j * 30, 100 + i * 30, 30, 30, null);
						}
						// Если это подбитая палуба корабля
						else if ((myGame.getMasPlay()[i][j] >= 8) && (myGame.getMasPlay()[i][j] <= 11)) {
							gr.drawImage(ranen, 500 + j * 30, 100 + i * 30, 30, 30, null);
						}
						// Если это палуба полностью подбитого корабля
						else if (myGame.getMasPlay()[i][j] >= 15) {
							gr.drawImage(ubit, 500 + j * 30, 100 + i * 30, 30, 30, null);
						}
						// Если был выстрел
						if (myGame.getMasPlay()[i][j] >= 5) {
							gr.drawImage(bomba, 500 + j * 30, 100 + i * 30, 30, 30, null);
						}
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		gr.setColor(Color.RED); // Красный цвет
		// Если курсор мыши внутри игрового поля компьютера
		if ((mX > 100) && (mY > 100) && (mX <400) && (mY < 400)) {
			// Если не конец игры и ход игрока
			try {
				if ((myGame.getEndg() == 0) && (myGame.isCompHod() == false)) {
					// Вычисляем номер строки в массиве
					int i = (mY - 100) / 30;
					// Вычисляем номер элемента в строке в массиве
					int j = (mX - 100) / 30;
					// Если ячйека подходит для выстрела
					if (myGame.getMasComp()[i][j] <= 4)
						// Рисуем квадрат с заливкой
						gr.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Отрисовка сетки игрового поля из синих линий
		gr.setColor(Color.BLUE);
		for (int i = 0; i <= 10; i++) {
			// Рисование линий сетки игрового поля Компьютера
			gr.drawLine(100 + i * 30, 100, 100 + i * 30, 400);
			gr.drawLine(100, 100 + i * 30, 400, 100 + i * 30);

			// Рисование линий сетки игрового поля Человека
			gr.drawLine(500 + i * 30, 100, 500 + i * 30, 400);
			gr.drawLine(500, 100 + i * 30, 800, 100 + i * 30);
		}

		// Установка шрифта
		gr.setFont(new Font("serif", 0, 20));
		// Установка цвета
		gr.setColor(Color.RED);
		// Введение цифр и букв слева и сверху от игровых полей
		for (int i = 1; i <= 10; i++) {
			// Вывод цифр
			gr.drawString("" + i, 73, 93 + i * 30);
			gr.drawString("" + i, 473, 93 + i * 30);

			// Вывод букв
			gr.drawString("" + (char) ('A' + i - 1), 78 + i * 30, 93);
			gr.drawString("" + (char) ('A' + i - 1), 478 + i * 30, 93);
		}
		// Вывод изображения конца игры - при окончании игры
		try {
			if (myGame.getEndg() == 1) // Если победил Игрок
			{
				gr.drawImage(end1, 300, 200, 300, 100, null);
			} else if (myGame.getEndg() == 2) // Если победил Компьютер
			{
				gr.drawImage(end2, 300, 200, 300, 100, null);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start() throws RemoteException{
		// Очищаем игровое поле игрока и компьютера
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				masPl[i][j] = 0;
				masSop[i][j] = 0;
			}
		}
		// Обнуляем признак чьей-то победы
		myGame.setEndg(0); 
		// Передаем первый ход игроку
		myGame.setCompHod(false);
		// Расставляем корабли игрока
		masPl = lRasstanovka.Rasstanovka(masPl);
		masSop = lRasstanovka.Rasstanovka(masSop);
		try {
			myGame.setMasPlay(masPl);
			myGame.setMasComp(masSop);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	
}