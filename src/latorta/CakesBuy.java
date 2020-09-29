package latorta;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CakesBuy {

	private JFrame frm;
	private JLabel lblTitle, lblProdId, lblProdName, lblProdStock, lblBrand, lblPrice, lblQuantity;
	private JButton btnBuy, btnCancel;
	private JPanel pnlRgstr, pnlTitle, pnlMid, pnlButton, pnlFields, pnlFieldsLeft, pnlFieldsRight;
	private JTextField txtPrdId, txtPrdName, txtPrdStock, txtBrand, txtPrice;
	private JTable table;
	private JSpinner spnrQty;
	private JScrollPane scrollPane;
	
	private Connect dbc;
	private ResultSet rs;
	
	public void init() {
		frm.setSize(800, 480);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.setResizable(false);
		frm.setVisible(true);
	}
	
	void getData() {
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
	
	public CakesBuy(int memberId) {
		frm = new JFrame("Buy Items");
		dbc = new Connect();
		lblTitle = new JLabel("Buy Cakes");
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 24));
		btnBuy = new JButton("Buy");
		btnBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Select a cake first from the table", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
				
					int quantity = (int) spnrQty.getValue();
					int stock = (int) (table.getValueAt(table.getSelectedRow(), 4));
					if (quantity > stock || quantity == 0) {
						JOptionPane.showMessageDialog(null, "Quantity must be numeric, more than zero, and less than stock", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						dbc.open();
						java.util.Date utilDate = new java.util.Date();
						java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
						int cakeId = (int) table.getValueAt(table.getSelectedRow(), 0);
						String query;
						
						try {
							query = "UPDATE cake"
									+ " SET Stock=" + (stock - quantity) + ""
									+ " WHERE CakeId=" + cakeId;
							dbc.doUpdate(query);
							System.out.println(query);
							getData();
							System.out.println("Succesfully updated stock");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						dbc.close();
						
						dbc.open();
						
						try {
							query = "INSERT INTO transaction"
									+ " VALUES (null, '" + memberId + "', '" + sqlDate + "')";
							dbc.doUpdate(query);
							System.out.println(query);
							System.out.println("Succesfully added into transaction database");
							spnrQty.setValue(0);
						} catch (HeadlessException e1) {
							e1.printStackTrace();
						}
						
						dbc.close();
						
						dbc.open();
						
						try {
							query = "INSERT INTO `detail transaction`"
									+ " VALUES (transactionId, '" + cakeId + "', '" + quantity + "')";
							dbc.doUpdate(query);
							System.out.println(query);
							System.out.println("Succesfully added into detail transaction database");
							spnrQty.setValue(0);
						} catch (HeadlessException e1) {
							e1.printStackTrace();
						}
						
						dbc.close();
						
						JOptionPane.showMessageDialog(null, "Succesfully added transaction and updated stock", "Success", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		frm.getContentPane().setLayout(new BorderLayout(0, 0));
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frm.dispose();
			}
		});
		
		pnlRgstr = new JPanel(new BorderLayout());
		pnlTitle = new JPanel();
		pnlMid = new JPanel();
		pnlButton = new JPanel();
		
		pnlTitle.add(lblTitle);
		pnlButton.add(btnBuy);
		pnlButton.add(btnCancel);
		
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
		txtPrdId.setEditable(false);
		txtPrdId.setEnabled(false);
		pnlFieldsLeft.add(txtPrdId);
		txtPrdId.setColumns(10);
		
		lblProdName = new JLabel(" Product Name :");
		pnlFieldsLeft.add(lblProdName);
		
		txtPrdName = new JTextField();
		txtPrdName.setEditable(false);
		txtPrdName.setEnabled(false);
		pnlFieldsLeft.add(txtPrdName);
		txtPrdName.setColumns(10);
		
		lblProdStock = new JLabel(" Stock :");
		pnlFieldsLeft.add(lblProdStock);
		
		txtPrdStock = new JTextField();
		txtPrdStock.setEditable(false);
		txtPrdStock.setEnabled(false);
		pnlFieldsLeft.add(txtPrdStock);
		txtPrdStock.setColumns(10);
		
		pnlFieldsRight = new JPanel();
		pnlFields.add(pnlFieldsRight);
		pnlFieldsRight.setLayout(new GridLayout(3, 2));
		
		lblBrand = new JLabel(" Brand :");
		pnlFieldsRight.add(lblBrand);
		
		txtBrand = new JTextField();
		txtBrand.setEditable(false);
		txtBrand.setEnabled(false);
		txtBrand.setColumns(10);
		pnlFieldsRight.add(txtBrand);
		
		lblPrice = new JLabel(" Price :");
		pnlFieldsRight.add(lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setEditable(false);
		txtPrice.setEnabled(false);
		txtPrice.setColumns(10);
		pnlFieldsRight.add(txtPrice);
		
		lblQuantity = new JLabel(" Quantity :");
		pnlFieldsRight.add(lblQuantity);
		
		spnrQty = new JSpinner();
		pnlFieldsRight.add(spnrQty);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtPrdId.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				txtBrand.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
				txtPrdName.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				txtPrice.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
				txtPrdStock.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
			}
		});
		scrollPane = new JScrollPane(table);
		pnlMid.add(scrollPane, BorderLayout.CENTER);
		pnlRgstr.add(pnlButton, BorderLayout.SOUTH);
		
		frm.getContentPane().add(pnlRgstr);
		getData();
		init();
	}

}
