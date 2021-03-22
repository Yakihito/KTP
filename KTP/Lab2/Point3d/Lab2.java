package point3d;
import java.util.Scanner;
public class Lab2 {
      public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while (true) {
			println("Введите координаты первой вершины треугольника");
			Point3d a = readNextPoint(in);
			println("Введите координаты второй вершины треугольника");
			Point3d b = readNextPoint(in);
			println("Введите координаты третьей вершины треугольника");
			Point3d c = readNextPoint(in);
			double area = new Triangle(a, b, c).area();
			if(area > 0) {
				println("Площадь заданного треугольника равна " + area);
			} else {
				println("Треугольник не существует");
			}
		}
	}
 //Вывод текста 
	private static void println(String s) {
		System.out.println(s);
	}
 //Ввод чисел
	private static Point3d readNextPoint(Scanner in) {
		double x = in.nextDouble();
		double y = in.nextDouble();
                double z = in.nextDouble();
		return new Point3d(x, y, z);
	}
    }