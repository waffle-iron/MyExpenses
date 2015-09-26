package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.services.CategoryDAO;
import vu.de.npolke.myexpenses.services.DAOFactory;

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
@WebServlet("/deletecategory")
public class DeleteCategoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		long categoryId = Long.parseLong(request.getParameter("id"));
		String isDeleteConfirmed = request.getParameter("confirmed");

		if ("yes".equalsIgnoreCase(isDeleteConfirmed)) {
			categoryDAO.deleteById(categoryId);
		}

		request.getRequestDispatcher("listcategories").forward(request, response);;
	}
}
