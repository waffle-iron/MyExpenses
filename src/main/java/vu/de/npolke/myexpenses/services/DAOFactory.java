package vu.de.npolke.myexpenses.services;

import java.util.HashMap;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.connections.ConnectionStrategy;
import vu.de.npolke.myexpenses.services.connections.JdbcConnectionStrategy;

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
public class DAOFactory {

	private static HashMap<Class<?>, AbstractConnectionDAO> daoRegistry = new HashMap<Class<?>, AbstractConnectionDAO>();

	static {
		ConnectionStrategy connectionStrategy = new JdbcConnectionStrategy();

		AccountDAO accountDAO = new AccountDAO();
		accountDAO.setConnectionStrategy(connectionStrategy);
		daoRegistry.put(Account.class, accountDAO);
	}

	public static AbstractConnectionDAO getDAO(final Class<?> entity) {
		return daoRegistry.get(entity);
	}
}
