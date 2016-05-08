package ua.deth.mb.server;

import java.rmi.RemoteException;

public class Rasstanovka{
	

public Rasstanovka()  {
	
	// TODO Auto-generated constructor stub
}
	public int[][] Rasstanovka(int[][] masPlay) {
		// Создаем один четырехпалубный корабль
		make4P(masPlay, 4);
		// Создаем два трехпалубных корабля
		for (int i = 1; i <= 2; i++)
			make4P(masPlay, 3);
		// Создаем три двухпалубных корабля
		for (int i = 1; i <= 3; i++)
			make4P(masPlay, 2);
		// Создаем четыре однопалубных корабля
		make1P(masPlay);
		return masPlay;
	
	}

	// Проверка ячейки для возможности размещения в ней палубы корабля
	public boolean testNewPaluba(int[][] mas, int i, int j) {
		// Если выход за границы массива
		if (testMasPoz(i, j) == false)
			return false;
		// Если в этой ячейке 0 или -2, то она нам подходит
		if ((mas[i][j] == 0) || (mas[i][j] == -2))
			return true;
		return false;
	}

	// Конечное окружение
	public void okrEnd(int[][] mas) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// Если значение элемента массива -2, то заменяем его на -1
				if (mas[i][j] == -2)
					mas[i][j] = -1;
			}
		}
	}

	// Окружение одной ячейки вокруг
	public void okrBegin(int[][] mas, int i, int j, int val) {
		setOkr(mas, i - 1, j - 1, val); // сверху слева
		setOkr(mas, i - 1, j, val); // сверху
		setOkr(mas, i - 1, j + 1, val); // сверху справа
		setOkr(mas, i, j + 1, val); // справа
		setOkr(mas, i + 1, j + 1, val); // снизу справа
		setOkr(mas, i + 1, j, val); // снизу
		setOkr(mas, i + 1, j - 1, val); // снизу слева
		setOkr(mas, i, j - 1, val); // слева
	}

	// Установить один элемент окружения
	public void setOkr(int[][] mas, int i, int j, int val) {
		// Если не происходит выход за пределы массива
		// и в ячейке нулевое значение
		if (testMasPoz(i, j) && (mas[i][j] == 0))
			// Устанавливаем необходимое значение
			setMasValue(mas, i, j, val);
	}

	// Проверка не выхода за границы массива
	public boolean testMasPoz(int i, int j) {
		if (((i >= 0) && (i <= 9)) && ((j >= 0) && (j <= 9))) {
			return true;
		} else
			return false;
	}

	// Запись значения в массив с проверкой границ массива
	public void setMasValue(int[][] mas, int i, int j, int val) {
		// Если не происходит выход за границы массива
		if (testMasPoz(i, j) == true) {
			// Записываем значение в массив
			mas[i][j] = val;
		}
	}

	// Создание четырех однопалубных кораблей
	public void make1P(int[][] mas) {
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

	// Создание корабля с несколькими палубами от 2-х до 4-х
	public void make4P(int[][] mas, int kolPaluba) {
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
	
}
