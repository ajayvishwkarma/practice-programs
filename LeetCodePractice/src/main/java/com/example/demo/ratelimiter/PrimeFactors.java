package com.example.demo.ratelimiter;

import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {
	public static void main(String[] args) {
		int num=24;
		int count=0;
		int a;
		List<Integer> list=new ArrayList<>();
		List<Integer> list2=new ArrayList<>();
		for(int i=2;i<=num/2;i++) {
			if(num%i==0) 
			{
				list.add(i);
			}
		}
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}
		System.out.println(list2);
	}
	
}
