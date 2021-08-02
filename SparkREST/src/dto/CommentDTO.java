package dto;

import beans.Customer;
import beans.Restaurant;

public class CommentDTO {
	private String customerUsername;
	private Restaurant restaurant;
	private String text;
	private int grade;
	
	public CommentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommentDTO(String customerUsername, Restaurant restaurant, String text, int grade) {
		super();
		this.customerUsername = customerUsername;
		this.restaurant = restaurant;
		this.text = text;
		this.grade = grade;
	}

	public String getCustomerUsername() {
		return customerUsername;
	}

	public void setCustomerUsername(String customerUsername) {
		this.customerUsername = customerUsername;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	
}
