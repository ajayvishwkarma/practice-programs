package com.example.demo.ratelimiter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RateLimiter {


//	public static Map<Long, Boolean> tokens = new TreeMap<>(Collections.reverseOrder());
    static List<Long> tokens = new ArrayList<>();
    private int totalLimit = 0;
    private int time = 0;

    /**
     * @param class1 number of allowed transactions
     * @param time  number of miliseconds
     */
    public RateLimiter( int time ,int totalLimit) {

        this.time = time;
        this.totalLimit = totalLimit;
    }

    public boolean store( ) throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        for(int i=0;i<tokens.size();i++) {
        	long iTime = currentTime-tokens.get(i);
        	if((currentTime - tokens.get(i)) > time) {
        		tokens.remove(i);
        	}
        }
        if (tokens.size() < totalLimit) {
            tokens.add(currentTime);
            Thread.sleep(100);
            return true;
        }
        int count = 0;
        for (Long token : tokens) {
            count++;
            if (count < totalLimit) {
                continue;
            } else if (count == totalLimit) {
                
               if ((currentTime - token) < time){
                    return false;
                }
            } else {
                break;
            }
        }
        return true;
    }

}
