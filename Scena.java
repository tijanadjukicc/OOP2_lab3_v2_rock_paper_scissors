package oop2_lab3_v2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Scena extends Canvas implements Runnable	{
	private Simulacija glavniProzor;
	private boolean radi = true;
	private Thread nitScena = new Thread(this);
	private List<Figura> figure = new ArrayList<>();
	private Stoperica stoperica;
	private boolean end = false;
	
	public Scena(Simulacija glp) {
		this.glavniProzor = glp;
		this.setBackground(Color.GRAY);
		nitScena.start();
		
	}	

	public void izbaciSlabiju(Figura f0, Figura f1) {
		if(f0.tekstualnaVrsta()==f1.tekstualnaVrsta()) return;
		Figura slabija, jaca;
		if(f0.jacaJeOdZadate(f1)) {
			slabija =f1;
			jaca = f0;
		}
		else {
			slabija = f0;
			jaca = f1;
		}
		
		Vektor pomeraj = slabija.getPomeraj();
		Vektor polozaj = slabija.getPolozaj();
		Double r = slabija.getR();
		
		figure.remove(slabija);
		figure.add(jaca.stvoriFiguruisteVrste(polozaj, pomeraj, r));
		
		repaint();
	}
	
	public boolean sudar(Figura f) {
		for(Figura fig:figure) {
			if(f!=fig) {
				if(f.kruzniceSePreklapaju(fig)) {
					odbijSe(f, fig);
					izbaciSlabiju(f, fig);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private Vektor oduzmiVektore(Vektor v0, Vektor v1) {
		return new Vektor(v0.getX()-v1.getX(), v0.getY()-v1.getY());
	}
	
	private double skalarniProizvod(Vektor v0, Vektor v1) {
		return v0.getX()*v1.getX()+ v0.getY()*v1.getY();
	}
	
	private Vektor mnozenjeVektoraSkalarom(Vektor v, double k) {
		return new Vektor(v.getX()*k, v.getY()*k);
	}
	
	private void odbijSe(Figura f1, Figura f2) {
		Vektor pomeraj1 = f1.getPomeraj(), polozaj1 = f1.getPolozaj();
		Vektor pomeraj2 = f2.getPomeraj(), polozaj2 = f2.getPolozaj();
		
		Vektor  pom12o = oduzmiVektore(pomeraj1, pomeraj2);
		Vektor 	pol12o = oduzmiVektore(polozaj1, polozaj2);
		Vektor  pom21o = oduzmiVektore(pomeraj2, pomeraj1);
		Vektor 	pol21o = oduzmiVektore(polozaj2, polozaj1);
		
		//racunanje pomeraja za fig1
		double pomeraj1skal = skalarniProizvod(pol12o, pom12o);
		double intenzitet1 = Math.pow(pol12o.getX(), 2)+Math.pow(pol12o.getY(), 2);
		Vektor novipom1 = oduzmiVektore(pomeraj1, mnozenjeVektoraSkalarom(pol12o, pomeraj1skal/intenzitet1));
		novipom1 = novipom1.ortVektor();
		f1.getPomeraj().setX(novipom1.getX());
		f1.getPomeraj().setY(novipom1.getY());
		
		//racunanje pomeraja za fig2
		double pomeraj2skal = skalarniProizvod(pom21o, pol21o);
		double intenzitet2 = Math.pow(pol21o.getX(), 2)+Math.pow(pol21o.getY(), 2);
		Vektor novipom2 = oduzmiVektore(pomeraj2, mnozenjeVektoraSkalarom(pol21o, pomeraj2skal/intenzitet2));
		novipom2 = novipom2.ortVektor();
		f2.getPomeraj().setX(novipom2.getX());
		f2.getPomeraj().setY(novipom2.getY());
	}


	private boolean sveSuIste() {
		if(figure.size()==0) return false;
		String tip = figure.get(0).tekstualnaVrsta();
		boolean iste = true;
		for(Figura f:figure) {
			if(f.tekstualnaVrsta()!=tip) iste = false;
		}
		return iste;
	}
	
	private void odbijSeOdZida(double x, double y, double r, Figura f) {
		if(y-r < 0) {
			f.getPomeraj().setY(f.getPomeraj().getY()*(-1));
		}
		else if((y+r) > getHeight()) {
			f.getPomeraj().setY(f.getPomeraj().getY()*(-1));
		}
		if((x-r) < 0) {
			f.getPomeraj().setX(f.getPomeraj().getX()*(-1));
		}
		if((x+r) > getWidth()) {
			f.getPomeraj().setX(f.getPomeraj().getX()*(-1));
		}
	}
	
	//pomeranje jedne figure
	private synchronized void pomeriFiguru(Figura f) {
		int pom = 3; //podrazumevana vrednost
		Vektor polozaj = f.getPolozaj();
		Vektor ort = f.getPomeraj().ortVektor();
		
		double x = polozaj.getX();
		double y = polozaj.getY();
		
		x+= ort.getX()*pom;
		y+=ort.getY()*pom;
		f.getPolozaj().setX(x);
		f.getPolozaj().setY(y);
		
		if(izlaziIzProzora(x, y, f.getR()))
		{
			odbijSeOdZida(x, y, f.getR(), f);
		}
		sudar(f);
		
		
		repaint();
//		if(sveSuIste()) zaustaviScenu();
	}
	
	//pomeranje svih figura
	@Override
	public void run() {
		try {
			while(!nitScena.interrupted()) {
				synchronized (nitScena) {
					if(!radi) {
						nitScena.wait();
					}
					
				}
				Thread.sleep(100);
				synchronized (this) {
					for(int i = 0; i< figure.size();i++) {
						pomeriFiguru(figure.get(i));
					}
				}
//				repaint();
				
			}
		} catch (Exception e) {
		}
		
	}
	
	public boolean scenaRadi() {
		return radi;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(Figura f:figure) {
			f.iscrtajFiguru(g);
		}
		if((sveSuIste()&& radi)||end) {
			//*************OVDE ONEMOGUCI SVE ONE KOMPONENTE
			g.setColor(Color.white);
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
			String kraj = "KRAJ";
			g.drawString(kraj, getWidth()/2-kraj.length()/2*50, getHeight()/2);
			this.zaustaviScenu();
			end = true;
		}
		if(!radi && !end) {
//			nitScena.interrupt();
			g.setColor(Color.black);
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
			String pauza = "PAUZA";
			g.drawString(pauza, getWidth()/2-pauza.length()/2*50, getHeight()/2);
		}
		
		
	}
	
	private boolean preklapanje(Figura f1) {
		for(Figura f: figure) {
			if(f1.kruzniceSePreklapaju(f)) return true;
		}
		
		return false;
	}
	
	private boolean izlaziIzProzora(double x, double y, double r) {
		return (x-r<0 ||y-r<0||y+r>(this.getHeight()-1)||x+r>(this.getWidth()-1));
	}
	
	public synchronized void dodajFiguru(Figura f) {
		if(preklapanje(f)) return;
		double x = f.getPolozaj().getX(), y = f.getPolozaj().getY(), r = f.getR();
		if(izlaziIzProzora(x, y, r)) return;
		figure.add(f);
		repaint();
	}
	//ovde azurirati i stopericu adekvatno!!!!!
	public void zaustaviScenu() {
		radi = false;
		nitScena.interrupt();
		stoperica.zavrsi();
	}
	
	public void pauzirajScenu() {
		radi = false;
		stoperica.pauziraj();
	}
	
	public void nastaviScenu() {
		radi = true;
		synchronized (nitScena) {
			nitScena.notify();
			stoperica.nastavi();
		}
	}
	
	public int brojVrste(String vrsta) {
		int broj = 0;
		for(Figura fig:figure) {
			if(fig.tekstualnaVrsta()==vrsta) broj++;
		}
		return broj;
	}
	
	public void postaviStopericu(Stoperica s) {
		stoperica = s;
		stoperica.zapocni();
	}
	
	public int brojFiguranaSceni() {
		return figure.size();
		
	}
	
	public void izbaciSveFigure() {
		glavniProzor.kamenje.setText("0");
		glavniProzor.papiri.setText("0");
		glavniProzor.makaze.setText("0");
		glavniProzor.ukupanBroj.setText("0");
		figure.removeAll(figure);
		repaint();
	}
	
	public void izbaciSveKojeObihvataju(Vektor vektor) {
		for(int i = 0; i< figure.size();i++) {
			if(figure.get(i).vektorJeUKruznici(vektor)) figure.remove(i);
		}
		
		glavniProzor.ukupanBroj.setText(brojFiguranaSceni()+"");
		glavniProzor.papiri.setText(brojOdredjeneVrste("Papir")+"");
		glavniProzor.makaze.setText(brojOdredjeneVrste("Makaze")+"");
		glavniProzor.kamenje.setText(brojOdredjeneVrste("Kamen")+"");
		repaint();
	}
	
	public int brojOdredjeneVrste(String s) {
		int broj = 0;
		
		for(Figura f:figure) {
			if(f.tekstualnaVrsta()==s) broj++;
		}
		return broj;
	}
}
/*
*
	private class QuitDialog extends Dialog{

		private Button ok = new Button("ok"), cancel = new Button("cancel");
		@Override
		public void paint(Graphics g) {
			.......................................
			super.paint(g);
		}
		
		public QuitDialog(Frame owner) {
			super(owner);
			setTitle
			setBounds
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			
			Panel buttons = new Panel();
			
			ok.addActionListener((ae)->
			{
				LoginForm.this.dispose();
			}
			);
			
			cancel.addActionListener((ae)->
			{
				QuitDialog.this.dispose();
			 
*/
