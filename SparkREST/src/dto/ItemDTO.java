package dto;

import beans.*;

public class ItemDTO {
	private String name;
	private double price;
	private ItemType type;
	private Restaurant restaurant;
	private double amount;
	private String description;
	private String imagePath;
	private Item oldItem;
	
	public ItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ItemDTO(String name, double price, ItemType type, Restaurant restaurant, double amount, String description,
			String imagePath) {
		super();
		this.name = name;
		this.price = price;
		this.type = type;
		this.restaurant = restaurant;
		this.amount = amount;
		this.description = description;
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Item getOldItem() {
		return oldItem;
	}

	public void setOldItem(Item oldItem) {
		this.oldItem = oldItem;
	}
	
	

}
