package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dao.*;
import beans.Item;


public class ItemService {
	private Items items = new Items();
	
	public Collection<Item> getItems() throws JsonGenerationException, JsonMappingException, IOException {
		return this.items.load();
	}
	
	public Item addItem(Item item) throws JsonMappingException, JsonGenerationException, IOException {
		items.save(item);	
		return item;
	}
	
	public ArrayList<Item> getItemsForRestaurant(String restaurantName) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Item> restaurantItems = new ArrayList<Item>();
		
		for (Item item : items.load()) {
			if (item.getRestaurant().getName().equals(restaurantName)) {
				restaurantItems.add(item);
			}
		}
		
		return restaurantItems;
	}
	
	public void editItem(Item oldItem, Item newItem) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Item> itemsFromFile = items.load();
		
		for (int i = 0; i < itemsFromFile.size(); i++) {
			if (itemsFromFile.get(i).getName().equals(oldItem.getName())) {
				itemsFromFile.remove(i);
			}
		}
		
		itemsFromFile.add(newItem);
		items.emptyFile();
		for (Item item : itemsFromFile) {
			items.save(item);
		}
	}
}
