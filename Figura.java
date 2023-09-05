package oop2_lab3_v2;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Figura {
	private Vektor polozaj;
	private Vektor pomeraj;
	private double r=20.; //opisana kruznica
	
	public Figura(Vektor polozaj, Vektor pomeraj) {
		this.polozaj = polozaj;
		this.pomeraj = pomeraj;
	}

	public Figura(Vektor polozaj, Vektor pomeraj, Double r) {
		this.polozaj = polozaj;
		this.pomeraj = pomeraj;
		this.r = r;
	}

	
	public abstract boolean vektorJeUKruznici(Vektor polozaj1); 
	public abstract boolean kruzniceSePreklapaju(Figura f1);
	
	public abstract Color bojaFigure();
	public abstract void iscrtajFiguru(Graphics g);
	 
	//getteri
	public Vektor getPolozaj() {
		return polozaj;
	}

	public Vektor getPomeraj() {
		return pomeraj;
	}

	public double getR() {
		return r;
	}
	
	public abstract String tekstualnaVrsta();
	public abstract Figura stvoriFiguruisteVrste(Vektor polozaj, Vektor pomeraj, Double r);
	public abstract boolean jacaJeOdZadate(Figura fig);
	
}
