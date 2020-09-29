package latorta;

import java.awt.*;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TransactionsManage {

	private JFrame frm;
	private JComboBox<String> cbxCakeName, cbxEmail;
	private JLabel lblTitle, lblProdId, lblProdName, lblProdStock, lblBrand, lblPrice;
	private JPanel pnlRgstr, pnlTitle, pnlMid, pnlButton, pnlFields, pnlFieldsLeft, pnlFieldsRight;
	private JTextField txtPrdId, txtPrice;
	private JTable table;
	private JSpinner spnrStock;
	private JButton btnDelete;
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
		dbc = new Connect();
		
		dbc.open();
		
		try {
			rs = dbc.doQuery("SELECT transaction.TransactionId, member.Email, cake.CakeName, "
					+ "`detail transaction`.Quantity, transaction.TransactionDate"
					+ " FROM transaction"
					+ " INNER JOIN member ON transaction.MemberId=member.MemberId"
					+ " INNER JOIN cake ON `detail transaction`.CakeId=cake.CakeId"
					+ " INNER JOIN `detail transaction`"
					+ " ON transaction.TransactionId='detail transaction'.TransactionId;");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Vector<String> resultTitle = new Vector<>();
		Vector<Vector<Object>> resultData = new Vector<>();
		resultTitle.add("Transaction ID");
		resultTitle.add("Email");
		resultTitle.add("Cake Name");
		resultTitle.add("Quantity");
		resultTitle.add("Date");
		
		try {
			while(rs.next()) {
				Vector<Object> row = new Vector<>();
				
				int transactionId = rs.getInt(1);
				String email = rs.getString(2);
				String cakeName = rs.getString(3);
				int quantity = rs.getInt(4);
				String transactionDate = rs.getDate(5).toString();
				
				row.add(transactionId);
				row.add(email);
				row.add(cakeName);
				row.add(quantity);
				row.add(transactionDate);
				
				resultData.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbc.close();
		
		DefaultTableModel dtm = new DefaultTableModel(resultData, resultTitle);
		table.setModel(dtm);
	}
	
	public TransactionsManage() {
		frm = new JFrame("Manage Transactions");
		lblTitle = new JLabel("Manage Transactions");
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 24));
		frm.getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnlRgstr = new JPanel(new BorderLayout());
		pnlTitle = new JPanel();
		pnlMid = new JPanel();
		pnlButton = new JPanel();
		
		pnlTitle.add(lblTitle);
		
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
							
							int id = 0;
							
							dbc.open();
							
							try {
								String query = "DELETE FROM transaction WHERE TransactionId=" + id;
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
		
		lblProdId = new JLabel(" Transaction ID :");
		pnlFieldsLeft.add(lblProdId);
		
		txtPrdId = new JTextField();
		txtPrdId.setEnabled(false);
		pnlFieldsLeft.add(txtPrdId);
		txtPrdId.setColumns(10);
		
		lblProdName = new JLabel(" Cake Name :");
		pnlFieldsLeft.add(lblProdName);
		
		cbxCakeName = new JComboBox<String>();
		cbxCakeName.setEnabled(false);
		pnlFieldsLeft.add(cbxCakeName);
		
		lblProdStock = new JLabel(" Date :");
		pnlFieldsLeft.add(lblProdStock);
		
		spnrStock = new JSpinner();
		spnrStock.setEnabled(false);
		pnlFieldsLeft.add(spnrStock);
		
		pnlFieldsRight = new JPanel();
		pnlFields.add(pnlFieldsRight);
		pnlFieldsRight.setLayout(new GridLayout(3, 2));
		
		lblBrand = new JLabel(" Email :");
		pnlFieldsRight.add(lblBrand);
		
		cbxEmail = new JComboBox<String>();
		cbxEmail.setEnabled(false);
		pnlFieldsRight.add(cbxEmail);
		
		lblPrice = new JLabel(" Quantity :");
		pnlFieldsRight.add(lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setEnabled(false);
		txtPrice.setColumns(10);
		pnlFieldsRight.add(txtPrice);
		
		table = new JTable();
		scrollPane = new JScrollPane(table);
		pnlMid.add(scrollPane, BorderLayout.CENTER);
		pnlRgstr.add(pnlButton, BorderLayout.SOUTH);
		
		frm.getContentPane().add(pnlRgstr);
		
		getData();
		init();
	}

}
