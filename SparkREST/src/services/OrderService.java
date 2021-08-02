package services;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import beans.Customer;
import beans.CustomerType;
import beans.Deliverer;
import beans.Item;
import beans.Manager;
import beans.Notification;
import beans.Order;
import beans.OrderStatus;
import beans.Request;
import beans.Restaurant;
import beans.ShoppingCart;
import beans.TypeName;
import beans.User;
import dao.Customers;
import dao.Deliverers;
import dao.Managers;
import dao.Notifications;
import dao.Orders;
import dao.Requests;
import dto.FilterDTO;
import dto.RequestDTO;
import dto.SearchDTO;
import sort.SortByDateAscending;
import sort.SortByDateDescending;
import sort.SortByPriceAscending;
import sort.SortByPriceDescending;
import sort.SortByRestaurantNameAscending;
import sort.SortByRestaurantNameDescending;

public class OrderService {
	private Orders orders = new Orders();
	private Customers customers = new Customers();
	private Managers managers = new Managers();
	private Requests requests = new Requests();
	private Deliverers deliverers = new Deliverers();

	private Notifications notifications = new Notifications();

	private static RestaurantService restaurantService = new RestaurantService();


	public Collection<Order> getOrders() throws JsonGenerationException, JsonMappingException, IOException {
		return orders.load();
	}

	public void createOrder(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> customerList = customers.load();
		Customer customer = new Customer();
		ShoppingCart shoppingCart = new ShoppingCart();
		for(int i = 0; i < customerList.size(); i++) {
			if(user.getUsername().equals(customerList.get(i).getUsername())) {
				customer = customerList.get(i);
				shoppingCart = customer.getShoppingCart();
				customerList.remove(i);
			}
		}
		Order newOrder = new Order(generateId(),
				shoppingCart.getItems(),
				getRestaurant(shoppingCart),
				getRestaurantLogo(shoppingCart),
				new Date(),
				calculatePriceWithDiscount(customer, shoppingCart.getPrice()),
				customer.getUsername(),
				OrderStatus.processing);
		orders.save(newOrder);
		customer.setShoppingCart(new ShoppingCart());
		customer.getOrders().add(newOrder);
		customer.setPoints(setPoints(newOrder.getPrice(), customer.getPoints()));
		customer.setCustomerType(updateCustomerType(customer.getCustomerType(), customer.getPoints()));
		customerList.add(customer);
		customers.emptyFile();
		for(Customer c : customerList) {
			customers.save(c);
		}
	}

	private double calculatePriceWithDiscount(Customer customer, double price) {
		double newPrice = 0.0;
		newPrice = price * ((100.0 - customer.getCustomerType().getDiscount()) / 100);
		return newPrice;
	}

	private CustomerType updateCustomerType(CustomerType ct, double points) {
		CustomerType customerType = new CustomerType();
		if(points >= 1500.0 && points < 3000.0) {
			customerType.setTypeName(TypeName.bronze);
			customerType.setDiscount(5.0);
			customerType.setRequiredPoints(3000.0 - points);
		} else if(points >= 3000.0 && points < 4500.0) {
			customerType.setTypeName(TypeName.silver);
			customerType.setDiscount(10.0);
			customerType.setRequiredPoints(4500.0 - points);
		} else if(points >= 4500.0) {
			customerType.setTypeName(TypeName.gold);
			customerType.setDiscount(15);
			customerType.setRequiredPoints(0.0);
		} else {
			customerType.setTypeName(TypeName.steel);
			customerType.setDiscount(0.0);
			customerType.setRequiredPoints(1500.0 - points);
		}
		return customerType;
	}

	private String generateId() {
		String id = RandomStringUtils.randomAlphanumeric(10);
		return id;
	}

	private String getRestaurant(ShoppingCart shoppingCart) {
		Restaurant restaurant = new Restaurant();
		ArrayList<Item> itemList = shoppingCart.getItems();
		restaurant = itemList.get(0).getRestaurant();
		return restaurant.getName();
	}

	private String getRestaurantLogo(ShoppingCart shoppingCart) {
		Restaurant restaurant = new Restaurant();
		ArrayList<Item> itemList = shoppingCart.getItems();
		restaurant = itemList.get(0).getRestaurant();
		return restaurant.getImgPath();
	}

	public boolean isCartEmpty(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> customerList = customers.load();
		ShoppingCart shoppingCart = new ShoppingCart();
		for(int i = 0; i < customerList.size(); i++) {
			if(user.getUsername().equals(customerList.get(i).getUsername())) {
				shoppingCart = customerList.get(i).getShoppingCart();
			}
		}
		return shoppingCart.getItems().isEmpty();
	}

