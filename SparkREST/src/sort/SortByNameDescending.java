package sort;

import java.util.Comparator;

import beans.Restaurant;

public class SortByNameDescending implements Comparator<Restaurant> {

	@Override
	public int compare(Restaurant o1, Restaurant o2) {
		return -(o1.getName().compareTo(o2.getName()));
	}

}
