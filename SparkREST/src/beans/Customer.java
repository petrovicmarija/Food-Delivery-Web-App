package beans;

import java.util.ArrayList;
import java.util.Date;

public class Customer extends User{
	private ArrayList<Order> orders;
	private ShoppingCart shoppingCart;
	private double points;
	private CustomerType customerType;
	private Date startCheck;
	private Date endCheck;
	private int actions;
	
	public Customer() {
		super();
	}

	public Customer(String username, String password, String name, String surname, Gender gender, Date date,
			Role role, boolean isBlocked, boolean deleted, ArrayList<Order> orders, ShoppingCart shoppingCart,
			double points, CustomerType customerType, Date startCheck, Date endCheck, int actions) {
		super(username, password, name, surname, gender, date, role, isBlocked, deleted);
		this.orders = orders;
		this.shoppingCart = shoppingCart;
		this.points = points;
		this.customerType = customerType;
		this.startCheck = startCheck;
		this.endCheck = endCheck;
		this.actions = actions;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public Date getStartCheck() {
		return startCheck;
	}

	public void setStartCheck(Date startCheck) {
		this.startCheck = startCheck;
	}

	public Date getEndCheck() {
		return endCheck;
	}

	public void setEndCheck(Date endCheck) {
		this.endCheck = endCheck;
	}

	public int getActions() {
		return actions;
	}

	public void setActions(int actions) {
		this.actions = actions;
	}
	
}
