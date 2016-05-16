package ua.deth.mb.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import ua.deth.mb.server.gameI;

import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class UserList implements Runnable{
	private String name = "";
	private JFrame frame;
	private gameI myGame;
	private ArrayList r;
	private JLabel lblHello;
	private JList list;

	
	
	/**
	 * Create the application.
	 */
	
	public UserList(String name) throws MalformedURLException, RemoteException, NotBoundException {
		initialize(name);
		this.name = name;
		myGame = (gameI)Naming.lookup("MBGame");
		r = new ArrayList<>();
		
		new Thread(this).start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String name) {
		this.name = name;
		list = new JList();
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/icon_340.png"));
		frame.setTitle("Морской Бой");
		frame.setBounds(100, 100, 399, 346);
		frame.setVisible(true);
		WindowListener exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(
		             null, "Вы уверены, что хотите выйти?", 
		             "Подвердите выход", JOptionPane.YES_NO_OPTION, 
		             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) {
		        	try {
						
						myGame.exit(name);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        	System.exit(0);
		        }
		    }
		};
		frame.addWindowListener(exitListener);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				
				
			}
		});
		list.setBounds(10, 61, 203, 235);
		panel.add(list);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 59, 205, 237);
		panel.add(scrollPane);
		
		JLabel lblOnline = new JLabel("Список пользователей onLine");
		lblOnline.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblOnline.setBounds(10, 41, 205, 14);
		panel.add(lblOnline);
		
		JButton button = new JButton("Выход");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showOptionDialog(
			             null, "Вы уверены, что хотите выйти?", 
			             "Подвердите выход", JOptionPane.YES_NO_OPTION, 
			             JOptionPane.QUESTION_MESSAGE, null, null, null);
			        if (confirm == 0) {
			        	try {
							
							myGame.exit(name);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        	System.exit(0);
			        }
			}
		});
		button.setBounds(261, 273, 89, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Игра с компьютером");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					new okno(name);
					frame.setVisible(false);
					} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button_1.setBounds(225, 239, 148, 23);
		panel.add(button_1);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(new Color(255, 250, 205));
		textPane.setText("Для выбора соперника, выделите его мышью.");
		textPane.setBounds(225, 118, 148, 79);
		panel.add(textPane);
		JLabel lblHello = new JLabel("");
		JButton btnNewButton = new JButton("Обновить список");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					r = myGame.userOnline();
					r.remove(name);
					if(r.isEmpty())r.add(new String("Игроков кроме вас нет"));
					list.setListData(r.toArray());
				} catch (RemoteException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(225, 66, 148, 41);
		panel.add(btnNewButton);
		
		
		lblHello.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblHello.setBackground(new Color(255, 228, 196));
		lblHello.setBounds(10, 9, 363, 23);
		lblHello.setText("Добро пожаловать, "+name+"!");
		lblHello.repaint();
		panel.add(lblHello);
		
		
		
	}
	public void run(){
		while(true){
		try {
			r = myGame.userOnline();
			r.remove(name);
			if(r.isEmpty())r.add(new String("Игроков кроме вас нет"));
			list.setListData(r.toArray());
		} catch (RemoteException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}	
	}
}
