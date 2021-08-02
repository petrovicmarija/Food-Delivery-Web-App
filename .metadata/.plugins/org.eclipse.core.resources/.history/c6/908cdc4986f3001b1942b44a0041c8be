package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import beans.Manager;
import beans.Restaurant;
import beans.User;
import dao.Managers;

public class ManagerService {
	private Managers managers = new Managers();
	
	public Collection<Manager> getManagers() throws JsonGenerationException, JsonMappingException, IOException {
		return managers.load();
	}
	
	public void editProfile(User oldUser, User newUser) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Manager> managerList = managers.load();
		Manager oldManager = new Manager();
		for (int i = 0; i < managerList.size(); i++) {
			if(managerList.get(i).getUsername().equals(oldUser.getUsername())) {
				managerList.remove(i);
				oldManager = new Manager(managerList.get(i).getUsername(),
						managerList.get(i).getPassword(),
						managerList.get(i).getName(),
						managerList.get(i).getSurname(),
						managerList.get(i).getGender(),
						managerList.get(i).getDateOfBirth(),
						managerList.get(i).getRole(),
						managerList.get(i).getIsBlocked(),
						managerList.get(i).isDeleted(),
						managerList.get(i).getRestaurant());
				break;
			}
		}
		
		Manager newManager = new Manager(newUser.getUsername(),
				newUser.getPassword(),
				newUser.getName(),
				newUser.getSurname(),
				newUser.getGender(),
				newUser.getDateOfBirth(),
				newUser.getRole(),
				newUser.getIsBlocked(),
				newUser.isDeleted(),
				oldManager.getRestaurant());
		managerList.add(newManager);
		managers.emptyFile();
		for(Manager manager : managerList) {
			managers.save(manager);
		}
	}
	
	public ArrayList<Manager> getActiveManagers() throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Manager> activeManagers = new ArrayList<Manager>();
		 ArrayList<Manager> allManagers = managers.load();
		 for (int i = 0; i < allManagers.size(); i++) {
			if(allManagers.get(i).isDeleted() == false) {
				activeManagers.add(allManagers.get(i));
			}
		}
		 return activeManagers;
	 }
	
	public void deleteManager(String username) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Manager> managerList = managers.load();
		Manager manager = new Manager();
		for(int i = 0; i < managerList.size(); i++) {
			if(managerList.get(i).getUsername().equals(username)) {
				manager = managerList.get(i);
				managerList.remove(i);
			}
		}
		
		manager.setDeleted(true);
		managerList.add(manager);
		managers.emptyFile();
		for(Manager m : managerList) {
			managers.save(m);
		}
	}
	
	public ArrayList<Manager> getAvailableManagers() throws JsonGenerationException, JsonMappingException, IOException{
		ArrayList<Manager> availableManagers = new ArrayList<Manager>();
		for (Manager manager : managers.load()) {
			if (manager.getRestaurant().getName() == null) {
				availableManagers.add(manager);
			} 
		}
		
		return availableManagers;
	}
	
	public Manager findByUsername(String username) throws JsonGenerationException, JsonMappingException, IOException {
		Manager manager = new Manager();
		for (Manager m : managers.load()) {
			if (m.getUsername().equals(username)) {
				manager = m;
			}
		}
		return manager;
	}
	
	public void addManager(Manager manager) throws JsonGenerationException, JsonMappingException, IOException {
		managers.save(manager);
	}
	
	public void removeManager(String username) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Manager> managersFromFile = managers.load();
		
		for (Manager m : managersFromFile) {
			if (m.getUsername().equals(username)) {
				managersFromFile.remove(m);
				break;
			}
		}
		managers.emptyFile();
		managers.saveAll(managersFromFile);
	}
	
	public Restaurant getManagerRestaurant(String username) throws JsonGenerationException, JsonMappingException, IOException{
		//ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		Restaurant restaurant = new Restaurant();
		for (Manager m : managers.load()) {
			if(m.getUsername().equals(username)) {
				restaurant = m.getRestaurant();
			}
		}
		
		return restaurant;
	}
	
}
