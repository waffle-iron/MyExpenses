package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Category;

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
@WebServlet("/editcategory")
public class EditCategoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		final String id = request.getParameter("id");

		EntityManager dbConnection = DB_CONNECT.connect();

		Category category = dbConnection.find(Category.class, Long.parseLong(id));

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		HttpSession session = request.getSession();
		session.setAttribute("category", category);

		response.sendRedirect("editcategory.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		final String name = request.getParameter("name");

		HttpSession session = request.getSession();

		Category category = (Category) session.getAttribute("category");
		category.setName(name);

		EntityManager dbConnection = DB_CONNECT.connect();

		dbConnection.merge(category);

		DB_CONNECT.commit();
		DB_CONNECT.close();

		response.sendRedirect("listcategories");
	}
}
