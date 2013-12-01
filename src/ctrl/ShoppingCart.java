package ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;
import model.ItemBean;
import model.ShoppingCartBean;

/**
 * Servlet implementation class ShoppingCart
 */
@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCart() {
        super();
        // TODO Auto-generated constructor stub
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Setup the application root for static files
		request.setAttribute("app_root", request.getContextPath());
		
		if(request.getParameter("action")!=null && request.getParameter("action").equals("remove")){
			String item_number = request.getParameter("in");
			
			//Get the shopping cart
			ShoppingCartBean sc = (ShoppingCartBean) request.getSession().getAttribute("shopping_cart");
			if(item_number==null || item_number.trim().isEmpty() || sc == null){
				request.setAttribute("error", "Item doesn't exist in your shopping cart");
			} else {
				sc.removeItem(item_number);
				response.sendRedirect(request.getContextPath() + "/ShoppingCart");
				return;
			}
 		}
		
		
		request.getRequestDispatcher("ShoppingCart.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("add_to_cart")!=null){
			ShoppingCartBean shoppingCart = (ShoppingCartBean) request.getSession().getAttribute("shopping_cart");
			if(shoppingCart==null){
				shoppingCart = new ShoppingCartBean();
				shoppingCart.setHstValue(Double.parseDouble(getServletContext().getInitParameter("HST")));
				shoppingCart.setShipping(Double.parseDouble(getServletContext().getInitParameter("base-shipping")));
			}
			// Grab the model
			ApplicationModel m = (ApplicationModel) getServletContext()
					.getAttribute("model");
			String itemNumber = request.getParameter("item_number");
			try{
				
				ItemBean i = m.getItem(itemNumber);
				int q = Integer.parseInt(request.getParameter("item_quantity"));
				i.setQuantity(q);
				shoppingCart.addItem(i, q);
			}catch (Exception e){
				//Do something
				System.out.println("exceptopn:" + e);
			}

			request.getSession().setAttribute("shopping_cart", shoppingCart);
		}
		response.sendRedirect(request.getHeader("referer"));
	}

}
