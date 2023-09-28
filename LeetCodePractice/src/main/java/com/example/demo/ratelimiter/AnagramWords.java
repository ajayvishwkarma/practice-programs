package com.example.demo.ratelimiter;

import java.util.ArrayList;
import java.util.List;

public class AnagramWords {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String [] str = {"eat","tea","tan","ate","nat","bat"};
		List<String> list = new ArrayList();
		List<List<String>> listOfList = new ArrayList();
		String str1 = "eat";
		String str2 = "tae";
		if(str1.equals(str2)) {
			System.out.println("Equals");
		}
		else {
			System.out.println("NotEquals");
		}
//		for(int i=0;i<str.length;i++) {
//			String s = str[i];
//			for(int j=0;j<str.length;j++) {
//				String s1 = str[j];
//				if(s!=s1 && s.length() == s1.length()) {
//					for(int k=0;k<s.length();k++) {
//						if()
//					}
//				}
//			}
//		}
		

	}

}
