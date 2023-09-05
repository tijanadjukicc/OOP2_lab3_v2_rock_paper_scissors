package oop2_lab3_v2;

import java.util.Random;

//napadna tacka??

public class Vektor {
	private double x, y;
	
	public Vektor(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vektor() {//stvaranje vektora sa slucajnim koordinatama
		double tempx=0.;
		double tempy=0.;
		
		while(tempx==0 && tempy==0) {
			tempx = (new Random().nextDouble())*2 -1;
			tempy = (new Random().nextDouble())*2 -1;
		}
		
		this.x = tempx;
		this.y = tempy;
	}

	//jedinicni vektor
	public double magnituda() {
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public Vektor ortVektor() {
		double m = magnituda();
		return new Vektor(x/m, y/m);
	}
	
	//setteri i getteri
	public double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}
	
	
	
}
