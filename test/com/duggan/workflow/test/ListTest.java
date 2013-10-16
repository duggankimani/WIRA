package com.duggan.workflow.test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
	
	
	public static void main(String[] args) {
		List<String> lst = new ArrayList<String>();
		List<String> lst2 = new ArrayList<String>();
		
		String[] things ={"Eggs","lasers", "hats", "pie"};
		String[] things2 ={ "hats", "pie"};
		
		//add array items to list1 
		for(String y:things2){
			lst2.add(y);
		}
		
		//Add array items to List 2
		for(String x:things){
			lst.add(x);
		}
		
		for(int i=0;i<lst.size();i++){
			System.out.printf("%s ",lst.get(i));
		}
		
		
		/*List<Comment> lst = new ArrayList<Comment>();
		
		//lst.add(new Document());
		lst.add(new Comment());
		lst.add(new Comment());
		Collections.sort(lst);
		
		
		for(int i=0; i<lst.size(); i++){
			Comment obj = lst.get(i);
			System.out.println(obj);
		}*/
	}
	
	public void editList(){
		
	}
}
