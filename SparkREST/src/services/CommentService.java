package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dao.AllComments;
import dao.Comments;
import beans.Comment;
import beans.Manager;
import beans.Restaurant;
import beans.User;

public class CommentService {
	private Comments comments = new Comments();
	private AllComments allComments = new AllComments();
	private static ManagerService managerService = new ManagerService();
	
	public Collection<Comment> getComments() throws JsonGenerationException, JsonMappingException, IOException {
		return this.comments.load();
	}
	
	public double countAverageGrade(String restaurantName) throws JsonGenerationException, JsonMappingException, IOException{
		double avg;
		int cnt = 0;
		int sum = 0;
		for (Comment c : comments.load()) {
			if (c.getRestaurant().getName().equals(restaurantName)) {
				sum += c.getGrade();
				cnt ++;
			}
		}
		
		avg = sum/cnt;
		
		return avg;
	}
	
	public ArrayList<Comment> getRestaurantComments(String restaurantName) throws JsonMappingException, JsonGenerationException, IOException {
		ArrayList<Comment> restaurantComments = new ArrayList<Comment>();
		for (Comment c : comments.load()) {
			if (c.getRestaurant().getName().equals(restaurantName)) {
				restaurantComments.add(c);
			} 
		}
		
		return restaurantComments;
	}
	
	public ArrayList<Restaurant> getRestaurantsByGrade(double grade) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		for (Comment c : comments.load()) {
			if (grade == countAverageGrade(c.getRestaurant().getName())) {
				restaurants.add(c.getRestaurant());
			}
		}
		
		return restaurants;
	}

	public void addComment(Comment comment) throws JsonMappingException, JsonGenerationException, IOException{
		allComments.save(comment);
	}
	
	public void approveComment(Comment comment) throws JsonGenerationException, JsonMappingException, IOException {
		comments.save(comment);
	}
	
	public ArrayList<Comment> getAllComments(User user) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Comment> managerComments = new ArrayList<Comment>();
		Manager manager = managerService.findManager(user);
		for (Comment c : allComments.load()) {
			if (c.getRestaurant().getName().equals(manager.getRestaurant().getName())) {
				managerComments.add(c);
			}
		}
		
		return managerComments;
	}
	
	public ArrayList<Comment> getCommentsForAdmin() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Comment> adminComments = new ArrayList<Comment>();
		for (Comment c : allComments.load()) {
			adminComments.add(c);
		}
		return adminComments;
	}
}
