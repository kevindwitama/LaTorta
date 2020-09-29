package latorta;

import java.sql.*;

public final class Connect {
	private final String USER = "root";
	private final String PASS = "";
	private final String DATABASE = "la_torta";
	private final String HOST = "localhost:3306";
	private final String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection con;
	private Statement st;
	
	public void open() {
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);  
            st = con.createStatement(); 
        } catch(Exception e) {
        	System.out.println("Failed to connect to the database");
        	e.printStackTrace();
        } 
    	System.out.println("Connected to database");
	}
	
	public ResultSet doQuery(String query) throws Exception {
		return st.executeQuery(query);
	}
	
    public void doUpdate(String query) {
		try {
			st.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void close() {
		try {
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
