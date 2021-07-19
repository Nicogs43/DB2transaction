
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class TransactionOne extends Thread {

	// identifier of the transactions
	int id;
	Connection conn;

	TransactionOne(int id, Connection conn) {
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
		System.out.println("transaction " + id + " started" + " (Transazione tipo 1) ");
		
		// replace this with a transaction
		try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			System.out.println("Transaction " + id + " Isolation Level: " + conn.getTransactionIsolation());
			Statement st1 = conn.createStatement();
			ResultSet rs = st1
					.executeQuery("SELECT \"Codice_fiscale\" , \"Nome\", \"Sconto\" FROM \"Cliente\" where \"Nome\" like 'Nico%'");

			while (rs.next()) {
				String codFisc = rs.getString("Codice_fiscale");
				String Name = rs.getString("Nome");
				int sconto = rs.getInt("Sconto");
				System.out.println(codFisc + " " + Name + " " + sconto + " -Tipo 1 ");
			}
			rs.close();

			Statement st2 = conn.createStatement();
			rs = st2.executeQuery("SELECT \"Codice_fiscale\"   FROM \"Vendite\" Where \"Codice_fiscale\" like 'NI%' ");

			while (rs.next()) {
				String cod_fisc = rs.getString("Codice_fiscale");
				System.out.println(cod_fisc + " -Tipo 1 ");
			}
			rs.close();

			Statement st3 = conn.createStatement();
			rs = st3.executeQuery("SELECT \"Codice_fiscale\" , \"Sconto\" FROM \"Cliente\"  where \"Nome\" like 'N%'");

			while (rs.next()) {
				String cod_fisc = rs.getString("Codice_fiscale");
				int sconto = rs.getInt("Sconto");
				System.out.println(cod_fisc + " " + sconto + " -Tipo 1 ");
			}
			rs.close();

			System.out.println(
					"+++++++++************************************************************************++++++++++++");

			conn.commit();
			conn.close();
		} catch (SQLException e1) {
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
		System.out.println("transaction " + id + " terminated"+ " (Transazione tipo 1)");
	}

}