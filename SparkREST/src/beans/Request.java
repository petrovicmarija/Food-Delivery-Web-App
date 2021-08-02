package beans;

public class Request {
	private String deliverer;
	private Order order;
	private boolean accepted;
	private boolean deleted;
	
	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Request(String deliverer, Order order, boolean accepted, boolean deleted) {
		super();
		this.deliverer = deliverer;
		this.order = order;
		this.accepted = accepted;
		this.deleted = deleted;
	}

	public String getDeliverer() {
		return deliverer;
	}

	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
