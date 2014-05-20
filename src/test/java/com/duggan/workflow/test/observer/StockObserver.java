package com.duggan.workflow.test.observer;

public class StockObserver implements Observer {
	
	private double ibmPrice;
	private double aaplPrice;
	private double googlePrice;
	
	private static int observerIDTracker=0;
	private int observerID;
	
	private Subject stockGrabber;
	
	public StockObserver(Subject stockGrabber) {
		this.stockGrabber = stockGrabber;
		
		this.observerID = ++observerIDTracker;
		
		System.out.println("New Observer " + this.observerID);
		
		stockGrabber.register(this);
	}
	@Override
	public void update(double ibmPrices, double aaplprices, double googleprices) {
		this.ibmPrice = ibmPrices;
		this.aaplPrice= aaplprices;
		this.googlePrice =googleprices;
		
		printThePrices();
	}
	
	public void printThePrices(){
		System.out.println(observerID + "\nIBM:" + ibmPrice + 
						 "\nApple:" + aaplPrice + "\nGoogle:"
						 + googlePrice);
	}

}
