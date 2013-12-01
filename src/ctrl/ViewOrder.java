package ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;

/**
 * Servlet implementation class ViewOrder
 */
@WebServlet("/ViewOrder")
public class ViewOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewOrder() {
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
		String fileName = request.getParameter("fn");
		if(fileName==null){
			response.sendRedirect(request.getContextPath()+"/NotFound");
		}
		//request.getRequestDispatcher("purchaseOrders/"+fileName).forward(request, response);
		response.sendRedirect("purchaseOrders/"+fileName);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
