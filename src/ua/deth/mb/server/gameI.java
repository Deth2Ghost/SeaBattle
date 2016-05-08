package ua.deth.mb.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface gameI extends Remote{
	// Признак конца игры
		// (0-игра идет, 1-победил игрок,2-победил компьютер)
	public int getEndg() throws RemoteException;
	// Признак хода компьютера (false - ходит игрок)
	public boolean isCompHod() throws RemoteException;
	// Массив для игрового поля игрока
	public int[][] getMasPlay() throws RemoteException;
	// Массив для игрового поля компьютера
	public int[][] getMasComp() throws RemoteException;
	// Выстрел игрока
	public int[][]  vistrelPlay(int[][] masC, int i, int j) throws RemoteException;
	// Запуск игры - начало игры
	public void setCompHod(boolean compHod) throws RemoteException;
	public void setEndg(int endg)throws RemoteException ;
	public ArrayList userOnline()throws RemoteException, ClassNotFoundException;
	public void setMasPlay(int[][] masPlay)throws RemoteException ;
	public void setMasComp(int[][] masComp)throws RemoteException;
	public int exit(String login)throws RemoteException, ClassNotFoundException;
	
	
}
