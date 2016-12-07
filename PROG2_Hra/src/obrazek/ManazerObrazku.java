package obrazek;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ManazerObrazku {

	private Map<String, BufferedImage> mapaObr;
	private ZdrojObrazku zo;
	
	public ManazerObrazku(ZdrojObrazku zo) {
		mapaObr = new HashMap<>();
		this.zo = zo;
		this.zo.naplnMapu();
	}
	
}
