import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class TransactionTwo extends Thread {

	// identifier of the transactions
	int id;
	Connection conn;

	TransactionTwo(int id, Connection conn) {
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
		System.out.println("transaction " + id + " started" + " (Transazione tipo 2)");
	
		// replace this with a transaction
		try {
			Thread.sleep(1000);
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			System.out.println("Transaction " + id + " Isolation Level: " + conn.getTransactionIsolation());
			Statement st1 = conn.createStatement();
			int i = st1.executeUpdate(
					"INSERT INTO \"Cliente\"(\"Nome\" , \"Cognome\" , \"Codice_fiscale\" , \"Sconto\" , \"Note\") VALUES ('Nicolo','Guai','GNZNCL27','30','Ok')");
			if (i == 1) {
				System.out.println("Insert riuscita (transazione tipo 2) ");
			}

			Statement st2 = conn.createStatement();
			ResultSet rs = st2
					.executeQuery("Select \"Nome\",\"Codice_fiscale\" From \"Cliente\" where  \"Nome\" like 'N%' ");

			while (rs.next()) {
				String CodFisc = rs.getString("Codice_fiscale");
				String Name = rs.getString("Nome");
				System.out.println(Name + " " + CodFisc + " -Tipo 2 ");
			}
			rs.close();

			Statement st3 = conn.createStatement();
			int test = st3.executeUpdate("Delete From \"Cliente\" Where \"Codice_fiscale\" = 'GNZNCL27' ");
			if (test == 1) {
				System.out.println("Delete riuscita (Transazione tipo 2)");
			}

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
		System.out.println("transaction " + id + " terminated" + " (Transazione tipo 2) ");
	}

}