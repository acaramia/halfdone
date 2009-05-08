package com.rim.samples.helloworld;

import java.util.TimerTask;

public class Tempo extends TimerTask {

	private CronometroScreen applic;
	private long start;
	private int tempoMax;
	
	public Tempo(CronometroScreen t){
		this.applic=t;
		this.myStart(60);
	}

	public void myStart(int tmax){
		this.start=System.currentTimeMillis();
		this.tempoMax=tmax;
	}
	
	public void run() {
		long c = System.currentTimeMillis()-start;
		int c2=(int) (c/1000); // in secondi
		c2=tempoMax-c2; 
		//java.lang.IllegalStateException: UI engine accessed without holding the event lock.
		applic.stampa(Integer.toString(c2));
		if(c2<=0){
			applic.stop();
			this.cancel();
		}					
	}
}
