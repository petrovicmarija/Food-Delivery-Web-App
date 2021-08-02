package dto;

public class RequestDTO {
	private String restaurantName;
	private double orderPrice;
	private String deliverer;
	private String orderId;
	
	
	public RequestDTO() {
		super();
	}


	public RequestDTO(String restaurantName, double orderPrice, String deliverer, String orderId) {
		super();
		this.restaurantName = restaurantName;
		this.orderPrice = orderPrice;
		this.deliverer = deliverer;
		this.orderId = orderId;
	}


	public String getRestaurantName() {
		return restaurantName;
	}


	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}


	public double getOrderPrice() {
		return orderPrice;
	}


	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}


	public String getDeliverer() {
		return deliverer;
	}


	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	
}
