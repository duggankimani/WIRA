package com.duggan.workflow.test.observer;

import java.util.ArrayList;
import java.util.List;


public class StockGrabber implements Subject {
	private List<Observer> Observerlist = new ArrayList<Observer>();
	private double ibmPrices;
	private double aaplPrices;
	private double googlePrices;
	
	public void setIbmPrices(double ibmPrices) {
		this.ibmPrices = ibmPrices;
		notifyObservers();
	}

	public void setAaplPrices(double aaplPrices) {
		this.aaplPrices = aaplPrices;
		notifyObservers();
	}

	public void setGooglePrices(double googlePrices) {
		this.googlePrices = googlePrices;
		notifyObservers();
	}

	@Override
	public void register(Observer newObserver) {
		Observerlist.add(newObserver);
	}

	@Override
	public void unregister(Observer deleteObserver) {
		int observerIndex = Observerlist.indexOf(deleteObserver);
		
		System.out.println("Observer " + observerIndex + " deleted");
		
		Observerlist.remove(observerIndex);
	}

	@Override
	public void notifyObservers() {
		
		for(Observer obs: Observerlist){
			obs.update(ibmPrices, aaplPrices, googlePrices);
		}
	}
	
	

}
