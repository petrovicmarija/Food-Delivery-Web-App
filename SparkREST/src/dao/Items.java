package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import beans.*;

public class Items {
	private HashMap<String, Item> items = new HashMap<String, Item>();
	
	public Items() {
		
	}

	public HashMap<String, Item> getItems() {
		return items;
	}

	public void setItems(HashMap<String, Item> items) {
		this.items = items;
	}
	
	public void save(Item item) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Item> items = load();
		items.add(item);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/items.json"), items);
	}
	
	public ArrayList<Item> load() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Item> itemsFromFile = new ArrayList<Item>();
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Item> items = mapper.readValue(new File("./static/files/items.json"), new TypeReference<List<Item>>(){});
		items.forEach(i -> itemsFromFile.add(i));
		
		return itemsFromFile;
	}
	
	public void emptyFile() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/items.json"), new ArrayList<Item>());
	}
}
