package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin/getPOList")
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
			} catch (Exception e) {
				e.printStackTrace();
				return;
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
