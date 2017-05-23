import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {
    static final int CELL_SIZE = 10; // размер ячейки
    static final int TOOL_BAR_SIZE = 80; // высота панели инструментов

    private int width; // ширна
    private int height; // высота поля для рисования

    private Graphics2D g2;

    private Color color = Color.blue; // цвет кисти
    private int xCoord, yCoord; // координаты рисуемой клетки
    private boolean firstDraw = true;
    public boolean drawGrid = true;

    private int xKeyCoord, yKeyCoord; // координаты на клавиатуре

    BufferedImage bi;

    public Panel(int w, int h) {
        width = w;
        height = h;

        xKeyCoord = (int)(width / (2 * CELL_SIZE)) * CELL_SIZE;
        yKeyCoord = (int)(height / (2 * CELL_SIZE)) * CELL_SIZE;

        addMouseListener(new Handler());
        addMouseMotionListener(new Handler());
        addKeyListener(new Handler());
        setFocusable(true);
    }
    // отрисовка сетки
    private void drawField() {
        setBackground(new Color(238, 238, 238));
        Rectangle2D background  = new Rectangle2D.Double(0, 0, this.width, this.height);
        g2.setPaint(Color.white);
        g2.fill(background);
        g2.setPaint(Color.black);
        g2.draw(background);

        if (drawGrid) {
            int k;

            int rows = height / CELL_SIZE;
            int columns = width / CELL_SIZE;

            for (k = 0; k < rows; k++)
                g2.drawLine(0, k * CELL_SIZE , width, k * CELL_SIZE);

            for (k = 0; k < columns; k++)
                g2.drawLine(k * CELL_SIZE , 0, k * CELL_SIZE , height);

        }
    }
    // отрисвка панели инструментов
    private void drawToolBar() {
        g2.setPaint(Color.WHITE);
        Rectangle2D rect  = new Rectangle2D.Double(0, height + 2 , width / 4 , TOOL_BAR_SIZE);
        g2.fill(rect);

        g2.setPaint(Color.RED);
        rect  = new Rectangle2D.Double(width / 4, height + 2, width / 4 , TOOL_BAR_SIZE);
        g2.fill(rect);

        g2.setPaint(Color.GREEN);
        rect  = new Rectangle2D.Double(2 * width / 4, height + 2, width / 4 , TOOL_BAR_SIZE);
        g2.fill(rect);

        g2.setPaint(Color.BLUE);
        rect  = new Rectangle2D.Double(3 * width / 4, height + 2, width / 4 , TOOL_BAR_SIZE);
        g2.fill(rect);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g2 = (Graphics2D)g;
        // отрисовка поля
        if(firstDraw) {
            drawField();
            drawToolBar();
            firstDraw = false;
        }
        // рисование новой клетки
        else {
            g2.setPaint(color);
            Rectangle2D rect  = new Rectangle2D.Double(xCoord + 1, yCoord + 1, CELL_SIZE - 1, CELL_SIZE - 1);
            g2.fill(rect);
        }
    }
    // рисование новой клетки
    private void drawCell(int x, int y) {
        xCoord = x;
        yCoord = y;
        paintImmediately(xCoord + 1,yCoord + 1,CELL_SIZE - 1,CELL_SIZE - 1);
    }

    private class Handler extends MouseAdapter implements KeyListener{
        // метод вызывается в mousePressed и mouseDragged, рисование
        private void mousePressAction(int x, int y, boolean isRight) {
            // выход за пределы
            if(x >= width || y >= height)
                return;

            int calcX = ( x / CELL_SIZE ) * CELL_SIZE, calcY = ( y / CELL_SIZE ) * CELL_SIZE;

            // резинка
            if (isRight) {
                Color temp = color;
                color = color.WHITE;
                drawCell(calcX, calcY);
                color = temp;
                return;
            }
            // закраска
            drawCell(calcX, calcY);
        }
        // выбор цвета
        private void pickColor(int x, int y) {
            if (y > height && y < height + TOOL_BAR_SIZE) {
                if (x > 0 && x < width / 4)
                    color = Color.WHITE;
                if (x > width / 4 && x < 2 * width / 4)
                    color = Color.RED;
                if (x > 2 * width / 4 && x < 3 * width / 4)
                    color = Color.GREEN;
                if (x > 3 * width / 4 && x < width)
                    color = Color.BLUE;
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            pickColor(e.getX(), e.getY());
            mousePressAction(e.getX(), e.getY(), SwingUtilities.isRightMouseButton(e));
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            mousePressAction(e.getX(), e.getY(), SwingUtilities.isRightMouseButton(e));
        }
        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        private void keyPressAction(int x, int y) {
            if ((x > -CELL_SIZE && x < width ) && (y > -CELL_SIZE && y < height )) {
                xKeyCoord = x;
                yKeyCoord = y;
                drawCell(xKeyCoord, yKeyCoord);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                // выбор направления
                case KeyEvent.VK_UP:
                    keyPressAction(xKeyCoord, yKeyCoord - CELL_SIZE);
                    if (e.isShiftDown()) {
                        keyPressAction(xKeyCoord, yKeyCoord - CELL_SIZE);
                        keyPressAction(xKeyCoord, yKeyCoord - CELL_SIZE);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    keyPressAction(xKeyCoord, yKeyCoord + CELL_SIZE);
                    if (e.isShiftDown()) {
                        keyPressAction(xKeyCoord, yKeyCoord + CELL_SIZE);
                        keyPressAction(xKeyCoord, yKeyCoord + CELL_SIZE);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    keyPressAction(xKeyCoord - CELL_SIZE, yKeyCoord);
                    if (e.isShiftDown()) {
                        keyPressAction(xKeyCoord - CELL_SIZE, yKeyCoord);
                        keyPressAction(xKeyCoord - CELL_SIZE, yKeyCoord);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    keyPressAction(xKeyCoord + CELL_SIZE, yKeyCoord);
                    if (e.isShiftDown()) {
                        keyPressAction(xKeyCoord + CELL_SIZE, yKeyCoord);
                        keyPressAction(xKeyCoord + CELL_SIZE, yKeyCoord);
                    }
                    break;
                // выбор цвета
                case KeyEvent.VK_W:
                    color = color.WHITE;
                    break;
                case KeyEvent.VK_R:
                    color = color.RED;
                    break;
                case KeyEvent.VK_G:
                    color = color.GREEN;
                    break;
                case KeyEvent.VK_B:
                    color = color.BLUE;
                    break;
            }
        }
    }

}
