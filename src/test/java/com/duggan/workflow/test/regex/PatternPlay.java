package com.duggan.workflow.test.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternPlay {

	public static void main(String[] args) {
		int x=0x2222;
		System.err.println(x+" = \nbin "+Integer.toBinaryString(x)
				+" \ninv "+(Integer.toBinaryString(~x))
				+" \nxor "+(Integer.toBinaryString(x^x)));
		
		//System.out.println(Pattern.MULTILINE +" "+Pattern.UNIX_LINES+" "+ (Pattern.MULTILINE | Pattern.UNIX_LINES));
	}
}
