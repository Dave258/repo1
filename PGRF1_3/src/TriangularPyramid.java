import transforms.Point3D;

public class TriangularPyramid extends GeometricObject{
	
	public TriangularPyramid() {
		
		vertexBuffer.add(new Point3D(43, -50, -43));
		vertexBuffer.add(new Point3D(43, 50, -50));
		vertexBuffer.add(new Point3D(-43, 0, -43));
		vertexBuffer.add(new Point3D(0, 0, 43));
		
	}
	
}