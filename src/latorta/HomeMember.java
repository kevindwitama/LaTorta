package latorta;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomeMember {
	
	private JFrame frm;
	
	public void init() {
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.setSize(1280, 720);
		frm.setLocationRelativeTo(null);
		frm.setVisible(true);
	}

	public HomeMember(String email, int id) {
		frm = new JFrame();
		
		JMenuBar menuBar = new JMenuBar();
		frm.setJMenuBar(menuBar);
		
		JMenu mnTransactions = new JMenu("Transactions");
		menuBar.add(mnTransactions);
		
		JMenuItem mntmBuyItems = new JMenuItem("Buy Items");
		mntmBuyItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Buy Items");
				new CakesBuy(id);
			}
		});
		mnTransactions.add(mntmBuyItems);
		
		JMenuItem mntmViewTransactions = new JMenuItem("View Transactions");
		mntmViewTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("View Transactions");
				new TransactionsView();
			}
		});
		mnTransactions.add(mntmViewTransactions);
		
		JMenu mnOthers = new JMenu("Others");
		menuBar.add(mnOthers);
		
		JMenuItem mntmLogout = new JMenuItem("Log Out");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Logged out");
				frm.dispose();
				new Main();
			}
		});
		mnOthers.add(mntmLogout);
		
		frm.setTitle("Welcome, "+ email.substring(0, email.indexOf('@')));
		init();
	}

}
