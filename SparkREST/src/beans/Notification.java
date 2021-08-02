package beans;

public class Notification {
	private String deliverer;
	private String orderId;
	private String content;
	private boolean deleted;
	
	public Notification() {
		super();
	}
	
	public Notification(String deliverer, String orderId, String content, boolean deleted) {
		super();
		this.deliverer = deliverer;
		this.orderId = orderId;
		this.content = content;
		this.deleted = deleted;
		
	}

	public String getDeliverer() {
		return deliverer;
	}
	
	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
