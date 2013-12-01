package ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ApplicationModel;
import model.ClientBean;
import model.ShoppingCartBean;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Setup the application root for static files
		request.setAttribute("app_root", request.getContextPath());
		
		//Only allow checkout if user is signed in
		ClientBean client = (ClientBean) request.getSession().getAttribute("client");
		if(client==null){
			response.sendRedirect(request.getContextPath()+"/Authorization?action=login");
			return;
		} else {
			if(request.getParameter("action") != null && request.getParameter("action").equals("finalize")){
				//Get the shopping cart
				ShoppingCartBean sc  = (ShoppingCartBean) (request.getSession().getAttribute("shopping_cart"));
				
				if(sc==null || sc.getItems().size()<1){
					response.sendRedirect(request.getContextPath() + "/Catalog");
					return;
				} else {
					//Let's get the model
					ApplicationModel m = (ApplicationModel) getServletContext().getAttribute("model");
					try {
						String filename = m.placeOrder(sc, client, this.getServletContext().getRealPath("purchaseOrders"));
						request.setAttribute("po_file", filename);
						request.getRequestDispatcher("OrderConfirmed.jspx").forward(request, response);
						request.getSession().setAttribute("shopping_cart", null);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			} else {
				//Otherwise just show the checkout page
				request.getRequestDispatcher("Checkout.jspx").forward(request, response);
			}
			
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
