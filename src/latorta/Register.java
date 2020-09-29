package latorta;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.Calendar;

public class Register {

	private JFrame frm;
	private JLabel lblUser, lblPass, lblTitle, lblPhone, lblDob, lblGender, lblAddr;
	private JTextField txtUser, txtPhone, txtAddr;
	private JPasswordField passField;
	private JButton btnRgstr, btnCancel;
	private JPanel pnlRgstr, pnlTitle, pnlFields, pnlButton, pnlGender;
	private JSpinner spinnerDob;
	private JRadioButton rdbtnMale, rdbtnFemale;
	
	private final ButtonGroup btnGrpGender = new ButtonGroup();
	
	private Connect dbc;
	
	public void init() {
		frm.setSize(400, 320);
		frm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.setResizable(false);
		frm.setVisible(true);
	}
	
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
	
	public Register() {
		frm = new JFrame("Register");
		lblUser = new JLabel(" Email : ");
		lblPass = new JLabel(" Password : ");
		lblPhone = new JLabel(" Phone : ");
		lblDob = new JLabel(" Date of Birth : ");
		lblGender = new JLabel(" Gender : ");
		lblAddr = new JLabel(" Password : ");
		lblTitle = new JLabel("Register");
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 24));
		txtUser = new JTextField();
		passField = new JPasswordField();
		btnRgstr = new JButton("Register");
		btnRgstr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbc = new Connect();
				String email = txtUser.getText();
				String pass = String.valueOf(passField.getPassword());
				String phoneNum = txtPhone.getText();
				Date dob = (Date) spinnerDob.getValue();
				String gender;
				String address = txtAddr.getText();
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
				} else if (pass.length() < 6 || pass.length() > 12) {
					JOptionPane.showMessageDialog(null, "Password must be between 6 and 12 characters", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!testPass(pass)) {
					JOptionPane.showMessageDialog(null, "Password must contain letters, digits, and symbols", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (phoneNum.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Phone number must be filled", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!phoneNum.startsWith("08")) {
					JOptionPane.showMessageDialog(null, "Phone number must begin with '08'", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (phoneNum.length() != 11 && phoneNum.length() != 12) {
					JOptionPane.showMessageDialog(null, "Phone number must be exactly 11 or 12 digits long", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (btnGrpGender.getSelection() == null) {
					JOptionPane.showMessageDialog(null, "Gender must be selected", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (address.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Address must be filled", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!address.endsWith("Street")) {
					JOptionPane.showMessageDialog(null, "Address must end with 'Street'", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					if (rdbtnFemale.isSelected()) {
						gender = "Female";
					} else {
						gender = "Male";
					}
					dbc.open();
					
					try {
						String query = "INSERT INTO member VALUES (null, '" + email + "', '" + pass + "', '" + phoneNum + "', '" + dob + "', '" + gender + "', '" + address + "', 'Member')";
						dbc.doUpdate(query);
						System.out.println(query);
						String success = "Succesfully added to database";
						System.out.println(success);
						JOptionPane.showMessageDialog(null, success, "Success", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					dbc.close();
				}
			}
		});
		
		pnlRgstr = new JPanel(new BorderLayout());
		pnlTitle = new JPanel();
		pnlFields = new JPanel(new GridLayout(6, 2));
		pnlButton = new JPanel();
		
		pnlTitle.add(lblTitle);
		
		pnlFields.add(lblUser);
		pnlFields.add(txtUser);
		pnlFields.add(lblPass);
		pnlFields.add(passField);
		pnlButton.add(btnRgstr);
		
		pnlRgstr.add(pnlTitle, BorderLayout.NORTH);
		pnlRgstr.add(pnlFields, BorderLayout.CENTER);
		
		lblPhone = new JLabel(" Phone :");
		pnlFields.add(lblPhone);
		
		txtPhone = new JTextField();
		pnlFields.add(txtPhone);
		txtPhone.setColumns(10);
		
		lblDob = new JLabel(" Date of Birth :");
		pnlFields.add(lblDob);
		
		spinnerDob = new JSpinner();
		spinnerDob.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		pnlFields.add(spinnerDob);
		
		lblGender = new JLabel(" Gender :");
		pnlFields.add(lblGender);
		
		pnlGender = new JPanel();
		pnlFields.add(pnlGender);
		
		rdbtnMale = new JRadioButton("Male");
		btnGrpGender.add(rdbtnMale);
		pnlGender.add(rdbtnMale);
		
		rdbtnFemale = new JRadioButton("Female");
		btnGrpGender.add(rdbtnFemale);
		pnlGender.add(rdbtnFemale);
		
		lblAddr = new JLabel(" Address :");
		pnlFields.add(lblAddr);
		
		txtAddr = new JTextField();
		pnlFields.add(txtAddr);
		txtAddr.setColumns(10);
		pnlRgstr.add(pnlButton, BorderLayout.SOUTH);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Main();
				frm.dispose();
			}
		});
		pnlButton.add(btnCancel);
		
		frm.getContentPane().add(pnlRgstr, BorderLayout.NORTH);
		
		init();
	}

}
