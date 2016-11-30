package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import hra.HraciPlocha;

public class FlappyFimHlavniApp extends JFrame {
	private HraciPlocha hp;
	public FlappyFimHlavniApp() {
	//TODO
		
	}
	
	public void initGUI(){
		setSize(HraciPlocha.SIRKA, HraciPlocha.VYSKA);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("FlappyFim");
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void spust(){
		hp = new HraciPlocha();
		hp.pripravHraciPlochu();
		getContentPane().add(hp, "Center");
		hp.setVisible(true);
		this.revalidate();
		this.repaint();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				FlappyFimHlavniApp app = new FlappyFimHlavniApp();
				app.initGUI();
				app.spust();
				
			}
		});
	}

}