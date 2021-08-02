package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;
import static spark.Spark.staticFiles;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;

import beans.Comment;
import beans.Customer;
import beans.Gender;
import beans.Item;
import beans.Manager;
import beans.Order;
import beans.OrderStatus;
import beans.Request;
import beans.Restaurant;
import beans.Role;
import beans.User;
import dao.Comments;
import dto.EditCartDTO;
import dto.FilterDTO;
import dto.ItemDTO;
import dto.LoginDTO;
import dto.ManagerDTO;
import dto.RequestDTO;
import dto.RestaurantDTO;
import dto.SearchDTO;
import dto.UserDTO;
import services.CustomerService;
import services.DelivererService;
import services.ItemService;
import services.ManagerService;
import services.OrderService;
import services.RestaurantService;
import services.UserService;
import spark.Session;

public class SparkAppMain {

	private static Gson g = new Gson();
	private static RestaurantService restaurantService = new RestaurantService();
	private static UserService userService = new UserService();
	private static CustomerService customerService = new CustomerService();
	private static ManagerService managerService = new ManagerService();
	private static DelivererService delivererService = new DelivererService();
	private static ItemService itemService = new ItemService();
	private static Comments comments = new Comments();
	private static OrderService orderService = new OrderService();

	public static void main(String[] args) throws Exception {
		port(8080);

		staticFiles.externalLocation(new File("./static").getCanonicalPath());

		get("rest/restaurants/", (req, res) -> {
			res.type("application/json");
			return g.toJson(restaurantService.getRestaurantsOC());
		});

		post("/rest/register", (req, res) -> {
			res.type("application/json");
			UserDTO userDTO = g.fromJson(req.body(), UserDTO.class);
			Gender gender;
			Date date;
			if(userDTO.getGender().equals("male")) {
				gender = Gender.male;
			} else {
				gender = Gender.female;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date) format.parse(userDTO.getDateOfBirth());
			User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getName(), userDTO.getSurname(), gender, date, Role.customer, false, false);
			userService.addUser(user);
			Session session = req.session(true);
			User isLoggedIn = session.attribute("user");
			if(isLoggedIn == null) {
				session.attribute("user", user);
			}
			return "SUCCESS";
		});


