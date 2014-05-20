package com.duggan.workflow.test;

public class GenericsLearn {
	
	public static void main(String[] args) {
		Integer[] iarray={1,2,3,4,5};
		Character[] carray={'m','m','m'};
		
//		printMe(iarray);
//		printMe(carray);
		
		System.out.println(max(42,20,12));
		System.out.println(max("Tom","Mary","J"));
		}
		
		//Generic Method
		public static <T> void printMe(T[] x){
			for(T b:x){
				System.out.printf("%s ",b);
			}
			System.out.println();
		}
		
		public static <T extends Comparable<T>> T max(T a, T b, T c){
			T m=a;
			
			if(b.compareTo(a)>0){
				m=b;
			}
			if (c.compareTo(b)>0){
				m=c;
			}
			
			return m;
		}
}
