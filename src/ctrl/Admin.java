package ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ApplicationModel;
import model.ShoppingCoupon;

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
				Long checkouts = checkout_times.get("checkouts");
				Double avg_time = (double) time/((double) checkouts);
				avg_time = avg_time/1000.0;
				avg_time = avg_time/60.0;
				request.setAttribute("total_checkout_time", time);
				request.setAttribute("total_checkouts", checkouts);
				request.setAttribute("avg_checkout_time", avg_time);
			} 
			
			//Coupons
			List<ShoppingCoupon> coupons = (List<ShoppingCoupon>) getServletContext().getAttribute("coupons");
			if(coupons==null){
				coupons = new ArrayList<ShoppingCoupon>();
				getServletContext().setAttribute("coupons", coupons);
			}
			request.setAttribute("coupons", coupons);
			request.getRequestDispatcher("../AdminDashboard.jspx").forward(request, response);
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("app_root", request.getContextPath());
		List<ShoppingCoupon> coupons = (List<ShoppingCoupon>) getServletContext().getAttribute("coupons");
		if(coupons==null){
			coupons = new ArrayList<ShoppingCoupon>();
			getServletContext().setAttribute("coupons", coupons);
		}
		if(request.getParameter("add_coupon") != null){
			request.setAttribute("app_root", request.getContextPath());
			String couponCode = request.getParameter("coupon_code");
			String couponMin = request.getParameter("coupon_min");
			String couponOff = request.getParameter("coupon_off");
			String password = request.getParameter("admin_password");
			if(couponCode==null || couponCode.trim().isEmpty() || couponMin==null || couponMin.trim().isEmpty() || couponOff==null || couponOff.trim().isEmpty()){
				request.setAttribute("error", "Not enough information provided.");
			} else if(!password.equals(getServletContext().getInitParameter("admin-password"))){
				request.setAttribute("error", "Incorrect password.");
			}else {
				try{
					double min = Double.parseDouble(couponMin);
					double off = Double.parseDouble(couponOff);
					ShoppingCoupon sc = new ShoppingCoupon(off, min, couponCode);
					//Check if coupon already exists
					Iterator<ShoppingCoupon> it = coupons.iterator();
					boolean exists = false;
					while(it.hasNext()){
						ShoppingCoupon temp = it.next();
						if(temp.getCode().equals(couponCode)){
							exists = true;
						}
					}
					if(!exists){
						coupons.add(sc);
					}else {
						request.setAttribute("error", "Coupon already exists.");
					}
					
				}catch(Exception e){
					request.setAttribute("error", "Not enough information provided.");
					e.printStackTrace();
				}
			}
		}
		request.setAttribute("coupons", coupons);
		request.getRequestDispatcher("../AdminDashboard.jspx").forward(request, response);
	}

}
