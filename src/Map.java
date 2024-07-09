import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Класс игрового поля игры "крестики-нолики"
 */
public class Map extends JPanel {

    //region Поля

    private static final Random RANDOM = new Random();
    private final int EMPTY_DOT = 0;
    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private static final int PADDING = 10;          //  Отступ для отрисовки элемента ('0' или 'X') в ячейке

    private static final int STATE_GAME_IN_PROCESS = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;
    private static final int STATE_DRAW = 3;

    private static final String MSG_WIN_HUMAN = "Победил игрок!";
    private static final String MSG_WIN_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья!";

    private int gameStateType;
    private int width, height, cellWidth, cellHeight;
    private int mode, fieldSizeX, fieldSizeY, winLen;
    private int[][] field;
    private boolean gameWork;

    //endregion


    //region Конструкторы

    /**
     * Конструктор игрового поля
     */
    Map() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (gameWork) {
                    update(e);
                }
            }
        });
    }

    //endregion


    //region Методы

    /**
     * Инициализация игрового поля
     */
    private void initMap() {
        field = new int[fieldSizeY][fieldSizeX];
    }

    /**
     * Начало новой игры
     * @param mode режим игры (человек против компьютера, человек против человека)
     * @param fieldSizeX размер игрового поля
     * @param fieldSizeY размер игрового поля
     * @param winLen выигрышная длина
     */
    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLen) {
        this.mode = mode;
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLen = winLen;

        initMap();
        gameWork = true;
        gameStateType = STATE_GAME_IN_PROCESS;

        repaint();
    }

    /**
     * Процесс игры
     * @param mouseEvent определение координаты по клику мыши
     */
    private void update(MouseEvent mouseEvent) {
        int x = mouseEvent.getX() / cellWidth;
        int y = mouseEvent.getY() / cellHeight;
        if (!isValidCell(x, y) || !isEmptyCell(x, y)) return;
        field[y][x] = HUMAN_DOT;
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;

        aiTurn();
        repaint();
        checkEndGame(AI_DOT, STATE_WIN_AI);
    }

    /**
     * Проверка доступности ячейки игрового поля
     * @param x координата игрового поля
     * @param y координата игрового поля
     * @return доступность ячейки игрового поля
     */
    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Проверка пустая ли ячейка игрового поля
     * @param x координата игрового поля
     * @param y координата игрового поля
     * @return пустая ли ячейка игрового поля
     */
    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }

    /**
     * Ход компьютера
     */
    private void aiTurn() {
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    /**
     * Проверка на ничью
     * @return
     */
    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    /**
     * Проверка окончания игры
     * @param dot фишка игрока
     * @param gameOverType
     * @return
     */
    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin(dot)) {
            this.gameStateType = gameOverType;
            repaint();
            return true;
        } else if (isMapFull()) {
            this.gameStateType = STATE_DRAW;
            repaint();
            return true;
        }
        return false;
    }

    /**
     * Проверка на победу
     * @param dot
     * @return
     */
    private boolean checkWin(int dot) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLen, dot)) return true;
                if (checkLine(i, j, 1, 1, winLen, dot)) return true;
                if (checkLine(i, j, 0, 1, winLen, dot)) return true;
                if (checkLine(i, j, 1, -1, winLen, dot)) return true;
            }
        }
        return false;
    }

    /**
     * Проверка на победу по направлению
     * @param x координата х
     * @param y координата y
     * @param vx направление по х
     * @param vy направление по y
     * @param len выигрышная длина
     * @param dot фишка игрока
     * @return true если линия подряд идущих фишек по напрвлению равна выигрышной длине
     */
    private boolean checkLine(int x, int y, int vx, int vy, int len, int dot) {
        int far_x = x + (len - 1) * vx;
        int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (field[y + i * vy][x + i * vx] != dot) {
                return false;
            }
        }
        return true;
    }

    /**
     * Переопределенный метод для отрсовки игрового поля
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameWork) {
            render(g);
        }
    }

    /**
     * Отрисовка игрового поля
     * @param g
     */
    private void render(Graphics g) {
        width = getWidth();
        height = getHeight();
        cellWidth = width / fieldSizeX;
        cellHeight = height / fieldSizeY;

        g.setColor(Color.BLACK);
        for (int h = 0; h < fieldSizeY; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, width, y);
        }
        for (int w = 0; w < fieldSizeX; w++) {
            int x = w * cellWidth;
            g.drawLine(x,0, x, height);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) {
                    continue;
                }
                if (field[y][x] == HUMAN_DOT) {
                    g.drawLine(x * cellWidth + PADDING, y * cellHeight + PADDING,
                            (x + 1) * cellWidth - PADDING, (y + 1) * cellHeight - PADDING);
                    g.drawLine(x * cellWidth + PADDING, (y + 1) * cellHeight - PADDING,
                            (x + 1) * cellWidth - PADDING, y * cellHeight + PADDING);
                } else if (field[y][x] == AI_DOT) {
                    g.drawOval(x * cellWidth + PADDING, y * cellHeight + PADDING,
                            cellWidth - PADDING * 2, cellHeight - PADDING * 2);
                } else {
                    throw new RuntimeException("Unexpected value " + field[y][x] +
                            " in cell: x=" + x + " y=" + y);
                }
            }
            if (gameStateType != STATE_GAME_IN_PROCESS) {
                showMessage(g);
            }
        }
    }

    /**
     * Метод для вывода сообщений о статусе окончания игры
     * @param g
     */
    private void showMessage(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, getHeight() / 2, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameStateType) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2 + 60);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 20, getHeight() / 2 + 60);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 70, getHeight() / 2 + 60);
                break;
            default:
                throw new RuntimeException("Unexpected gameOverState: " + gameStateType);
        }
        gameWork = false;
    }

    //endregion
}
