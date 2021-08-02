package dto;

public class EditCartDTO {
	private String name;
	private double amount;
	
	public EditCartDTO(String name, double amount) {
		super();
		this.name = name;
		this.amount = amount;
	}

	public EditCartDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
