package ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;

/**
 * Servlet implementation class Welcome
 */
@WebServlet("/Welcome")
public class Welcome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Welcome() {
        super();
    }
    @Override
   	public void init() throws ServletException {
   		super.init();
   		try {
   			ApplicationModel model =(ApplicationModel) this.getServletContext().getAttribute("model");
   			if(model==null){
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
		//Setup the application root for static files
		request.setAttribute("app_root", request.getContextPath());
		request.getRequestDispatcher("Welcome.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
