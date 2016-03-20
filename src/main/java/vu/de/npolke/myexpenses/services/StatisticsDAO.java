package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;

/**
 * Copyright 2015 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Niklas Polke
 */
public class StatisticsDAO extends AbstractConnectionDAO {

	//@formatter:off
	private static final String SQL_SELECT_DISTINCT_MONTHS =
			"SELECT DISTINCT year(e.day)+'.'+lpad(month(e.day),2,'0') AS month " +
					"FROM expense e " +
					"WHERE e.account_id = ? " +
					"ORDER BY month DESC";

	private static final String SQL_SELECT_STATISTICS_FOR_MONTH =
			"SELECT c.name as category, sum(e.amount) as sumofamount " +
			"FROM category c " +
			"LEFT OUTER JOIN ( " +
					"SELECT category_id, amount " +
					"FROM expense " +
					"WHERE year(day)+'.'+lpad(month(day),2,'0') = ? AND account_id = ? ) e " +
			"ON e.category_id = c.id " +
			"WHERE c.account_id = ? " +
			"GROUP BY c.name " +
			"ORDER BY c.name ASC";
	//@formatter:on

	public List<String> readDistinctMonthsByAccountId(final long accountId) {
		List<String> months = new ArrayList<String>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_SELECT_DISTINCT_MONTHS);
			readStatement.setLong(1, accountId);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				months.add(result.getString("month"));
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return months;
	}

	public List<StatisticsPair> readStatisticsByMonthsAndAccountId(final String month, final long accountId) {
		List<StatisticsPair> statisticsPairs = new ArrayList<StatisticsPair>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_SELECT_STATISTICS_FOR_MONTH);
			readStatement.setString(1, month);
			readStatement.setLong(2, accountId);
			readStatement.setLong(3, accountId);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				String category = result.getString("category");
				Double value = result.getDouble("sumofamount");
				statisticsPairs.add(new StatisticsPair(category, value));
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return statisticsPairs;
	}
}