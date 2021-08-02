package sort;

import java.util.Comparator;

import beans.Order;

public class SortByDateDescending implements Comparator<Order> {

	@Override
	public int compare(Order o1, Order o2) {
		return -(o1.getDateAndTime().compareTo(o2.getDateAndTime()));
	}
	

}
