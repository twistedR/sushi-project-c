package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

public class ApplicationModel {

	private final DAO dao;
	private Map<String, String> itemSortMap;

	public ApplicationModel() throws ClassNotFoundException {
		dao = new DAO();

		// TODO: Perhaps look into getting this from Web XML or DAO

		// Using LinkedHashMap to maintain order
		itemSortMap = new LinkedHashMap<String, String>();
		itemSortMap.put("Item Name", "NAME");
		itemSortMap.put("Item Number", "NUMBER");
		itemSortMap.put("Category", "CATID");
		itemSortMap.put("Price - Low to High", "PRICE ASC");
		itemSortMap.put("Price - High to Low", "PRICE DESC");

	}

	public List<ItemBean> getItems(String search_term, String cat_id, String page,
			String min_price, String max_price, String sort) {
		int catId;
		int c = 10, o, p;
		double minPrice, maxPrice;
		String s;
		// We need separate category ID
		try {
			catId = Integer.parseInt(cat_id);
		} catch (Exception e) {
			catId = -1;
		}

		// Setup the page
		try {
			p = Integer.parseInt(page);
		} catch (Exception e) {
			p = 1;
		}

		if (p < 1) {
			p = 1;
		}
		// calculate offset
		o = (p - 1) * c;

		// Setup the min_price
		try {
			minPrice = Double.parseDouble(min_price);
		} catch (Exception e) {
			minPrice = 0;
		}
		if(minPrice<0){
			minPrice = 0;
		}
		

		// Setup the max_price
		try {
			maxPrice = Double.parseDouble(max_price);
		} catch (Exception e) {
			maxPrice = Double.MAX_VALUE;
		}
		if(maxPrice < 0 ){
			maxPrice = Double.MAX_VALUE;
		}
		
		if(minPrice > maxPrice){
			maxPrice = Double.MAX_VALUE;
		}
		
		// Setup the sorting
		s  = itemSortMap.get(sort);
		if(s==null){
			s = "NAME";
		}

		List<ItemBean> items = new ArrayList<ItemBean>();
		try {
			if(search_term == null || search_term.trim().isEmpty()){
				items = dao.getItems(catId, o, c, minPrice, maxPrice, s);
			} else {
				items = dao.getSearchItems(search_term, catId, o, c, minPrice, maxPrice, s);
			}
			
		} catch (Exception e) {
			System.out.println("Could not get items because of:" + e);
		}
		return items;
	}
	
	public int getItemsCount(String search_term, String cat_id,
			String min_price, String max_price, String sort) {
		int catId;
		double minPrice, maxPrice;
		String s;
		// We need separate category ID
		try {
			catId = Integer.parseInt(cat_id);
		} catch (Exception e) {
			catId = -1;
		} 
		
		// Setup the min_price
		try {
			minPrice = Double.parseDouble(min_price);
		} catch (Exception e) {
			minPrice = 0;
		}
		if(minPrice<0){
			minPrice = 0;
		}
		

		// Setup the max_price
		try {
			maxPrice = Double.parseDouble(max_price);
		} catch (Exception e) {
			maxPrice = Double.MAX_VALUE;
		}
		if(maxPrice < 0 ){
			maxPrice = Double.MAX_VALUE;
		}
		
		if(minPrice > maxPrice){
			maxPrice = Double.MAX_VALUE;
		}
		
		// Setup the sorting
		s  = itemSortMap.get(sort);
		if(s==null){
			s = "NAME";
		}

		int count = 0;
		try {
			if(search_term == null || search_term.trim().isEmpty()){
				count = dao.getItemsCount(catId, minPrice, maxPrice);
			} else {
				count = dao.getSearchItemsCount(search_term, catId, minPrice, maxPrice);
			}
			
		} catch (Exception e) {
			System.out.println("Could not get items count because of:" + e);
		}
		return count;
	}



	public List<CategoryBean> getCategories() {
		List<CategoryBean> cats = new ArrayList<CategoryBean>();
		try {
			cats = dao.getCategories();
		} catch (Exception e) {
			System.out.println("Could not get categories because of:" + e);
		}
		return cats;
	}

	public List<String> getSortList() {
		List<String> sortList = new ArrayList<String>(this.itemSortMap.keySet());
		return sortList;
	}