	private double setPoints(double price, double oldPoints) {
		return oldPoints + (price/1000*133);
	}

	public ArrayList<Order> getCustomerOrders(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> customerList = customers.load();
		Customer customer = new Customer();
		for(int i = 0; i < customerList.size(); i++) {
			if(customerList.get(i).getUsername().equals(user.getUsername())) {
				customer = customerList.get(i);
			}
		}

		return customer.getOrders();
	}

	public void cancelOrder(String id) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> orderList = orders.load();
		String username = "";
		double price = 0.0;
		for(int i = 0; i < orderList.size(); i++) {
			if(orderList.get(i).getId().equals(id)) {
				username = orderList.get(i).getCustomer();
				price = orderList.get(i).getPrice();
				orderList.remove(i);
			}
		}
		orders.emptyFile();
		for (Order order : orderList) {
			orders.save(order);
		}

		deleteOrderFromCustomer(username, id, price);

	}

	private void deleteOrderFromCustomer(String username, String id, double orderPrice) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> customerList = customers.load();
		Customer customer = new Customer();
		for(int i = 0; i < customerList.size(); i++) {
			if(username.equals(customerList.get(i).getUsername())) {
				customer = customerList.get(i);
				customerList.remove(i);
			}
		}

		ArrayList<Order> orderList = customer.getOrders();
		for(int i = 0; i < orderList.size(); i++) {
			if(orderList.get(i).getId().equals(id)) {
				orderList.remove(i);
				break;
			}
		}
		double oldPoints = customer.getPoints();
		int oldActions = customer.getActions();
		customer.setPoints(oldPoints - lostPoints(oldPoints, orderPrice));
		customer.setActions(oldActions+1);
		customer.setCustomerType(updateCustomerType(customer.getCustomerType(), customer.getPoints()));
		customer.setOrders(orderList);
		customerList.add(customer);
		customers.emptyFile();
		for (Customer cus : customerList) {
			customers.save(cus);
		}
	}

	private double lostPoints(double oldPoints, double price) {
		return price/1000*133*4;
	}

	public ArrayList<Order> getCustomerUndeliveredOrders(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Customer> customerList = customers.load();
		ArrayList<Order> undeliveredOrders = new ArrayList<Order>();
		ArrayList<Order> allOrders = new ArrayList<Order>();
		Customer customer = new Customer();
		for(int i = 0; i < customerList.size(); i++) {
			if(customerList.get(i).getUsername().equals(user.getUsername())) {
				customer = customerList.get(i);
				allOrders = customer.getOrders();
			}
		}

		for(int i = 0; i < allOrders.size(); i++) {
			if(allOrders.get(i).getStatus() != OrderStatus.canceled && allOrders.get(i).getStatus() != OrderStatus.delivered ) {
				undeliveredOrders.add(allOrders.get(i));
			}
		}

		return undeliveredOrders;
	}

	public ArrayList<Order> getManagerOrders(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> managerOrders = new ArrayList<Order>();
		ArrayList<Manager> managerList = managers.load();
		Manager manager = new Manager();
		for(int i = 0; i < managerList.size(); i++) {
			if(managerList.get(i).getUsername().equals(user.getUsername())) {
				manager = managerList.get(i);
			}
		}

		Restaurant restaurant = manager.getRestaurant();
		ArrayList<Order> orderList = orders.load();
		for(int i = 0; i < orderList.size(); i++) {
			if(orderList.get(i).getRestaurant().equals(restaurant.getName())) {
				managerOrders.add(orderList.get(i));
			}
		}
		return managerOrders;
	}

	public void changeOrderStatus(String id, OrderStatus status) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> orderList = orders.load();
		Order order = new Order();
		for(int i = 0; i < orderList.size(); i++) {
			if(orderList.get(i).getId().equals(id)) {
				order = orderList.get(i);
				orderList.remove(i);
			}
		}

		order.setStatus(status);
		orderList.add(order);
		orders.emptyFile();
		for (Order order1 : orderList) {
			orders.save(order1);
		}

		changeOrderStatusCustomer(status, order.getId(), order.getCustomer());
	}

	private void changeOrderStatusCustomer(OrderStatus status, String orderId, String username) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayList<Customer> customerList = customers.load();
		Customer customer = new Customer();
		for(int i = 0; i < customerList.size(); i++) {
			if(customerList.get(i).getUsername().equals(username)) {
				customer = customerList.get(i);
				customerList.remove(i);
			}
		}
		ArrayList<Order> customerOrders = customer.getOrders();
		Order order = new Order();
		for(int i = 0; i < customerOrders.size(); i++) {
			if(customerOrders.get(i).getId().equals(orderId)) {
				order = customerOrders.get(i);
				customerOrders.remove(i);
			}
		}

		order.setStatus(status);
		customerOrders.add(order);
		customer.setOrders(customerOrders);
		customerList.add(customer);
		customers.emptyFile();
		for (Customer c : customerList) {
			customers.save(c);
		}
	}

	private void changeOrderStatusRequest(String deliverer, String orderId) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Request> allRequests = requests.load();
		Request request = new Request();
		for(int i = 0; i < allRequests.size(); i++) {
			if(allRequests.get(i).getDeliverer().equals(deliverer) && allRequests.get(i).getOrder().getId().equals(orderId)) {
				request = allRequests.get(i);
				allRequests.remove(i);
			}
		}
		request.getOrder().setStatus(OrderStatus.delivered);
		allRequests.add(request);
		requests.emptyFile();
		for (Request r : allRequests) {
			requests.save(r);
		}
	}

	public void changeOrderStatusDeliverer(Order order, User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Deliverer> allDeliverers = deliverers.load();
		Deliverer deliverer = new Deliverer();
		for(int i = 0; i < allDeliverers.size(); i++) {
			if(allDeliverers.get(i).getUsername().equals(user.getUsername())) {
				deliverer = allDeliverers.get(i);
				allDeliverers.remove(i);
			}
		}

		ArrayList<Order> delivererOrders = deliverer.getOrders();
		for(int i = 0; i < delivererOrders.size(); i++) {
			if(order.getId().equals(delivererOrders.get(i).getId())) {
				delivererOrders.remove(i);
			}
		}
		order.setStatus(OrderStatus.delivered);
		delivererOrders.add(order);
		allDeliverers.add(deliverer);
		deliverers.emptyFile();
		for (Deliverer d : allDeliverers) {
			deliverers.save(d);
		}

		changeOrderStatus(order.getId(), OrderStatus.delivered);
		changeOrderStatusRequest(deliverer.getUsername(), order.getId());
	}

	public ArrayList<Order> getWaitingOrders() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> allOrders = orders.load();
		ArrayList<Order> waitingOrders = new ArrayList<Order>();
		for(int i = 0; i < allOrders.size(); i++) {
			if(allOrders.get(i).getStatus() == OrderStatus.waiting) {
				waitingOrders.add(allOrders.get(i));
			}
		}
		return waitingOrders;
	}

	public Request newRequest(Request request) throws JsonMappingException, JsonGenerationException, IOException {
		ArrayList<Request> allRequests = requests.load();
		for (int i = 0; i < allRequests.size(); i++) {
			if(allRequests.get(i).getDeliverer().equals(request.getDeliverer()) &&
					allRequests.get(i).getOrder().getId().equals(request.getOrder().getId())) {
				return null;
			}
		}
		requests.save(request);
		return request;
	}

	public ArrayList<Request> getManagerRequests(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Request> allRequests = requests.load();
		ArrayList<Manager> allManagers = managers.load();
		ArrayList<Request> managerRequests = new ArrayList<Request>();
		Manager manager = new Manager();
		if(allRequests.size() == 0) {
			return managerRequests;
		}
		for(Manager m : allManagers) {
			if(user.getUsername().equals(m.getUsername())) {
				manager = m;
			}
		}
		for(int i = 0; i < allRequests.size(); i++) {
			if(allRequests.get(i).getOrder().getRestaurant().equals(manager.getRestaurant().getName()) && allRequests.get(i).isDeleted() == false) {
				managerRequests.add(allRequests.get(i));
			}
		}
		return managerRequests;
	}

	public ArrayList<RequestDTO> getManagerRequestsDTO(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<RequestDTO> list = new ArrayList<RequestDTO>();
		RequestDTO requestDTO = new RequestDTO();
		ArrayList<Request> managerReq = getManagerRequests(user);
		if(managerReq.size() == 0) {
			return list;
		}
		for (Request request : managerReq) {
			requestDTO = new RequestDTO(request.getOrder().getRestaurant(), request.getOrder().getPrice(), request.getDeliverer(), request.getOrder().getId());
			list.add(requestDTO);
		}
		return list;
	}

	public void acceptRequest(RequestDTO dto) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Request> requestList = requests.load();
		ArrayList<Deliverer> delivererList = deliverers.load();
		ArrayList<Customer> customerList = customers.load();
		ArrayList<Order> orderList = orders.load();
		Request validRequest = new Request();
		Deliverer deliverer = new Deliverer();
		Customer customer = new Customer();
		for(int i = 0; i < requestList.size(); i++) {
			if(requestList.get(i).getDeliverer().equals(dto.getDeliverer()) && requestList.get(i).getOrder().getId().equals(dto.getOrderId())) {
				validRequest = requestList.get(i);
				validRequest.setAccepted(true);
				validRequest.setDeleted(true);
				requestList.remove(i);
				break;
			}
		}

		for(int i = 0; i < delivererList.size(); i++) {
			if(delivererList.get(i).getUsername().equals(dto.getDeliverer())) {
				deliverer = delivererList.get(i);
				delivererList.remove(i);
				break;
			}
		}

		Order order = validRequest.getOrder();
		order.setStatus(OrderStatus.transport);
		deliverer.getOrders().add(order);

		for(int i = 0; i < orderList.size(); i++) {
			if(orderList.get(i).getId().equals(order.getId())) {
				orderList.remove(i);
				orderList.add(order);
				break;
			}
		}

		for(int i = 0; i < customerList.size(); i++) {
			if(customerList.get(i).getUsername().equals(order.getCustomer())) {
				customer = customerList.get(i);
				customerList.remove(i);
				break;
			}
		}

		ArrayList<Order> customerOrders = customer.getOrders();
		for(int i = 0; i < customerOrders.size(); i++) {
			if(order.getId().equals(customerOrders.get(i).getId())) {
				customerOrders.remove(i);
				break;
			}
		}

		validRequest.setOrder(order);
		requestList.add(validRequest);

		customerOrders.add(order);
		customer.setOrders(customerOrders);

		delivererList.add(deliverer);
		customerList.add(customer);

		requests.emptyFile();
		deliverers.emptyFile();
		customers.emptyFile();
		orders.emptyFile();

		for (Request r : requestList) {
			requests.save(r);
		}

		for (Deliverer d : delivererList) {
			deliverers.save(d);
		}

		for(Customer c : customerList) {
			customers.save(c);
		}

		for (Order o : orderList) {
			orders.save(o);
		}

		Notification notification = new Notification(deliverer.getUsername(), order.getId(), "Vaš zahtev je prihvaćen!", false);
		notifications.save(notification);
	}

	public void rejectRequest(RequestDTO dto) throws JsonMappingException, JsonGenerationException, IOException {
		Notification notification = new Notification(dto.getDeliverer(), dto.getOrderId(), "Vaš zahtev je odbijen!", false);
		notifications.save(notification);
		ArrayList<Request> allRequests = requests.load();
		Request request = new Request();
		for(int i = 0; i < allRequests.size(); i++) {
			if(allRequests.get(i).getDeliverer().equals(dto.getDeliverer()) &&
					allRequests.get(i).getOrder().getId().equals(dto.getOrderId())) {
				request = allRequests.get(i);
				allRequests.remove(i);
			}
		}
		request.setDeleted(true);
		allRequests.add(request);
		requests.emptyFile();
		for (Request r : allRequests) {
			requests.save(r);
		}
	}

	public ArrayList<Order> sortByPrice(FilterDTO fromJson) {
		ArrayList<Order> found = fromJson.getOrders();

		if (fromJson.isAscending()) {
			Collections.sort(found, new SortByPriceAscending());
		} else if (!fromJson.isAscending()) {
			Collections.sort(found, new SortByPriceDescending());
		}

		return found;
	}

	public ArrayList<Order> sortByDate(FilterDTO fromJson) {
		ArrayList<Order> found = fromJson.getOrders();

		if (fromJson.isAscending()) {
			Collections.sort(found, new SortByDateAscending());
		} else if (!fromJson.isAscending()) {
			Collections.sort(found, new SortByDateDescending());
		}

		return found;
	}

	public ArrayList<Order> sortByRestaurantName(FilterDTO fromJson) {
		ArrayList<Order> found = fromJson.getOrders();

		if (fromJson.isAscending()) {
			Collections.sort(found, new SortByRestaurantNameAscending());
		} else if (!fromJson.isAscending()) {
			Collections.sort(found, new SortByRestaurantNameDescending());
		}

		return found;
	}

	public ArrayList<Order> findOrders(SearchDTO fromJson) throws ParseException {
		ArrayList<Order> foundOrders = new ArrayList<Order>();
		ArrayList<Order> userOrders = fromJson.getOrders();
		boolean canAdd = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for (Order o : userOrders) {

			if (!fromJson.getRestaurantName().isEmpty()) {
				if (fromJson.getRestaurantName().equals(o.getRestaurant())) {
					canAdd = true;
				} else {
					continue;
				}
			}

			if (fromJson.getStartPrice() != 0 && fromJson.getEndPrice() != 0) {
				if (o.getPrice() <= fromJson.getEndPrice() && o.getPrice() >= fromJson.getStartPrice()) {
					canAdd = true;
				} else {
					continue;
				}
			}

			if (!fromJson.getStartDate().isEmpty() && !fromJson.getEndDate().isEmpty()) {
				Date startDate = (Date) format.parse(fromJson.getStartDate());
				Date endDate = (Date) format.parse(fromJson.getEndDate());
				if (o.getDateAndTime().before(endDate) && o.getDateAndTime().after(startDate)) {
					canAdd = true;
				} else {
					continue;
				}
			}

			if (canAdd) {
				foundOrders.add(o);
			}
		}

		return foundOrders;
	}

	public ArrayList<Order> findOrdersForManager(SearchDTO fromJson) throws ParseException {
		ArrayList<Order> foundOrders = new ArrayList<Order>();
		ArrayList<Order> userOrders = fromJson.getOrders();
		boolean canAdd = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for (Order o : userOrders) {

			if (fromJson.getStartPrice() != 0 && fromJson.getEndPrice() != 0) {
				if (o.getPrice() <= fromJson.getEndPrice() && o.getPrice() >= fromJson.getStartPrice()) {
					canAdd = true;
				} else {
					continue;
				}
			}

			if (!fromJson.getStartDate().isEmpty() && !fromJson.getEndDate().isEmpty()) {
				Date startDate = (Date) format.parse(fromJson.getStartDate());
				Date endDate = (Date) format.parse(fromJson.getEndDate());
				if (o.getDateAndTime().before(endDate) && o.getDateAndTime().after(startDate)) {
					canAdd = true;
				} else {
					continue;
				}
			}

			if (canAdd) {
				foundOrders.add(o);
			}
		}

		return foundOrders;
	}

	public ArrayList<Order> filterOrders(FilterDTO fromJson) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> filtered = new ArrayList<Order>();
		ArrayList<Order> found = fromJson.getOrders();
		boolean canAdd = false;

		for (Order o : found) {
			if (fromJson.getType() != null) {
				if (fromJson.getOrderStatus() != null) {
					for (String s : fromJson.getOrderStatus()) {
						for (String t : fromJson.getType()) {
							if (s.equals(o.getStatus().toString()) && t.equals(restaurantService.getRestaurantType(o.getRestaurant())) ) {
								//filtered.add(o);
								canAdd = true;
							}
						}
					}
				} else {
					for (String s : fromJson.getType()) {
						if (s.equals(restaurantService.getRestaurantType(o.getRestaurant()))) {
							//filtered.add(o);
							canAdd = true;
						}
					}
				}

				if (canAdd) {
					filtered.add(o);
				}

			} else {
				if (fromJson.getOrderStatus() != null) {
					for (String s : fromJson.getOrderStatus()) {
						if (s.equals(o.getStatus().toString())) {
							filtered.add(o);
							canAdd = true;
						}
					}
				} else {
					continue;
				}

				if (canAdd) {
					filtered.add(o);
				}
			}

		}
		return filtered;
	}

	public ArrayList<Order> filterByStatus(FilterDTO fromJson) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> filtered = new ArrayList<Order>();
		ArrayList<Order> found = fromJson.getOrders();

		for (Order o : found) {
			if (fromJson.getOrderStatus() != null) {
				for (String s : fromJson.getOrderStatus()) {
					if (s.equals(o.getStatus().toString())) {
						filtered.add(o);
					}
				}
			}
		}
		return filtered;
	}

	public ArrayList<Order> filterByType(FilterDTO fromJson) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Order> filtered = new ArrayList<Order>();
		ArrayList<Order> found = fromJson.getOrders();

		for (Order o : found) {
			if (fromJson.getType() != null) {
				for (String s : fromJson.getType()) {
					if (s.equals(restaurantService.getRestaurantType(o.getRestaurant()))) {
						filtered.add(o);
					}
				}
			}
		}
		return filtered;
	}
}
