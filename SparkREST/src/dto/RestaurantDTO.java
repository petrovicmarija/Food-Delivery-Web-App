package dto;

import beans.*;
import java.util.ArrayList;


public class RestaurantDTO {
	private String name;
	private RestaurantType type;
	private ArrayList<Item> items;
	private RestaurantStatus status;
	private Location location;
	private String imgPath;
	
	public RestaurantDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public RestaurantDTO(String name, RestaurantType type, ArrayList<Item> items, RestaurantStatus status,
			Location location, String imgPath) {
		super();
		this.name = name;
		this.type = type;
		this.items = items;
		this.status = status;
		this.location = location;
		this.imgPath = imgPath;
	} 

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public RestaurantType getType() {
		return type;
	}


	public void setType(RestaurantType type) {
		this.type = type;
	}


	public ArrayList<Item> getItems() {
		return items;
	}


	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}


	public RestaurantStatus getStatus() {
		return status;
	}


	public void setStatus(RestaurantStatus status) {
		this.status = status;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public String getImgPath() {
		return imgPath;
	}


	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	
}
