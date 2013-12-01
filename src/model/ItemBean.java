package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"name", "price", "quantity", "total"})
public class ItemBean {
	private String number, name;
	private double price, costprice, total;
	private int quantity, onorder, reorder,catid, supid;
	private String unit;
	private CategoryBean category;
	
	public ItemBean(){
		
	}
	
	@XmlAttribute(name="number")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="price")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@XmlTransient
	public double getCostprice() {
		return costprice;
	}

	public void setCostprice(double costprice) {
		this.costprice = costprice;
	}
	@XmlElement(name="extended")
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	@XmlElement(name="quantity")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		
		this.quantity = quantity;
	}

	@XmlTransient
	public int getOnorder() {
		return onorder;
	}

	public void setOnorder(int onorder) {
		this.onorder = onorder;
	}

	@XmlTransient
	public int getReorder() {
		return reorder;
	}

	public void setReorder(int reorder) {
		this.reorder = reorder;
	}

	@XmlTransient
	public int getCatid() {
		return catid;
	}

	public void setCatid(int catid) {
		this.catid = catid;
	}

	@XmlTransient
	public int getSupid() {
		return supid;
	}

	public void setSupid(int supid) {
		this.supid = supid;
	}

	@XmlTransient
	public String getUnit() {
		return unit;
	}

	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@XmlTransient
	public CategoryBean getCategory() {
		return category;
	}

	public void setCategory(CategoryBean category) {
		this.category = category;
	}

}
