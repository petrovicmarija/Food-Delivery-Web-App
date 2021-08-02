package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import beans.Comment;
import beans.Item;
import beans.Restaurant;
import beans.RestaurantStatus;
import dao.Restaurants;
import dto.SearchDTO;
import sort.SortByGradeAscending;
import sort.SortByGradeDescending;
import sort.SortByLocationAscending;
import sort.SortByLocationDescending;
import sort.SortByNameAscending;
import sort.SortByNameDescending;
import dto.FilterDTO;

public class RestaurantService {
	private Restaurants restaurants = new Restaurants();
	private static CommentService commentService = new CommentService();
	private static ItemService itemService = new ItemService();
	
	public Collection<Restaurant> getRestaurants() throws JsonGenerationException, JsonMappingException, IOException{
		return this.restaurants.load();
	}
	
	public Restaurant addRestaurant(Restaurant restaurant) throws JsonGenerationException, JsonMappingException, IOException {
		restaurants.save(restaurant);
		return restaurant;
	}
	
	public Restaurant getRestaurantByName(String name) {
		Restaurant restaurant = new Restaurant();
		
		try {
			for (Restaurant r : restaurants.load()) {
				if (r.getName().equals(name)) {
					restaurant = r;
				}
			}
			return restaurant;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch(JsonGenerationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return restaurant;
	}
	
	public ArrayList<Restaurant> filterRestaurantsByStatus(FilterDTO fromJson) throws IOException, JsonGenerationException, JsonMappingException {
		ArrayList<Restaurant> filtered = new ArrayList<Restaurant>();
		ArrayList<Restaurant> found = fromJson.getRestaurants();
		
		for (Restaurant r : found) {
			if (fromJson.getType() != null) {
				for (String s : fromJson.getType()) {
					if (s.equals(r.getType().toString()) && r.getStatus().equals(RestaurantStatus.open)) {
						filtered.add(r);
					} 
				}
			}
		}
		
		return filtered;
	}
	
	public ArrayList<Restaurant> sortByGrade(FilterDTO fromJson) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Restaurant> found = fromJson.getRestaurants();
		ArrayList<Restaurant> sorted = new ArrayList<Restaurant>();

		for (Restaurant r : found) {
			double avg;
			int cnt = 0;
			int sum = 0;
			for (Comment c : commentService.getComments()) {
				if (c.getRestaurant().getName().equals(r.getName())) {
					sum += c.getGrade();
					cnt++;
				} 
			}
			avg = sum/cnt;
			r.setAverageGrade(avg);
			sorted.add(r);
		}
		
		if (fromJson.isAscending()) {
			Collections.sort(sorted, new SortByGradeAscending());
		} else if (!fromJson.isAscending()) {
			Collections.sort(sorted, new SortByGradeDescending());
		}
		
		return sorted;
	}
	
	public ArrayList<Restaurant> sortByName(FilterDTO fromJson) {
		ArrayList<Restaurant> found = fromJson.getRestaurants();
		
		if (fromJson.isAscending()) {
			Collections.sort(found, new SortByNameAscending());
		} else if (!fromJson.isAscending()) {
			Collections.sort(found, new SortByNameDescending());
		}
		
		return found;
	}
	
	public ArrayList<Restaurant> sortByLocation(FilterDTO fromJson) {
		ArrayList<Restaurant> found = fromJson.getRestaurants();
		
		if (fromJson.isAscending()) {
			Collections.sort(found, new SortByLocationAscending());
		} else if(!fromJson.isAscending()) {
			Collections.sort(found, new SortByLocationDescending());
		}
		
		return found;
	}
	
	
	public ArrayList<Restaurant> findRestaurants(SearchDTO fromJson) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Restaurant> foundRestaurants = new ArrayList<Restaurant>();
		ArrayList<Restaurant> allRestaurants = this.restaurants.load();
		boolean canAdd = false;
		
		for (Restaurant r : allRestaurants) {
			if (fromJson.getCity().isEmpty()) {
				if (!fromJson.getCountry().isEmpty()) {
					if (fromJson.getCountry().toLowerCase().equals(r.getLocation().getAddress().getCity().getState().getState().toLowerCase())) {
						canAdd = true;
					} else {
						continue;
						//canAdd = false;
					}
				}
			} else if (!fromJson.getCity().isEmpty()) {
				if (!fromJson.getCountry().isEmpty()) {
					if (fromJson.getCountry().toLowerCase().equals(r.getLocation().getAddress().getCity().getState().getState().toLowerCase()) 
							&& fromJson.getCity().toLowerCase().equals(r.getLocation().getAddress().getCity().getCity().toLowerCase())) {
						canAdd = true;
					} else {
						continue;
						//canAdd = false;
					}
				}
			}
			
			if (!fromJson.getRestaurantName().isEmpty()) {
				if (fromJson.getRestaurantName().equals(r.getName())) {
					canAdd = true;
				} else {
					//canAdd = false;
					continue;
				}
			} 
			
			if (!fromJson.getRestaurantType().isEmpty()) {
				if (fromJson.getRestaurantType().equals(r.getType().toString())) {
					canAdd = true;
				} else {
					//canAdd = false;
					continue;
				}
			} 
			
			if (fromJson.getGrade() != 0) {
			/*	if (fromJson.getGrade() == commentService.countAverageGrade(fromJson.getRestaurantName())) {
					canAdd = true;
				} else {
					//canAdd = false;
					continue;
				} */
				double avg = commentService.countAverageGrade(r.getName());
				if (avg == fromJson.getGrade()) {
					//restaurants.add(r);
					canAdd = true;
				} else {
					continue;
				}
			} 
			
			if (canAdd == true) {
				ArrayList<Item> items = new ArrayList<Item>();
				for (Item i : itemService.getItemsForRestaurant(r.getName())) {
					items.add(i);
				}
				r.setItems(items);
				foundRestaurants.add(r);
			}
		}
		return foundRestaurants;
	}
	
	public ArrayList<Restaurant> getRestaurantsByGrade(SearchDTO fromJson) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		for (Restaurant r : getRestaurants()) {
			double avg = commentService.countAverageGrade(r.getName());
			if (avg == fromJson.getGrade()) {
				restaurants.add(r);
			} else {
				continue;
			}
		}
		
		return restaurants;
	}
	
	public ArrayList<Restaurant> getOpenRestaurants() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Restaurant> openRestaurants = new ArrayList<Restaurant>();
		for (Restaurant r : restaurants.load()) {
			if (r.getStatus().equals(RestaurantStatus.open)) openRestaurants.add(r);
		}
		return openRestaurants;
	}
	
	public ArrayList<Restaurant> getClosedRestaurants() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Restaurant> closedRestaurants = new ArrayList<Restaurant>();
		for (Restaurant r : restaurants.load()) {
			if (r.getStatus().equals(RestaurantStatus.closed)) closedRestaurants.add(r);
		}
		return closedRestaurants;
	}
	
	public ArrayList<Restaurant> getRestaurantsOC() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Restaurant> allRestaurants = new ArrayList<Restaurant>();
		allRestaurants.addAll(getOpenRestaurants());
		allRestaurants.addAll(getClosedRestaurants());
		return allRestaurants;
	}
	
	public String getRestaurantType(String name) throws JsonGenerationException, JsonMappingException, IOException {
		String type = "";
		for (Restaurant r : restaurants.load()) {
			if (r.getName().equals(name)) {
				type = r.getType().toString();
			}
		}
		return type;
	}
	
}
