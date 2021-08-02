package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import beans.Customer;
import beans.CustomerType;
import beans.Deliverer;
import beans.Manager;
import beans.Order;
import beans.Restaurant;
import beans.Role;
import beans.ShoppingCart;
import beans.User;
import dao.Customers;
import dao.Deliverers;
import dao.Managers;
import dao.Users;
import dto.LoginDTO;

public class UserService {
	private Users users = new Users();
	private Customers customers = new Customers();
	private Managers managers = new Managers();
	private Deliverers deliverers = new Deliverers();
	private CustomerService customerService = new CustomerService();
	private ManagerService managerService = new ManagerService();
	private DelivererService delivererService = new DelivererService();
	
	
	public Collection<User> getUsers() throws JsonGenerationException, JsonMappingException, IOException {
		return this.users.load();
	}
	
	public User addUser(User user) throws JsonMappingException, JsonGenerationException, IOException {
		User userExists = users.findByUsername(user.getUsername());
		if(userExists != null) {
			return null;
		}
		if(user.getRole() == Role.customer) {
			Date startCheck = new Date();
			Date endCheck = addMonth(startCheck, 1);
			Customer customer = new Customer(user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.getGender(), user.getDateOfBirth(), Role.customer, user.getIsBlocked(), user.isDeleted(), new ArrayList<Order>(), new ShoppingCart(), 0, new CustomerType(), startCheck, endCheck, 0);
			customers.save(customer);
		} else if(user.getRole() == Role.manager) {
			Manager manager = new Manager(user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.getGender(), user.getDateOfBirth(), Role.manager, user.getIsBlocked(), user.isDeleted(), new Restaurant());
			managers.save(manager);
		} else if(user.getRole() == Role.deliverer) {
			Deliverer deliverer = new Deliverer(user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.getGender(), user.getDateOfBirth(), Role.deliverer, user.getIsBlocked(), user.isDeleted(), new ArrayList<Order>());
			deliverers.save(deliverer);
		}
		users.save(user);
		return user;
	}
	
	public User login(LoginDTO user) throws JsonGenerationException, JsonMappingException, IOException {
		User userExists = users.findByUsername(user.getUsername());
		if(userExists != null ) {
			if(userExists.getPassword().equals(user.getPassword()) && !userExists.isDeleted() && !userExists.getIsBlocked()) {
				if(userExists.getRole() == Role.customer) {
					setStartAndEndCheck(userExists);
				}
				return userExists;
			}
		}
		return null;
	}
	
	private void setStartAndEndCheck(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> allCustomers = customers.load();
		Customer customer = new Customer();
		int index = 0;
		for(int i = 0; i < allCustomers.size(); i++) {
			if(allCustomers.get(i).getUsername().equals(user.getUsername())) {
				customer = allCustomers.get(i);
				index = i;
			}
		}
		Date now = new Date();
		if(customer.getEndCheck().compareTo(now) < 0 || customer.getEndCheck().compareTo(now) == 0) {
			customer.setStartCheck(now);
			customer.setEndCheck(addMonth(now, 1));
			customer.setActions(0);
			allCustomers.remove(index);
			allCustomers.add(customer);
			for (Customer c : allCustomers) {
				customers.save(c);
			}
		}
	}
	
	public User editUser(User oldUser, User newUser) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<User> userList = users.load();
		for (int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getUsername().equals(oldUser.getUsername())) {
				userList.remove(i);
			}
		}
		userList.add(newUser);
		users.emptyFile();
		for (User user : userList) {
			users.save(user);
		}
		if(oldUser.getRole() == Role.customer) {
			customerService.editProfile(oldUser, newUser);
		} else if(oldUser.getRole() == Role.deliverer) {
			delivererService.editProfile(oldUser, newUser);
		} else if(oldUser.getRole() == Role.manager) {
			managerService.editProfile(oldUser, newUser);
		}
		return null;
	}
	
	public void deleteUser(String username) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<User> userList = users.load();
		User user = new User();
		for (int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getUsername().equals(username)) {
				user = userList.get(i);
				userList.remove(i);
			}
		}
		
		if(user.getRole() == Role.customer) {
			customerService.deleteCustomer(username);
		} else if(user.getRole() == Role.manager) {
			managerService.deleteManager(username);
		} else if(user.getRole() == Role.deliverer) {
			delivererService.deleteDeliverer(username);
		}
		
		user.setDeleted(true);
		userList.add(user);
		users.emptyFile();
		for (User u : userList) {
			users.save(u);
		}
		
	}
	
	public boolean isUserDeleted(String username) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<User> userList = users.load();
		for (User user : userList) {
			if(user.getUsername().equals(username)) {
				return user.isDeleted();
			}
		}
		return false;
	}
	
	public boolean isUserBlocked(String username) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<User> userList = users.load();
		for (User user : userList) {
			if(user.getUsername().equals(username)) {
				return user.getIsBlocked();
			}
		}
		return false;
	}
	
	public void blockUser(String username) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<User> userList = users.load();
		User user = new User();
		for (int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getUsername().equals(username)) {
				user = userList.get(i);
				userList.remove(i);
			}
		}
		
		if(user.getRole() == Role.customer) {
			customerService.blockCustomer(username);
		}
		
		user.setIsBlocked(true);
		userList.add(user);
		users.emptyFile();
		for (User u : userList) {
			users.save(u);
		}
		
		
	}
	
	public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }
	
	public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }
	
}
