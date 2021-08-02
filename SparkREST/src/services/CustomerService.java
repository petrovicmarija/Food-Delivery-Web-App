package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import beans.Customer;
import beans.Item;
import beans.Manager;
import beans.Order;
import beans.ShoppingCart;
import beans.User;
import dao.Customers;
import dao.Items;
import dao.Managers;
import dto.EditCartDTO;

public class CustomerService {
	private Customers customers = new Customers();
	private Items items = new Items();
	private Managers managers = new Managers();

	public Collection<Customer> getCustomers() throws JsonGenerationException, JsonMappingException, IOException {
		return this.customers.load();
	}

	 public Customer editProfile(User oldUser, User newUser) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> customerList = customers.load();
		Customer oldCustomer = new Customer();
		for (int i = 0; i < customerList.size(); i++) {
			if(customerList.get(i).getUsername().equals(oldUser.getUsername())) {
				customerList.remove(i);
				oldCustomer = new Customer(customerList.get(i).getUsername(),
						customerList.get(i).getPassword(),
						customerList.get(i).getName(),
						customerList.get(i).getSurname(),
						customerList.get(i).getGender(),
						customerList.get(i).getDateOfBirth(),
						customerList.get(i).getRole(),
						customerList.get(i).getIsBlocked(),
						customerList.get(i).isDeleted(),
						customerList.get(i).getOrders(),
						customerList.get(i).getShoppingCart(),
						customerList.get(i).getPoints(),
						customerList.get(i).getCustomerType(),
						customerList.get(i).getStartCheck(),
						customerList.get(i).getEndCheck(),
						customerList.get(i).getActions());
				break;
			}
		}
		Customer newCustomer = new Customer(newUser.getUsername(),
				newUser.getPassword(),
				newUser.getName(),
				newUser.getSurname(),
				newUser.getGender(),
				newUser.getDateOfBirth(),
				newUser.getRole(),
				newUser.getIsBlocked(),
				newUser.isDeleted(),
				oldCustomer.getOrders(),
				oldCustomer.getShoppingCart(),
				oldCustomer.getPoints(),
				oldCustomer.getCustomerType(),
				oldCustomer.getStartCheck(),
				oldCustomer.getEndCheck(),
				oldCustomer.getActions());
		customerList.add(newCustomer);
		customers.emptyFile();
		for(Customer customer : customerList) {
			customers.save(customer);
		}
		return null;
	}

	 public void addItemToShoppingCart(User user, Item item) throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> customerList = customers.load();
		 ArrayList<Item> items = new ArrayList<Item>();
		 Customer newCustomer = new Customer();
		 int brojac = 0;

		 for (int i = 0; i < customerList.size(); i++) {
			if(user.getUsername().equals(customerList.get(i).getUsername())) {
				newCustomer = customerList.get(i);
				if(newCustomer.getShoppingCart().getItems() == null) {
					newCustomer.getShoppingCart().setItems(new ArrayList<Item>());
				}
				items = newCustomer.getShoppingCart().getItems();
				for(int j = 0; j < items.size(); j++) {
					if(items.get(j).getName().equals(item.getName())) {
						brojac++;
						double amount = item.getAmount();
						amount += items.get(j).getAmount();
						item.setAmount(amount);
						items.remove(j);
						items.add(item);
					}
				}
				if(brojac == 0) {
					items.add(item);
				}
				newCustomer.getShoppingCart().setItems(items);
				customerList.remove(i);
				customerList.add(newCustomer);
			}
		 }
		 customers.emptyFile();
		 for (Customer customer : customerList) {
			customers.save(customer);
		}
		 setShoppingCart(user);
	 }

	 public void setShoppingCart(User user) throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> customerList = customers.load();
		 Customer customer = new Customer();
		 for (int i = 0; i < customerList.size(); i++) {
			 if(customerList.get(i).getUsername().equals(user.getUsername())) {
				 customer = customerList.get(i);
				 customerList.remove(i);
			 }
		 }
		 double totalPrice = 0.0;
		 for (Item item : customer.getShoppingCart().getItems()) {
			totalPrice += item.getPrice() * item.getAmount();
		 }
		 customer.getShoppingCart().setPrice(totalPrice);
		 customerList.add(customer);
		 customers.emptyFile();
		 for (Customer customer2 : customerList) {
			customers.save(customer2);
		}
	 }

	 public Customer getCustomer(User user) throws JsonGenerationException, JsonMappingException, IOException  {
		ArrayList<Customer> customerList = customers.load();
		for(int i = 0; i < customerList.size(); i++) {
			if(user.getUsername().equals(customerList.get(i).getUsername())) {
				return customerList.get(i);
			}
		}
		return null;
	}

	 public void removeFromCart(User user, Item item) throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> customerList = customers.load();
		 Customer customer = new Customer();
		 ArrayList<Item> items =  new ArrayList<Item>();
		 for(int i  = 0; i < customerList.size(); i++) {
			 if(user.getUsername().equals(customerList.get(i).getUsername())) {
				 customer = customerList.get(i);
				 customerList.remove(i);
			 }
		 }

		 items = customer.getShoppingCart().getItems();
		 for(int i = 0; i < items.size(); i++) {
			 if(item.getName().equals(items.get(i).getName())) {
				 items.remove(i);
			 }
		 }

		 customer.getShoppingCart().setItems(items);
		 customerList.add(customer);
		 customers.emptyFile();
		 for (Customer customer1 : customerList) {
			customers.save(customer1);
		}

		 setShoppingCart(user);
	 }

	 public Item findItem(String name) throws JsonGenerationException, JsonMappingException, IOException {
		 Item item = new Item();
		 ArrayList<Item> itemList = items.load();
		 for (Item item2 : itemList) {
			if(item2.getName().equals(name)) {
				item = item2;
			}
		}
		 return item;
	 }

	 public void deleteCustomer(String username) throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> customerList = customers.load();
		 Customer customer = new Customer();
		 for(int i = 0; i < customerList.size(); i++) {
			 if(customerList.get(i).getUsername().equals(username)) {
				 customer = customerList.get(i);
				 customerList.remove(i);
			 }
		 }
		 customer.setDeleted(true);
		 customerList.add(customer);
		 customers.emptyFile();
		 for (Customer c : customerList) {
			customers.save(c);
		}
	 }

	 public void blockCustomer(String username) throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> customerList = customers.load();
		 Customer customer = new Customer();
		 for(int i = 0; i < customerList.size(); i++) {
			 if(customerList.get(i).getUsername().equals(username)) {
				 customer = customerList.get(i);
				 customerList.remove(i);
			 }
		 }
		 customer.setIsBlocked(true);
		 customerList.add(customer);
		 customers.emptyFile();
		 for (Customer c : customerList) {
			customers.save(c);
		}
	 }

	 public ArrayList<Customer> getActiveCustomers() throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> activeCustomers = new ArrayList<Customer>();
		 ArrayList<Customer> allCustomers = customers.load();
		 for (int i = 0; i < allCustomers.size(); i++) {
			if(allCustomers.get(i).isDeleted() == false) {
				activeCustomers.add(allCustomers.get(i));
			}
		}
		 return activeCustomers;
	 }

	 public ShoppingCart editShoppingCart(User user, EditCartDTO editDTO) throws JsonGenerationException, JsonMappingException, IOException {
		 Customer customer = new Customer();
		 ArrayList<Customer> customerList = customers.load();
		 ArrayList<Item> items = new ArrayList<Item>();
		 Item item = new Item();
		 for(int i = 0; i < customerList.size(); i++) {
			 if(customerList.get(i).getUsername().equals(user.getUsername())) {
				 customer = customerList.get(i);
				 customerList.remove(i);
			 }
		 }
		 items = customer.getShoppingCart().getItems();
		 for(int i = 0; i < items.size(); i++) {
			 if(items.get(i).getName().equals(editDTO.getName())) {
				 item = items.get(i);
				 items.remove(i);
			 }
		 }
		 item.setAmount(editDTO.getAmount());
		 items.add(item);
		 customer.getShoppingCart().setItems(items);
		 customerList.add(customer);
		 customers.emptyFile();
		 for (Customer c : customerList) {
			customers.save(c);
		 }
		 setShoppingCart(user);
		 return customer.getShoppingCart();
	 }


	 public ArrayList<Customer> getSuspiciousCustomers() throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> allCustomers = customers.load();
		 ArrayList<Customer> suspiciousCustomers = new ArrayList<Customer>();
		 for(int i = 0; i < allCustomers.size(); i++) {
			 if(allCustomers.get(i).getActions() >= 5 &&
					 !allCustomers.get(i).getIsBlocked() &&
					 !allCustomers.get(i).isDeleted()) {
				 suspiciousCustomers.add(allCustomers.get(i));
			 }
		 }
		 return suspiciousCustomers;
	 }

	 public ArrayList<Customer> getCustomersForManager(User user) throws JsonGenerationException, JsonMappingException, IOException {
		 ArrayList<Customer> allCustomers = customers.load();
		 ArrayList<Manager> allManagers = managers.load();
		 Manager manager = new Manager();
		 ArrayList<Customer> customersForManager = new ArrayList<Customer>();
		 ArrayList<Order> customerOrders = new ArrayList<Order>();
		 for(int i = 0; i < allManagers.size(); i++) {
			 if(user.getUsername().equals(allManagers.get(i).getUsername())) {
				 manager = allManagers.get(i);
			 }
		 }

		 for(int i = 0; i < allCustomers.size(); i++) {
			 customerOrders = allCustomers.get(i).getOrders();
			 for(int j = 0; j < customerOrders.size(); j++) {
				 if(customerOrders.get(j).getRestaurant().equals(manager.getRestaurant().getName())) {
					 customersForManager.add(allCustomers.get(i));
					 break;
				 }
			 }
		 }
		 return customersForManager;
	 }

	 public Customer findCustomer(String username) throws JsonGenerationException, JsonMappingException, IOException {
		 Customer found = new Customer();
		 for (Customer c : this.customers.load()) {
			 if (c.getUsername().equals(username)) {
				 found = c;
			 }
		 }
		 return found;

	 }

}
