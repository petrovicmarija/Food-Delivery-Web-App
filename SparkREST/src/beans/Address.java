package beans;

public class Address {
	private String address;
	private int number;
	private City city;
	
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Address(String address, int number, City city) {
		super();
		this.address = address;
		this.number = number;
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
}
