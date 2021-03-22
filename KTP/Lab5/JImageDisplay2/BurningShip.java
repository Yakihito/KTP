package jimagedisplay2; 
import java.awt.geom.Rectangle2D;

//Этот класс является подклассом FractalGenerator.
//Он используется для вычисления фрактала Пылающего Корабля.
public class BurningShip extends FractalGenerator
{
    //Константа для количества максимальных итераций.
    public static final int MAX_ITERATIONS = 2000;
    
    //Этот метод позволяет генератору фракталов указать,
    //какая часть комплексной плоскости наиболее интересна для фрактала.
    //Ему передается объект прямоугольника, и метод изменяет поля
    //прямоугольника, чтобы показать правильный начальный диапазон для фрактала.
    //Эта реализация устанавливает начальный диапазон равный 
    ////x = -2, y = -2,5, width = height = 4.
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }
    
    //Этот метод реализует итерационную функцию для фрактала Пылающего Корабля.
    //Он принимает два двойных значения для действительной и мнимой частей
    //комплексной плоскости и возвращает количество итераций для
    //соответствующей координаты.
    public int numIterations(double x, double y)
    {
        int iteration = 0;
        double zreal = 0;
        double zimaginary = 0;
        
        // Вычислите Zn=(abs[Re(zn-1)]+i(abs[img(zn-1)]))^2+c
        //где значения представляют собой
        //комплексные числа, представленные zreal и zimaginary,
        //Z0=0, а c - это конкретная точка во фрактале,
        // который мы отображаем (задается x и y). Он повторяется до тех пор,
        //пока Z^2>4 (абсолютное значение Z больше 2)
        //или пока не будет достигнуто максимальное количество итераций.
        while (iteration < MAX_ITERATIONS &&
               zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = 2 * Math.abs(zreal)
            * Math.abs(zimaginary) + y;
            
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            
            iteration += 1;
        }
        
        //Если количество итераций достигло максимума, то возвращает -1,
        //чтобы указать, что точка не вышла за границу.
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }
        
        return iteration;
    }
    
//Реализация toString () Возвращает имя фрактала: «Burning Ship».
    public String toString() {
        return "Burning Ship";
    }
    
}
