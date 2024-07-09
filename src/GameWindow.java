import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс главного игрового окна игры "крестики-нолики"
 */
public class GameWindow extends JFrame {

    //region Поля

    private static final int WINDOW_WIDTH = 555;
    private static final int WINDOW_HEIGHT = 507;

    JButton btnStart, btnExit;
    Map map;
    SettingsWindow settingsWindow;

    //endregion


    //region Конструктор

    /**
     * Конструктор окна
     */
    GameWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe");
        setResizable(false);

        btnStart = new JButton("New game");
        btnExit = new JButton("Exit");
        settingsWindow = new SettingsWindow(this);
        map = new Map();

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsWindow.setVisible(true);
            }
        });

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(btnStart);
        bottomPanel.add(btnExit);
        add(bottomPanel, BorderLayout.SOUTH);

        add(map);
        setVisible(true);
    }

    //endregion


    //region Методы

    /**
     * Метод старта новой игры
     * @param mode режим игры
     * @param fieldSizeX размер игрового поля
     * @param fieldSizeY размер игрового поля
     * @param winLen выигрышная длина
     */
    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLen) {
        map.startNewGame(mode, fieldSizeX, fieldSizeX, winLen);
    }

    //endregion
}
