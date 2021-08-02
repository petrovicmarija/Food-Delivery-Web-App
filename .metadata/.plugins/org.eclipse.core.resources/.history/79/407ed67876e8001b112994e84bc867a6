package sort;

import java.util.Comparator;

import beans.Restaurant;

public class SortByLocationDescending implements Comparator<Restaurant> {

	@Override
	public int compare(Restaurant o1, Restaurant o2) {
		return -(o1.getLocation().toString().compareTo(o2.getLocation().toString()));
	}
}
