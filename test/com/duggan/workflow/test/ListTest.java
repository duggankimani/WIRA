package com.duggan.workflow.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Document;

public class ListTest {

	public static void main(String[] args) {
		List<Comment> lst = new ArrayList<Comment>();
		
		//lst.add(new Document());
		lst.add(new Comment());
		lst.add(new Comment());
		Collections.sort(lst);
		
		
		for(int i=0; i<lst.size(); i++){
			Comment obj = lst.get(i);
			System.out.println(obj);
		}
	}
}
