package model;

public class ShoppingCoupon {
	private double percent;
	private double minimum;
	private String code;
	
	public ShoppingCoupon(){
		
	}
	
	public ShoppingCoupon(double percent, double minimum, String code) {
		super();
		this.percent = percent;
		this.minimum = minimum;
		this.code = code;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public double getMinimum() {
		return minimum;
	}
	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
