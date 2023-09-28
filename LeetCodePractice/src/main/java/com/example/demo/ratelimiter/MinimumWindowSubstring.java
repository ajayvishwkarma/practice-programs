package com.example.demo.ratelimiter;

public class MinimumWindowSubstring {
	
	public static void main(String[] args) {
		String s = "ADOBECODEBANC";
		String t = "ABC";
		String a = "";
		String minString = "";
		for(int i=0;i<t.length();i++) {
			for(int j=0;j<s.length();j++) {
				if(s.charAt(j) == t.charAt(i)) {
					minString =minString + s.charAt(j);	
				}
				else{
					minString = minString +s.charAt(j);
				}
			}
		}
		System.out.println(minString);
	}

}
