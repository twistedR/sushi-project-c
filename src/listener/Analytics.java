package listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;


/**
 * Application Lifecycle Listener implementation class Analytics
 *
 */
@WebListener
public class Analytics implements ServletRequestAttributeListener, HttpSessionAttributeListener {

    /**
     * Default constructor. 
     */
    public Analytics() {
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent arg0) {
        
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent arg0) {
        calculateAvgItemTime(arg0);
    }

	/**
     * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
     */
    public void attributeAdded(ServletRequestAttributeEvent evt) {
        if(evt.getName().equals("po_file")){
        	Long now = (new Date()).getTime();
        	HttpServletRequest req= (HttpServletRequest) evt.getServletRequest();
        	HttpSession sess =req.getSession();
        	Map<String, Long> checkout_times= (Map<String, Long>) sess.getServletContext().getAttribute("checkout_times");
      		Long totalTime;
      		Long checkouts;
      		if(checkout_times==null){
      			checkout_times = new HashMap<String, Long>();
      			totalTime = 0L;
      			checkouts = 0L;
      		} else {
      			totalTime = checkout_times.get("time");
      			checkouts = checkout_times.get("checkouts");
      		}
      		
      		Long diff = now -sess.getCreationTime();
      		totalTime = totalTime + diff;
      		checkouts++;
      		checkout_times.put("time", totalTime);
      		checkout_times.put("checkouts", checkouts);
      		
//      		System.out.println("Average Time: "+ ((double)totalTime/(double)checkouts));
      		
      		sess.getServletContext().setAttribute("checkout_times", checkout_times);
      		
        }
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent arg0) {
    	calculateAvgItemTime(arg0);
    }

	/**
     * @see ServletRequestAttributeListener#attributeRemoved(ServletRequestAttributeEvent)
     */
    public void attributeRemoved(ServletRequestAttributeEvent arg0) {
        // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
     */
    public void attributeReplaced(ServletRequestAttributeEvent arg0) {
        // TODO Auto-generated method stub
    }
    
    private void calculateAvgItemTime(HttpSessionBindingEvent event){
    	
    	if(event.getName().equals("shopping_cart")){
    		//To calculate average we need the last time we added stuff to it
    		//We can use the session for this purpose
    		Long lastAdded = (Long) event.getSession().getAttribute("last_cart_update_time");
    		Long now = (new Date()).getTime();
    		if(lastAdded == null){
    			lastAdded = event.getSession().getCreationTime();
    		}
    		//Calculate time difference
    		Long diff = now - lastAdded;
    		Map<String, Long> shopping_times= (Map<String, Long>) event.getSession().getServletContext().getAttribute("shopping_times");
    		Long totalTime;
    		Long items;
    		if(shopping_times==null){
    			shopping_times = new HashMap<String, Long>();
    			totalTime = 0L;
    			items = 0L;
    		} else {
    			totalTime = shopping_times.get("time");
      		items = shopping_times.get("items");
    		}
    		
    		totalTime = totalTime + diff;
    		items++;
    		shopping_times.put("time", totalTime);
    		shopping_times.put("items", items);
    		
//    		System.out.println("Average Time: "+ ((double)totalTime/(double)items));
    		
    		event.getSession().setAttribute("last_cart_update_time", now);
    		event.getSession().getServletContext().setAttribute("shopping_times", shopping_times);
    		
    	}
    }
	
}
