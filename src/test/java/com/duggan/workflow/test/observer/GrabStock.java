package com.duggan.workflow.test.observer;

public class GrabStock {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		StockGrabber stockGrabber = new StockGrabber();
		
		StockObserver observer =new StockObserver(stockGrabber);
		
		stockGrabber.setAaplPrices(150.25);
		stockGrabber.setIbmPrices(200.25);
		stockGrabber.setGooglePrices(300.25);
	}

}
