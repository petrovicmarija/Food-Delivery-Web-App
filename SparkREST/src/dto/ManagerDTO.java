package dto;

import beans.Gender;
import beans.Restaurant;
import beans.Role;

public class ManagerDTO {
	private String username;
	private String password;
	private String name;
	private String surname;
	private Gender gender;
	private String dateOfBirth;
	private Role role;
	private boolean isBlocked;
	private boolean deleted;
	private Restaurant restaurant;
	
	public ManagerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ManagerDTO(String username, String password, String name, String surname, Gender gender, String dateOfBirth,
			Role role, boolean isBlocked, boolean deleted, Restaurant restaurant) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.role = role;
		this.isBlocked = isBlocked;
		this.deleted = deleted;
		this.restaurant = restaurant;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	

}
