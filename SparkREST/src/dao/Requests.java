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

import beans.Request;

public class Requests {
private HashMap<String, Request> requests = new HashMap<String, Request>();
	
	public Requests() {
		
	}
	
	public HashMap<String, Request> getOrders() {
		return requests;
	}

	public void setOrders(HashMap<String, Request> requests) {
		this.requests = requests;
	}
	
	public void save(Request request) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Request> requests = load();
		requests.add(request);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/requests.json"), requests);
	}
	
	public ArrayList<Request> load() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Request> requestsFromFile = new ArrayList<Request>();
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Request> requests = mapper.readValue(new File("./static/files/requests.json"), new TypeReference<List<Request>>(){});
		requests.forEach(o -> requestsFromFile.add(o));
		
		return requestsFromFile;
	}
	
	public void emptyFile() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/requests.json"), new ArrayList<Request>());
	}
}
