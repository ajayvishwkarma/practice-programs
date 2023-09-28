package com.example.demo.ratelimiter;

import java.util.*;

public class BigBom {
	
public List<List<String>> groupAnagrams(String[] strs) {
	Map<String, List<String>> res = new HashMap<>();
	List<List<String>> list = new ArrayList();
	List<String> listStr = Arrays.asList(strs);

//	listStr.stream().filter(str -> str);
	
	
	
	for (String str : strs) {
		
		char[] charArray = str.toCharArray();
		Arrays.sort(charArray);
		String sortedStr = new String(charArray);

		if (res.containsKey(sortedStr)) {
			res.get(sortedStr).add(str);
		} else {
			List<String> group = new ArrayList<>();
			group.add(str);
			res.put(sortedStr, group);
		}
		
	}
	
	for(Map.Entry<String, List<String>> map: res.entrySet())
	{
		list.add(map.getValue());
	}
	return list;
    }

	public static void main(String[] args) {
	String[] strs = { "eat", "tea", "tan", "ate", "nat", "bat" };
	BigBom bigBom = new BigBom();
	System.out.println(bigBom.groupAnagrams(strs));
		
		
	}
}
