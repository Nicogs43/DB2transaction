/*
Codice di partenza per transazioni concorrenti —
Adattato da Nikolas Augsten 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.sql.*;

public class ConcurrentTransactions {

	public static void main(String[] args) {
      
	    Connection conn = null;
		
		// read command line parameters
		if (args.length != 2) {
			System.err.println("params: numThreads maxConcurrent");
			System.exit(-1);
		}	
		int numThreads = Integer.parseInt(args[0]);
		int maxConcurrent = Integer.parseInt(args[1]);

		// create numThreads transaction

		ArrayList<Runnable> trans = getClientList(conn, numThreads);
		
		// start all transactions using a connection pool 
		ExecutorService pool = Executors.newFixedThreadPool(maxConcurrent);				
		for (Iterator<Runnable> iterator = trans.iterator(); iterator.hasNext();) {
			Runnable i = iterator.next();
			pool.execute(i);
		}
		pool.shutdown();
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

	public static ArrayList<Runnable> getClientList(Connection conn, int numThreads) {
		ArrayList<Runnable> trans = new ArrayList<Runnable>();
		boolean a=true ;
		boolean	b=true;
		for (int i=0; i<numThreads; i++) {
			if(a) {
				trans.add(new TransactionOne(i , conn));
				a=false;
			}
			else {
				if (b) {
					b=false;
					trans.add(new TransactionTwo(i, conn));
				}
				else {
					trans.add(new TransactionThree(i, conn));
					a=true;
					b=true;
				}
			}
			
		}
		return trans;
	} 
                
	}


