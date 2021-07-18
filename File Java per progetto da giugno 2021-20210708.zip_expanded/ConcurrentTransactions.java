/*
Codice di partenza per transazioni concorrenti —
Adattato da Nikolas Augsten 
 */

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.sql.*;

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

		// create numThreads transaction
		TransactionOne trans1 = new TransactionOne(1 , conn);
		TransactionTwo trans2 = new TransactionTwo(2 , conn);
		
		ArrayList<Runnable> trans = new ArrayList<Runnable>();
		boolean a,b=true;
		for (int i=0; i<numThreads; i++) {
			if(a) {
				trans.add(new TransactionOne(1 , conn));
				a=false;
			}
			else {
				if (b) {
					b=false;
					trans.add(new TransactionTwo(1 , conn));
				}
				else {
					trans.add(new TransactionThree(1 , conn));
					a=true;
					b=true;
				}
			}
			
		}
		

		// start all transactions using a connection pool 
		ExecutorService pool = Executors.newFixedThreadPool(maxConcurrent);				
		pool.execute(trans1);
		pool.execute(trans2);
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

