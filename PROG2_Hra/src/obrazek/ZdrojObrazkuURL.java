package obrazek;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ZdrojObrazkuURL extends ZdrojObrazku {
	private static final String SOUBOR = "tmp.txt";
	private static final String ODDELOVAC = ";";
	
	
	@Override
	public void naplnMapu() {
		nactiCSV();
	}

	private void nactiCSV() {
		
		//po skonceni try vetve, se reader sam uzavre
		try(BufferedReader vstup = new BufferedReader(new FileReader(SOUBOR))){
			String radek;
			
			for(int i = 0; i < Obrazek.getSize(); i++){
				if((radek = vstup.readLine()) != null){
					zpracujRadek(radek);
				}
			}
			
			
		} catch (IOException e) {
			System.out.println("Pri cteni dosko k chybe: "+ e.getMessage());
		}
		
	}

	private void zpracujRadek(String radek) {
		StringTokenizer st = new StringTokenizer(radek, ODDELOVAC);
		if(st.countTokens() == 2){ //cteme jen prvni dva tokeny
			String prvek = st.nextToken(); //hrac
			String odkaz = st.nextToken(); //url
			
			if(jePrvkeVSeznamu(prvek)) {
				getMapa().put(prvek, odkaz);
			} else {
				System.out.println("Prvek "+prvek+" neni v seznamu typu");
			}
		}
	}

	private boolean jePrvkeVSeznamu(String prvek) {
		for(Obrazek o: Obrazek.getObrazky()){
			if(o.getKlic().equals(prvek)){
				return true;
			}
		}
		return false;
	}

	@Override
	public BufferedImage getObrazek() throws IOException {
		URL url = new URL(getZdroj());
		URLConnection urlSpojeni = url.openConnection();
		
		urlSpojeni.setReadTimeout(5000); //5 sekund cti, pak prestan
		
		InputStream is = urlSpojeni.getInputStream();
		
		BufferedImage img = ImageIO.read(is);
		
		is.close();
		
		return img;
	}

}
