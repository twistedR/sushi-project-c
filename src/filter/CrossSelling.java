package filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;
import model.ItemBean;

/**
 * Servlet Filter implementation class CrossSelling
 */
@WebFilter(dispatcherTypes = {
				DispatcherType.REQUEST, 
				DispatcherType.FORWARD
		}
					, urlPatterns = { "/CrossSelling" }, servletNames = { "ctrl.ShoppingCart" })
public class CrossSelling implements Filter {

    /**
     * Default constructor. 
     */
    public CrossSelling() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		ModifiedResponse wrapper = new ModifiedResponse((HttpServletResponse)response);
		chain.doFilter(request, wrapper);
		String servletResponse = new String(wrapper.toString());
		HttpServletRequest req = (HttpServletRequest) request;
		ApplicationModel m = (ApplicationModel) req.getServletContext().getAttribute("model");
		//Check if we have the item in the cart
		if(servletResponse.contains("1409S413") && !servletResponse.contains("2002H712")){
			
			ItemBean i;
			String ad = "";
			try {
				i = m.getItem("2002H712");
				//Let's create a div for advertisement!
				ad ="<h4>You might also like:</h4>";//Add!
				ad += "<div class=\"media-body\"><h4 class=\"media-heading\">";
				ad += i.getName() + "</h4>";
				DecimalFormat df = new DecimalFormat("#.00"); 
				ad += "<p>$" + df.format(i.getPrice()) + "</p></div>";
				ad += "<form method=\"post\" action=\"/ProjectC/ShoppingCart\">";
				ad += "<input value=\""+i.getNumber()+"\" name=\"item_number\" type=\"hidden\">";
				ad += "<input class=\"form-control input-sm price-input-small\" value=\"1\" name=\"item_quantity\" type=\"text\">";
				ad += "<button class=\"btn btn-sm btn-success\" name=\"add_to_cart\" type=\"submit\">Add";
				ad += "</button></form>";
 			} catch (Exception e) {
				e.printStackTrace();
			}

			int si = servletResponse.indexOf("suggested_items");
			int fi = servletResponse.indexOf(">", si)+1;
			String f = servletResponse.substring(0, fi) + ad +servletResponse.substring(fi);
			servletResponse = f;
		}
		
    out.write(servletResponse); // Here you can change the response
//		
//		String servletResponse = new String(responseWrapper.toString());
//		response.getWriter().print("this is a test");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
