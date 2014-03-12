package com.duggan.workflow.test.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.eval.ComplexEvaluator;
import org.matheclipse.parser.client.eval.ComplexVariable;

public class FormulaParser {

	public static void main(String[] args) {
		String str = "group1";
		System.err.println(str.split(",")[0]);
	}
	public static void main4(String[] args) {
		String sum="ThisA23";
		System.out.println(sum.substring(0,sum.indexOf("23")));
	}
	
	public static void main3(String[] args) {
		String sum = "Plus[A$12,34,566,C]";
		sum = sum.replaceAll("C", "C:weed");
		System.err.println("sum="+sum);
		
		ComplexEvaluator engine = new ComplexEvaluator();
		engine.defineVariable("A$12", new ComplexVariable(12));
		
		Double val = engine.evaluate(sum).abs();	
		System.out.println(val);
	}
	
	public static void main2(String[] args) {
		//Aggregate function
		String sum = "100+Plus[total,tax1A,1000]";
		List<String> fields = Arrays.asList("total,qty,unitPrice,tax");
		
		Properties values = new Properties();
		values.put("1|total", 10.0);
		values.put("2|total", 10.0);
		values.put("3|total", 10.0);
		
		values.put("1|total", 3.0);
		values.put("2|total", 3.0);
		values.put("3|total", 3.0);
		
		//all fields are unique
		List<String> dependentFields = new ArrayList<String>();
		String regex = "[(\\+)+|(\\*)+|(\\/)+|(\\-)+|(\\=)+|(\\s)+(\\[)+|(\\])+|(,)+]";
		
		String[] arr= sum.split(regex);
		
		String digitsOnlyRegex = "[-+]?[0-9]*\\.?[0-9]+"; //isNot a number
		for(String s: arr){
			if(!s.matches(digitsOnlyRegex)){
				System.err.println(s);
			}
			
		}

		
		/*
		 * if total is a list field - it needs to be substituted with
		 * actual field names; 1|total, 2|total,3|total etc 
		 */
		
	}
	
	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		String plus = "Plus[1,2,3,4]";
		
		ComplexEvaluator engine = new ComplexEvaluator();
		
		//System.err.println(engine.evaluate(sum).abs());
	}

}
