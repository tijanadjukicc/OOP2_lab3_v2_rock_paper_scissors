package oop2_lab3_v2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Simulacija extends Frame{
	Scena scena;
	double prevx = 0, prevy = 0;
	Label ukupanBroj = new Label("0");
	Label papiri = new Label("0");
	Label kamenje = new Label("0");
	Label makaze = new Label("0");
	int pomeraj = 3;
	Label pomerajLabel = new Label("3");
	Button podrazumevan = new Button("x");
	Button smanji = new Button("<<");
	Button povecaj = new Button(">>");
	CheckboxGroup grupa = new CheckboxGroup();
	Checkbox papirC = new Checkbox("Papir",true,  grupa);
	Checkbox kamenC = new Checkbox("Kamen",false,  grupa);
	Checkbox makazeC = new Checkbox("Makaze",false,  grupa);
	Panel papirP = new Panel();
	Panel kamenP = new Panel();
	Panel makazeP = new Panel();
	Panel old = papirP;
	
	Label stopericaLabela = new Label();
	Stoperica stoperica = new Stoperica(stopericaLabela);
	
	private class Osluskivaci{
		public Osluskivaci() {
			scena.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==MouseEvent.BUTTON1) {
						if(scena.scenaRadi()==true) return;
						double x = e.getX();
						double y = e.getY();
						Vektor polozaj = new Vektor(x, y);
						Vektor pomeraj = new Vektor();
						
						old.getComponent(0).setForeground(Color.black);
						String tip = grupa.getSelectedCheckbox().getLabel();
						grupa.getSelectedCheckbox().setForeground(Color.white);
						
						if(tip=="Papir") {
							scena.dodajFiguru(new Papir(polozaj, pomeraj));
							old = papirP;
							old.getComponent(0).setForeground(Color.white);
						}
						else if(tip=="Kamen") {
							scena.dodajFiguru(new Disk(polozaj, pomeraj));
							old = kamenP;
							old.getComponent(0).setForeground(Color.white);
						}
						else {
							scena.dodajFiguru(new Makaze(polozaj, pomeraj));
							old = makazeP;
							old.getComponent(0).setForeground(Color.white);
						}
						ukupanBroj.setText(scena.brojFiguranaSceni()+"");
						papiri.setText(scena.brojOdredjeneVrste("Papir")+"");
						kamenje.setText(scena.brojOdredjeneVrste("Kamen")+"");
						makaze.setText(scena.brojOdredjeneVrste("Makaze")+"");
					}
					else if(e.getButton()==MouseEvent.BUTTON3) {
						double x = e.getX();
						double y = e.getY();
						Vektor polozaj = new Vektor(x, y);
						scena.izbaciSveKojeObihvataju(polozaj);
					}
					
				}
				
				
			});
			
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					scena.zaustaviScenu();
					dispose();
				}
			});
			
			
			scena.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					char key = Character.toUpperCase(e.getKeyChar());
					switch(key) {
					case KeyEvent.VK_SPACE:{
						System.out.println("kliknut je spejss");
						//dodatii sta treba ovde
						if(scena.scenaRadi()==true) scena.pauzirajScenu();
						else scena.nastaviScenu();
						break;
					}
					case KeyEvent.VK_ESCAPE:{
						System.out.println("kliknut je esc");
						//dodati ovde sta treba
						scena.zaustaviScenu();
						dispose();
						break;
					}
					case KeyEvent.VK_RIGHT:{
						if(pomeraj+1<=5) {
							pomeraj++;
							pomerajLabel.setText(pomeraj+"");
							break;
						}
					}
					case KeyEvent.VK_LEFT:{
						if(pomeraj-1>=1) {
							pomeraj--;
							pomerajLabel.setText(pomeraj+"");
						}
						break;
					}
					}
				}
			});
			
			smanji.addActionListener(al->{
				if(pomeraj-1>=1) {
					pomeraj--;
					pomerajLabel.setText(pomeraj+"");
				}
			});
			povecaj.addActionListener(al->{
				if(pomeraj+1<=5) {
					pomeraj++;
					pomerajLabel.setText(pomeraj+"");
				}
			});
			podrazumevan.addActionListener(al->{
				pomeraj = 3;
				pomerajLabel.setText("3");
			});
			
		
		}
	}
	
	public void popuniProzor(){
		//ovde prvo kreirati scenu
		scena = new Scena(this);
		this.add(scena, BorderLayout.CENTER);
		
		//meni
		MenuBar meniTraka= new MenuBar();
		Menu meni = new Menu("Meni");
		
		MenuItem ugasi = new MenuItem("Zatvori prozor", new MenuShortcut('C'));//c za close
		ugasi.addActionListener((a)->{
			scena.zaustaviScenu();
			dispose();
		});
		meni.add(ugasi);
		
//		MenuItem generisi= new MenuItem("Generisi", new MenuShortcut('G'));
//		//***DODATI LAMBDA IZRAZ OVDE
//		meni.add(generisi);
		
		MenuItem obrisi = new MenuItem("Obrisi sve", new MenuShortcut('B'));
		obrisi.addActionListener((ae)->{
			scena.izbaciSveFigure();
		});
		meni.add(obrisi);
		
		MenuItem resetujStopericu = new MenuItem("Resetuj stopericu", new MenuShortcut('S'));
		resetujStopericu.addActionListener(a->{
			stoperica.resetuj();
		});
		meni.add(resetujStopericu);
		
		meniTraka.add(meni);
		this.setMenuBar(meniTraka);
		
		//pomeraj
		Panel pomeraj = new Panel(new GridLayout(2, 1));
		Panel pp = new Panel();
		pp.add(pomerajLabel, new BorderLayout().CENTER);
		pomeraj.add(pp, new BorderLayout().CENTER);
		pomeraj.setBackground(Color.LIGHT_GRAY);
		
		Panel upravljajpomerajem = new Panel();
		upravljajpomerajem.add(smanji);
		upravljajpomerajem.add(podrazumevan);
		upravljajpomerajem.add(povecaj);
		upravljajpomerajem.setBackground(Color.LIGHT_GRAY);
		pomeraj.add(upravljajpomerajem);
		this.add(pomeraj, new BorderLayout().NORTH);
//		this.add(upravljajpomerajem, new BorderLayout().NORTH);
		
		//donji deo
		Panel bottom = new Panel();
		bottom.setBackground(Color.lightGray);
		
		bottom.add(new Label("Broj figura:"));
		bottom.add(ukupanBroj);
		bottom.add(new Label("Papiri:"));
		bottom.add(papiri);
		bottom.add(new Label("Kamenje:"));
		bottom.add(kamenje);
		bottom.add(new Label("Makaze:"));
		bottom.add(makaze);
		this.add(bottom, new BorderLayout().SOUTH);
		bottom.add(new Label("Proteklo vreme:"));
		bottom.add(stopericaLabela);
		scena.postaviStopericu(stoperica);
		
		//desni deo
		Panel desno = new Panel(new GridLayout(3, 1));
		papirP.setBackground(Color.red);
		papirP.add(papirC);
//		Panel kamenP = new Panel();
		kamenP.setBackground(Color.blue);
		kamenP.add(kamenC);
//		Panel makazeP = new Panel();
		makazeP.setBackground(Color.green);
		makazeP.add(makazeC);
		
		
		papirP.setForeground(Color.white);
		desno.add(papirP);
		desno.add(kamenP);
		desno.add(makazeP);

		this.add(desno, new BorderLayout().EAST);
		
//		//levi deo
//		Panel left = new Panel();
//		Choice padajuci = new Choice();
//		padajuci.add("opcija 0");
//		padajuci.add("opcija 1");
//		padajuci.add("opcija 2");
//		left.add(padajuci);
//		this.add(left, new BorderLayout().WEST);
	}
	
	public Simulacija() {
		setTitle("Simulacija");
		setSize(570, 470);
		setResizable(false);
		popuniProzor();
		new Osluskivaci();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Simulacija s = new Simulacija();
	}
	
}



