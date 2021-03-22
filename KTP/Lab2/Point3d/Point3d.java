package point3d;
        
public class Point3d {
 private final double x;
 private final double y;
 private final double z;
 //Точки
	public Point3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
                this.z = z;
	}
//расстояние между точками
	public double distanceTo(Point3d that) {
		double dx = this.x - that.x;
		double dy = this.y - that.y;
                double dz = this.z - that.z;
                return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
}
//Поиск площали треугольника
class Triangle {
	private final Point3d a;
	private final Point3d b;
	private final Point3d c;
 
	public Triangle(Point3d a, Point3d b, Point3d c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
 
	private double area = -1;
 
	public double area() {
		if(area == -1) {
			double ab = a.distanceTo(b);
			double bc = b.distanceTo(c);
			double ac = a.distanceTo(c);
			double p = (ab + bc + ac) / 2;
			area = Math.sqrt(p * (p-ab) * (p-bc) * (p-ac));
		}
		return area;
	}
}