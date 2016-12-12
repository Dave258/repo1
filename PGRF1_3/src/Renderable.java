import java.awt.image.BufferedImage;
import java.util.List;

import transforms.Mat4;

public interface Renderable {
	
	public void draw(GeometricObject go);
	public void draw(List<GeometricObject> gos);
	
	void setModel(Mat4 mat);
	void setView(Mat4 mat);
	void setProj(Mat4 mat);
	void setBufferedImage(BufferedImage img);

}
