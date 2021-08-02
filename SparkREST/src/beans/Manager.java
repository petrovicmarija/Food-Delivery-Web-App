package beans;


public class Manager extends User{
	private Restaurant restaurant;

	public Manager() {
		super();
		this.restaurant = new Restaurant();
	}

	public Manager(String username, String password, String name, String surname, Gender gender, java.util.Date dateOfBirth,
			Role role, boolean isBlocked, boolean deleted, Restaurant restaurant) {
		super(username, password, name, surname, gender, dateOfBirth, role, isBlocked, deleted);
		this.restaurant = restaurant;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

}
