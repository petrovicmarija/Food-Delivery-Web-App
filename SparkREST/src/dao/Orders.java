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

public class Orders {
	private HashMap<String, Order> orders = new HashMap<String, Order>();
	
	public Orders() {
		
	}
	
	public HashMap<String, Order> getOrders() {
		return orders;
	}

	public void setOrders(HashMap<String, Order> orders) {
		this.orders = orders;
	}
	
	public void save(Order order) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Order> orders = load();
		orders.add(order);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/orders.json"), orders);
	}
	
	public ArrayList<Order> load() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> ordersFromFile = new ArrayList<Order>();
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Order> orders = mapper.readValue(new File("./static/files/orders.json"), new TypeReference<List<Order>>(){});
		orders.forEach(o -> ordersFromFile.add(o));
		
		return ordersFromFile;
	}
	
	public void emptyFile() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/orders.json"), new ArrayList<Order>());
	}

}
