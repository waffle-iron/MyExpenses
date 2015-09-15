package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;

public class JdbcSqlScriptExecutorTest {

	private static final String FILE = "target/test-classes/myexpenses_sqlite_initialised.db";

	private JdbcSqlScriptExecutor executor;

	@Before
	public void setup() {
		executor = new JdbcSqlScriptExecutor(FILE);
	}

	@Test
	public void dbFileCreation() {
		executor.executeSqlScript(JdbcSqlScriptExecutor.INITIALISE_DB_SCRIPT);
		File result = new File(FILE);
		assertTrue(result.isFile());
	}

	@Test
	public void initialisedDb() throws SQLException {
		executor.executeSqlScript(SqlScriptExecutor.INITIALISE_DB_SCRIPT);

		Connection connection = DriverManager.getConnection(JdbcSqlScriptExecutor.URL_PREFIX + FILE);
		Statement allExpensesQuery = connection.createStatement();
		ResultSet result = allExpensesQuery.executeQuery("select * from expense");
		int countResults = 0;
		while (result.next()) {
			countResults++;
			Expense expense = new Expense();
			expense.setAmount(result.getDouble("amount"));
			expense.setReason(result.getString("reason"));
			expense.setId(result.getLong("id"));
			System.out.println(expense);
		}
		assertEquals(3, countResults);
	}
}