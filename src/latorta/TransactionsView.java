package latorta;

import java.awt.*;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TransactionsView {
	
	private JFrame frm;
	private JPanel pnlTitle, pnlMid;
	private JLabel lblTitle;
	private JScrollPane scrollPane;
	
	private Connect dbc;
	private ResultSet rs;
	private JTable table;
	
	private void init() {
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
			rs = dbc.doQuery("SELECT cake.CakeName, brand.BrandName, transaction.TransactionDate, "
					+ "`detail transaction`.Quantity, cake.Price"
					+ " FROM transaction"
					+ " INNER JOIN `detail transaction` ON transaction.TransactionId=`detail transaction`.TransactionId"
					+ " INNER JOIN cake ON `detail transaction`.CakeId=cake.CakeId"
					+ " INNER JOIN brand ON cake.BrandId=brand.BrandId;");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Vector<String> resultTitle = new Vector<>();
		Vector<Vector<Object>> resultData = new Vector<>();
		resultTitle.add("Cake Name");
		resultTitle.add("Brand");
		resultTitle.add("Date");
		resultTitle.add("Quantity");
		resultTitle.add("Unit Price");
		resultTitle.add("Total Price");
		
		try {
			while(rs.next()) {
				Vector<Object> row = new Vector<>();
				
				String cakeName = rs.getString(1);
				String brandName = rs.getString(2);
				String transDate = rs.getDate(3).toString();
				int quantity = rs.getInt(4);
				int unitPrice = rs.getInt(5);
				int totalPrice = unitPrice * quantity;
				
				row.add(cakeName);
				row.add(brandName);
				row.add(transDate);
				row.add(quantity);
				row.add(unitPrice);
				row.add(totalPrice);
				
				resultData.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbc.close();
		
		DefaultTableModel dtm = new DefaultTableModel(resultData, resultTitle);
		table.setModel(dtm);
	}

	public TransactionsView() {
		frm = new JFrame();
		frm.setTitle("My Transactions");
		frm.getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnlTitle = new JPanel();
		frm.getContentPane().add(pnlTitle, BorderLayout.NORTH);
		
		lblTitle = new JLabel("My Transactions");
		pnlTitle.add(lblTitle);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 24));
		
		pnlMid = new JPanel();
		frm.getContentPane().add(pnlMid, BorderLayout.CENTER);
		pnlMid.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane((Component) null);
		pnlMid.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		getData();
		init();
	}

}
