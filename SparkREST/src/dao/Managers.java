package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import beans.Manager;

public class Managers {
	private ArrayList<Manager> managerList = new ArrayList<Manager>();

	public Managers() {
		super();
	}

	public ArrayList<Manager> getManagers() {
		return managerList;
	}

	public void setManagers(ArrayList<Manager> managers) {
		this.managerList = managers;
	}
	
	public void save(Manager manager) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Manager> managers = load();
		managers.add(manager);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/managers.json"), managers);
	}
	
	public ArrayList<Manager> load() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Manager> managersFromFile = new ArrayList<Manager>();
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Manager> managers = mapper.readValue(new File("./static/files/managers.json"), new TypeReference<List<Manager>>(){});
		managers.forEach(c -> managersFromFile.add(c));
		
		return managersFromFile;
	}
	
	public void emptyFile() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/managers.json"), new ArrayList<Manager>());
	}
	
	public void saveAll(ArrayList<Manager> managers) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Manager> managersFromFile = load();
		managersFromFile.addAll(managers);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/managers.json"), managersFromFile);
	}
	
}
