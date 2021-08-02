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

public class Customers {
	private HashMap<String, Customer> customers = new HashMap<String, Customer>();
	
	public Customers() {
		
	}
	
	public HashMap<String, Customer> getCustomers() {
		return customers;
	}
	
	public void setCustomers(HashMap<String, Customer> customers) {
		this.customers = customers;
	}
	
	public void save(Customer customer) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Customer> customers = load();
		customers.add(customer);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/customers.json"), customers);
	}
	
	public ArrayList<Customer> load() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> customersFromFile = new ArrayList<Customer>();
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Customer> customers = mapper.readValue(new File("./static/files/customers.json"), new TypeReference<List<Customer>>(){});
		customers.forEach(c -> customersFromFile.add(c));
		
		return customersFromFile;
	}
	
	public void emptyFile() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/customers.json"), new ArrayList<Customer>());
	}

}
