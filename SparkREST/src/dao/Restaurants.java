package dao;

import beans.*;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Restaurants {
	private HashMap<String, Restaurant> restaurants = new HashMap<String, Restaurant>();
	private ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();

	public Restaurants() {
	}

	public HashMap<String, Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(HashMap<String, Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

    public ArrayList<Restaurant> getRestaurantList() {
		Restaurant rest = new Restaurant("Petrus", RestaurantType.grill, new ArrayList<Item>(), RestaurantStatus.open, new Location(), "");
		restaurantList.add(rest);
		return restaurantList;
	} 

	public void setRestaurantList(ArrayList<Restaurant> restaurantList) {
		this.restaurantList = restaurantList;
	}

	public void save(Restaurant restaurant) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Restaurant> restaurants = load();
		restaurants.add(restaurant);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/restaurants.json"), restaurants);
	}

	public ArrayList<Restaurant> load() throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Restaurant> restaurantsFromFile = new ArrayList<Restaurant>();
		ArrayList<Restaurant> restaurants = mapper.readValue(new File("./static/files/restaurants.json"), new TypeReference<List<Restaurant>>(){});
		restaurants.forEach(r -> restaurantsFromFile.add(r));

		return restaurantsFromFile;
	}
	
	public Restaurant getRestaurantByName(String name) {
		Restaurant restaurant = new Restaurant();
		
		try {
			for (Restaurant r : load()) {
				if (r.getName().equals(name)) {
					restaurant = r;
				}
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch(JsonGenerationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return restaurant;
	}

}
