package sort;

import java.util.Comparator;

import beans.Restaurant;

public class SortByLocationAscending implements Comparator<Restaurant> {
	
	@Override
	public int compare(Restaurant o1, Restaurant o2) {
		return o1.getLocation().getAddress().getCity().getCity().compareTo(o2.getLocation().getAddress().getCity().getCity());
	}

}
