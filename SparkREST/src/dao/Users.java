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

import beans.User;

public class Users {
	private HashMap<String, User> users = new HashMap<String, User>();
	private ArrayList<User> userList = new ArrayList<User>();
	
	public Users() {
		
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}
	
	public User findByUsername(String username) throws JsonGenerationException, JsonMappingException, IOException {
		userList = load();
		for (User user : userList) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}
	
	public void save(User user) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<User> users = load();
		users.add(user);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/users.json"), users);
	}
	
	public ArrayList<User> load() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<User> usersFromFile = new ArrayList<User>();
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<User> users = mapper.readValue(new File("./static/files/users.json"), new TypeReference<List<User>>(){});
		users.forEach(u -> usersFromFile.add(u));
		
		return usersFromFile;
	}
	
	public void emptyFile() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/users.json"), new ArrayList<User>());
	}

}
