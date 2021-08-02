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

import beans.Notification;

public class Notifications {
	
	public Notifications() {
		super();
	}
	
	public void save(Notification notification) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Notification> notifications = load();
		notifications.add(notification);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/notifications.json"), notifications);
	}
	
	public ArrayList<Notification> load() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Notification> notificationsFromFile = new ArrayList<Notification>();
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Notification> notifications = mapper.readValue(new File("./static/files/notifications.json"), new TypeReference<List<Notification>>(){});
		notifications.forEach(o -> notificationsFromFile.add(o));
		
		return notificationsFromFile;
	}
	
	public void emptyFile() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/notifications.json"), new ArrayList<Notification>());
	}
}
