package com.example.demo.ratelimiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public class LargestNumberSeries {
	public static void main(String[] args) {
		int arr[] = { 90, 3, 30, 34, 5, 45, 9 };
		List l = new ArrayList();
		for (int i = 0; i < arr.length; i++) {
			String s = String.valueOf(arr[i]);
			if (s.length() > 1) {
				for (int j = 0; j < s.length(); j++) {
					l.add(s.charAt(j));
				}
			}

		}
		Collections.sort(l);
		for (int k = l.size() - 1; k < 0; k++) {
			System.out.print(l.get(k));
		}
		System.out.println("");
//		List<String> l = new ArrayList<>();
//		for(int i:arr) {
//			String s= String.valueOf(i);
//			l.add(s);
//			
//		}
//		Collections.sort(l);
//		System.out.println(l);
//		for(int i=l.size()-1;i>=0;--i) {
//			System.out.print(l.get(i));
//		}
//		
//		List<Integer> l1 = new ArrayList<>();
//		for(int i:arr) {
//			//String s= String.valueOf(i);
//			l1.add(i);
//			
//			
//		}

//		System.out.println(l1);
//		l1.stream().filter(i-> )
//		

	}

}
