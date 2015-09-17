package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;

@WebServlet("/editexpense")
public class EditExpenseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		final String id = request.getParameter("id");

		EntityManager dbConnection = DB_CONNECT.connect();

		TypedQuery<Category> findAllCategoriesQuery = dbConnection.createNamedQuery("Category.findAll", Category.class);
		List<Category> categories = findAllCategoriesQuery.getResultList();
		Expense expense = dbConnection.find(Expense.class, Long.parseLong(id));

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		HttpSession session = request.getSession();
		session.setAttribute("expense", expense);
		session.setAttribute("categories", categories);

		response.sendRedirect("editexpense.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		final Double amount = Double.valueOf(request.getParameter("amount").replaceAll(",", "."));
		final String reason = request.getParameter("reason");
		final String day = request.getParameter("day");
		final String month = request.getParameter("month");
		final String year = request.getParameter("year");
		final String categoryId = request.getParameter("category");

		HttpSession session = request.getSession();

		Expense expense = (Expense) session.getAttribute("expense");
		expense.setAmount(amount);
		expense.setReason(reason);
		expense.setReadableDateAsString(day + "." + month + "." + year);

		EntityManager dbConnection = DB_CONNECT.connect();

		if (Long.parseLong(categoryId) != expense.getCategory().getId()) {
			Category category = dbConnection.find(Category.class, Long.parseLong(categoryId));
			expense.setCategory(category);
		}

		dbConnection.merge(expense);

		DB_CONNECT.commit();
		DB_CONNECT.close();

		response.sendRedirect("listexpenses");
	}
}