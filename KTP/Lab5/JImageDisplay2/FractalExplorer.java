package jimagedisplay2;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;

//Этот класс позволяет исследовать различные части фрактала,
//Создавая и показывая графический интерфейс Swing,
//и обрабатывает события, вызванные различными взаимодействиями пользователя.
public class FractalExplorer
{
    //Поля для кнопки сохранения, кнопки сброса и поля со списком для enableUI.
    private JButton saveButton;
    private JButton resetButton;
    private JComboBox myComboBox;
    
    //Количество строк, оставшихся для рисования.
    private int rowsRemaining;
    
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
    
    // Этот метод инициализирует графический интерфейс Swing с помощью JFrame,
    // содержащего объект JImageDisplay и кнопку для сброса отображения,
    // кнопку для сохранения текущего фрактального изображения
    // и JComboBox для выбора типа фрактала. 
    public void createAndShowGUI()
    {
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");
        
        // Добавляем объект отображения изображения в BorderLayout.CENTER.
        myFrame.add(display, BorderLayout.CENTER);
        
        // Кнопка сброса.
        resetButton = new JButton("Reset");
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);
        
        // Операция закрытия фрейма по умолчанию на «выход».
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Установка поля со списком.
        myComboBox = new JComboBox();
        
        // Добавьте каждый фрактал в поле со списком.
        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);
        
        // Установка ButtonHandler в поле со списком.
        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);
        
        // Создаём новый объект JPanel, добавляем к нему объект JLabel
        // и объект JComboBox и добавляем панель в BorderLayout.NORTH 
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);
        
        // Создайём кнопку сохранения, добавляем ее в JPanel
        // в позицию BorderLayout.SOUTH вместе с кнопкой сброса.
        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);
        
        // Установка ButtonHandler на кнопке сохранения.
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);
        
        // Разместить содержимое фрейма,
        // сделать его видимым и запретите изменение размера окна.
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
        
    }
    
    // Приватный вспомогательный метод для отображения фрактала.
    private void drawFractal()
    {
        // Вызов enableUI (false) для отключения всех элементов управления 
        // пользовательского интерфейса во время рисования.
        enableUI(false);
        
        // Общее количество строк.
        rowsRemaining = displaySize;
        
        // Прокрутка каждой строки на дисплее и вызов FractalWorker,
        //чтобы нарисовать ее.
        for (int x=0; x<displaySize; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }

    }
    
    // Включает или отключает кнопки и поле со списком интерфейса в зависимости
    // от указанного значения. Обновляет включенное состояние 
    // кнопки сохранения, кнопки сброса и поля со списком.
    private void enableUI(boolean val) {
        myComboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
    }
    
    //Внутренний класс для обработки событий ActionListener.
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Определить источник действия.
            String command = e.getActionCommand();
            
            // Если источником является поле со списком, определяет фрактал,
            // выбранный пользователем, и отображает его.
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();
                
            }
             // Если источником является кнопка сброса, сбросывает отображение
             // и рисует фрактал
            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            // Если источником является кнопка сохранения,
            // сохраняет текущее фрактальное изображение.
            else if (command.equals("Save")) {
                
                // Разрешить пользователю выбрать файл
                // для сохранения изображения.
                JFileChooser myFileChooser = new JFileChooser();
                
                // Сохраняет только PNG изображения.
                FileFilter extensionFilter =
                new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);

                // Гарантирует, что средство выбора файлов не разрешит
                //имена файлов, отличных от ". Png".
                myFileChooser.setAcceptAllFileFilterUsed(false);
                
                // Всплывает окно «Сохранить файл», в котором пользователь
                //может выбрать каталог и файл для сохранения.
                int userSelection = myFileChooser.showSaveDialog(display);
                
                // Если результат операции выбора файла 
                // APPROVE_OPTION, продолжает операцию сохранения файла.
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    
                    // Получить файл и имя файла.
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();
                    
                    // Попытка сохранить фрактальное изображение на диск.
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }
                    // Сообщение об ошибке, если файл не удается сохранить.
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                        exception.getMessage(), "Cannot Save Image",
                        JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Если операция сохранения файла не APPROVE_OPTION, возврат.
                else return;
            }
        }
    }
    
    // Внутренний класс для обработки событий MouseListener с дисплея.
    private class MouseHandler extends MouseAdapter
    {
        // Когда обработчик получает событие щелчка мыши, он сопоставляет
        // пиксельные координаты щелчка с областью отображаемого фрактала,
        // а затем вызывает метод генератора correnterAndZoomRange()
        // с координатами, по которым щелкнули и увеличивает масштаб.
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // Вернуть, если rowRemaining не равен нулю.
            if (rowsRemaining != 0) {
                return;
            }
            // Получить x координату области отображения щелчка мыши.
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
            range.x + range.width, displaySize, x);
            
            // Получить y координату области отображения щелчка мыши.
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
            range.y + range.height, displaySize, y);
            
            // Вызовите метод генератора RecenterAndZoomRange () с координатами,
            // по которым был выполнен щелчок и увеличивает масштаб.
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            
            // Перерисовка фрактал после изменения отображаемой области.
            drawFractal();
        }
    }
    
    // Вычисляет значения цвета для одной строки фрактала.
    private class FractalWorker extends SwingWorker<Object, Object>
    {
        // Поле целочисленной координаты y строки, которая будет вычислена.
        int yCoordinate;
        
        // Массив целых чисел для хранения вычисленных
        // значений RGB для каждого пикселя в строке.
        int[] computedRGBValues;
        
        // Конструктор принимает координату y в качестве 
        // аргумента и сохраняет ее.
        private FractalWorker(int row) {
            yCoordinate = row;
        }
        
        // Этот метод вызывается в фоновом потоке.
        // Он вычисляет значение RGB для всех пикселей в 1 строке
        // и сохраняет его в соответствующем элементе целочисленного массива.
        // Возвращает null.
        protected Object doInBackground() {
            
            computedRGBValues = new int[displaySize];
            
            // Перебрать все пиксели в строке.
            for (int i = 0; i < computedRGBValues.length; i++) {

                // Найти соответствующие координаты xCoord и yCoord
                // в области отображения фрактала.
                double xCoord = fractal.getCoord(range.x,
                range.x + range.width, displaySize, i);
                double yCoord = fractal.getCoord(range.y,
                range.y + range.height, displaySize, yCoordinate);
            
                // Вычислите количество итераций для координат
                // в области отображения фрактала.
                int iteration = fractal.numIterations(xCoord, yCoord);
                        
                // Если количество итераций равно -1, установите текущий int
                //в массиве int вычисленных значений RGB на черный.
                if (iteration == -1){
                    computedRGBValues[i] = 0;
                }
            
                else {
                    // В противном случае выберите значение оттенка
                    // в зависимости от количества итераций.
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                
                    // Обновите массив int цветом текущего пикселя.
                    computedRGBValues[i] = rgbColor;
                }
            }
            return null;
            
        }
        
        // Вызывается, когда фоновая задача завершена.
        //Рисует пиксели для текущей строки и
        //обновляет отображение для этой строки.
        protected void done() {
            
            // Обходит массив строковых данных, рисуя пиксели,
            // которые были вычислены в doInBackground ().
            // Перерисовывает измененную строку.
            for (int i = 0; i < computedRGBValues.length; i++) {
                display.drawPixel(i, yCoordinate, computedRGBValues[i]);
            }
            display.repaint(0, 0, yCoordinate, displaySize, 1);
            
            // Уменьшить количество оставшихся строк.
            //Если 0, вызывает enableUI (true).
            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
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
