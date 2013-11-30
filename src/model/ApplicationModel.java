package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
			System.out.println("Could not get items because of:" + e);
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
}
