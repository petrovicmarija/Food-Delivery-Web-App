package beans;

public class CustomerType {
	private TypeName typeName;
	private double discount;
	private double requiredPoints;
	
	public CustomerType() {
		super();
		this.typeName = TypeName.steel;
		this.discount = 0.0;
		this.requiredPoints = 0.0;
	}
	
	public CustomerType(TypeName typeName, double discount, int requiredPoints) {
		super();
		this.typeName = typeName;
		this.discount = discount;
		this.requiredPoints = requiredPoints;
	}

	public TypeName getTypeName() {
		return typeName;
	}

	public void setTypeName(TypeName typeName) {
		this.typeName = typeName;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getRequiredPoints() {
		return requiredPoints;
	}

	public void setRequiredPoints(double requiredPoints) {
		this.requiredPoints = requiredPoints;
	}
	
	
	
	
}
