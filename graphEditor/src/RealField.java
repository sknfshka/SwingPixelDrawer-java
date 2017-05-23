import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class RealField extends JPanel {
    // размер поля
    private final int size;
    // размер клетки
    private final int cellSize;
    // координаты Дамы
    public int x, y;

    private boolean press = false;

    public RealField(int size) {
        this.size = size;
        cellSize = (int)( size / 8 );
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseHandler());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        setBackground(new Color(255,255,255));
        Rectangle2D background  = new Rectangle2D.Double(0, 0, size, size);

        g2.setPaint(Color.white);
        g2.fill(background);
        g2.setPaint(Color.black);
        g2.draw(background);

        drawBlackCells(g2);

        if(x > 0 && x < 9 && y > 0 && y < 9){
            drawQueenLines(x, y, g2);
            drawQueenSymbol(x, y, g2);
        }
    }
    // отрисовка черных клеток на поле
    public void drawBlackCells(Graphics2D g2) {
        for (int j = 0; j < size; j += cellSize) {
            int startX = (( j / cellSize + 1 ) % 2) * cellSize;

            for (int i = startX; i < size; i += cellSize * 2)
                g2.fill(new Rectangle2D.Double(i, j, cellSize, cellSize));

        }
    }
    // отрисовка символа дамы
    public void drawQueenSymbol(int x, int y, Graphics2D g2){
        int w = cellSize * --x;
        int h = cellSize * --y;
        String text = "Q";
        Font font = new Font("Verdana", Font.BOLD, 24);
        g2.setColor(Color.black);

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int textX = (int)((cellSize - fm.stringWidth(text)) / 2);
        int textY = (int)((cellSize - (fm.getHeight() - fm.getDescent())) / 2);
        textY += fm.getAscent() - fm.getDescent();

        g2.drawString(text, w + textX, h + textY);
    }
    // отрисовка линий атаки
    public void drawQueenLines(int x, int y, Graphics2D g2){
        int w = cellSize * --x;
        int h = cellSize * --y;
        Color filter= new Color(0.0f, 0.8901961f, 0.14509805f, 0.65f);

        g2.setPaint(filter);
        // отрисовка верт и гориз линий
        g2.fill(new Rectangle2D.Double(w, 0, cellSize, cellSize * 8));
        g2.fill(new Rectangle2D.Double(0, h, cellSize * 8, cellSize));
        // отрисовка диагоналей
        drawQueenDiagonals(w, h, g2);
    }
    // отрисовка части диагонали
    public void drawQueenDiagonal(int w, int h, int firstRange, int secondRange, Graphics2D g2) {
        int firstIter = firstRange == size ? cellSize : -cellSize;
        int secondIter = secondRange == size ? cellSize : -cellSize;
        int i = w + firstIter;
        int j = h + secondIter;

        while ( i != firstRange && j != secondRange) {
            g2.fill(new Rectangle2D.Double(i, j, cellSize, cellSize));

            i += firstIter;
            j += secondIter;
        }
    }
    // отрисовка диагоналей
    public void drawQueenDiagonals(int w, int h, Graphics2D g2) {
        drawQueenDiagonal(w, h, size, size, g2);
        drawQueenDiagonal(w, h, -cellSize, -cellSize, g2);
        drawQueenDiagonal(w, h, size, -cellSize, g2);
        drawQueenDiagonal(w, h, -cellSize, size, g2);
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int xQueen = (e.getX() / cellSize) + 1;
            int yQueen = (e.getY() / cellSize) + 1;

            if( xQueen == x && yQueen == y ) {
                press = true;
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (press) {
                press = false;

                int xQueen = (e.getX() / cellSize) + 1;
                int yQueen = (e.getY() / cellSize) + 1;

                if(xQueen > 0 && xQueen < 9 && yQueen > 0 && yQueen < 9) {
                    x = xQueen;
                    y = yQueen;
                }
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (press) {
                int xQueen = (e.getX() / cellSize) + 1;
                int yQueen = (e.getY() / cellSize) + 1;

                if(xQueen > 0 && xQueen < 9 && yQueen > 0 && yQueen < 9) {
                    x = xQueen;
                    y = yQueen;
                    repaint();
                }
            }
        }
    }

}
