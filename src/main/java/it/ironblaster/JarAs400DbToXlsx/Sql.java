package it.ironblaster.JarAs400DbToXlsx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sql {
	static String USER = null;
	static String PASS = null;
	 //JDBC driver name and database URL
	static String DB_URL = null;
	static String database = null;
	static {

			try {
				Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			
		
	}
	
	public static String Setparameter(String ip,String user,String password) {
		//ip=10.5.26.95   user=SPROJECT   pass=SPROJECT
			DB_URL ="jdbc:as400://"+ip;
	
			USER = user;
	
			PASS = password;
	return null;
	}
	
	
private static Connection conn = null;
	
	private static synchronized Connection getconnection () throws SQLException{
		
		if(conn==null) 
			conn = newconnection();
		

		
		if(!conn.isValid(5)) 
			conn = newconnection();
	
		return conn;
	}
	
	
	
	
	private static Connection newconnection() throws SQLException {
		//VARIABILI MYSQL
		 
		 return DriverManager.getConnection(DB_URL, USER, PASS);	
	}
	
	
	
public static Map <String,String> getStruttura(String fileStruttura) throws SQLException {
		
		Map <String,String> struttura = new HashMap<String, String>();
		

		Statement stmt = getconnection().createStatement();
		ResultSet rs= stmt.executeQuery("SELECT * FROM "+fileStruttura);
    	
    	while (rs.next()) {
    		struttura.put(rs.getString("WHFLDI").trim(), rs.getString("WHFTXT").trim());
			/*System.out.print(rs.getString("WHFLDI"));
			  System.out.print("    ");
    		  System.out.println(rs.getString("WHFTXT"));
    		*/
    		
    		 //list.add(rs.getString("Tables_in_"+database));
    	}
    
    	stmt.close();
		
		return struttura;
	}
	


	public static ResultSet GetValori (String fileDati) throws SQLException {

		
		ResultSet rs = null;

			Statement stmt = getconnection().createStatement();
		    rs = stmt.executeQuery("SELECT * FROM "+fileDati);
			
			
		

		
		return rs;
	}
	
	
	
	public static ArrayList<String> GetNameOriginalColumn (ResultSet rs) throws SQLException {
		ArrayList<String> nomi = new ArrayList<String>();
		
		
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int i = rsmd.getColumnCount();
			

			for (int t=1; t<=i;t++) {
				nomi.add(rsmd.getColumnName(t));
				
				
			}
			
	

		return nomi;
	}
	
	
	
	
	
	
}
