package dto;

import java.util.ArrayList;
import java.util.Date;

import beans.Order;
import beans.Restaurant;

public class FilterDTO {
	private ArrayList<String> type;
	private String status;
	private ArrayList<Restaurant> restaurants;
	private boolean ascending;
	private ArrayList<Order> orders;
	private double price;
	private Date date;
	private ArrayList<String> orderStatus;
	
	public FilterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FilterDTO(ArrayList<String> type, String status, ArrayList<Restaurant> restaurants, boolean ascending) {
		super();
		this.type = type;
		this.status = status;
		this.restaurants = restaurants;
		this.ascending = ascending;
	}

	public ArrayList<String> getType() {
		return type;
	}

	public void setType(ArrayList<String> type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(ArrayList<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ArrayList<String> getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(ArrayList<String> orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
}
