package beans;

import java.util.Date;
import java.util.ArrayList;

public class Order {
	private String id;
	private ArrayList<Item> orderedItems;
	private String restaurant;
	private String restLogo;
	private Date dateAndTime;
	private double price;
	private String customer;
	private OrderStatus status;
	
	public Order() {
		super();
	}

	public Order(String id, ArrayList<Item> orderedItems, String restaurant, String restLogo, Date dateAndTime, double price,
			String customer, OrderStatus status) {
		super();
		this.id = id;
		this.orderedItems = orderedItems;
		this.restaurant = restaurant;
		this.restLogo = restLogo;
		this.dateAndTime = dateAndTime;
		this.price = price;
		this.customer = customer;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Item> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(ArrayList<Item> orderedItems) {
		this.orderedItems = orderedItems;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public Date getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getRestLogo() {
		return restLogo;
	}

	public void setRestLogo(String restLogo) {
		this.restLogo = restLogo;
	}
	
	
}
