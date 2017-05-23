import javax.swing.*;
import java.awt.*;

public class Field extends JFrame {

    public Field() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)(screenSize.getWidth() / 2);
        int screenHeight = (int)(screenSize.getHeight() / 2);
        // расчет размера панели исходя из размеров экрана и велчичины клетки
        int calcInnerWidth = ((int)(screenWidth / Panel.CELL_SIZE)) * Panel.CELL_SIZE;
        int calcInnerHeight =  ((int)(screenHeight / Panel.CELL_SIZE)) * Panel.CELL_SIZE;

        Panel panel = new Panel(calcInnerWidth, calcInnerHeight);
        add(panel);

        getContentPane().setPreferredSize(new Dimension(calcInnerWidth + 1, calcInnerHeight + Panel.TOOL_BAR_SIZE ));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
}
