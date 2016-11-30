package hra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import obrazek.Obrazek;
import obrazek.ZdojObrazkuSoubor;
import obrazek.ZdrojObrazku;

public class HraciPlocha extends JPanel {
	public static final boolean DEBUG = true;
	public static final int VYSKA = 838;
	public static final int SIRKA = 600;

	// rychlost behu pozadi
	public static final int RYCHLOST = -2;
	
	
	//musi byl alespon 3 zdi, jinak se prvni zed "nestihne posunout"
	//za levy okraj = nestihne zajet za levy okraj plochy drive, nez
	//je potreba ji posunout pred pravy okraj hraci plochy a vykreslit
	public static final int POCET_ZDI = 4;
	
	private SeznamZdi seznamZdi;
	private Zed aktualniZed;
	private Zed predchoziZed;
	
	private int skore = 0; //kolika zdmi hrac uspesne prosel bez narazu
	
	private JLabel lbSkore;
	private JLabel lbZprava;
	private Font font;
	private Font fontZpravy;
	
	private Hrac hrac;
	private BufferedImage imgPozadi;
	private Timer casovacAnimace;
	private boolean pauza = false;
	private boolean hraBezi = false;
	private int posunPozadiX = 0;

	public HraciPlocha() {
		//TODO
		ZdojObrazkuSoubor z = new ZdojObrazkuSoubor();
		z.naplnMapu();
		z.setZdroj(Obrazek.POZADI.getKlic());
		
		try {
			imgPozadi = z.getObrazek();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		z.setZdroj(Obrazek.HRAC.getKlic());
		BufferedImage imgHrac;
		//hrac = new Hrac(null);
		try {
			imgHrac = z.getObrazek();
			hrac = new Hrac(imgHrac);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		z.setZdroj(Obrazek.ZED.getKlic());
		BufferedImage imgZed;
		
		try {
			imgZed = z.getObrazek();
			Zed.setObrazek(imgZed);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		seznamZdi = new SeznamZdi();
		
		
		vyrobFontyALabely();
	}

	private void vyrobFontyALabely(){
		font = new Font("Arial", Font.BOLD, 40);
		fontZpravy = new Font("Arial", Font.BOLD, 20);
		
		this.setLayout(new BorderLayout());
		
		lbZprava = new JLabel("");
		lbZprava.setFont(fontZpravy);
		lbZprava.setForeground(Color.YELLOW);
		lbZprava.setHorizontalAlignment(SwingConstants.CENTER);
		
		lbSkore = new JLabel("0");
		lbSkore.setFont(font);
		lbSkore.setForeground(Color.YELLOW);
		lbSkore.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.add(lbSkore, BorderLayout.NORTH);
		this.add(lbZprava, BorderLayout.CENTER);
	}
	
	private void vyrobZdi(int pocet){
		
		int vzadenost = HraciPlocha.SIRKA;
		
		for(int i = 0; i < pocet; i++){
			
			seznamZdi.add(new Zed(vzadenost));
			vzadenost += HraciPlocha.SIRKA/2;
		}
		
		vzadenost -= HraciPlocha.SIRKA - Zed.SIRKA;
		Zed.setVzdalenostPosledniZdi(vzadenost);
		
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		// dve pozadi za sebe pro plynule prechody
		// prvni
		g.drawImage(imgPozadi, posunPozadiX, 0, null);
		// druhe je posunuto o sirku obrazku
		g.drawImage(imgPozadi, posunPozadiX + imgPozadi.getWidth(), 0, null);
		if (HraciPlocha.DEBUG) {
			g.setColor(Color.RED);
			g.drawString("posunPozadiX="+posunPozadiX, 0, 10);
		}
		
		for (Zed zed : seznamZdi) {
			zed.paint(g);
		}
		
		hrac.paint(g);
		
		lbSkore.paint(g);
		lbZprava.paint(g);
	}

	private void posun() {
		if (hraBezi && !pauza) {

			//nastavime zed v poradi
			aktualniZed = seznamZdi.getActualniZed();
			
			//nastav predchozi zed
			predchoziZed = seznamZdi.getPredchoziZed();
			
			
			//detekce kolizi
			if(isKolizeSHranicicHraciPlochy(hrac) ||
					isKolizeZdi(aktualniZed,hrac) ||
					isKolizeZdi(predchoziZed, hrac)) {
				
				ukonciAVyresetujHruPoNarazu();
				
			} else {
				
				for (Zed zed : seznamZdi) {
				zed.posun();
				}			
			
				hrac.posun();
				
				//hrac prosel zdi bez narazu
				//yjisti kde se nachazi
				//bud pred aktualni zdi - nedelej nic
				//nebo za aktualni zdi  - posun dalsi zed v poradi a pricti skore
				if(hrac.getX() >= aktualniZed.getX()) {
					seznamZdi.nastavDalsiZedNaAktualni();
					zvedniSkoreZed();
					lbSkore.setText(skore + "");
				}
				
			}
			
		
			// posun pozice pozadi hraci plochy (scrollovani)
			posunPozadiX = posunPozadiX + HraciPlocha.RYCHLOST;
			// kdyz se pozadi cele doposouva, zacni od zacatku
			if (posunPozadiX == -imgPozadi.getWidth()) {
				posunPozadiX = 0;
			}
		}
	}
	
	
	private void ukonciAVyresetujHruPoNarazu() {
		hraBezi = false;
		casovacAnimace.stop();
		casovacAnimace = null;
		vyresetujHru();
		
		nastavZpravuNarazDoZdi();
	}

	private boolean isKolizeZdi(Zed zed, Hrac hrac) {
		return (zed.getMezSpodniCasti().intersects(hrac.getMez())) ||
				(zed.getMezHorniCastiZdi().intersects(hrac.getMez()));
	}
	
	private boolean isKolizeSHranicicHraciPlochy(Hrac hrac) {
		return (hrac.getY() <= 0) || (hrac.getY() >= HraciPlocha.VYSKA - hrac.getVyskaHrace() - 40);
	}
	
	

	private void spustHru() {
		casovacAnimace = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
				posun();
			}
		});

		nastavZpravuPrazdna();
		hraBezi = true;
		casovacAnimace.start();
	}

