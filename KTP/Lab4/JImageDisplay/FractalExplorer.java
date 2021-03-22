package jimagedisplay;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

//Этот класс позволяет исследовать различные части фрактала,
//Создавая и показывая графический интерфейс Swing,
//и обрабатывает события, вызванные различными взаимодействиями пользователя.
public class FractalExplorer
{
    //Ширина и высота отображения в пикселях.
    private int displaySize;
    
    //Обновление ибражения с помощью различных методов
    //по мере вычисления фрактала.
    private JImageDisplay display;
    
    //Для отображения других типов фракталов в будущем.
    private FractalGenerator fractal;
    
    //Определяет диапазон комплекса, который мы в настоящее время отображаем.
    private Rectangle2D.Double range;
    
    //Конструктор, который принимает размер отображения,
    //сохраняет его и инициализирует объекты диапазона и генератора фракталов.
    public FractalExplorer(int size) {
        displaySize = size;
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }
    
    //Этот метод инициализирует графический интерфейс Swing с помощью JFrame,
    //содержащего объект JImageDisplay и кнопку для сброса отображения.
    public void createAndShowGUI()
    {
        display.setLayout(new BorderLayout());
        JFrame myframe = new JFrame("Fractal Explorer");
        myframe.add(display, BorderLayout.CENTER);
        
        //Кнопка сброса.
        JButton resetButton = new JButton("Reset Display");
        ResetHandler handler = new ResetHandler();
        resetButton.addActionListener(handler);
        myframe.add(resetButton, BorderLayout.SOUTH);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);
        
        //Операция закрытия фрейма по умолчанию на «выход».
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myframe.pack();
        myframe.setVisible(true);
        myframe.setResizable(false);
    }
    
    //Метод для отображения фрактала.
    //Этот метод просматривает каждый пиксель на дисплее и вычисляет
    //количество итераций для соответствующих координат
    //в области отображения фрактала.
    private void drawFractal()
    {
        //Просматривайте каждый пиксель на дисплее.
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){
                
                //Поиск соответствующих координат xCoord и yCoord
                //в области отображения фрактала.
                double xCoord = fractal.getCoord(range.x,
                range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y,
                range.y + range.height, displaySize, y);
                
                //Вычислите количество итераций для координат
                //в области отображения фрактала.
                int iteration = fractal.numIterations(xCoord, yCoord);
                
                //Если количество итераций равно -1, установите черный пиксель.
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }
                else {
                    
                    //Выбор значения оттенка
                    //в зависимости от количества итераций.
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                
                    //Обновление каждого пикселя цветом.
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        
        //Перекрасите JImageDisplay в соответствии с текущим изображением.
        display.repaint();
    }
    
    //Внутренний класс для обработки событий ActionListener от кнопки сброса.
    private class ResetHandler implements ActionListener
    {
        
        //Обработчик сбрасывает диапазон до начального диапазона,
        //заданного генератором, а затем рисует фрактал.
        public void actionPerformed(ActionEvent e)
        {
            fractal.getInitialRange(range);
            drawFractal();
        }
    }
    
    //Внутренний класс для обработки событий MouseListener с дисплея.
    private class MouseHandler extends MouseAdapter
    {
        
        //Когда обработчик получает событие щелчка мыши, он сопоставляет
        //пиксельные координаты щелчка с областью отображаемого фрактала,
        //а затем вызывает метод генератора correnterAndZoomRange()
        //с координатами, по которым щелкнули и увеличивает масштаб.
        @Override
        public void mouseClicked(MouseEvent e)
        {
            
            //Получить x координату области отображения щелчка мыши.
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
            range.x + range.width, displaySize, x);
            
            //Получить y координату области отображения щелчка мыши.
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
            range.y + range.height, displaySize, y);
            
            //Вызовите метод генератора RecenterAndZoomRange () с координатами,
            //по которым был выполнен щелчок и увеличивает масштаб.
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            
            //Перерисовка фрактал после изменения отображаемой области.
            drawFractal();
        }
    }
    
    //Статический метод main () для запуска FractalExplorer.
    //Инициализирует новый экземпляр FractalExplorer с размером отображения 600,
    //вызывает createAndShowGUI () в объекте проводника,
    //а затем вызывает drawFractal () в проводнике,
    //чтобы увидеть первоначальный вид.
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
