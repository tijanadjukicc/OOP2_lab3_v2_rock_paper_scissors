package oop2_lab3_v2;

import java.awt.Label;

public class Stoperica extends Thread{
	private Label labela;
	private int interval = 1000;
	private int protekloVreme = 0;
	private boolean aktivnaStoperica= false;
	public Stoperica(Label label) {
		this.labela = label;
		labela.setText(protekloVreme+"");
		this.start();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			while(!this.interrupted()) {
//				synchronized (this) {
//					if(!aktivnaStoperica) {
//						this.wait();
//					}
//				}
				while(aktivnaStoperica) {
					sleep(interval);
					protekloVreme++;
					labela.setText(protekloVreme+"");
				}
				
			}
		} catch (Exception e) {
		}
	}
	
	public void zapocni() {
		protekloVreme = 0;
		aktivnaStoperica = true;
	}
	
	public void pauziraj() {
		aktivnaStoperica = false;
	}
	
	public void nastavi() {
		aktivnaStoperica = true;
	}
	
	public void zavrsi() {
		this.interrupt();
	}
	
	public void resetuj() {
		this.pauziraj();
		protekloVreme = 0;
		this.nastavi();
	}
}
