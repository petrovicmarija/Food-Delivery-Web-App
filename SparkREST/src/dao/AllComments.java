package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import beans.Comment;

public class AllComments {
	private HashMap<String, Comment> comments = new HashMap<String, Comment>();
	private ArrayList<Comment> commentList = new ArrayList<Comment>();
	
	public AllComments() {
		
	}

	public HashMap<String, Comment> getComments() {
		return comments;
	}

	public void setComments(HashMap<String, Comment> comments) {
		this.comments = comments;
	}

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}
	
	public void save(Comment comment) throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> comments = load();
		comments.add(comment);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.writeValue(new File("./static/files/commentsForManagers.json"), comments);
	}
	
	public ArrayList<Comment> load() throws JsonGenerationException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> commentsFromFile = new ArrayList<Comment>();
		ArrayList<Comment> comments = mapper.readValue(new File("./static/files/commentsForManagers.json"), new TypeReference<List<Comment>>(){}); 
		comments.forEach(c -> commentsFromFile.add(c));
		
		return commentsFromFile;
	}

}
