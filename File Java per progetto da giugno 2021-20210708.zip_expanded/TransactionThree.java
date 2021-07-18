import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>
 * Run numThreads transactions, where at most maxConcurrent transactions 
 * can run in parallel.
 * 
 * <p>params: numThreads maxConcurrent
 *
 */
class TransactionThree extends Thread {

	// identifier of the transaction
	int id;
	Connection conn;
	
	TransactionThree(int id, Connection conn) {
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