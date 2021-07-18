/*
Codice di partenza per transazioni concorrenti —
Adattato da Nikolas Augsten 
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.sql.*;

/** 
 * Dummy transaction that prints a start message, waits for a random time 
 * (up to 100ms) and finally prints a status message at termination.
 */



class TransactionOne extends Thread {

	// identifier of the transaction
	int id;
	Connection conn;
	
	TransactionOne(int id, Connection conn) {
		this.id = id;
		this.conn = conn;
	}
	
	@Override
	public void run() {
		 	String url = "jdbc:postgresql://127.0.0.1:5432/ProgettoDB2"; 
		    String user ="postgres"; 
		    String pass ="Nicogs43"; 
		    
		    try{  
		        Class.forName("org.postgresql.Driver"); 
		        conn = DriverManager.getConnection(url, user, pass);
		        //PreparedStatement st = conn.prepareStatement("set search_path to account");
		       // st.executeUpdate();
		        }catch(SQLException se){  
		      se.printStackTrace();  
		   }catch(Exception e){  
		      e.printStackTrace();  
		   }  
		System.out.println("transaction " + id + " started");

		// replace this with a transaction
        try{  
        	conn.setAutoCommit(false);
            Statement st1 = conn.createStatement();
            ResultSet rs = st1.executeQuery("SELECT \"Codice_fiscale\" , \"Nome\" FROM \"Cliente\" where \"Nome\" like 'Nico%'");
            
            while (rs.next()) {
                String codFisc = rs.getString("Codice_fiscale");
                String Name = rs.getString("Nome");
                System.out.println(codFisc +" "+ Name + "     ");
            }
            rs.close();
            
            Statement st2 = conn.createStatement();
            rs = st2.executeQuery("SELECT \"Codice_fiscale\"  FROM \"Vendite\" Where \"Codice_fiscale\" like 'NI%' ");
           
           while (rs.next()) {
               String Name = rs.getString("Codice_fiscale");
               System.out.println(Name + "     ");
           }
           rs.close();
           
           Statement st3 = conn.createStatement();
           rs = st3.executeQuery("SELECT \"Codice_fiscale\" FROM \"Cliente\"  where \"Nome\" like 'N%'");
           
           while (rs.next()) {
               String Name = rs.getString("Codice_fiscale");
               System.out.println(Name + "     ");
           }
           rs.close();
           
            
            System.out.println("*******************************************************************");
            
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
		// end of portion to be replaced						
		System.out.println("transaction " + id + " terminated");
	}	
	
}

/**
 * <p>
 * Run numThreads transactions, where at most maxConcurrent transactions 
 * can run in parallel.
 * 
 * <p>params: numThreads maxConcurrent
 *
 */
public class ConcurrentTransactions {

	public static void main(String[] args) {

        // CODICE DA MODIFICARE CON VOSTRI DATI PER CONNESSIONE

      
	    Connection conn = null;
		

		// read command line parameters
		if (args.length != 2) {
			System.err.println("params: numThreads maxConcurrent");
			System.exit(-1);
		}	
		int numThreads = Integer.parseInt(args[0]);
		int maxConcurrent = Integer.parseInt(args[1]);

		// create numThreads transactions
		TransactionOne trans1 = new TransactionOne(1 , conn);
		Runnable trans =trans1;
		

		// start all transactions using a connection pool 
		ExecutorService pool = Executors.newFixedThreadPool(maxConcurrent);				
		pool.execute(trans1);
		//pool.execute
		pool.shutdown(); // end program after all transactions are done	

               
                //CHIUSURA CONNESSIONE
		try {
			if (!pool.awaitTermination(10,TimeUnit.SECONDS))
			 {
			    pool.shutdownNow();
                            try{  
				conn.close(); 
			    }catch(SQLException se){  
	                           se.printStackTrace();  
	                           }catch(Exception e){  
	                                  e.printStackTrace();  
	                           } 
			 }
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
                
	}
}

