package beans;

public class State {
	private String state; 
	private int id;
	
	public State() {
		super();
		// TODO Auto-generated constructor stub
	}

	public State(String state, int id) {
		super();
		this.state = state;
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
