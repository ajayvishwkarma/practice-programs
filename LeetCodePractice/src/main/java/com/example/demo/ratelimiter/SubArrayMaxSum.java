package com.example.demo.ratelimiter;

import java.util.ArrayList;
import java.util.List;

public class SubArrayMaxSum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int arr[] = {1,2,3,-1,4,2};
		int max = 0;
		int sum;
		List<Integer> list1 = new ArrayList<>();
		for(int i =0;i<arr.length;i++) {
			sum = 0;
			List<Integer> list = new ArrayList<>();
			for(int j =i;j<arr.length;j++) {
				sum = sum + arr[j];
				list.add(arr[j]);
				if(sum >= max) {
					max = sum;
					list1.removeAll(list1);
					list1.addAll(list);
				}
			}
		}
		System.out.println(max);
		System.out.println(list1);
	}

}
