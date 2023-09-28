package com.example.demo.ratelimiter;

import java.util.ArrayList;
import java.util.List;

public class PermutionOfStrings {
	public List<Integer> test(String s,String []arr) {
		String s1 = "";
		String s2 = "";
		List<String> list = new ArrayList<>();
		List<Integer> list1 = new ArrayList<>();
		for(int i =0; i<arr.length ;i++) {
			s1 = ""+arr[i];
			s2 = ""+arr[i];
			for(int j =0; j<arr.length ;j++) {
				if(i!=j) {
					
					 s1+=arr[j];
				}
				else continue;	
			}
			list.add(s1);
			for(int k =arr.length-1; k>=0 ;k--) {
				if(i!=k) {
					 s2+=arr[k];
				}
				else continue;	
			}
			list.add(s2);
			s1="";
			s2="";
		}
		for(String s3 : list) {
			if(s.contains(s3+"")) {
				list1.add(s.indexOf(s3));
			}
		}	
		return list1;
		
	}
	public static void main(String[] args) {
		PermutionOfStrings p = new PermutionOfStrings();
		String s = "barfoofoobarthefoobarman";
		String arr[] ={"foo","foo","bar","bar"};
		
		System.out.println(p.test(s,arr));
	}	
}
