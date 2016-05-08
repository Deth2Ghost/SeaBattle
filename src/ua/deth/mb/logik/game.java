package ua.deth.mb.logik;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import ua.deth.mb.server.DBconnect;
import ua.deth.mb.server.gameI;

//Класс реализации логики игры
public class game extends UnicastRemoteObject implements gameI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Массив для игрового поля игрока
	public int[][] masPlay;

	public int[][] getMasPlay() throws RemoteException{
		return masPlay;
	}
	public void setMasComp(int[][] masComp)throws RemoteException {
		this.masComp = masComp;
	}
	public void setMasPlay(int[][] masPlay)throws RemoteException {
		this.masPlay = masPlay;
	}

	// Массив для игрового поля компьютера
	public int[][] masComp;

	public int[][] getMasComp() throws RemoteException{
		return masComp;
	}

	// Признак хода компьютера (false - ходит игрок)
	public boolean compHod;

	public boolean isCompHod() throws RemoteException{
		return compHod;
	}

	// Признак конца игры
	// (0-игра идет, 1-победил игрок,2-победил компьютер)
	public int endg;
public void setEndg(int endg)throws RemoteException {
	this.endg = endg;
}
public void setCompHod(boolean compHod) throws RemoteException{
	this.compHod = compHod;
}
	public int getEndg() throws RemoteException{
		return endg;
	}

	// Конструктор класса
	public game() throws RemoteException{
		
		//Создаем массив 10x10 - игровое поле игрока
		//masPlay = new int[10][10];
		//Создаем массив 10x10 - игровое поле компьютера
		//masComp = new int[10][10];
	}

	// Запуск игры - начало игры
	

	// Анализ убитого корабля
	private void analizUbit(int[][] mas, int i, int j, int kolPalub) {
		// Количество раненых палуб
		int kolRanen = 0;
		// Выполняем подсчет раненых палуб
		for (int k = i - (kolPalub - 1); k <= i + (kolPalub - 1); k++) {
			for (int g = j - (kolPalub - 1); g <= j + (kolPalub - 1); g++) {
				// Если это палуба раненого корабля
				if (testMasPoz(k, g) && (mas[k][g] == kolPalub + 7))
					kolRanen++;
			}
		}
		// Если количество раненых палуб совпадает с количеством палуб
		// корабля, то он убит -прибавляем число 7
		if (kolRanen == kolPalub) {
			for (int k = i - (kolPalub - 1); k <= i + (kolPalub - 1); k++) {
				for (int g = j - (kolPalub - 1); g <= j + (kolPalub - 1); g++) {
					// Если это палуба раненого корабля
					if (testMasPoz(k, g) && (mas[k][g] == kolPalub + 7)) {
						// помечаем палубой убитого корабля
						mas[k][g] += 7;
						// окружаем палубу убитого корабля
						okrPodbit(mas, k, g);
					}
				}
			}
		}
	}

	// Проверка убит ли корабль
	private void testUbit(int[][] mas, int i, int j) {
		// Если однопалубный
		if (mas[i][j] == 8) {
			// делаем выстрел
			mas[i][j] += 7;
			// окружаем убитый корабль
			okrPodbit(mas, i, j);
		}
		// Если двухпалубный
		else if (mas[i][j] == 9)
			analizUbit(mas, i, j, 2);
		// Если трехпалубный
		else if (mas[i][j] == 10)
			analizUbit(mas, i, j, 3);
		// Если четырехпалубный
		else if (mas[i][j] == 11)
			analizUbit(mas, i, j, 4);
	}

	// Расстановка кораблей
	private void rasstanovkaKorabley(int[][] mas) {
		// Создаем один четырехпалубный корабль
		make4P(mas, 4);
		// Создаем два трехпалубных корабля
		for (int i = 1; i <= 2; i++)
			make4P(mas, 3);
		// Создаем три двухпалубных корабля
		for (int i = 1; i <= 3; i++)
			make4P(mas, 2);
		// Создаем четыре однопалубных корабля
		make1P(mas);
	}
 
	// Выстрел игрока
	public int[][] vistrelPlay(int[][] masC, int i, int j) throws RemoteException{
		// При выстреле прибавляем число 7
		masComp = masC;
		masComp[i][j] += 7;
		// Проверяем убит ли корабль
		testUbit(masComp, i, j);
		// Проверяем конец игры
		testEndGame();
		// Если был промах - передаем ход компьютеру
		if (masComp[i][j] < 8) {
			compHod = true; // передаем ход компьютеру
			// Ходит компьютер- пока попадает в цель
			while (compHod == true)
				compHod = compHodit();
		}
		masC = masComp;
		return masC;
	}

	// Выстрел компьютера -
	// возвращает истину - если попал
	private boolean compHodit() {
		// Признак попадания в цель
		boolean rez = false;
		// Признак выстрела в раненый
		// корабль
		boolean flag = false;
		_for1:
		// Пробегаем все игровое поле игрока
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// Если находим раненую палубу
				if ((masPlay[i][j] >= 9) && (masPlay[i][j] <= 11)) {
					flag = true;
					// ячейка сверху
					// Проверяем, что можно сделать выстрел
					if (testMasPoz(i - 1, j) && (masPlay[i - 1][j] <= 4) && (masPlay[i - 1][j] != -2)) {
						// делаем выстрел
						masPlay[i - 1][j] += 7;
						// проверяем, что убит
						testUbit(masPlay, i - 1, j);
						// если произошло попадание
						if (masPlay[i - 1][j] >= 8)
							rez = true;
						// прерываем сразу все циклы
						break _for1;
					}
					// ячейка снизу
					// Проверяем, что можно сделать выстрел
					else if (testMasPoz(i + 1, j) && (masPlay[i + 1][j] <= 4) && (masPlay[i + 1][j] != -2)) {
						// делаем выстрел
						masPlay[i + 1][j] += 7;
						// проверяем, что убит
						testUbit(masPlay, i + 1, j);
						// если произошло попадание
						if (masPlay[i + 1][j] >= 8)
							rez = true;
						// прерываем сразу все циклы
						break _for1;
					}
					// ячейка слева
					// Проверяем, что можно сделать выстрел
					if (testMasPoz(i, j - 1) && (masPlay[i][j - 1] <= 4) && (masPlay[i][j - 1] != -2)) {
						// делаем выстрел
						masPlay[i][j - 1] += 7;
						// проверяем, что убит
						testUbit(masPlay, i, j - 1);
						// если произошло попадание
						if (masPlay[i][j - 1] >= 8)
							rez = true;
						// прерываем сразу все циклы
						break _for1;
					}
					// ячейка справа
					// Проверяем, что можно сделать выстрел
					else if (testMasPoz(i, j + 1) && (masPlay[i][j + 1] <= 4) && (masPlay[i][j + 1] != -2)) {
						// делаем выстрел
						masPlay[i][j + 1] += 7;
						// проверяем, что убит
						testUbit(masPlay, i, j + 1);
						// если произошло попадание
						if (masPlay[i][j + 1] >= 8)
							rez = true;
						// прерываем сразу все циклы
						break _for1;
					}
				}
			}
		}
		// если не было выстрела в раненую палубу
		if (flag == false) {
			// делаем 100 случайных попыток выстрела
			// в случайное место
			for (int l = 1; l <= 100; l++) {
				// Находим случайную позицию на игровом поле
				int i = (int) (Math.random() * 10);
				int j = (int) (Math.random() * 10);
				// Проверяем, что можно сделать выстрел
				if ((masPlay[i][j] <= 4) && (masPlay[i][j] != -2)) {
					// делаем выстрел
					masPlay[i][j] += 7;
					// проверяем, что убит
					testUbit(masPlay, i, j);
					// если произошло попадание
					if (masPlay[i][j] >= 8)
						rez = true;
					// выстрел произошел
					flag = true;
					// прерываем цикл
					break;
				}
			}
			// если выстрела еще не было
			if (flag == false) {
				// начинаем пробегать весь массив от начала до конца
				_for2: for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						// Проверяем, что можно сделать выстрел
						if ((masPlay[i][j] <= 4) && (masPlay[i][j] != -2)) {
							// делаем выстрел
							masPlay[i][j] += 7;
							// проверяем, что убит
							testUbit(masPlay, i, j);
							// если произошло попадание
							if (masPlay[i][j] >= 8)
								rez = true;
							// прерываем сразу все циклы
							break _for2;
						}
					}
				}
			}
		}
		// проверяем конец игры
		testEndGame();
		// возвращаем результат
		return rez;
	}

	// Установить один элемент окружения подбитого корабля
	private void setOkrPodbit(int[][] mas, int i, int j) {
		// Если не происходит выход за пределы массива
		// и в ячейке нулевое значение
		if (testMasPoz(i, j) == true) {
			// Устанавливаем необходимое значение
			if ((mas[i][j] == -1) || (mas[i][j] == 6))
				mas[i][j]--;
		}
	}

	// Окружение одной ячейки подбитого вокруг
	private void okrPodbit(int[][] mas, int i, int j) {
		setOkrPodbit(mas, i - 1, j - 1); // сверху слева
		setOkrPodbit(mas, i - 1, j); // сверху
		setOkrPodbit(mas, i - 1, j + 1); // сверху справа
		setOkrPodbit(mas, i, j + 1); // справа
		setOkrPodbit(mas, i + 1, j + 1); // снизу справа
		setOkrPodbit(mas, i + 1, j); // снизу
		setOkrPodbit(mas, i + 1, j - 1); // снизу слева
		setOkrPodbit(mas, i, j - 1); // слева
	}

	// Проверка окончания игры
	private void testEndGame() {
		// Тестовое число = 15*4+16*2*3+17*3*2+18*4
		// Ситуация, когда все корабли убиты
		int testNumber = 330;
		int kolComp = 0; // Сумма убитых палуб компьютера
		int kolPlay = 0; // Сумма убитых палуб игрока
		// Перебираем все элементы сразу двух массивов
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// Суммируем подбитые палубы игрока
				if (masPlay[i][j] >= 15)
					kolPlay += masPlay[i][j];
				// Суммируем подбитые палубы компьютера
				if (masComp[i][j] >= 15)
					kolComp += masComp[i][j];
			}
		}
		if (kolPlay == testNumber)
			endg = 2; // Если победил игрок
		else if (kolComp == testNumber)
			endg = 1; // Если победил компьютер
	}

	// Проверка не выхода за границы массива
	private boolean testMasPoz(int i, int j) {
		if (((i >= 0) && (i <= 9)) && ((j >= 0) && (j <= 9))) {
			return true;
		} else
			return false;
	}

	// Запись значения в массив с проверкой границ массива
	private void setMasValue(int[][] mas, int i, int j, int val) {
		// Если не происходит выход за границы массива
		if (testMasPoz(i, j) == true) {
			// Записываем значение в массив
			mas[i][j] = val;
		}
	}

	// Установить один элемент окружения
	private void setOkr(int[][] mas, int i, int j, int val) {
		// Если не происходит выход за пределы массива
		// и в ячейке нулевое значение
		if (testMasPoz(i, j) && (mas[i][j] == 0))
			// Устанавливаем необходимое значение
			setMasValue(mas, i, j, val);
	}

	// Окружение одной ячейки вокруг
	private void okrBegin(int[][] mas, int i, int j, int val) {
		setOkr(mas, i - 1, j - 1, val); // сверху слева
		setOkr(mas, i - 1, j, val); // сверху
		setOkr(mas, i - 1, j + 1, val); // сверху справа
		setOkr(mas, i, j + 1, val); // справа
		setOkr(mas, i + 1, j + 1, val); // снизу справа
		setOkr(mas, i + 1, j, val); // снизу
		setOkr(mas, i + 1, j - 1, val); // снизу слева
		setOkr(mas, i, j - 1, val); // слева
	}

	// Конечное окружение
	private void okrEnd(int[][] mas) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// Если значение элемента массива -2, то заменяем его на -1
				if (mas[i][j] == -2)
					mas[i][j] = -1;
			}
		}
	}

	// Создание четырех однопалубных кораблей
	private void make1P(int[][] mas) {
		// Циклfor делает четыре шага - для четырех кораблей
		for (int k = 1; k <= 4; k++) {
			// Глухой циклwhile
			while (true) {
				// Находим случайную позицию на игровом поле
				int i = (int) (Math.random() * 10);
				int j = (int) (Math.random() * 10);
				// Проверяем, что там ничего нет и можно разместить корабль
				if (mas[i][j] == 0) {
					// Размещаем однопалубный корабль
					mas[i][j] = 1;
					// Выполняем окружение
					okrBegin(mas, i, j, -1);
					// Прерываем цикл
					break;
				}
			}
		}
	}

	// Проверка ячейки для возможности размещения в ней палубы корабля
	private boolean testNewPaluba(int[][] mas, int i, int j) {
		// Если выход за границы массива
		if (testMasPoz(i, j) == false)
			return false;
		// Если в этой ячейке 0 или -2, то она нам подходит
		if ((mas[i][j] == 0) || (mas[i][j] == -2))
			return true;
		return false;
	}

	// Создание корабля с несколькими палубами от 2-х до 4-х
	private void make4P(int[][] mas, int kolPaluba) {
		// Глухой цикл
		while (true) {

			boolean flag = false;

			// Координаты головы корабля
			int i = 0, j = 0;

			// Создание первой палубы - головы корабля
			// Получение случайной строки
			i = (int) (Math.random() * 10);
			// Получение случайной колонки
			j = (int) (Math.random() * 10);
			// Выбираем случайное направление построения корабля
			// 0 - вверх, 1 -вправо, 2 - вниз, 3 - влево
			int napr = (int) (Math.random() * 4);
			if (testNewPaluba(mas, i, j) == true) {
				if (napr == 0) // вверх
				{
					// Если можно расположить палубу
					if (testNewPaluba(mas, i - (kolPaluba - 1), j) == true)
						flag = true;
				} else if (napr == 1) // вправо
				{
					// Если можно расположить палубу
					if (testNewPaluba(mas, i, j + (kolPaluba - 1)) == true)
						flag = true;
				} else if (napr == 2) // вниз
				{
					// Если можно расположить палубу
					if (testNewPaluba(mas, i + (kolPaluba - 1), j) == true)
						flag = true;
				} else if (napr == 3) // влево
				{
					// Если можно расположить палубу
					if (testNewPaluba(mas, i, j - (kolPaluba - 1)) == true)
						flag = true;
				}
			}
			if (flag == true) {
				// Помещаем в ячейку число палуб
				mas[i][j] = kolPaluba;
				// Окружаем минус двойками
				okrBegin(mas, i, j, -2);
				if (napr == 0) // вверх
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						// Помещаем в ячейку число палуб
						mas[i - k][j] = kolPaluba;
						// Окружаем минус двойками
						okrBegin(mas, i - k, j, -2);
					}
				} else if (napr == 1) // вправо
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						// Помещаем в ячейку число палуб
						mas[i][j + k] = kolPaluba;
						// Окружаем минус двойками
						okrBegin(mas, i, j + k, -2);
					}
				} else if (napr == 2) // вниз
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						// Помещаем в ячейку число палуб
						mas[i + k][j] = kolPaluba;
						// Окружаем минус двойками
						okrBegin(mas, i + k, j, -2);
					}
				} else if (napr == 3) // влево
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						// Помещаем в ячейку число палуб
						mas[i][j - k] = kolPaluba;
						// Окружаем минус двойками
						okrBegin(mas, i, j - k, -2);
					}
				}
				break;
			}
		}
		// Конечное окружение
		okrEnd(mas);
	}
	public ArrayList userOnline() throws RemoteException, ClassNotFoundException{
		ArrayList users = new ArrayList<>();
		try {
			users = new DBconnect().CloseDB();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
		
	}
	public void exit(String login)throws RemoteException, ClassNotFoundException{
		try {
			new DBconnect().exit(login);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}