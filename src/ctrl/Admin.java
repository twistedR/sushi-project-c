package ctrl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;

/**
 * Servlet implementation class Admin
 */
@WebServlet({"/Admin/getPOList", "/Admin/Analytics"})
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Admin() {
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
		String ru = request.getRequestURI();
		request.setAttribute("app_root", request.getContextPath());
		if(ru.endsWith("/Admin/getPOList")){
			ApplicationModel  m = (ApplicationModel) getServletContext().getAttribute("model");
			String startDate = request.getParameter("start_date");
			String endDate = request.getParameter("end_date");
			
			try {
				List<String> fileList = m.getPurchaseOrderList(startDate, endDate, this.getServletContext().getRealPath("purchaseOrders"));
				
				StringBuilder builder = new StringBuilder();
		    if(fileList.size()>0){
		    	builder.append(fileList.remove(0));

			    for( String s : fileList) {
			        builder.append( "\n");
			        builder.append( s);
			    }
		    }
				response.getWriter().println(builder.toString());
				return;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} else if(ru.endsWith("/Admin/Analytics")){
			//We need the data
			Map<String, Long> shopping_times = (Map<String, Long>) getServletContext().getAttribute("shopping_times");
			if(shopping_times!=null){
				Long time = shopping_times.get("time");
				Long items = shopping_times.get("items");
				Double avg_time = (double) time/((double) items);
				avg_time = avg_time/1000.0;
				avg_time = avg_time/60.0;
				request.setAttribute("total_shopping_time", time);
				request.setAttribute("total_shopping_items", items);
				request.setAttribute("avg_shopping_time", avg_time);
			} 
			Map<String, Long> checkout_times = (Map<String, Long>) getServletContext().getAttribute("checkout_times");
			if(checkout_times!=null){
				Long time = checkout_times.get("time");
				Long checkouts = checkout_times.get("items");
				Double avg_time = (double) time/((double) checkouts);
				avg_time = avg_time/1000.0;
				avg_time = avg_time/60.0;
				request.setAttribute("total_checkout_time", time);
				request.setAttribute("total_checkouts", checkouts);
				request.setAttribute("avg_checkout_time", avg_time);
			} 
			request.getRequestDispatcher("../AdminDashboard.jspx").forward(request, response);
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
