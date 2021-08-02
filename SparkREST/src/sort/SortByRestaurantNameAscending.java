package sort;

import java.util.Comparator;

import beans.Order;
import beans.Restaurant;

public class SortByRestaurantNameAscending implements Comparator<Order> {

	@Override
	public int compare(Order o1, Order o2) {
		return o1.getRestaurant().compareTo(o2.getRestaurant());
	}
	
	

}
