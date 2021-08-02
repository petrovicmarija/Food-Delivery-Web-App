package sort;

import java.util.Comparator;

import beans.Order;

public class SortByPriceAscending implements Comparator<Order> {

	@Override
	public int compare(Order o1, Order o2) {
		
		if (o1.getPrice() == o2.getPrice()) {
			return 0;
		} else if (o1.getPrice() > o2.getPrice()) {
			return 1;
		} else {
			return -1;
		}
	}
	
	

}
