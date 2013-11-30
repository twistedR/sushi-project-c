package ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;
import model.ClientBean;

/**
 * Servlet implementation class Authorization
 */
@WebServlet("/Authorization")
public class Authorization extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authorization() {
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
		//Setup the application root for static files
		request.setAttribute("app_root", request.getContextPath());
				
		if(request.getParameter("action")!=null && request.getParameter("action").equals("logout")){
			request.getSession().setAttribute("shopping_cart", null);
			request.getSession().setAttribute("client", null);
			response.sendRedirect(request.getContextPath());
			return;
		} else {
			request.setAttribute("referer_page", request.getHeader("referer"));
			if(request.getSession().getAttribute("client")!= null){
				response.sendRedirect(request.getContextPath() + "/Catalog");
				return;
			}
			
			request.getRequestDispatcher("Login.jspx").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Setup the application root for static files
		request.setAttribute("app_root", request.getContextPath());
		
		String location = "Login.jspx";
		if(request.getParameter("login")!=null){
			String user_number = request.getParameter("user_number");
			String password = request.getParameter("password");
			
			// Grab the model
			ApplicationModel m = (ApplicationModel) getServletContext().getAttribute("model");
			request.setAttribute("referer_page", request.getParameter("referer_page"));
			try {
				ClientBean client = m.authorize(user_number, password);
				request.getSession().setAttribute("client", client);
				
				response.sendRedirect(request.getParameter("referer_page"));
				return;
			} catch (Exception e) {
				request.setAttribute("error", "Login Failed: Invalid Credentials.");
			}
		} 
		
		request.getRequestDispatcher(location).forward(request, response);;
	} 

}
