package com.example.demo.ratelimiter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ReturnAllTheTriplet {

	public static void main(String[] args) {
		List<List<Integer>> list1 = new ArrayList<>();
		Set<ArrayList<Integer>>set = new HashSet<>();
		// TODO Auto-generated method stub
		int nums[] = { -1, 0, 1, 2, -1, -4 };
		
//		System.out.println(nums);
		int n = 0;
		List<Integer> list = null;
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums.length; j++) {
					for (int k = 0; k < nums.length; k++) {
						list = new ArrayList<>();
							if (i!=j && i!=k && j!=k) {
								if ((nums[i] + nums[j] + nums[k]) == 0) {
									System.out.println(n++);
									list.add(nums[i]);
									list.add(nums[j]);
									list.add(nums[k]);
									System.out.println("nums[i]" + i + " " + "nums[j]" + j + " " + "nums[k] " + k + " :"
											+ "[" + nums[i] + "," + nums[j] + "," + nums[k] + "]");
									set.add((ArrayList<Integer>) list);
								}
							}
						
					}
				}

			}
//		System.out.println(list1);

		System.out.println(set);
		
	}
}

