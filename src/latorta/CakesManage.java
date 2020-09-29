package latorta;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CakesManage {

	private JFrame frm;
	private JLabel lblTitle, lblProdId, lblProdName, lblProdStock, lblBrand, lblPrice;
	private JButton btnCancel;
	private JPanel pnlRgstr, pnlTitle, pnlMid, pnlButton, pnlFields, pnlFieldsLeft, pnlFieldsRight;
	private JTextField txtPrdId, txtPrdName, txtPrice;
	private JTable table;
	private JScrollPane scrollPane;
	private JSpinner spnrStock;
	private JButton btnInsert, btnUpdate, btnDelete;
	
	private Connect dbc;
	private ResultSet rs;
	
	private int state = 0;
	private JSpinner spnrBrand;
	
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
			rs = dbc.doQuery("SELECT cake.CakeId, brand.BrandName, cake.CakeName, cake.Price, cake.Stock"
					+ " FROM cake"
					+ " INNER JOIN brand"
					+ " ON cake.BrandId=brand.BrandId;");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Vector<String> resultTitle = new Vector<>();
		Vector<Vector<Object>> resultData = new Vector<>();
		resultTitle.add("Cake ID");
		resultTitle.add("Brand Name");
		resultTitle.add("Cake Name");
		resultTitle.add("Price");
		resultTitle.add("Stock");
		
		try {
			while(rs.next()) {
				Vector<Object> row = new Vector<>();
				
				int cakeId = rs.getInt(1);
				String brandName = rs.getString(2);
				String cakeName = rs.getString(3);
				int price = rs.getInt(4);
				int stock = rs.getInt(5);
				
				row.add(cakeId);
				row.add(brandName);
				row.add(cakeName);
				row.add(price);
				row.add(stock);
				
				resultData.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbc.close();
		
		DefaultTableModel dtm = new DefaultTableModel(resultData, resultTitle);
		table.setModel(dtm);
	}
	
	public CakesManage() {
		frm = new JFrame("Buy Items");
		lblTitle = new JLabel("Manage Cakes");
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 24));
		frm.getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnlRgstr = new JPanel(new BorderLayout());
		pnlTitle = new JPanel();
		pnlMid = new JPanel();
		pnlButton = new JPanel();
		
		pnlTitle.add(lblTitle);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int cakeId = Integer.parseInt(txtPrdId.getText());
				int brandId = Integer.parseInt(spnrBrand.getValue().toString());
				String cakeName = txtPrdName.getText();
				int price = Integer.parseInt(txtPrice.getText());
				int stock = Integer.parseInt(spnrStock.getValue().toString());
				
				if (state == 0) {
					txtPrdId.setEnabled(true);
					txtPrdId.setText("");
					txtPrdName.setEnabled(true);
					txtPrdName.setText("");
					spnrStock.setEnabled(true);
					spnrBrand.setEnabled(true);
					txtPrice.setEnabled(true);
					txtPrice.setText("");
					btnInsert.setText("Save Insert");
					btnUpdate.setEnabled(false);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					
					state = 1;
				} else if (state == 1) {
					dbc.open();
					
					try {
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					dbc.close();
					
					dbc.open();
					
					try {
						String query = "INSERT INTO cake VALUES ('" + cakeId + "', '" + brandId + "', '" + cakeName + "', '"
								+ price + "', '" + stock + "')";
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
					txtPrdId.setEnabled(false);
					txtPrdName.setEnabled(false);
					spnrStock.setEnabled(false);
					spnrBrand.setEnabled(false);
					txtPrice.setEnabled(false);
					btnUpdate.setEnabled(true);
					btnDelete.setEnabled(true);
					btnCancel.setEnabled(false);
					btnInsert.setText("Insert");
					getData();
				}
			}
		});
		pnlButton.add(btnInsert);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int cakeId = Integer.parseInt(txtPrdId.getText());
				int brandId = Integer.parseInt(spnrBrand.getValue().toString());
				String cakeName = txtPrdName.getText();
				int price = Integer.parseInt(txtPrice.getText());
				int stock = Integer.parseInt(spnrStock.getValue().toString());
				
				if (state == 0) {
					txtPrdId.setEnabled(true);
					txtPrdName.setEnabled(true);
					spnrStock.setEnabled(true);
					spnrBrand.setEnabled(true);
					txtPrice.setEnabled(true);
					btnUpdate.setText("Save Update");
					btnInsert.setEnabled(false);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					
					state = 2;
				} else if (state == 2) {
					dbc.open();
					
					try {
						String query = "SELECT BrandId"
								+ " FROM brand"
								+ " INNER JOIN cake"
								+ " ON brand.BrandId=cake.BrandId";
						rs = dbc.doQuery(query);
						if (rs.next()) {
							brandId = rs.getInt(0); 
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					dbc.close();
					
					dbc.open();
					
					try {
						String query = "UPDATE cake SET BrandId='" + brandId + "', CakeName='" + cakeName + "',"
								+ " Price='"+ price + "', Stock='" + stock + "' WHERE CakeId=" + cakeId;
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
					txtPrdId.setEnabled(false);
					txtPrdName.setEnabled(false);
					spnrStock.setEnabled(false);
					spnrBrand.setEnabled(false);
					txtPrice.setEnabled(false);
					btnUpdate.setEnabled(true);
					btnDelete.setEnabled(true);
					btnCancel.setEnabled(false);
					btnInsert.setText("Insert");
					getData();
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
							
							dbc.open();
							
							try {
								String query = "DELETE FROM cake WHERE CakeId = " + Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
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
		
		pnlRgstr.add(pnlTitle, BorderLayout.NORTH);
		pnlRgstr.add(pnlMid, BorderLayout.CENTER);
		pnlMid.setLayout(new BorderLayout(0, 0));
		
		pnlFields = new JPanel();
		pnlMid.add(pnlFields, BorderLayout.SOUTH);
		pnlFields.setLayout(new GridLayout(1, 2));
		
		pnlFieldsLeft = new JPanel();
		pnlFields.add(pnlFieldsLeft);
		pnlFieldsLeft.setLayout(new GridLayout(3, 2));
		
		lblProdId = new JLabel(" Product ID :");
		pnlFieldsLeft.add(lblProdId);
		
		txtPrdId = new JTextField();
		txtPrdId.setEnabled(false);
		pnlFieldsLeft.add(txtPrdId);
		txtPrdId.setColumns(10);
		
		lblProdName = new JLabel(" Product Name :");
		pnlFieldsLeft.add(lblProdName);
		
		txtPrdName = new JTextField();
		txtPrdName.setEnabled(false);
		pnlFieldsLeft.add(txtPrdName);
		txtPrdName.setColumns(10);
		
		lblProdStock = new JLabel(" Stock :");
		pnlFieldsLeft.add(lblProdStock);
		
		spnrStock = new JSpinner();
		spnrStock.setEnabled(false);
		spnrStock.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));
		pnlFieldsLeft.add(spnrStock);
		
		pnlFieldsRight = new JPanel();
		pnlFields.add(pnlFieldsRight);
		pnlFieldsRight.setLayout(new GridLayout(3, 2));
		
		lblBrand = new JLabel(" Brand ID :");
		pnlFieldsRight.add(lblBrand);
		
		spnrBrand = new JSpinner();
		spnrBrand.setEnabled(false);
		pnlFieldsRight.add(spnrBrand);
		
		lblPrice = new JLabel(" Price :");
		pnlFieldsRight.add(lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setEnabled(false);
		txtPrice.setColumns(10);
		pnlFieldsRight.add(txtPrice);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtPrdId.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				txtPrdName.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				txtPrice.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
			}
		});
		scrollPane = new JScrollPane(table);
		pnlMid.add(scrollPane, BorderLayout.CENTER);
		pnlRgstr.add(pnlButton, BorderLayout.SOUTH);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch (state) {
				case 1:
					state = 0;
					txtPrdId.setEnabled(false);
					txtPrdName.setEnabled(false);
					spnrStock.setEnabled(false);
					spnrBrand.setEnabled(false);
					txtPrice.setEnabled(false);
					btnDelete.setEnabled(true);
					btnCancel.setEnabled(false);
					btnInsert.setText("Insert");
					btnUpdate.setEnabled(true);
					break;
				case 2:
					state = 0;
					txtPrdId.setEnabled(false);
					txtPrdName.setEnabled(false);
					spnrStock.setEnabled(false);
					spnrBrand.setEnabled(false);
					txtPrice.setEnabled(false);
					btnDelete.setEnabled(true);
					btnCancel.setEnabled(false);
					btnInsert.setEnabled(true);
					btnUpdate.setText("Update");
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
