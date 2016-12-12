import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

public class WireFrameRenderer implements Renderable{

	private BufferedImage img;
	private Mat4 modelM;
	private Mat4 viewM;
	private Mat4 projM;
	
	@Override
	public void draw(GeometricObject go) {
		
		Mat4 finalMat = modelM.mul(viewM).mul(projM);
		
		Vec3D vecA;
		Vec3D vecB;
		
		
		// aplikujeme transformace
		// for cyklem vybereme kazdou dvojici z vertexBufferu
		for(int i = 0; i < go.indexBuffer.size(); i += 2)
		{
			Point3D pointA = go.vertexBuffer.get(go.indexBuffer.get(i));
			Point3D pointB = go.vertexBuffer.get(go.indexBuffer.get(i+1));
			
			// Model, View, Proj - 4D->4D
			pointA = pointA.mul(finalMat);
			pointB = pointB.mul(finalMat);
			
			// clip podle W
			
			// dehomogenizace - 4D->3D
			Optional<Vec3D> oA = pointA.dehomog();
			Optional<Vec3D> oB = pointB.dehomog();
			
			if(oA.isPresent() && oB.isPresent()){
				vecA = oA.get();
				vecB = oB.get();
				
				// clip ZO (zobrazovací objem)
				
				// 3D->2D
				// viewporotvá transformace
				int w = img.getWidth();
				int h = img.getHeight();
				
				double x1 = (vecA.getX()+1)*((w-1)/2);
				double x2 = (vecB.getX()+1)*((w-1)/2);
				
				double y1 = (-vecA.getY()+1)*((h-1)/2);
				double y2 = (-vecB.getY()+1)*((h-1)/2);
				
				
				// rasterizace
				//drawLine()
			};
		}
		
		
		
		
		
		
		
		
		
		
		//APLIKACNI TRIDA	
		//Mat4 M,U,P
		//draw(GO)
		
		//new Renderer()
		//r.setBufferImage(img)
		//Go Cube = new Cube
		//c.setM(new MatScale(10,10,10).mul(new Mat4Transform(x, y, z)))
		//buï c.setP(new Mat4Persp(pi/4, 1, 0.1, 200)) // do 1 se musí zapoèítat šíøka a výška
		//nebo c.setP(new Mat4Orto(300, 200, 0.1, 200))
		//c.setV(new Mat4View(new Vec3(10,10,10) pozorovatel, new Vec3(-1,-1,-1) smerPohledu, new Vec3(0,1,0) kdeJeNahore))
		//lepší camera.with.. nastavuje azimut, zenitu
		//c.setV(Camera.getViewMatrix)
		//c.drawCube();
		
		
		
		
		
	}
	
	@Override
	public void draw(List<GeometricObject> gos) {
		for (GeometricObject g : gos) {
			draw(g);
		}
	}

	@Override
	public void setModel(Mat4 m) {
		modelM = m;
	}

	@Override
	public void setView(Mat4 m) {
		viewM = m;
	}

	@Override
	public void setProj(Mat4 m) {
		projM = m;
	}

	@Override
	public void setBufferedImage(BufferedImage img) {
		this.img = img;
	}

}
