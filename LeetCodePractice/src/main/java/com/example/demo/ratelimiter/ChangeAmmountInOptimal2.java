package com.example.demo.ratelimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChangeAmmountInOptimal2 {
	public static void main(String[] args) {
		Map<Integer,Integer> map= new HashMap<>();
		map.put(100, 2000);
		map.put(25, 2000);
		map.put(10, 2000);
		map.put(5, 2000);
		map.put(1, 2000);
		
		int cent=0;
		int nikleToCent=0;
		int dimeToCent=0;
		int quarterToCent=0;
		int dollerToCent=0;
		int sum=0;
		
		for(Map.Entry<Integer, Integer> map1: map.entrySet()) {
			if(map1.getKey()==1) {
				cent = map1.getValue();
			}
			else if(map1.getKey()==5) {
				int nikle = map1.getValue();
				nikleToCent = nikle*5;
			}
			else if(map1.getKey()==10) {
				int dime = map1.getValue();
				dimeToCent = dime*10;
			} 
			else if(map1.getKey()==25) {
				int quarter = map1.getValue();
				quarterToCent = quarter*25;
			}
			else if(map1.getKey()==100) {
				int doller = map1.getValue();
				dollerToCent = doller*100;
			}
			sum = cent+nikleToCent+dimeToCent+quarterToCent+dollerToCent;
			
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Amount in Doller : ");
		double d = sc.nextDouble();
		int ammountInCent = (int) (d*100);
		int aDoller=0,rDoller=0,remDoller=0,cDoller=0,aQuarter=0,cQuarter=0,remQuarter=0,remDime=0,cDime=0,aDime=0,rDime=0;
		int aNickle=0,rNickle=0,remNickle=0,cNickle=0,aCent=0,rCent=0,remCent=0,cCent=0;
		if(ammountInCent<sum) {
			
			for(Map.Entry<Integer, Integer> map1: map.entrySet()) {
				if(map1.getKey()==100) {
					aDoller = map1.getValue()*100;
					if(ammountInCent>aDoller) {
						remDoller = ammountInCent-aDoller;
						cDoller = aDoller/100;
						System.out.println("Doller : " +cDoller);
					}else if(ammountInCent<=aDoller) {
						cDoller = ammountInCent/100;
						remDoller = ammountInCent%100;
						System.out.println("Doller : " +cDoller);
						map1.setValue(remDoller);
					}
					
				}
				if(map1.getKey()==25) {
					aQuarter = map1.getValue()*25;
					if(remDoller>aQuarter) {
						remQuarter = remDoller-aQuarter;
						cQuarter = aQuarter/25;
						System.out.println("Quarter: " +cQuarter);
					}else if(remDoller<=aQuarter) {
						cQuarter = remDoller/25;
						remQuarter = remDoller%25;
						System.out.println("Quarter: " +cQuarter);
						map1.setValue(remQuarter);
					}
				}
				if(map1.getKey()==10) {
					aDime = map1.getValue()*10;
					if(remQuarter>aDime) {
						remDime = remQuarter-aDime;
						cDime = aDime/10;
						System.out.println("Dime   : " +cDime);
					}else if(remQuarter<=aDime) {
						cDime = remQuarter/10;
						remDime = remQuarter%10;
						System.out.println("Dime   : " +cDime);
						map1.setValue(remDime);
					}
				}
				if(map1.getKey()==5) {
					aNickle = map1.getValue()*5;
					if(remDime>aDime || remQuarter>aDime) {
						remNickle = remDime-aNickle;
						cNickle = aNickle/5;
						System.out.println("Nickle : " +cNickle);
					}else if(remDime<=aDoller|| remQuarter<aDime) {
						cNickle = remDime/5;
						remNickle= remDime%5;
						System.out.println("Nickle : " +cNickle);
						map1.setValue(remNickle);
					}
				}
				if(map1.getKey()==1) {
					aCent = map1.getValue()*1;
					if(remNickle>aCent) {
						remCent = remNickle-aCent;
						cCent = remNickle;
						System.out.println("Cent   : " +cCent);
					}else if(remNickle<=aCent) {
						remCent = remNickle;
						
						
						System.out.println("Cent   : " +cCent);
						map1.setValue(remCent);
					}
				}
				
			}
//			int doller = ammountInCent/100;
//			int remCent = ammountInCent%100;
//			int quarter = remCent/25;
//			int quarterRem = remCent%25;
//			int dime = quarterRem/10;
//			int dimeRem = quarterRem%10;
//			int nikle = dimeRem/5;
//			int nikleRem = dimeRem%5;
//			 cent = nikleRem;
//			 System.out.println("please Collect your cash.....");
//			 if(doller>0) {
//				 System.out.println("Doller  : "+doller);
//			 }
//			 if(quarter>0) {
//				 System.out.println("Quarter : "+quarter);
//			 }
//			 if(dime>0) {
//				 System.out.println("Dime    : "+dime);
//			 }
//			 if(nikle>0) {
//				 System.out.println("Nikle   : "+nikle);
//			 }
//			 if(cent>0) {
//				 System.out.println("Cent    : "+cent);
//			 }
//			 System.out.println("avialble cent : "+ (sum-ammountInCent));
//			 
			//System.out.println("Doller "+ doller+ " "+"qaurter "+ quarter +" "+"dime "+dime +" "+"nikle " +nikle +" "+ "cent "+cent);
			System.out.println("avialble cent : "+ (sum-ammountInCent));
		}else {
			System.out.println("amount you want to withdraw is more then in this atm");
			System.out.println("Available cent : "+sum);
		}
	}

	
}

