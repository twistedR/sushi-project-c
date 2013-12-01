package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="order")
@XmlType(propOrder={"customer", "items", "total", "shipping", "hst", "grandTotal"})
public class PurchaseOrder {
	private int orderId;
	private String submitted;
	private ClientBean customer;
	private List<ItemBean> items;
	private double total;
	private double shipping;
	private double hst;
	private double grandTotal;
	
	public PurchaseOrder(){
		
	}
	
	@XmlAttribute(name="id")
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	@XmlAttribute(name="submitted")
	public String getSubmitted() {
		return submitted;
	}
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
	
	@XmlElement(name="customer")
	public ClientBean getCustomer() {
		return customer;
	}
	public void setCustomer(ClientBean customer) {
		this.customer = customer;
	}
	@XmlElementWrapper(name="items")
	@XmlElement(name="item")
	public List<ItemBean> getItems() {
		return items;
	}
	public void setItems(List<ItemBean> items) {
		this.items = items;
	}
	@XmlElement(name="total")
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	@XmlElement(name="shipping")
	public double getShipping() {
		return shipping;
	}
	public void setShipping(double shipping) {
		this.shipping = shipping;
	}
	@XmlElement(name="HST")
	public double getHst() {
		return hst;
	}
	public void setHst(double hst) {
		this.hst = hst;
	}
	@XmlElement(name="grandTotal")
	public double getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}
	
}
