package com.walmart.ticketing;


public class Main {
	public static void main(String[] args) {
		int num = 49;
		for(long i = 1; i <= num; i++) {
	        if(num % i == 0 && num/i!=1) {
	            System.out.println(i + " " + num/i);
	            }
	    }
		
	}
	
		
	
	
	
}
