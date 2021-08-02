package sort;

import java.util.Comparator;
import beans.Restaurant;

public class SortByGradeAscending implements Comparator<Restaurant> {

	@Override
	public int compare(Restaurant o1, Restaurant o2) {
		
		if (o1.getAverageGrade() == o2.getAverageGrade()) {
			return 0;
		} else if (o1.getAverageGrade() > o2.getAverageGrade()) {
			return 1;
		} else {
			return -1;
		}
	}

}
