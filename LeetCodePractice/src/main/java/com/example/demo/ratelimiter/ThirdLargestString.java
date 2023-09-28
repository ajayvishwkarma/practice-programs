package com.example.demo.ratelimiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ThirdLargestString {
	public static void main(String[] args) {
		String arr[] = {"AjAy","Vijay","an","Chandan","Aman","saoaana","kaMASC"};
		int first=0,sec=0,third=0;
		int max=0;
		ArrayList<Integer> ar= new ArrayList<>();
		TreeMap<Integer, String> map = new TreeMap<>();
		for(int i=0; i<arr.length; i++) {
			System.out.println(arr[i].length());
			map.put(arr[i].length(),arr[i]);	
			ar.add(arr[i].length());
			}
		Collections.sort(ar);
		System.out.println(ar);
		System.out.println(ar.get(ar.size()-3));
		System.out.println(map);
		//System.out.println(i);	
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			 Integer key = entry.getKey();
			 String val = entry.getValue();
			 if(ar.get(ar.size()-3)==entry.getKey()) {
				 System.out.println(entry.getValue());
			 }
			
		}
		
		}
		TreeMap<Integer, String> tm= new TreeMap<>();
//		for(int i=0; i<arr.length; i++) {
//			//System.out.println(arr[i].length());
//			tm.put(arr[i].length(), arr[i]);
//			}
//		tm.descendingKeySet();
//		
//		System.out.println(tm.descendingKeySet());
//		List<Integer> list = new LinkedList();
//		for (Map.Entry<Integer, String> entry : tm.entrySet()) {
//			
//			Integer key = entry.getKey();
//			String val = entry.getValue();
//			System.out.println(key +" "+val);
//			
//			
			
//		}
		
		
	}

