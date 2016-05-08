package ua.deth.mb.gui;


import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ua.deth.mb.server.LoginI;

import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;
import java.awt.Font;
import java.awt.Color;


public class Login extends JDialog {
	/**
	 * author Deth
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textLogin;
	private JPasswordField textPasswd;
	private JTextField textLoginRegister;
	private JPasswordField textPasswordREgister;
	private JFormattedTextField textFieldEmail;
	private JLabel enterLogin;
	private JLabel enterPassword;
	private JLabel errorLogin;
	
	private LoginI log;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			Login dialog = new Login();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Login() {
		// Подключаемся к нашему серверу RMI
		try {
			log = (LoginI)Naming.lookup("MB");;
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		setTitle("Логин в игру \"Морской Бой\"");
		setBounds(100, 100, 432, 258);
		getContentPane().setLayout(null);
		setResizable(false);
		// Две панели для выбоа типа вход/регистрация
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 413, 215);
		getContentPane().add(tabbedPane);
		// панель на вход
		JPanel panel = new JPanel();
		tabbedPane.addTab("Вход", null, panel, null);
		panel.setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 154, 408, 33);
			panel.add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String login = textLogin.getText();
						String passwd = String.copyValueOf(textPasswd.getPassword());
						// Проверяем поле логин ( что не пусто)
						if(textLogin.getText().isEmpty()){
							enterLogin.setText("*");
							errorLogin.setText("Поле логин не может быть пустым");
						}else{
							enterLogin.setText("");
							errorLogin.setText("");
							// Проверяем поле пароль ( что не пусто)
							if(passwd.isEmpty()){
								enterPassword.setText("*");
								errorLogin.setText("Поле пароль не может быть пустым");
							}else{
								enterPassword.setText("");
								errorLogin.setText("");
								// Проверка логина и пароля по базе данных
								try {
									int i = 5;
									i = log.login(login, passwd);
									if(i == 0){
										new okno(login);
										
										setVisible(false);
									}else{
										errorLogin.setText("Логин или пароль введены неверно");
									}
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				//Кнопка выхода
				JButton cancelButton = new JButton("Выход");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		textLogin = new JTextField();
		textLogin.setToolTipText("");
		textLogin.setBounds(10, 40, 217, 20);
		panel.add(textLogin);
		textLogin.setColumns(10);
		
		JLabel label = new JLabel("Логин:");
		label.setBounds(10, 11, 46, 14);
		panel.add(label);
		
		JLabel label_1 = new JLabel("Пароль:");
		label_1.setBounds(10, 71, 78, 14);
		panel.add(label_1);
		
		textPasswd = new JPasswordField();
		textPasswd.setBounds(10, 96, 217, 20);
		panel.add(textPasswd);
		textPasswd.setColumns(10);
		
		enterLogin = new JLabel("");
		enterLogin.setForeground(Color.RED);
		enterLogin.setBounds(237, 39, 161, 14);
		panel.add(enterLogin);
		
		enterPassword = new JLabel("");
		enterPassword.setForeground(Color.RED);
		enterPassword.setBounds(237, 96, 161, 14);
		panel.add(enterPassword);
		
		errorLogin = new JLabel("");
		errorLogin.setForeground(Color.RED);
		errorLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
		errorLogin.setBounds(75, 127, 303, 27);
		panel.add(errorLogin);
		
		
		// панель регистраци
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Регистрация", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Логин:");
		lblNewLabel.setBounds(10, 11, 114, 14);
		panel_1.add(lblNewLabel);
		
		textLoginRegister = new JTextField();
		textLoginRegister.setBounds(10, 36, 199, 20);
		panel_1.add(textLoginRegister);
		textLoginRegister.setColumns(10);
		
		JLabel label_2 = new JLabel("Пароль:");
		label_2.setBounds(10, 56, 95, 14);
		panel_1.add(label_2);
		
		textPasswordREgister = new JPasswordField();
		textPasswordREgister.setBounds(10, 75, 199, 20);
		panel_1.add(textPasswordREgister);
		textPasswordREgister.setColumns(10);
		
		JLabel lblEmail = new JLabel("Введите email:");
		lblEmail.setBounds(10, 94, 199, 14);
		panel_1.add(lblEmail);
		
		textFieldEmail = new JFormattedTextField();
		textFieldEmail.setToolTipText("");
		textFieldEmail.setColumns(10);
		textFieldEmail.setBounds(10, 117, 199, 20);
		panel_1.add(textFieldEmail);
		JLabel loginErrorR = new JLabel("");
		loginErrorR.setForeground(Color.RED);
		loginErrorR.setBounds(219, 39, 179, 14);
		panel_1.add(loginErrorR);
		
		JLabel passwdErrorR = new JLabel("");
		passwdErrorR.setForeground(Color.RED);
		passwdErrorR.setBounds(219, 78, 179, 14);
		panel_1.add(passwdErrorR);
		
		JLabel emailError = new JLabel("");
		emailError.setForeground(Color.RED);
		emailError.setBounds(219, 120, 179, 14);
		panel_1.add(emailError);
		JButton btnRegister = new JButton("Регистрация");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String login = textLoginRegister.getText();
				String password = String.copyValueOf(textPasswordREgister.getPassword());
				String email = textFieldEmail.getText();
				// Проверка полей на наличие значений
				if(!login.isEmpty()){
					if(!password.isEmpty()){
						if(!email.isEmpty()){
							//Проверка имени на уникальность и регистрация
							try {
								int i = 5;
								i = log.register(login, password, email);
								if(i == 1){
									loginErrorR.setText("Такое имя уже есть");
								}
								
								if(i == 0){
									new okno(login);
									
									setVisible(false);
								}
							} catch (RemoteException | ClassNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}else{
							emailError.setText("Введите ваш email");
						}
					}else{
						passwdErrorR.setText("Введите пароль");
					}
				}else{
					loginErrorR.setText("Введите логин");
				}
				
			}
		});
		btnRegister.setBounds(71, 148, 114, 23);
		panel_1.add(btnRegister);
		// кнопка выхода
		JButton btnExit = new JButton("Выход");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(200, 148, 89, 23);
		panel_1.add(btnExit);
		
		
	}
}
