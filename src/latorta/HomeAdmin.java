package latorta;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomeAdmin {
	
	private JFrame frm;

	public HomeAdmin() {
		frm = new JFrame();
		frm.setTitle("Welcome, Admin");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setSize(1280, 720);
		frm.setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		frm.setJMenuBar(menuBar);
		
		JMenu mnTransactions = new JMenu("Manage...");
		menuBar.add(mnTransactions);
		
		JMenuItem mntmBuyItems = new JMenuItem("Members");
		mntmBuyItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MembersManage();
			}
		});
		mnTransactions.add(mntmBuyItems);
		
		JMenuItem mntmViewTransactions = new JMenuItem("Cakes");
		mntmViewTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CakesManage();
			}
		});
		mnTransactions.add(mntmViewTransactions);
		
		JMenuItem mntmTransactions = new JMenuItem("Transactions");
		mntmTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TransactionsManage();
			}
		});
		mnTransactions.add(mntmTransactions);
		
		JMenu mnOthers = new JMenu("Others");
		menuBar.add(mnOthers);
		
		JMenuItem mntmLogout = new JMenuItem("Log Out");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frm.dispose();
				new Main();
			}
		});
		mnOthers.add(mntmLogout);
		frm.setVisible(true);
	}

}
