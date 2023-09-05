package oop2_lab3_v2;

import java.awt.Color;
import java.awt.Graphics;

public class Makaze extends Figura {
	private Color boja = Color.green;
	private int brojUglova = 3;
	private int xKoord[] = new int[brojUglova];
	private int yKoord[] = new int[brojUglova];
	public Makaze(Vektor polozaj, Vektor pomeraj) {
		super(polozaj, pomeraj);
		// TODO Auto-generated constructor stub
	}

	public Makaze(Vektor polozaj, Vektor pomeraj, Double r) {
		super(polozaj, pomeraj, r);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean vektorJeUKruznici(Vektor polozaj1) {
		return Math.sqrt(Math.pow(super.getPolozaj().getX()-polozaj1.getX(), 2)+
				Math.pow(super.getPolozaj().getY()-polozaj1.getY(), 2))<super.getR();
	}

	@Override
	public boolean kruzniceSePreklapaju(Figura f1) {//ovde je mozda samo manje
		
		return Math.sqrt(Math.pow(super.getPolozaj().getX()-f1.getPolozaj().getX(), 2)+
				Math.pow(super.getPolozaj().getY()-f1.getPolozaj().getY(), 2))<= super.getR()+f1.getR(); //ovde je mozda samo < a ne <=
	}

	@Override
	public Color bojaFigure() {
		return boja;
	}

	@Override
	public void iscrtajFiguru(Graphics g) {
		Double ugao = 2*Math.PI/brojUglova;
		Double u = 0.;
		
		xKoord[0] = (int)(super.getPolozaj().getX()+dohvatiX(u));
		yKoord[0] = (int)(super.getPolozaj().getY()+dohvatiY(u));
		u = ugao;
		for(int i=1; i<brojUglova; i++, u=ugao*i) {
			xKoord[i] = (int)(super.getPolozaj().getX()+dohvatiX(u));
			yKoord[i] = (int)(super.getPolozaj().getY()+dohvatiY(u));
		}
		
		g.setColor(boja);
		g.fillPolygon(xKoord,yKoord,brojUglova);
	}
	
	//izracunavanje pozicije temena
	private double dohvatiX(Double angle) {
		return (super.getR()*Math.cos(angle));
	}
	
	private double dohvatiY(Double angle) {
		return (super.getR()*Math.sin(angle));
	}
	@Override
	public String tekstualnaVrsta() {
		return "Makaze";
	}

	@Override
	public Figura stvoriFiguruisteVrste(Vektor polozaj, Vektor pomeraj, Double r) {
		return new Makaze(polozaj, pomeraj, r);
	}

	@Override
	public boolean jacaJeOdZadate(Figura fig) {
		if(fig instanceof Papir) return true;
		else return false;
	}

}
