package beans;

import java.util.ArrayList;

public class Deliverer extends User{
	private ArrayList<Order> orders;

	public Deliverer() {
		super();
	}
	
	public Deliverer(String username, String password, String name, String surname, Gender gender, java.util.Date dateOfBirth,
			Role role, boolean isBlocked, boolean deleted, ArrayList<Order> orders) {
		super(username, password, name, surname, gender, dateOfBirth, role, isBlocked, deleted);
		this.orders = orders;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}
	
}
