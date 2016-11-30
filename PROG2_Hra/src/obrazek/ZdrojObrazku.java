package obrazek;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ZdrojObrazku {
	private String zdroj;
	private Map<String, String> mapa;
	
	public ZdrojObrazku() {
		mapa = new HashMap<>();
	}
	
	public abstract void naplnMapu();
	
	public abstract BufferedImage getObrazek() throws IOException;
	
	public Map<String, String> getMapa() {
		return mapa;
	}
	
	public void setZdroj(String klic) {
		this.zdroj = mapa.get(klic);
	}
	
	public String getZdroj() {
		return zdroj;
	}
	
	
}