package latorta;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class MembersManage {

	private JFrame frm;
	private JLabel lblTitle, lblEmail, lblPass, lblGender, lblPhone, lblDob, lblAddr;
	private JButton btnInsert, btnCancel, btnUpdate, btnDelete;
	private JPanel pnlRgstr, pnlTitle, pnlMid, pnlButton, pnlFields, pnlFieldsLeft, pnlFieldsRight, pnlGender;
	private JTextField txtEmail, txtPhone, txtAddr, txtPasswd;
	private JTable table;
	private JSpinner spnrDob;
	private JRadioButton rdbtnMale, rdbtnFemale;
	private JScrollPane scrollPane;
	private final ButtonGroup btnGrpGender = new ButtonGroup();
	
	private Connect dbc;
	private ResultSet rs;
	
	private int state = 0;
	
	public void init() {
		frm.setSize(800, 480);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.setResizable(false);
		frm.setVisible(true);
	}
	
	void getData() {
		dbc = new Connect();
		dbc.open();
		
		try {
			rs = dbc.doQuery("SELECT * FROM member");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Vector<String> resultTitle = new Vector<>();
		Vector<Vector<Object>> resultData = new Vector<>();
		resultTitle.add("Email");
		resultTitle.add("Password");
		resultTitle.add("Phone");
		resultTitle.add("DOB");
		resultTitle.add("Gender");
		resultTitle.add("Address");
		
		try {
			while(rs.next()) {
				Vector<Object> row = new Vector<>();
				
				String email = rs.getString(2);
				String password = rs.getString(3);
				String phone = rs.getString(4);
				String dob = rs.getString(5);
				String gender = rs.getString(6);
				String address = rs.getString(7);
				
				row.add(email);
				row.add(password);
				row.add(phone);
				row.add(dob);
				row.add(gender);
				row.add(address);
				
				resultData.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dbc.close();
		
		DefaultTableModel dtm = new DefaultTableModel(resultData, resultTitle);
		table.setModel(dtm);
	}
	
	public MembersManage() {
		frm = new JFrame("Manage Members");
		lblTitle = new JLabel("Manage Members");
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 24));
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			
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

			public void actionPerformed(ActionEvent e) {
				if (state == 0){
					txtEmail.setText("");
					txtEmail.setEnabled(true);
					txtPasswd.setText("");
					txtPasswd.setEnabled(true);
					txtPhone.setText("");
					txtPhone.setEnabled(true);
					rdbtnMale.setSelected(false);
					rdbtnMale.setEnabled(true);
					rdbtnFemale.setSelected(false);
					rdbtnFemale.setEnabled(true);
					txtAddr.setText("");
					txtAddr.setEnabled(true);
					btnUpdate.setEnabled(false);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					spnrDob.setEnabled(true);
					btnInsert.setText("Save Insert");
					state = 1;
				} else if (state == 1) {
					try {
						dbc = new Connect();
						String email = txtEmail.getText();
						String pass = txtPasswd.getText();
						String phoneNum = txtPhone.getText();
						Date dob = (Date) spnrDob.getValue();
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
							state = 0;
							txtEmail.setEnabled(false);
							txtPasswd.setEnabled(false);
							txtPhone.setEnabled(false);
							rdbtnMale.setEnabled(false);
							rdbtnFemale.setEnabled(false);
							txtAddr.setEnabled(false);
							btnUpdate.setEnabled(true);
							btnDelete.setEnabled(true);
							btnCancel.setEnabled(false);
							spnrDob.setEnabled(false);
							btnInsert.setText("Insert");
							getData();
						}
					} catch (Exception e1){
						e1.printStackTrace();
					}
				}
			}
		});
		frm.getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnlRgstr = new JPanel(new BorderLayout());
		pnlTitle = new JPanel();
		pnlMid = new JPanel();
		pnlButton = new JPanel();
		
		pnlTitle.add(lblTitle);
		pnlButton.add(btnInsert);
		
		pnlRgstr.add(pnlTitle, BorderLayout.NORTH);
		pnlRgstr.add(pnlMid, BorderLayout.CENTER);
		pnlMid.setLayout(new BorderLayout(0, 0));
		
		pnlFields = new JPanel();
		pnlMid.add(pnlFields, BorderLayout.SOUTH);
		pnlFields.setLayout(new GridLayout(1, 2));
		
		pnlFieldsLeft = new JPanel();
		pnlFields.add(pnlFieldsLeft);
		pnlFieldsLeft.setLayout(new GridLayout(3, 2));
		
		lblEmail = new JLabel(" Email :");
		pnlFieldsLeft.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setEnabled(false);
		pnlFieldsLeft.add(txtEmail);
		txtEmail.setColumns(10);
		
		lblPass = new JLabel(" Password :");
		pnlFieldsLeft.add(lblPass);
		
		txtPasswd = new JTextField();
		txtPasswd.setEnabled(false);
		pnlFieldsLeft.add(txtPasswd);
		txtPasswd.setColumns(10);
		
		lblGender = new JLabel(" Gender :");
		pnlFieldsLeft.add(lblGender);
		
		pnlGender = new JPanel();
		pnlFieldsLeft.add(pnlGender);
		
		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		btnGrpGender.add(rdbtnMale);
		pnlGender.add(rdbtnMale);
		
		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		btnGrpGender.add(rdbtnFemale);
		pnlGender.add(rdbtnFemale);
		
		pnlFieldsRight = new JPanel();
		pnlFields.add(pnlFieldsRight);
		pnlFieldsRight.setLayout(new GridLayout(3, 2));
		
		lblPhone = new JLabel(" Phone :");
		pnlFieldsRight.add(lblPhone);
		
		txtPhone = new JTextField();
		txtPhone.setEnabled(false);
		txtPhone.setColumns(10);
		pnlFieldsRight.add(txtPhone);
		
		lblDob = new JLabel(" Date of Birth :");
		pnlFieldsRight.add(lblDob);
		
		spnrDob = new JSpinner();
		spnrDob.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		spnrDob.setEnabled(false);
		pnlFieldsRight.add(spnrDob);
		
		lblAddr = new JLabel(" Address :");
		pnlFieldsRight.add(lblAddr);
		
		txtAddr = new JTextField();
		txtAddr.setEnabled(false);
		pnlFieldsRight.add(txtAddr);
		txtAddr.setColumns(10);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtEmail.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				txtPasswd.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
				txtPhone.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				if (table.getValueAt(table.getSelectedRow(), 4).toString().equals("Male")) {
					rdbtnFemale.setSelected(false);
					rdbtnMale.setSelected(true);
				} else {
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(true);
				}
				txtAddr.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
			}
		});
		scrollPane = new JScrollPane(table);
		pnlMid.add(scrollPane, BorderLayout.CENTER);
		pnlRgstr.add(pnlButton, BorderLayout.SOUTH);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			
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

			public void actionPerformed(ActionEvent e) {
				if (state == 0) {
					if (table.getSelectionModel().isSelectionEmpty()) {
						JOptionPane.showMessageDialog(null, "Table must have a selection", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						txtEmail.setEnabled(true);
						txtPasswd.setEnabled(true);
						txtPhone.setEnabled(true);
						rdbtnMale.setSelected(false);
						rdbtnMale.setEnabled(true);
						rdbtnFemale.setSelected(false);
						rdbtnFemale.setEnabled(true);
						txtAddr.setEnabled(true);
						btnUpdate.setText("Save Update");
						btnDelete.setEnabled(false);
						btnCancel.setEnabled(true);
						spnrDob.setEnabled(true);
						btnInsert.setEnabled(false);
						state = 2;
					}
				} else if (state == 2) {
					try {
						dbc = new Connect();
						String email = txtEmail.getText();
						String pass = txtPasswd.getText();
						String phoneNum = txtPhone.getText();
						Date dob = (Date) spnrDob.getValue();
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
							
							int memberId = 0;
							
							dbc.open();
							
							try {
								String query = "SELECT MemberID FROM member WHERE Email='" + table.getValueAt(table.getSelectedRow(), 0).toString() + "'";
								rs = dbc.doQuery(query);
								if (rs.next()) {
									memberId = rs.getInt(1);
								}
								System.out.println(query);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							dbc.close();
							
							dbc.open();
							
							try {
								String query = "UPDATE member SET Email='" + email + "', Password='" + pass + "',"
										+ " Phone='" + phoneNum + "', DOB='" + dob + "',"
										+ " Gender='" + gender + "', Address='" + address + "'"
										+ " WHERE MemberId=" + memberId;
								dbc.doUpdate(query);
								System.out.println(query);
								String success = "Succesfully added to database";
								System.out.println(success);
								JOptionPane.showMessageDialog(null, success, "Success", JOptionPane.INFORMATION_MESSAGE);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							dbc.close();
							
							state = 0;
							btnCancel.setEnabled(false);
							txtEmail.setEnabled(false);
							txtPasswd.setEnabled(false);
							txtPhone.setEnabled(false);
							rdbtnMale.setEnabled(false);
							rdbtnFemale.setEnabled(false);
							txtAddr.setEnabled(false);
							spnrDob.setEnabled(false);
							btnUpdate.setText("Update");
							btnDelete.setEnabled(true);
							btnInsert.setEnabled(true);
							getData();
						}
					} catch (Exception e1){
						e1.printStackTrace();
					}
				}
			}
		});
		pnlButton.add(btnUpdate);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectionModel().isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Table must have a selection", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?",
							"Confirmation", JOptionPane.YES_NO_OPTION);
					if (confirmation == JOptionPane.YES_OPTION) {
						try {
							dbc = new Connect();
							
							int memberId = 0;
							
							dbc.open();
							
							try {
								String query = "SELECT MemberID FROM member WHERE Email='" + table.getValueAt(table.getSelectedRow(), 0).toString() + "'";
								rs = dbc.doQuery(query);
								if (rs.next()) {
									memberId = rs.getInt(1);
								}
								System.out.println(query);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							dbc.close();
							
							dbc.open();
							
							try {
								String query = "DELETE FROM member WHERE MemberId = " + memberId;
								dbc.doUpdate(query);
								System.out.println(query);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							dbc.close();
							
							getData();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		pnlButton.add(btnDelete);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch (state) {
				case 1:
					state = 0;
					btnCancel.setEnabled(false);
					txtEmail.setEnabled(false);
					txtPasswd.setEnabled(false);
					txtPhone.setEnabled(false);
					rdbtnMale.setEnabled(false);
					rdbtnFemale.setEnabled(false);
					txtAddr.setEnabled(false);
					spnrDob.setEnabled(false);
					btnUpdate.setEnabled(true);
					btnDelete.setEnabled(true);
					btnInsert.setText("Insert");
					break;
				case 2:
					state = 0;
					btnCancel.setEnabled(false);
					txtEmail.setEnabled(false);
					txtPasswd.setEnabled(false);
					txtPhone.setEnabled(false);
					rdbtnMale.setEnabled(false);
					rdbtnFemale.setEnabled(false);
					txtAddr.setEnabled(false);
					spnrDob.setEnabled(false);
					btnUpdate.setText("Update");
					btnDelete.setEnabled(true);
					btnInsert.setEnabled(true);
					break;
				}
			}
		});
		btnCancel.setEnabled(false);
		pnlButton.add(btnCancel);
		
		frm.getContentPane().add(pnlRgstr);
		
		getData();
		init();
	}

}
