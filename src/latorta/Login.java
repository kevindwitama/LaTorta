package latorta;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login {
	
	private JFrame frm;
	private JLabel lblUser, lblPass, lblTitle, lblNull;
	private JTextField txtUser;
	private JPasswordField passField;
	private JCheckBox ckBx;
	private JButton btnLogin, btnCancel;
	private JPanel pnlLogin, pnlTitle, pnlLoginFld, pnlLoginBtn;
	
	private Connect dbc;
	private ResultSet rs;
	
	private void init() {
		frm.setSize(480, 240);
		frm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.setResizable(false);
		frm.setVisible(true);
	}
	
	// Validate Password
	public boolean testPass(String pass) {
		int digitCount = 0;
		int letterCount = 0;
		int symbolCount = 0;
		String symbols = "!@#$%&*()_+=-|<>?{}[]~";
		
		for (int i = 0; i < pass.length(); i++) {
			if (Character.isDigit(pass.charAt(i))) {
				digitCount++;
			} else if (Character.isLetter(pass.charAt(i))) {
				letterCount++;
			} else if (symbols.contains(String.valueOf(pass.charAt(i)))) {
				symbolCount++;
			}
		}
		
		if (digitCount > 0 && letterCount > 0 && symbolCount > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Login() {
		dbc = new Connect();
		frm = new JFrame("Login");
		lblUser = new JLabel(" Email : ");
		lblPass = new JLabel(" Password : ");
		lblTitle = new JLabel("Login");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNull = new JLabel();
		txtUser = new JTextField();
		passField = new JPasswordField();
		ckBx = new JCheckBox("I agree with the terms & conditions");
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = txtUser.getText();
				String pass = String.valueOf(passField.getPassword());
				String emailCheck = email.replace("@", "");
				
				if (email.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Email must be filled", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!email.contains(".") || !email.contains("@")) {
					JOptionPane.showMessageDialog(null, "Email must contain '@' and '.'", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (email.length() - emailCheck.length() > 1) {
					JOptionPane.showMessageDialog(null, "Email must have only one '@'", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (email.startsWith("@") || email.endsWith("@") || email.startsWith(".") || email.endsWith(".")) {
					JOptionPane.showMessageDialog(null, "Email must not start or end with '@' or '.'", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (pass.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Password must be filled", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!testPass(pass)) {
					JOptionPane.showMessageDialog(null, "Password must contain letters, digits, and symbols", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!ckBx.isSelected()) {
					JOptionPane.showMessageDialog(null, "You must agree to the terms and conditions", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					dbc.open();
					
					try {
						String query = "SELECT RoleName, MemberId FROM member WHERE Email='" + email + "' and Password='" + pass + "'";
						rs = dbc.doQuery(query);
						System.out.println(query);
						if (rs.next()) {
							String roleName = rs.getString(1);
							if (roleName.equals("Member")) {
								int memberId = rs.getInt(2);
								new HomeMember(email, memberId);
								frm.dispose();
								System.out.println("Succesfully logged in");
							} else if (roleName.equals("Admin")) {
								new HomeAdmin();
								frm.dispose();
								System.out.println("Succesfully logged in as Admin");
							}
						} else if (!rs.next()) {
							JOptionPane.showMessageDialog(null, "Email and password incorrect or not registered", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					dbc.close();
	
				}
			}
		});
		frm.getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnlLogin = new JPanel(new BorderLayout());
		pnlTitle = new JPanel();
		pnlLoginFld = new JPanel(new GridLayout(3, 2));
		pnlLoginBtn = new JPanel();
		
		pnlTitle.add(lblTitle);
		
		pnlLoginFld.add(lblUser);
		pnlLoginFld.add(txtUser);
		pnlLoginFld.add(lblPass);
		pnlLoginFld.add(passField);
		
		pnlLoginFld.add(lblNull);
		pnlLoginFld.add(ckBx);
		pnlLoginBtn.add(btnLogin);
		
		pnlLogin.add(pnlTitle, BorderLayout.NORTH);
		pnlLogin.add(pnlLoginFld, BorderLayout.CENTER);
		pnlLogin.add(pnlLoginBtn, BorderLayout.SOUTH);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Main();
				frm.dispose();
			}
		});
		pnlLoginBtn.add(btnCancel);
		
		frm.getContentPane().add(pnlLogin);
		
		init();
	}
	
}
