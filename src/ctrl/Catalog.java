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
@WebServlet("/Catalog")
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

		String location;
		location = "Catalog.jspx";

		String cat_id = request.getParameter("cat_id");
		request.setAttribute("current_cat", cat_id);
		
		String page = request.getParameter("page");
		if(page == null || page.trim().isEmpty()) page ="1";
		request.setAttribute("curr_page", page);
		
		List<ItemBean> items = m.getItems(cat_id, page);
		request.setAttribute("items_list", items);
		
		int itemCount = m.getItemsCount(cat_id);
		request.setAttribute("item_count", itemCount);
		
		int maxPages = (int) Math.ceil(itemCount / 10.0);
		request.setAttribute("max_pages", maxPages);

		request.getRequestDispatcher(location).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
