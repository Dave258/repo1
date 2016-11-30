package hra;

import java.util.ArrayList;

public class SeznamZdi extends ArrayList<Zed>{
	
	private int ktera;
	
	public SeznamZdi(){
		super();
		ktera = 0;
	}
	
	@Override
	public void clear() {
		super.clear();
		ktera = 0;
	}
	
	
	public Zed getActualniZed(){
		return this.get(ktera);
	}
	
	public Zed getPredchoziZed(){
		if(ktera > 0){
			return this.get(ktera - 1);
		}
		
		return this.get(this.size()-1);
	}
	
	public void nastavDalsiZedNaAktualni(){
		if(ktera < this.size()-1){
			ktera++;
		} else {
			ktera = 0;
		}
	}
}
