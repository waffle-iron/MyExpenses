package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.CategoryDAO;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.ExpenseDAO;

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
@WebServlet("/addexpense")
public class AddExpenseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);
	private CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();

		Account account = (Account) session.getAttribute("account");

		List<Category> categories = categoryDAO.readByAccountId(account.getId());
		Calendar now = Calendar.getInstance(Locale.GERMANY);
		now.setTimeInMillis(System.currentTimeMillis());

		session.setAttribute("categories", categories);
		session.setAttribute("defaultDate", now);
		response.sendRedirect("addexpense.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();

		double amount = Double.parseDouble(request.getParameter("amount").replaceAll(",", "."));
		String reason = request.getParameter("reason");
		String day = request.getParameter("day");
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		long categoryId = Long.valueOf(request.getParameter("category"));
		Account account = (Account) session.getAttribute("account");

		String readableDate = day + "." + month + "." + year;
		expenseDAO.create(readableDate, amount, reason, categoryId, account.getId());

		response.sendRedirect("listexpenses");
	}
}
