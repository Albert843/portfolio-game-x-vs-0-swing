import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс окна настроек игры "крестики-нолики"
 */
public class SettingsWindow extends JFrame {

    //region Поля

    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 350;

    private JButton btnStart;
    JRadioButton humanVsHuman, humanVsAi;
    JSlider sliderFieldSize, sliderWinLength;
    JLabel labelFieldSize, labelWinLength;
    GameWindow gameWindow;

    //endregion


    //region Конструктор

    /**
     * Конструктор окна
     */
    SettingsWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setLocationRelativeTo(gameWindow);
        setLocation(getX() - WINDOW_WIDTH/2, getY() - WINDOW_HEIGHT/2);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Settings");

        btnStart = new JButton("Start new game");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                int mode = humanVsAi.isSelected() ? 1 : 0;
                gameWindow.startNewGame(mode, sliderFieldSize.getValue(), sliderFieldSize.getValue(), sliderWinLength.getValue());
            }
        });
        add(btnStart, BorderLayout.SOUTH);
        add(createSettingsPanel());
    }

    //endregion


    //region Методы

    /**
     * Метод создания панели режима игры
     * @return панель
     */
    private Component createModePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));

        JLabel jLabel = new JLabel("Выберите режим игры");
        humanVsAi = new JRadioButton("человек против компьютера");
        humanVsHuman = new JRadioButton("Человек против человека");
        humanVsAi.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(humanVsAi);
        buttonGroup.add(humanVsHuman);

        panel.add(jLabel);
        panel.add(humanVsAi);
        panel.add(humanVsHuman);

        return panel;
    }

    /**
     * Метод создания панели выбора размера игрового поля
     * @return панель
     */
    private Component createFieldSizePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));

        JLabel jLabel = new JLabel("Выберите размеры поля");
        labelFieldSize = new JLabel();
        sliderFieldSize = new JSlider(3, 10, 3);
        sliderFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                labelFieldSize.setText("Установленный размер поля: " + sliderFieldSize.getValue());
            }
        });

        panel.add(jLabel);
        panel.add(labelFieldSize);
        panel.add(sliderFieldSize);

        return panel;
    }

    /**
     * Метод создания панели выбора выигрышного размера
     * @return панель
     */
    private Component createWinLengthPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));

        JLabel jLabel = new JLabel("Выберите длину для победы");
        labelWinLength = new JLabel();
        sliderWinLength = new JSlider(3, 10, 3);
        sliderWinLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                labelWinLength.setText("Установленная длина: " + sliderWinLength.getValue());
            }
        });

        panel.add(jLabel);
        panel.add(labelWinLength);
        panel.add(sliderWinLength);

        return panel;
    }

    /**
     * Метод создания панели для трёх предыдущих панелей
     * @return панель
     */
    private Component createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new GridLayout(3, 1));
        settingsPanel.add(createModePanel());
        settingsPanel.add(createFieldSizePanel());
        settingsPanel.add(createWinLengthPanel());

        return settingsPanel;
    }

    //endregion
}
