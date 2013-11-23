package model;

import java.util.ArrayList;
import java.util.List;


public class ApplicationModel {
	private final DAO dao;

	public ApplicationModel() throws ClassNotFoundException {
		dao = new DAO();
	}

	public List<ItemBean> getItems(String cat_id) {
		int catId;
		
		//We need separate category ID
		try{
			catId = Integer.parseInt(cat_id);
		} catch(Exception e){
			catId = -1;
		}
		try{
			List<ItemBean> items = dao.getItems(catId);
		}catch(Exception e){
			
		}
		return null;
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
