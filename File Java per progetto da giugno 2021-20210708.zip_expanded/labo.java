/* Esempio di programma che si connette a DB attraverso JDBC e esegue una insert di conto  */


import java.sql.*;
import java.io.*;
import java.util.*;

class labo
{

public static Connection connect()throws SQLException {
	String url = "jdbc:postgresql://127.0.0.1:5432/ProgettoDB2"; 
	String user ="postgres";
	String pass ="Nicogs43"; 
	try{
		Class.forName ("org.postgresql.Driver");
	}catch(java.lang.ClassNotFoundException e) {
		System.err.print("ClassNotFoundException: "); 
		System.err.println(e.getMessage());
	}
	// CONNESSIONE
	return  DriverManager.getConnection(url, user, pass);
	
}

public static void transactionOne() {
	Connection conn=null;
	try {
		conn = connect();
	} catch (SQLException e2) {
		e2.printStackTrace();
	}
    // INIZIALIZZAZIONE AUTOCOMMIT A FALSE PER IMPOSTARE COMPORTAMENTO TRANSAZIONALE
	 try {
	conn.setAutoCommit(false);  
    // ESECUZIONE COMANDO
	
    Statement st1 = conn.createStatement();
    ResultSet rs = st1.executeQuery("SELECT \"Codice_fiscale\" , \"Nome\" FROM \"Cliente\" where \"Nome\" like 'Nico%'");
    
    while (rs.next()) {
        String codFisc = rs.getString("Codice_fiscale");
        String Name = rs.getString("Nome");
        System.out.println(codFisc + Name + "     ");
    }
    rs.close();
    
    System.out.println("*******************************************************************");
    
    Statement st2 = conn.createStatement();
     rs = st2.executeQuery("SELECT \"Codice_fiscale\"  FROM \"Vendite\" Where \"Codice_fiscale\" like 'NI%' ");
    
    while (rs.next()) {
        String Name = rs.getString("Codice_fiscale");
        System.out.println(Name + "     ");
    }
    rs.close();
    
    System.out.println("*******************************************************************");
    
    Statement st3 = conn.createStatement();
    rs = st3.executeQuery("SELECT \"Codice_fiscale\" FROM \"Cliente\"  where \"Nome\" like 'N%'");
    
    while (rs.next()) {
        String Name = rs.getString("Codice_fiscale");
        System.out.println(Name + "     ");
    }
    rs.close();
    
	// chiusura connessione
		conn.commit();
		conn.close();
	 }catch (SQLException e1) {
		 e1.printStackTrace();
	 
		try{
			if (conn != null) 
				conn.rollback();
			} catch (SQLException e) {
			 		while( e!=null){ 
						System.out.println("SQLState: " + e.getSQLState());
						System.out.println("    Code: " + e.getErrorCode());
						System.out.println(" Message: " + e.getMessage());
						e = e.getNextException();
						}	}
	 }
				}


public static void transactionTwo() {
	Connection conn=null;
	try {
		conn = connect();
	} catch (SQLException e2) {
		e2.printStackTrace();
	}
	 // INIZIALIZZAZIONE AUTOCOMMIT A FALSE PER IMPOSTARE COMPORTAMENTO TRANSAZIONALE
	 try {
	conn.setAutoCommit(false);
	
	
	    Statement st1 = conn.createStatement();
	    int i = st1.executeUpdate("INSERT INTO \"Cliente\"(\"Nome\" , \"Cognome\" , \"Codice_fiscale\" , \"Sconto\" , \"Note\") VALUES ('Nicolo','Guai','GNZNCL27','30','Ok')");
	    if(i==1) {
	    	 System.out.println("Insert riuscita");
	    }

	
	    Statement st2 = conn.createStatement();
	    ResultSet rs = st2.executeQuery("Select \"Nome\",\"Codice_fiscale\" From \"Cliente\" where  \"Nome\" like 'N%' ");
	    
	    while (rs.next()) {
	        String Name = rs.getString("Nome");
	        String CodFisc = rs.getString("Codice_fiscale");

	        System.out.println(Name + " " + CodFisc+ "     ");
	    }
	    rs.close();
	    
	    Statement st3 = conn.createStatement();
	    int test = st3.executeUpdate("Delete From \"Cliente\" Where \"Codice_fiscale\" = 'GNZNCL27' ");
	    if(test==1) {
	    	 System.out.println("Delete riuscita");
	    }
	    

	conn.commit();
	conn.close();
 }catch (SQLException e1) {
	 e1.printStackTrace();
	try{
		if (conn != null) conn.rollback();
		 } catch (SQLException e) {
		 		while( e!=null){ 
					System.out.println("SQLState: " + e.getSQLState());
					System.out.println("    Code: " + e.getErrorCode());
					System.out.println(" Message: " + e.getMessage());
					e = e.getNextException();
					}	}
    
			}
}

public static void transactionThree() {
	Connection conn=null;
	try {
		conn = connect();
	} catch (SQLException e2) {
		e2.printStackTrace();
	}
	 // INIZIALIZZAZIONE AUTOCOMMIT A FALSE PER IMPOSTARE COMPORTAMENTO TRANSAZIONALE
	 try {
	conn.setAutoCommit(false);
	
	  Statement st1 = conn.createStatement();
	    int i = st1.executeUpdate("INSERT INTO \"Vendite\" ( \"Codice_fiscale\" , \"Data\" , \"Quantità\", \"Tipo\" , \"Marca\") VALUES ('GNZNCL27','2021-07-18', 40 ,'Pasta', 'rummo')");
	    if(i==1) {
	    	 System.out.println("Insert riuscita");
	    }
	    
	    Statement st2 = conn.createStatement();
	    st2.executeUpdate("UPDATE \"Cliente\" Set \"Sconto\" = 30 Where \"Nome\" Like 'Nic%'");

	    
	    Statement st3 = conn.createStatement();
	    int test = st3.executeUpdate("Delete From \"Vendite\" Where \"Codice_fiscale\" = 'GNZNCL27' ");
	    if(test==1) {
	    	 System.out.println("Delete riuscita");
	    }

	    Statement st4 = conn.createStatement();
	    st4.executeUpdate("UPDATE \"Cliente\" Set \"Sconto\" = 10 Where \"Nome\" Like 'Nic%'");

	conn.commit();
	conn.close();
 }catch (SQLException e1) {
	 e1.printStackTrace();
	try{
		if (conn != null) conn.rollback();
		 } catch (SQLException e) {
		 		while( e!=null){ 
					System.out.println("SQLState: " + e.getSQLState());
					System.out.println("    Code: " + e.getErrorCode());
					System.out.println(" Message: " + e.getMessage());
					e = e.getNextException();
					}	}
    
			}
}

	
	

 public static void main (String args [])
 {
     
    // APERTURA CONNESSIONE -- CODICE DA MODIFICARE CON VOSTRI DATI 
	 transactionOne();
	 transactionTwo();
	 transactionThree();
   }
}

