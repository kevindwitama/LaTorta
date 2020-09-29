package latorta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
	
	private JFrame frm;
	private JMenuBar mnuBar;
	private JMenu mnuMenu;
	private JMenuItem mnuItmLogin, mnuItmRegister;
	
	public void init() {
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setSize(1280, 720);
		frm.setLocationRelativeTo(null);
		frm.setVisible(true);
		frm.getContentPane().setLayout(new BorderLayout(0, 0));
	}
	
	public JFrame getFrame() {
		return frm;
	}

	public Main() {
		frm = new JFrame();

		mnuBar = new JMenuBar();
		mnuMenu = new JMenu("Menu");
		mnuItmLogin = new JMenuItem("Login");
		mnuItmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Login();
                frm.dispose();
			}
		});
		mnuItmRegister = new JMenuItem("Register");
		mnuItmRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Register();
				frm.dispose();
			}
		});
		
		mnuMenu.add(mnuItmLogin);
		mnuMenu.add(mnuItmRegister);
		mnuBar.add(mnuMenu);
		frm.setJMenuBar(this.mnuBar);
		
		init();
	}

	public static void main(String[] args) {
		new Main();
	}
}
