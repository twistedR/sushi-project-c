package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShoppingCartBean {
	private ClientBean client;
	private double total;
	private double shipping;
	private double hstValue;
	private double baseShipping;
	private double hst;
	private double grandTotal;
	private List<ItemBean> items;
	private ShoppingCoupon coupon;
	private double discount;
	
	public ShoppingCartBean(){
		client = null;
		items = new ArrayList<ItemBean>();
	}

	public ClientBean getClient() {
		return client;
	}

	public void setClient(ClientBean client) {
		this.client = client;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getShipping() {
		return shipping;
	}

	public void setShipping(double shipping) {
		this.shipping = shipping;
	}

	public double getHst() {
		return hst;
	}

	public void setHst(double hst) {
		this.hst = hst;
	}
	
	public double getHstValue() {
		return hstValue;
	}

	public void setHstValue(double hstValue) {
		this.hstValue = hstValue;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
	}
	
	public ShoppingCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(ShoppingCoupon coupon) {
		this.coupon = coupon;
	}

	public void addItem(ItemBean i, int quantity) {
		Iterator<ItemBean> it = items.iterator();
		boolean exists = false;
		while(it.hasNext()){
			ItemBean temp = it.next();
			if(temp.getNumber().equals(i.getNumber())){
				exists = true;
				//Edge case
				if(quantity<1){
					this.items.remove(temp);
				}else{
					temp.setQuantity(quantity);
				}
				break;
			}
		}
		if(exists != true){
			i.setQuantity(quantity);
			this.items.add(i);
		}
		
		this.calculateTotal();
		
	}
	
	public void removeItem(String item_number) {
		Iterator<ItemBean> it = items.iterator();
		ItemBean removal = null;
		while(it.hasNext()){
			ItemBean temp = it.next();
			if(temp.getNumber().equals(item_number)){
				removal = temp;
				break;
			}
		}
		if(removal != null){
			items.remove(removal);
		}
		this.calculateTotal();
	}
	
	public boolean applyCoupon(ShoppingCoupon sc){
		if(this.getTotal()>sc.getMinimum()){
			this.setCoupon(sc);
			this.calculateTotal();
			return true;
		}
		return false;
	}
	
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getBaseShipping() {
		return baseShipping;
	}

	public void setBaseShipping(double baseShipping) {
		this.baseShipping = baseShipping;
	}

	private void calculateTotal(){
		double t = 0;
		Iterator<ItemBean> it = items.iterator();
		while(it.hasNext()){
			ItemBean temp = it.next();
			double tot = (temp.getQuantity()*temp.getPrice());
			t += tot;
			temp.setTotal(tot);
		}
		this.setTotal(t);
		
		double off = 0;
		if(getCoupon()!=null){
			if(getCoupon().getMinimum() < getTotal()){
				off = (getCoupon().getPercent() * getTotal())/100.0;
				this.setDiscount(off);
			} else {
				this.setDiscount(0);
				this.setCoupon(null);
			}
		} else {
			this.setDiscount(0);
		}
		double fp = getTotal()-getDiscount();
		
		if(fp>100){
			this.setShipping(0);
		} else {
			this.setShipping(getBaseShipping());
		}
		double taxes = (fp + this.getShipping()) * (this.getHstValue()/100.0);
		this.setHst(taxes);
		
		this.setGrandTotal(this.getHst() + fp + this.getShipping());
	}
}
