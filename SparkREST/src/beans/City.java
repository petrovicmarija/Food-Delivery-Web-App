package beans;

public class City {
	private String city;
	private double zipcode;
	private State state;
	private int id;
		
	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public City(String city, double zipCode, State state) {
		super();
		this.city = city;
		this.zipcode = zipCode;
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getZipCode() {
		return zipcode;
	}

	public void setZipCode(double zipCode) {
		this.zipcode = zipCode;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	

}
