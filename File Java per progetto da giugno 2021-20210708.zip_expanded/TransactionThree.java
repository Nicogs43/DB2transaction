import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>
 * Run numThreads transactions, where at most maxConcurrent transactions can run
 * in parallel.
 * 
 * <p>
 * params: numThreads maxConcurrent
 *
 */
class TransactionThree extends Thread {

	// identifier of the transactions
	int id;
	Connection conn;

	TransactionThree(int id, Connection conn) {
		this.id = id;
		this.conn = conn;
	}

	@Override
	public void run() {
		String url = "jdbc:postgresql://127.0.0.1:5432/ProgettoDB2";
		String user = "postgres";
		String pass = "Nicogs43";

		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, user, pass);
			// PreparedStatement st = conn.prepareStatement("set search_path to account");
			// st.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(
				"+++++++++************************************************************************++++++++++++");
		System.out.println("transaction " + id + " started" + " (Transazione tipo 3)");

		// replace this with a transaction
		try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			System.out.println("Transaction " + id + " Isolation Level: " + conn.getTransactionIsolation());
			Statement st1 = conn.createStatement();
			int i = st1.executeUpdate(
					"INSERT INTO \"Vendite\" ( \"Codice_fiscale\" , \"Data\" , \"Quantit�\", \"Tipo\" , \"Marca\") VALUES ('GNZNCL27','2021-07-18', 40 ,'Pasta', 'rummo')");
			if (i == 1) {
				System.out.println("Insert riuscita (transazione tipo 3)");
			}

			Statement st2 = conn.createStatement();
			st2.executeUpdate("UPDATE \"Cliente\" Set \"Sconto\" = 30 Where \"Nome\" Like 'Nic%'");
			System.out.println("Database updated successfully (transazione tipo 3 update numero 1) ");
			
			Thread.sleep(1000);
			
			Statement st3 = conn.createStatement();
			int test = st3.executeUpdate("Delete From \"Vendite\" Where \"Codice_fiscale\" = 'GNZNCL27' ");
			if (test == 1) {
				System.out.println("Delete riuscita (transazione tipo 3 )");
			}

			Statement st4 = conn.createStatement();
			st4.executeUpdate("UPDATE \"Cliente\" Set \"Sconto\" = 10 Where \"Nome\" Like 'Nic%'");
			System.out.println("Database updated successfully (transazione tipo 3 update numero 2) ");

			System.out.println(
					"+++++++++************************************************************************++++++++++++");

			conn.commit();
			conn.close();
		} catch (SQLException | InterruptedException e1) {
			e1.printStackTrace();

			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException e) {
				while (e != null) {
					System.out.println("SQLState: " + e.getSQLState());
					System.out.println("    Code: " + e.getErrorCode());
					System.out.println(" Message: " + e.getMessage());
					e = e.getNextException();
				}
			}
		}
		// end of portion to be replaced
		System.out.println("transaction " + id + " terminated" + " (Transazione tipo 3)");
	}

}