	public ItemBean getItem(String itemNumber) throws Exception{
		ItemBean i = dao.getItem(itemNumber);
		if(i==null){
			throw new Exception("No such item");
		}
		return i;
	}

	public ClientBean authorize(String user_number, String password) throws Exception 
	{
		 int clientNumber = Integer.parseInt(user_number);
		 ClientBean client = this.dao.getClient(clientNumber, password);
		 if (client == null)
		 {
			 throw new Exception ("Inavalid Credentials");
		 }
		 return client;
		
	}
	
	public String placeOrder(ShoppingCartBean sc, ClientBean c, String folderPath) throws JAXBException, IOException{

		String poFormat = "po"+c.getNumber()+"_";
		Date n =  new Date();
		String d = (new SimpleDateFormat("yyyy-MM-dd")).format(n).toString();
		
		//Determine the filename
		//Go through all files in the directory
		File f = new File(folderPath);
		File[] folderList = f.listFiles();
		int lastOrder = 1;
		for(File fl : folderList){
			if(fl.isDirectory()){
				File[] list = fl.listFiles();
				for(File fi : list){
					if(fi.isFile() && fi.getName().endsWith(".xml") && fi.getName().startsWith(poFormat)){
						String orderNumber = fi.getName().substring(poFormat.length(), fi.getName().indexOf(".xml"));
						try{
							int t = Integer.parseInt(orderNumber);
							if(t>lastOrder){
								lastOrder = t;
							}
						}catch(Exception e){
							//We messed up file lookup, don't do anything
						}
					}
				}
			}
		}
		lastOrder++;
		String on = (String) (lastOrder < 10 ? "0"+lastOrder : ""+lastOrder);
		String newOrderFile = poFormat + on + ".xml";
		
		
		//Prepare the purchase order
		PurchaseOrder po = new PurchaseOrder();
		po.setOrderId(lastOrder);
		
		po.setSubmitted(d);
		po.setCustomer(c);
		po.setItems(sc.getItems());
		po.setHst(sc.getHst());
		po.setTotal(sc.getTotal());
		po.setShipping(sc.getShipping());
		po.setGrandTotal(sc.getGrandTotal());
		
		
		JAXBContext jx = JAXBContext.newInstance(po.getClass());
		Marshaller marshaller = jx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		
		StringWriter sw = new StringWriter();
		sw.write("<?xml version=\"1.0\"?>");
		sw.write("<?xml-stylesheet type=\"text/xsl\" href=\"../po.xsl\"?>");
//		sw.write("\n");
		marshaller.marshal(po, new StreamResult(sw));
		System.out.println(sw.toString());
		
		//Check if a folder for todays date exists
		String finalFolder = Paths.get(folderPath, d).toString();
		File t = new File(finalFolder);
		if(!t.exists()){
			t.mkdir();
		}
		
		//Write it to file!
		String finalFile = Paths.get(finalFolder, newOrderFile).toString();
		
		FileWriter fw = new FileWriter(finalFile);
		fw.write(sw.toString());
		fw.close();
		
		
		return Paths.get(d, newOrderFile).toString();
	}

	public List<String> getPurchaseOrderList(String startDate, String endDate, String folderPath) throws Exception {
		if(startDate==null){
			throw new Exception("Start date is required");
		}
		List<String> dates = new ArrayList<String>();
		if(endDate != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			Date date = format.parse(startDate);
			Date date2 = format.parse(endDate);
		
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			dates.add(format.format(cal.getTime()));
			while (cal.getTime().before(date2)) {
			    cal.add(Calendar.DATE, 1);
			    dates.add(format.format(cal.getTime()));
			}
		}else {
			dates.add(startDate);
		}
		
		List<String> poList = new ArrayList<String>();
		Iterator<String> it = dates.iterator();
		while(it.hasNext()){
			String temp = it.next();
			System.out.println(temp);
			File tf = new File(Paths.get(folderPath, temp).toString());
			if(!tf.exists()) continue;
			File[] fileList = tf.listFiles();
			for(File f : fileList){
				poList.add(temp+"/"+f.getName());
			}
		}
		
		return poList;
	}

}