	public void pripravHraciPlochu() {
		
		nastavZpravuOvladani();
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					//skok hrace
					hrac.skok();
					
				}
				// pauza
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (hraBezi) {
						if (pauza) {
							nastavZpravuPrazdna();
							pauza = false;
						} else {
							nastavZpravuPauza();
							pauza = true;
						}
					} else {
						pripravNovouHru();
						spustHru();
					}
				}
			}
		});

		setSize(SIRKA, VYSKA);

	}

	protected void pripravNovouHru() {
		vyresetujHru();
	}
	
	
	private void vyresetujHru(){
		resetujVsechnyZdi();
		hrac.reset();
		
		lbSkore.setText(skore + "");
		
		vynulujSkore();
	}


	private void vynulujSkore() {
		skore = 0;
	}

	private void zvedniSkoreZed(){
		skore += Zed.BODY_ZA_ZED;
	}

	private void resetujVsechnyZdi() {
		
		seznamZdi.clear();
		vyrobZdi(POCET_ZDI);
		
	}
	
	private void nastavZpravuNarazDoZdi(){
		lbZprava.setFont(font);
		lbZprava.setText("Narazil jsi, zkus to znovu.");
	}
	
	private void nastavZpravuPauza() {
		lbZprava.setFont(font);
		lbZprava.setText("Pauza");
	}
	
	private void nastavZpravuOvladani() {
		lbZprava.setFont(fontZpravy);
		lbZprava.setText("pravy klik = start/stop, levy klik = skok");
	}
	
	private void nastavZpravuPrazdna() {
		lbZprava.setFont(font);
		lbZprava.setText("");
	}
}