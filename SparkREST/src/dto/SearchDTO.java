package dto;

import java.util.ArrayList;
import java.util.Date;

import beans.Order;
import beans.User;

public class SearchDTO {
	private String restaurantName;
	private String city;
	private String location;
	private String country;
	private double grade;
	private String restaurantType;
	private User user;
	private ArrayList<Order> orders;
	private double startPrice;
	private double endPrice;
	private String startDate;
	private String endDate;
	
	
	public SearchDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearchDTO(String restaurantName, String city, String location, String country, double grade,
			String restaurantType) {
		super();
		this.restaurantName = restaurantName;
		this.city = city;
		this.location = location;
		this.country = country;
		this.grade = grade;
		this.restaurantType = restaurantType;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public String getRestaurantType() {
		return restaurantType;
	}

	public void setRestaurantType(String restaurantType) {
		this.restaurantType = restaurantType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	public double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}

	public double getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(double endPrice) {
		this.endPrice = endPrice;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
		
}