		post("/rest/addEmployee", (req, res) -> {
			res.type("application/json");
			UserDTO userDTO = g.fromJson(req.body(), UserDTO.class);
			Gender gender;
			Date date;
			Role role;
			if(userDTO.getGender().equals("male")) {
				gender = Gender.male;
			} else {
				gender = Gender.female;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date) format.parse(userDTO.getDateOfBirth());
			if(userDTO.getRole().equals("manager")) {
				role = Role.manager;
			} else {
				role = Role.deliverer;
			}
			User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getName(), userDTO.getSurname(), gender, date, role, false, false);
			userService.addUser(user);
			return "SUCCESS";
		});



		post("/rest/addRestaurant", (req, res) -> {
			try {
				res.type("application/json");
				RestaurantDTO restaurantDTO = g.fromJson(req.body(), RestaurantDTO.class);
				Restaurant restaurant = new Restaurant(restaurantDTO.getName(), restaurantDTO.getType(), restaurantDTO.getItems(), restaurantDTO.getStatus(),
						restaurantDTO.getLocation(), restaurantDTO.getImgPath());
				restaurantService.addRestaurant(restaurant);
				return "SUCCESS";

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});

		post("/rest/login", (req, res) -> {
			res.type("application/json");
			LoginDTO loginDTO = g.fromJson(req.body(), LoginDTO.class);
			User user = userService.login(loginDTO);
			if(user != null) {
				Session session = req.session(true);
				User isLoggedIn = session.attribute("user");
				if(isLoggedIn == null) {
					session.attribute("user", user);
				}
			} else {
				return "";
			}
			return g.toJson(user);
		});

		get("/rest/isLogged", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			return g.toJson(user);
		});

		get("/rest/logout", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			if (session.attribute("user") != null) {
				session.invalidate();
			}
			return "SUCCESS";
		});

		get("/rest/restaurants/:name", (req, res) -> {
			res.type("application/json");
			String name = req.params("name");
			System.out.println(name);
			Restaurant restaurant = restaurantService.getRestaurantByName(name);
			restaurant.setItems(itemService.getItemsForRestaurant(name));
			return g.toJson(restaurant);
		});

		get("/rest/customers", (req, res) -> {
			res.type("application/json");
			return g.toJson(customerService.getCustomers());
		});

		get("/rest/managers", (req, res) -> {
			res.type("application/json");
			return g.toJson(managerService.getManagers());
		});

		get("/rest/deliverers", (req, res) -> {
			res.type("application/json");
			return g.toJson(delivererService.getDeliverers());
		});


		post ("/rest/addItem", (req, res) -> {
			try {
				res.type("application/json");
				ItemDTO itemDTO = g.fromJson(req.body(), ItemDTO.class);
				Item item = new Item(itemDTO.getName(), itemDTO.getPrice(), itemDTO.getType(), restaurantService.getRestaurantByName(itemDTO.getRestaurant().getName()),
						itemDTO.getAmount(), itemDTO.getDescription(), itemDTO.getImagePath());
				itemService.addItem(item);
				return "SUCCESS";
			} catch (Exception e) {
				 e.printStackTrace();
				 return "";
			}
		});

		put("/rest/editProfile", (req, res) ->{
			res.type("application/json");
			User newUser = g.fromJson(req.body(), User.class);
			Session session = req.session(true);
			User oldUser = session.attribute("user");
			userService.editUser(oldUser, newUser);
			session.invalidate();
			Session sessionNew = req.session(true);
			sessionNew.attribute("user", newUser);
			return "";

		});

		get("/rest/getComments", (req, res) -> {
			res.type("application/json");
			ArrayList<Comment> commentsFromFile = comments.load();
			return g.toJson(commentsFromFile);
		});


		post("/rest/addToCart", (req, res) -> {
			res.type("application/json");
			Item item = g.fromJson(req.body(), Item.class);
			Session session = req.session(true);
			User user = session.attribute("user");
			customerService.addItemToShoppingCart(user, item);
			return "";
		});

		post("/rest/restaurants/findRestaurants", (req, res) -> {
			res.type("application/json");
			System.out.println(req.body());
			System.out.println(restaurantService.findRestaurants(g.fromJson(req.body(), SearchDTO.class)));
			return g.toJson(restaurantService.findRestaurants(g.fromJson(req.body(), SearchDTO.class)));
		});

		get("/rest/getCustomer", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			Customer customer = customerService.getCustomer(user);
			if(customer == null) {
				return "ERROR";
			}
			return g.toJson(customer);
		});

		get("/rest/setShoppingCart", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			customerService.setShoppingCart(user);
			return "";
		});

		delete("/rest/removeFromCart/:name", (req, res) -> {
			res.type("application/json");
			String name = req.params("name");
			Item item = customerService.findItem(name);
			Session session = req.session(true);
			User user = session.attribute("user");
			customerService.removeFromCart(user, item);
			return "";
		});

		delete("/rest/deleteUser/:username", (req, res) ->{
			res.type("application/json");
			String username = req.params("username");
			userService.deleteUser(username);
			return "";
		});

		get("/rest/getCustomers", (req, res) -> {
			return "";
		});


		post("/rest/restaurants/filterRestaurants", (req, res) -> {
			res.type("application/json");
			System.out.println(req.body());
			System.out.println(restaurantService.filterRestaurantsByStatus(g.fromJson(req.body(), FilterDTO.class)));
			return g.toJson(restaurantService.filterRestaurantsByStatus(g.fromJson(req.body(), FilterDTO.class)));
		});

		post("/rest/restaurants/findByGrade", (req, res) -> {
			res.type("application/json");
			return g.toJson(restaurantService.getRestaurantsByGrade(g.fromJson(req.body(), SearchDTO.class)));
		});


		get("/rest/activeCustomers", (req, res) -> {
			res.type("application/json");
			return g.toJson(customerService.getActiveCustomers());
		});

		get("/rest/activeManagers", (req, res) -> {
			res.type("application/json");
			return g.toJson(managerService.getActiveManagers());
		});

		get("/rest/activeDeliverers", (req, res) -> {
			res.type("application/json");
			return g.toJson(delivererService.getActiveDeliverers());
		});

		get("/rest/isDeleted/:username", (req, res) -> {
			res.type("application/json");
			String username = req.params("username");
			if(userService.isUserDeleted(username) == true) {
				return "YES";
			} else {
				return "NO";
			}
		});

		put("/rest/editShoppingCart", (req, res) -> {
			res.type("application/json");
			EditCartDTO dto = g.fromJson(req.body(), EditCartDTO.class);
			Session session = req.session(true);
			User user = session.attribute("user");
			return g.toJson(customerService.editShoppingCart(user, dto).getPrice());
		});

		post("/rest/createOrder", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			orderService.createOrder(user);
			return "";
		});

		get("/rest/isCartEmpty", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			if(orderService.isCartEmpty(user)) {
				return "YES";
			} else {
				return "NO";
			}
		});

		post("/rest/restaurants/sortByName", (req, res) -> {
			res.type("application/json");
			return g.toJson(restaurantService.sortByName(g.fromJson(req.body(), FilterDTO.class)));
		});

		post("/rest/restaurants/sortByLocation", (req, res) -> {
			res.type("application/json");
			return g.toJson(restaurantService.sortByLocation(g.fromJson(req.body(), FilterDTO.class)));
		});

		post("/rest/restaurants/sortByGrade", (req, res) -> {
			res.type("application/json");
			System.out.println(req.body());
			System.out.println(restaurantService.sortByGrade(g.fromJson(req.body(), FilterDTO.class)));
			return g.toJson(restaurantService.sortByGrade(g.fromJson(req.body(), FilterDTO.class)));
		});

		get("/rest/getAvailableManagers", (req, res) -> {
			res.type("application/json");
			return g.toJson(managerService.getAvailableManagers());
		});

		post("/rest/addManagerToRestaurant", (req, res) -> {
			res.type("application/json");
			Manager fromJson = g.fromJson(req.body(), Manager.class);
			Manager fromFile = managerService.findByUsername(fromJson.getUsername());
			Manager newManager = new Manager(fromFile.getUsername(), fromFile.getPassword(), fromFile.getName(), fromFile.getSurname(),
					fromFile.getGender(), fromFile.getDateOfBirth(), fromFile.getRole(), fromFile.getIsBlocked(), fromFile.isDeleted(), fromJson.getRestaurant());
			managerService.removeManager(fromFile.getUsername());
			managerService.addManager(newManager);
			return "SUCCESS";
		});

		get("/rest/getCustomerOrders", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			return g.toJson(orderService.getCustomerOrders(user));
		});

		post("/rest/addNewManager", (req, res) -> {
			res.type("application/json");
			ManagerDTO fromJson = g.fromJson(req.body(), ManagerDTO.class);
			Date date;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date) format.parse(fromJson.getDateOfBirth());
			Restaurant restaurant = new Restaurant();
			Manager newManager = new Manager(fromJson.getUsername(), fromJson.getPassword(), fromJson.getName(), fromJson.getSurname(), fromJson.getGender(),
					date, fromJson.getRole(), fromJson.isBlocked(), fromJson.isDeleted(), restaurant);
			User user = new User(fromJson.getUsername(), fromJson.getPassword(), fromJson.getName(), fromJson.getSurname(), fromJson.getGender(), date,
					fromJson.getRole(), fromJson.isBlocked(), fromJson.isDeleted());
			managerService.addManager(newManager);
			userService.addUser(user);
			return "SUCCESS";
		});


		post("/rest/cancelOrder", (req, res) -> {
			res.type("application/json");
			String id = g.fromJson(req.body(), String.class);
			orderService.cancelOrder(id);
			return "";
		});
		
		get("/rest/getManagerRestaurant/:username", (req, res) -> {
			res.type("application/json");
			String username = req.params("username");
			return g.toJson(managerService.getManagerRestaurant(username));

		});
		
		put("/rest/editItem", (req, res) ->{
			res.type("application/json");
			ItemDTO fromJson = g.fromJson(req.body(), ItemDTO.class);
			Item oldItem = fromJson.getOldItem();
			Item newItem = new Item(fromJson.getName(), fromJson.getPrice(), fromJson.getType(), restaurantService.getRestaurantByName(fromJson.getRestaurant().getName()), fromJson.getAmount(),
					fromJson.getDescription(), fromJson.getImagePath());
			itemService.editItem(oldItem, newItem);
			return "";
		});
		
		get("/rest/getCustomerUndeliveredOrders", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			return g.toJson(orderService.getCustomerUndeliveredOrders(user));
		});
		
		get("/rest/getManagerOrders", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			return g.toJson(orderService.getManagerOrders(user));
		});
		
		post("/rest/changeStatusToPreparing", (req, res) -> {
			String id = g.fromJson(req.body(), String.class);
			orderService.changeOrderStatus(id, OrderStatus.preparing);
			return "";
		});
		
		post("/rest/changeStatusToWaiting", (req, res) -> {
			String id = g.fromJson(req.body(), String.class);
			orderService.changeOrderStatus(id, OrderStatus.waiting);
			return "";
		});
		
		post("/rest/changeStatusToDelivered", (req, res) -> {
			Order order = g.fromJson(req.body(), Order.class);
			Session session = req.session(true);
			User user = session.attribute("user");
			orderService.changeOrderStatusDeliverer(order, user);
			return "";
		});
		
		get("/rest/getWaitingOrders", (req, res) -> {
			res.type("application/json");
			return g.toJson(orderService.getWaitingOrders());
		});
		
		post("/rest/newRequest", (req, res) -> {
			res.type("application/json");
			Order order = g.fromJson(req.body(), Order.class);
			Session session = req.session(true);
			User user = session.attribute("user");
			Request request = new Request(user.getUsername(), order, false);
			Request r = new Request();
			r = orderService.newRequest(request);
			if(r == null) {
				return "Already sent!";
			} else {
				return "OK";
			}
			
		});
		
		get("/rest/getManagerRequests", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			if(orderService.getManagerRequests(user).size() == 0) {
				return "Empty request list";
			} else {
				return g.toJson(orderService.getManagerRequestsDTO(user));
			}
		});
		
		put("/rest/acceptRequest", (req, res) -> {
			res.type("application/json");
			RequestDTO dto = g.fromJson(req.body(), RequestDTO.class);
			orderService.acceptRequest(dto);
			return "";
		});
		
		put("/rest/rejectRequest", (req, res) -> {
			res.type("application/json");
			return "";
		});
		
		get("/rest/getDelivererOrders", (req, res) -> {
			res.type("application/json");
			Session session = req.session(true);
			User user = session.attribute("user");
			return g.toJson(delivererService.getDelivererOrders(user));
		});

	}
}
