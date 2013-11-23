package model;

import java.util.ArrayList;
import java.util.List;


public class ApplicationModel {
	private final DAO dao;

	public ApplicationModel() throws ClassNotFoundException {
		dao = new DAO();
	}

	public List<ItemBean> getItems(String cat_id, String page) {
		int catId;
		int c = 10, o, p;
		//We need separate category ID
		try{
			catId = Integer.parseInt(cat_id);
		} catch(Exception e){
			catId = -1;
		}
		
		try{
			p = Integer.parseInt(page);
		}catch(Exception e){
			p= 1;
		}
		
		if(p < 1) {
			p=1;
		}
		//calculate offset
		o = (p-1)*c;
		
		List<ItemBean> items = new ArrayList<ItemBean>();
		try{
			items = dao.getItems(catId, o, c);
		}catch(Exception e){
			System.out.println("Could not get items because of:" + e);
		}
		return items;
	}
	
	public int getItemsCount(String cat_id){
		int catId;
		int count = 0;
		//We need separate category ID
		try{
			catId = Integer.parseInt(cat_id);
		} catch(Exception e){
			catId = -1;
		}
	
		try{
			count = dao.getItemsCount(catId);
		}catch(Exception e){
			System.out.println("Could not get item count because of:" + e);
		}
		return count;
	}

	public List<CategoryBean> getCategories() {
		List<CategoryBean> cats = new ArrayList<CategoryBean>();
		try{
			cats = dao.getCategories();
		} catch(Exception e){
			System.out.println("Could not get categories because of:" + e);
		}
		return cats;
	}
}
