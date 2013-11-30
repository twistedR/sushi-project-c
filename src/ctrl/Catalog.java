package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;
import model.ItemBean;
import model.CategoryBean;

/**
 * Servlet implementation class Start
 */
@WebServlet({ "/Catalog", "/Search" })
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Catalog() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			ApplicationModel model = (ApplicationModel) this
					.getServletContext().getAttribute("model");
			if (model == null) {
				model = new ApplicationModel();
			}
			this.getServletContext().setAttribute("model", model);

		} catch (ClassNotFoundException e) {
			System.out.println("Class not loaded " + e);
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Setup the application root for static files
		request.setAttribute("app_root", request.getContextPath());

		// Grab the model
		ApplicationModel m = (ApplicationModel) getServletContext()
				.getAttribute("model");

		// We always need the categories
		List<CategoryBean> cat = m.getCategories();
		request.setAttribute("cat_list", cat);

		// We always need sorting
		List<String> sortList = m.getSortList();
		request.setAttribute("sort_list", sortList);

		String location;
		location = "Catalog.jspx";

		
		//Check if we have a search term
		String searchTerm = request.getParameter("search_term");
		request.setAttribute("current_search_term", searchTerm);
		
		
		String cat_id = request.getParameter("cat_id");
		request.setAttribute("current_cat_id", cat_id);

		// Min Price
		String min_price = request.getParameter("min_price");
		request.setAttribute("current_min_price", min_price);

		String max_price = request.getParameter("max_price");
		request.setAttribute("current_max_price", max_price);

		String current_sort = request.getParameter("sort");
		request.setAttribute("current_sort", current_sort);

		String page = request.getParameter("page");
		if (page == null || page.trim().isEmpty())
			page = "1";
		request.setAttribute("curr_page", page);

		List<ItemBean> items = m.getItems(searchTerm, cat_id, page, min_price, max_price,
				current_sort);
		request.setAttribute("items_list", items);

		// Things for pagination
		int itemCount = m.getItemsCount(searchTerm, cat_id, min_price, max_price,
				current_sort);
		request.setAttribute("item_count", itemCount);

		int maxPages = (int) Math.ceil(itemCount / 10.0);
		request.setAttribute("max_pages", maxPages);

		// Done Pagination
		request.getRequestDispatcher(location).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

}
