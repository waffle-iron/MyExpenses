package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.servlets.util.HashUtil;

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
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		final String login = request.getParameter("login");
		final String password = request.getParameter("password");
		final String passwordHash = HashUtil.toMD5(password);

		EntityManager dbConnection = DB_CONNECT.connect();

		TypedQuery<Account> checkLoginQuery = dbConnection.createNamedQuery("Account.checkLogin", Account.class);
		checkLoginQuery.setParameter("login", login);
		checkLoginQuery.setParameter("password", passwordHash);

		Account account;
		try  {
			account = checkLoginQuery.getSingleResult();
		} catch (NoResultException nre) {
			account = null;
		}

		DB_CONNECT.commit();
		DB_CONNECT.close();

		if (account != null) {
			HttpSession session = request.getSession();
			session.setAttribute("account", account);
			response.sendRedirect("listexpenses");
		} else {
			response.sendRedirect("index.jsp");
		}
	}
}