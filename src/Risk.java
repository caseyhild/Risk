import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class Risk extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    private final int width;
    private final int height;
    Timer tm = new Timer(5, this);
    private int frame;
    private String gameState;
    private int numPlayers;
    private Player[] players;
    private int colorIndex;
    private final ArrayList<Integer> colors;
    private int turn;
    private String mode;
    private int selectedRow;
    private int selectedCol;
    private int[][] board;
    private int[][] armiesCopy;
    private int size;
    private int topBorder;
    private int border;
    private int startingArmies;
    private int neutralArmies;
    private int armiesPerTurn;
    private boolean deployArmiesScreen;
    private int deployArmiesScreenX;
    private int deployArmiesScreenY;
    private int deployArmiesScreenClickX;
    private int deployArmiesScreenClickY;
    private boolean draggingScreen;
    private int originalScreenX;
    private int originalScreenY;
    private String numDeployedString;
    private double offenseKillRate;
    private double defenseKillRate;
    private boolean showAttackArrows;
    private int attackingRow;
    private int attackingCol;
    private ArrayList<Attack>[] attacks;
    private Vector current;
    private Vector player;
    private int start;
    private boolean selectArmiesScreen;
    private boolean selectArmiesScreenClosed;
    private int numAvailable;
    private int numAttacking;
    private String numAttackingString;
    private int cursorPos;
    private int time;
    private int remainder;
    private boolean draggingArmiesAmount;
    private int mouseX;
    private int mouseY;
    private boolean mouseClicked;
    private boolean mousePressed;
    private boolean keyPressed;
    private boolean keyReleased;
    private KeyEvent key;
    public Risk()
    {
        width = 800;
        height = 600;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        mouseX = 0;
        mouseY = 0;
        mouseClicked = false;
        mousePressed = false;
        keyPressed = false;
        keyReleased = false;
        key = null;
        colors = new ArrayList<>();
        colors.add(rgbNum(255, 0, 0));
        colors.add(rgbNum(0, 255, 0));
        colors.add(rgbNum(0, 0, 255));
        colors.add(rgbNum(192, 192, 0));
        colors.add(rgbNum(255, 0, 255));
        colors.add(rgbNum(0, 255, 255));
        colors.add(rgbNum(255, 128, 0));
        colors.add(rgbNum(128, 0, 255));
        colors.add(rgbNum(0, 128, 0));
        gameState = "intro";
        frame = 0;
        setBackground(new Color(128, 128, 128));
    }

    public void addNotify()
    {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        switch (gameState) {
            case "intro" -> {
                colorIndex = 2;
                numPlayers = 8;
                size = 10;
                startingArmies = 5;
                neutralArmies = 2;
                armiesPerTurn = 5;
                offenseKillRate = 0.6;
                defenseKillRate = 0.7;
                gameState = "menu";
            }
            case "menu" -> {
                Font font = new Font("Verdana", Font.PLAIN, height / 3);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(new Color(0, 0, 0));
                g.drawString("RISK", width / 2 - fm.stringWidth("RISK") / 2, height / 8 + fm.getAscent() / 2);
                font = new Font("Verdana", Font.PLAIN, height / 12);
                g.setFont(font);
                fm = g.getFontMetrics();
                if (mouseX >= 3 * width / 8 && mouseX <= 5 * width / 8 && mouseY >= height / 2 && mouseY <= 5 * height / 8)
                    g.setColor(new Color(64, 64, 64));
                else
                    g.setColor(new Color(128, 128, 128));
                g.fillRoundRect(3 * width / 8, height / 2, width / 4, height / 8, height / 16, height / 16);
                g.setColor(new Color(0, 0, 0));
                g.drawRoundRect(3 * width / 8, height / 2, width / 4, height / 8, height / 16, height / 16);
                g.drawString("PLAY", width / 2 - fm.stringWidth("PLAY") / 2, 9 * height / 16 + fm.getAscent() / 2 - 5);
                if (mouseX >= 5 * width / 16 && mouseX <= 11 * width / 16 && mouseY >= 11 * height / 16 && mouseY <= 13 * height / 16)
                    g.setColor(new Color(64, 64, 64));
                else
                    g.setColor(new Color(128, 128, 128));
                g.fillRoundRect(5 * width / 16, 11 * height / 16, 3 * width / 8, height / 8, height / 16, height / 16);
                g.setColor(new Color(0, 0, 0));
                g.drawRoundRect(5 * width / 16, 11 * height / 16, 3 * width / 8, height / 8, height / 16, height / 16);
                g.drawString("SET         ", width / 2 - fm.stringWidth("SET         ") / 2 + 1, 3 * height / 4 + fm.getAscent() / 2 - 5);
                g.drawString("      TINGS", width / 2 - fm.stringWidth("      TINGS") / 2 - 1, 3 * height / 4 + fm.getAscent() / 2 - 5);
                if (mouseClicked && mouseX >= 3 * width / 8 && mouseX <= 5 * width / 8 && mouseY >= height / 2 && mouseY <= 5 * height / 8)
                    startGame();
                else if (mouseClicked && mouseX >= 5 * width / 16 && mouseX <= 11 * width / 16 && mouseY >= 11 * height / 16 && mouseY <= 13 * height / 16)
                    gameState = "settings";
            }
            case "settings" -> {
                Font font = new Font("Verdana", Font.PLAIN, height / 6);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(new Color(0, 0, 0));
                g.drawString("SET         ", width / 2 - fm.stringWidth("SET         ") / 2 - 1, height / 8 + fm.getAscent() / 2 - 5);
                g.drawString("      TINGS", width / 2 - fm.stringWidth("      TINGS") / 2 + 1, height / 8 + fm.getAscent() / 2 - 5);
                font = new Font("Verdana", Font.PLAIN, height / 30);
                g.setFont(font);
                fm = g.getFontMetrics();
                g.drawLine(0, height / 4, width, height / 4);
                g.drawString("Color:", width / 16, height / 3);
                for (int i = 0; i < colors.size(); i++) {
                    if (mouseClicked && mouseX >= 5 * width / 64 + fm.stringWidth("Color:") + (5 * fm.getAscent() / 4) * i && mouseX <= 5 * width / 64 + fm.stringWidth("Color:") + (5 * fm.getAscent() / 4) * i + fm.getAscent() && mouseY >= height / 3 + 3 - fm.getAscent() && mouseY <= height / 3 + 3)
                        colorIndex = i;
                    g.setColor(new Color(getR(colors.get(i)), getG(colors.get(i)), getB(colors.get(i))));
                    g.fillRect(5 * width / 64 + fm.stringWidth("Color:") + (5 * fm.getAscent() / 4) * i, height / 3 + 3 - fm.getAscent(), fm.getAscent(), fm.getAscent());
                    g.setColor(new Color(0, 0, 0));
                    g.drawRect(5 * width / 64 + fm.stringWidth("Color:") + (5 * fm.getAscent() / 4) * i + 1, height / 3 + 3 - fm.getAscent() + 1, fm.getAscent() - 2, fm.getAscent() - 2);
                    g.drawRect(5 * width / 64 + fm.stringWidth("Color:") + (5 * fm.getAscent() / 4) * i, height / 3 + 3 - fm.getAscent(), fm.getAscent(), fm.getAscent());
                }
                g.setColor(new Color(255, 255, 0));
                g.drawRect(5 * width / 64 + fm.stringWidth("Color:") + (5 * fm.getAscent() / 4) * colorIndex + 1, height / 3 + 3 - fm.getAscent() + 1, fm.getAscent() - 2, fm.getAscent() - 2);
                g.drawRect(5 * width / 64 + fm.stringWidth("Color:") + (5 * fm.getAscent() / 4) * colorIndex, height / 3 + 3 - fm.getAscent(), fm.getAscent(), fm.getAscent());
                g.setColor(new Color(0, 0, 0));
                g.drawString("Number of AIs: 7", width / 16, height / 3 + height / 20);
                g.drawString("Board Size: 10x10", width / 16, height / 3 + height / 10);
                if (mouseX >= 57 * width / 64 && mouseX <= 63 * width / 64 && mouseY >= height / 64 && mouseY <= height / 16)
                    g.setColor(new Color(64, 64, 64));
                else
                    g.setColor(new Color(128, 128, 128));
                g.fillRoundRect(57 * width / 64, height / 64, 3 * width / 32, 3 * height / 64, height / 60, height / 60);
                g.setColor(new Color(0, 0, 0));
                g.drawRoundRect(57 * width / 64, height / 64, 3 * width / 32, 3 * height / 64, height / 60, height / 60);
                g.drawString("BACK", 15 * width / 16 - fm.stringWidth("BACK") / 2, 5 * height / 128 - 2 + fm.getAscent() / 2);
                if (mouseClicked && mouseX >= 57 * width / 64 && mouseX <= 63 * width / 64 && mouseY >= height / 64 && mouseY <= height / 16)
                    gameState = "menu";
            }
            case "play" -> {
                if (!selectArmiesScreen && !draggingArmiesAmount && !draggingScreen) {
                    if (mouseClicked && mouseX >= width / 64 && mouseX <= 17 * width / 64 && mouseY >= 9 * height / 32 && mouseY <= 53 * height / 160)
                        mode = "deploy";
                    else if (mouseClicked && mouseX >= width / 64 && mouseX <= 17 * width / 64 && mouseY >= 11 * height / 32 && mouseY <= 63 * height / 160)
                        mode = "attack";
                    else if (mouseClicked && mouseX >= width / 64 && mouseX <= 17 * width / 64 && mouseY >= 13 * height / 32 && mouseY <= 73 * height / 160)
                        mode = "finish";
                }
                boolean click = false;
                for (int r = 0; r < size; r++) {
                    for (int c = 0; c < size; c++) {
                        if (mouseClicked && !draggingArmiesAmount && !draggingScreen && !selectArmiesScreen && mouseX >= width - (height - topBorder - border) + (height - topBorder - 2 * border) * c / size && mouseX <= width - (height - topBorder - border) + (height - topBorder - 2 * border) * (c + 1) / size && mouseY >= topBorder + border + (height - topBorder - 2 * border) * r / size && mouseY <= topBorder + border + (height - topBorder - 2 * border) * (r + 1) / size) {
                            selectedRow = r;
                            selectedCol = c;
                            click = true;
                        }
                    }
                }
                if (mouseClicked && !draggingArmiesAmount && !draggingScreen && !selectArmiesScreen && !click) {
                    selectedRow = -1;
                    selectedCol = -1;
                }
                Font font = new Font("Verdana", Font.PLAIN, height / 6);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(new Color(0, 0, 0));
                g.drawString("RISK", width / 2 - fm.stringWidth("RISK") / 2, height / 8 + fm.getAscent() / 2 - 10);
                font = new Font("Verdana", Font.PLAIN, height / 30);
                g.setFont(font);
                fm = g.getFontMetrics();
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, topBorder, width, (height - topBorder));
                g.setColor(new Color(255, 255, 255));
                g.drawString("Turn " + turn, width - (height - topBorder) - fm.stringWidth("Turn " + turn), topBorder + border + fm.getAscent());
                g.setColor(new Color(128, 128, 128));
                switch (mode) {
                    case "deploy" ->
                            g.fillRoundRect(width / 64, 9 * height / 32 - 3, width / 4, height / 20, height / 20, height / 20);
                    case "attack" ->
                            g.fillRoundRect(width / 64, 11 * height / 32 - 3, width / 4, height / 20, height / 20, height / 20);
                    case "finish" ->
                            g.fillRoundRect(width / 64, 13 * height / 32 - 3, width / 4, height / 20, height / 20, height / 20);
                }
                g.setColor(new Color(255, 255, 255));
                g.drawString("Deploy", width / 32, 5 * height / 16);
                g.drawString("Attack/Transfer", width / 32, 3 * height / 8);
                g.drawString("Finish", width / 32, 7 * height / 16);
                font = new Font("Verdana", Font.PLAIN, Math.max(160 / size, 8));
                g.setFont(font);
                fm = g.getFontMetrics();
                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {
                        g.setColor(new Color(getR(players[board[row][col]].getColor()), getG(players[board[row][col]].getColor()), getB(players[board[row][col]].getColor())));
                        g.fillRect(width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col / size, topBorder + border + ((height - topBorder) - 2 * border) * row / size, ((height - topBorder) - 2 * border) / size + 1, ((height - topBorder) - 2 * border) / size + 1);
                        g.setColor(new Color(0, 0, 0));
                        g.drawString("" + players[board[row][col]].getArmies(row, col), width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col / size + ((height - topBorder) - 2 * border) / size / 2 - fm.stringWidth("" + players[board[row][col]].getArmies(row, col)) / 2, topBorder + border + ((height - topBorder) - 2 * border) * row / size + ((height - topBorder) - 2 * border) / size / 2 + fm.getAscent() / 2 - 2);
                    }
                }
                g.setColor(new Color(0, 0, 0));
                for (int i = 0; i < size; i++) {
                    g.drawLine(width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * i / size, topBorder + border, width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * i / size, topBorder + (height - topBorder) - border);
                    g.drawLine(width - (height - topBorder) + border, topBorder + border + ((height - topBorder) - 2 * border) * i / size, width - border, topBorder + border + ((height - topBorder) - 2 * border) * i / size);
                }
                if (!selectArmiesScreen && selectedRow >= 0 && selectedCol >= 0) {
                    g.setColor(new Color(255, 255, 0));
                    g.drawRect(width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * selectedCol / size + 1, topBorder + border + ((height - topBorder) - 2 * border) * selectedRow / size + 1, ((height - topBorder) - 2 * border) / size - 2, ((height - topBorder) - 2 * border) / size - 2);
                    g.drawRect(width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * selectedCol / size, topBorder + border + ((height - topBorder) - 2 * border) * selectedRow / size, ((height - topBorder) - 2 * border) / size, ((height - topBorder) - 2 * border) / size);
                    g.drawRect(width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * selectedCol / size - 1, topBorder + border + ((height - topBorder) - 2 * border) * selectedRow / size - 1, ((height - topBorder) - 2 * border) / size + 2, ((height - topBorder) - 2 * border) / size + 2);
                }
                for (int i = 0; i < attacks[0].size(); i++) {
                    drawArrow(attacks[0].get(i).getRow1(), attacks[0].get(i).getCol1(), attacks[0].get(i).getRow2(), attacks[0].get(i).getCol2(), new Color(getR(players[1].getColor()), getG(players[1].getColor()), getB(players[1].getColor()), 128), g);
                }
                if (selectedRow >= 0 && selectedCol >= 0) {
                    g.setColor(new Color(getR(players[board[selectedRow][selectedCol]].getColor()), getG(players[board[selectedRow][selectedCol]].getColor()), getB(players[board[selectedRow][selectedCol]].getColor())));
                    g.fillRect(width / 30, 2 * height / 3, height / 10, height / 10);
                    g.setColor(new Color(128, 128, 128));
                    g.drawRect(width / 30, 2 * height / 3, height / 10, height / 10);
                    g.drawRect(width / 30 + 1, 2 * height / 3 + 1, height / 10 - 2, height / 10 - 2);
                    int x = width / 30 + height / 10 + height / 60;
                    g.drawString(players[board[selectedRow][selectedCol]].getArmies(selectedRow, selectedCol) + " armies", x, 2 * height / 3 + height / 40 + fm.getAscent() / 2 - 1);
                    g.drawString("Controlled by " + players[board[selectedRow][selectedCol]].getName(), x, 2 * height / 3 + 3 * height / 40 + fm.getAscent() / 2 - 1);
                }
                switch (mode) {
                    case "deploy" -> {
                        if (mouseClicked && ((Human) players[1]).getArmiesDeployed() < ((Human) players[1]).getArmiesPerTurn() && selectedRow >= 0 && selectedCol >= 0 && board[selectedRow][selectedCol] == 1 && mouseX >= width - (height - topBorder - border) + (height - topBorder - 2 * border) * selectedCol / size && mouseX <= width - (height - topBorder - border) + (height - topBorder - 2 * border) * (selectedCol + 1) / size && mouseY >= topBorder + border + (height - topBorder - 2 * border) * selectedRow / size && mouseY <= topBorder + border + (height - topBorder - 2 * border) * (selectedRow + 1) / size) {
                            ((Human) players[1]).deploy(selectedRow, selectedCol);
                        }
                        if (mouseClicked && selectedRow >= 0 && selectedCol >= 0 && board[selectedRow][selectedCol] == 1 && mouseX >= width - (height - topBorder - border) + (height - topBorder - 2 * border) * selectedCol / size && mouseX <= width - (height - topBorder - border) + (height - topBorder - 2 * border) * (selectedCol + 1) / size && mouseY >= topBorder + border + (height - topBorder - 2 * border) * selectedRow / size && mouseY <= topBorder + border + (height - topBorder - 2 * border) * (selectedRow + 1) / size) {
                            int numAlreadyDeployed = 0;
                            for (int r = 0; r < size; r++) {
                                for (int c = 0; c < size; c++) {
                                    if (players[1].getArmies(r, c) - armiesCopy[r][c] > 0 && !(selectedRow == r && selectedCol == c))
                                        numAlreadyDeployed += players[1].getArmies(r, c) - armiesCopy[r][c];
                                }
                            }
                            numAvailable = ((Human) players[1]).getArmiesPerTurn() - numAlreadyDeployed;
                            numDeployedString = "" + (players[1].getArmies(selectedRow, selectedCol) - armiesCopy[selectedRow][selectedCol]);
                            cursorPos = numDeployedString.length();
                            deployArmiesScreen = true;
                        }
                        g.setColor(new Color(255, 255, 255));
                        font = new Font("Verdana", Font.PLAIN, height / 30);
                        g.setFont(font);
                        g.drawString("Armies Deployed: " + ((Human) players[1]).getArmiesDeployed() + "/" + ((Human) players[1]).getArmiesPerTurn(), width / 32, 9 * height / 16);
                        font = new Font("Verdana", Font.PLAIN, height / 40);
                        g.setFont(font);
                        fm = g.getFontMetrics();
                        if (mouseX >= width / 32 && mouseX <= 5 * width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            g.setColor(new Color(32, 32, 32));
                        else
                            g.setColor(new Color(0, 0, 0));
                        g.fillRoundRect(width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.setColor(new Color(255, 255, 255));
                        g.drawRoundRect(width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.drawString("Start Over", 3 * width / 32 - fm.stringWidth("Start Over") / 2, 61 * height / 64 - 2 + fm.getAscent() / 2);
                        if (mouseX >= (width - (height - topBorder)) - 5 * width / 32 && mouseX <= (width - (height - topBorder)) - width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            g.setColor(new Color(32, 32, 32));
                        else
                            g.setColor(new Color(0, 0, 0));
                        g.fillRoundRect((width - (height - topBorder)) - 5 * width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.setColor(new Color(255, 255, 255));
                        g.drawRoundRect((width - (height - topBorder)) - 5 * width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.drawString("Next", (width - (height - topBorder)) - 3 * width / 32 - fm.stringWidth("Next") / 2, 61 * height / 64 - 2 + fm.getAscent() / 2);
                        if (mouseClicked && mouseX >= width / 32 && mouseX <= 5 * width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            resetArmies();
                        if (mouseClicked && mouseX >= (width - (height - topBorder)) - 5 * width / 32 && mouseX <= (width - (height - topBorder)) - width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            mode = "attack";
                        if (deployArmiesScreen)
                            drawDeployArmiesScreen(g);
                    }
                    case "attack" -> {
                        if (mouseClicked && !selectArmiesScreen && !showAttackArrows && selectedRow >= 0 && selectedCol >= 0 && board[selectedRow][selectedCol] == 1 && mouseX >= width - (height - topBorder - border) + (height - topBorder - 2 * border) * selectedCol / size && mouseX <= width - (height - topBorder - border) + (height - topBorder - 2 * border) * (selectedCol + 1) / size && mouseY >= topBorder + border + (height - topBorder - 2 * border) * selectedRow / size && mouseY <= topBorder + border + (height - topBorder - 2 * border) * (selectedRow + 1) / size) {
                            attackingRow = selectedRow;
                            attackingCol = selectedCol;
                            showAttackArrows = true;
                        }
                        if (selectedRow < 0 || selectedCol < 0 || (!(selectedRow == attackingRow && selectedCol == attackingCol) && !connected(selectedRow, selectedCol, attackingRow, attackingCol))) {
                            if (selectedRow >= 0 && selectedCol >= 0 && board[selectedRow][selectedCol] != 1) {
                                attackingRow = -1;
                                attackingCol = -1;
                                showAttackArrows = false;
                            } else {
                                attackingRow = selectedRow;
                                attackingCol = selectedCol;
                            }
                        }
                        if (showAttackArrows)
                            drawAttackArrows(attackingRow, attackingCol, new Color(255, 0, 0, 128), g);
                        if (mouseClicked && !selectArmiesScreen && showAttackArrows && attackingRow >= 0 && attackingCol >= 0 && !(selectedRow == attackingRow && selectedCol == attackingCol) && connected(selectedRow, selectedCol, attackingRow, attackingCol)) {
                            int numAlreadyAttacking = 0;
                            for (int i = 0; i < attacks[0].size(); i++) {
                                if (attacks[0].get(i).getRow1() == attackingRow && attacks[0].get(i).getCol1() == attackingCol && !(attacks[0].get(i).getRow2() == selectedRow && attacks[0].get(i).getCol2() == selectedCol))
                                    numAlreadyAttacking += attacks[0].get(i).getNum();
                            }
                            numAvailable = players[1].getArmies(attackingRow, attackingCol) - numAlreadyAttacking;
                            numAttacking = numAvailable;
                            numAttackingString = "" + numAttacking;
                            cursorPos = numAttackingString.length();
                            selectArmiesScreen = true;
                        }
                        font = new Font("Verdana", Font.PLAIN, height / 40);
                        g.setFont(font);
                        fm = g.getFontMetrics();
                        if (!selectArmiesScreen && mouseX >= width / 32 && mouseX <= 5 * width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            g.setColor(new Color(32, 32, 32));
                        else
                            g.setColor(new Color(0, 0, 0));
                        g.fillRoundRect(width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.setColor(new Color(255, 255, 255));
                        g.drawRoundRect(width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.drawString("Start Over", 3 * width / 32 - fm.stringWidth("Start Over") / 2, 61 * height / 64 - 2 + fm.getAscent() / 2);
                        if (!selectArmiesScreen && mouseX >= (width - (height - topBorder)) - 5 * width / 32 && mouseX <= (width - (height - topBorder)) - width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            g.setColor(new Color(32, 32, 32));
                        else
                            g.setColor(new Color(0, 0, 0));
                        g.fillRoundRect((width - (height - topBorder)) - 5 * width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.setColor(new Color(255, 255, 255));
                        g.drawRoundRect((width - (height - topBorder)) - 5 * width / 32, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.drawString("Next", (width - (height - topBorder)) - 3 * width / 32 - fm.stringWidth("Next") / 2, 61 * height / 64 - 2 + fm.getAscent() / 2);
                        if (mouseClicked && !selectArmiesScreen && mouseX >= width / 32 && mouseX <= 5 * width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            resetAttacks();
                        if (mouseClicked && !selectArmiesScreen && mouseX >= (width - (height - topBorder)) - 5 * width / 32 && mouseX <= (width - (height - topBorder)) - width / 32 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            mode = "finish";
                        if (selectArmiesScreen)
                            drawSelectArmiesScreen(g);
                        if (selectArmiesScreenClosed) {
                            if (numAttacking >= 0)
                                numAttackingString = "" + numAttacking;
                            else
                                numAttackingString = "" + numAvailable;
                            int index = -1;
                            for (int i = 0; i < attacks[0].size(); i++) {
                                if (attacks[0].get(i).getRow1() == attackingRow && attacks[0].get(i).getCol1() == attackingCol && attacks[0].get(i).getRow2() == selectedRow && attacks[0].get(i).getCol2() == selectedCol) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index >= 0 && numAttacking >= 0)
                                attacks[0].remove(index);
                            if (numAttacking > 0)
                                attacks[0].add(new Attack(attackingRow, attackingCol, selectedRow, selectedCol, numAttacking));
                            attackingRow = -1;
                            attackingCol = -1;
                            selectedRow = -1;
                            selectedCol = -1;
                            showAttackArrows = false;
                            selectArmiesScreenClosed = false;
                        }
                    }
                    case "finish" -> {
                        font = new Font("Verdana", Font.PLAIN, height / 40);
                        g.setFont(font);
                        fm = g.getFontMetrics();
                        if (mouseX >= (width - (height - topBorder)) / 2 - width / 16 && mouseX <= (width - (height - topBorder)) / 2 + width / 16 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            g.setColor(new Color(32, 32, 32));
                        else
                            g.setColor(new Color(0, 0, 0));
                        g.fillRoundRect((width - (height - topBorder)) / 2 - width / 16, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.setColor(new Color(255, 255, 255));
                        g.drawRoundRect((width - (height - topBorder)) / 2 - width / 16, 15 * height / 16, width / 8, height / 32, height / 32, height / 32);
                        g.drawString("Finish", (width - (height - topBorder)) / 2 - fm.stringWidth("Finish") / 2, 61 * height / 64 - 2 + fm.getAscent() / 2);
                        if (mouseClicked && mouseX >= (width - (height - topBorder)) / 2 - width / 16 && mouseX <= (width - (height - topBorder)) / 2 + width / 16 && mouseY >= 15 * height / 16 && mouseY <= 31 * height / 32)
                            nextTurn();
                    }
                }
                if (keyReleased && key.getKeyCode() == KeyEvent.VK_A)
                    nextTurn();
            }
            case "win" -> {
                Font font = new Font("Verdana", Font.PLAIN, height / 4);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(new Color(0, 0, 0));
                g.drawString("You Win!", width / 2 - fm.stringWidth("You Win!") / 2, height / 2 + fm.getAscent() / 2);
            }
            case "lose" -> {
                Font font = new Font("Verdana", Font.PLAIN, height / 4);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(new Color(0, 0, 0));
                g.drawString("You Lose!", width / 2 - fm.stringWidth("You Lose!") / 2, height / 2 + fm.getAscent() / 2);
            }
        }
        if(mouseClicked)
            mouseClicked = false;
        if(keyReleased)
            keyReleased = false;
        tm.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        frame++;
        repaint();
    }

    public void startGame()
    {
        players = new Player[numPlayers + 1];
        board = new int[size][size];
        armiesCopy = new int[size][size];
        int neutralColor = rgbNum(192, 192, 192);
        players[0] = new Neutral(neutralColor, defenseKillRate, neutralArmies, size);
        int color = colors.remove(colorIndex);
        players[1] = new Human("Player 1", color, offenseKillRate, defenseKillRate, armiesPerTurn, size);
        int row = (int) (Math.random() * size);
        int col = (int) (Math.random() * size);
        players[1].setArmies(row, col, startingArmies);
        players[0].setArmies(row, col, 0);
        board[row][col] = 1;
        player = new Vector(row, col);
        current = new Vector();
        for(int i = 2; i <= numPlayers; i++)
        {
            int random = (int) (Math.random() * colors.size());
            if(!colors.isEmpty())
                color = colors.remove(random);
            else
                color = rgbNum((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            players[i] = new AI("AI " + (i - 1), color, offenseKillRate, defenseKillRate, armiesPerTurn, size);
            do
            {
                row = (int) (Math.random() * size);
                col = (int) (Math.random() * size);
            }while(board[row][col] != 0);
            players[i].setArmies(row, col, startingArmies);
            players[0].setArmies(row, col, 0);
            board[row][col] = i;
        }
        setArmiesPerTurn();
        for(int r = 0; r < size; r++)
        {
            for(int c = 0; c < size; c++)
            {
                armiesCopy[r][c] = players[1].getArmies(r, c);
            }
        }
        border = height/60;
        topBorder = height/4;
        selectedRow = -1;
        selectedCol = -1;
        attackingRow = -1;
        attackingCol = -1;
        showAttackArrows = false;
        attacks = new ArrayList[numPlayers];
        for(int i = 0; i < numPlayers; i++)
        {
            attacks[i] = new ArrayList<>();
        }
        deployArmiesScreen = false;
        deployArmiesScreenX = width/2;
        deployArmiesScreenY = height/2;
        deployArmiesScreenClickX = width/2;
        deployArmiesScreenClickY = height/2;
        draggingScreen = false;
        originalScreenX = width/2;
        originalScreenY = height/2;
        numDeployedString = "";
        selectArmiesScreen = false;
        selectArmiesScreenClosed = false;
        numAvailable = 0;
        numAttacking = 0;
        numAttackingString = "";
        cursorPos = 0;
        time = 0;
        remainder = -1;
        draggingArmiesAmount = false;
        turn = 1;
        start = (int) (Math.random() * numPlayers);
        mode = "deploy";
        gameState = "play";
        mouseClicked = false;
        mousePressed = false;
        keyPressed = false;
        keyReleased = false;
    }

    public void resetArmies()
    {
        ((Human) players[1]).setArmiesDeployed(0);
        for(int r = 0; r < size; r++)
        {
            for(int c = 0; c < size; c++)
            {
                players[1].setArmies(r, c, armiesCopy[r][c]);
            }
        }
    }

    public void resetAttacks()
    {
        attacks[0].clear();
        numAttacking = 0;
        numAttackingString = "";
    }

    public void nextTurn()
    {
        setAIDeployments();
        setAIAttacks();
        doAttacks();
        for(int i = 0; i < numPlayers; i++)
        {
            attacks[i].clear();
        }
        ((Human) players[1]).setArmiesDeployed(0);
        for(int r = 0; r < size; r++)
        {
            for(int c = 0; c < size; c++)
            {
                armiesCopy[r][c] = players[1].getArmies(r, c);
            }
        }
        if(win())
        {
            gameState = "win";
            setBackground(new Color(0, 0, 255));
        }
        else if(lose())
        {
            gameState = "lose";
            setBackground(new Color(255, 0, 0));
        }
        else
        {
            deployArmiesScreen = false;
            draggingScreen = false;
            selectArmiesScreen = false;
            selectArmiesScreenClosed = false;
            draggingArmiesAmount = false;
            setArmiesPerTurn();
            start++;
            start %= numPlayers;
            mode = "deploy";
            turn++;
        }
    }

    public void drawAttackArrows(int row, int col, Color color, Graphics g)
    {
        if(row >= 0 && row < size && col >= 0 && col < size - 1)
            drawArrow(row, col, row, col + 1, 0, color, g);
        if(row >= 1 && row < size && col >= 0 && col < size)
            drawArrow(row, col, row - 1, col, 90, color, g);
        if(row >= 0 && row < size && col >= 1 && col < size)
            drawArrow(row, col, row, col - 1, 180, color, g);
        if(row >= 0 && row < size - 1 && col >= 0 && col < size)
            drawArrow(row, col, row + 1, col, 270, color, g);
    }

    public void drawArrow(int row1, int col1, int row2, int col2, double angle, Color color, Graphics g)
    {
        double c = Math.cos(Math.toRadians(angle));
        double s = Math.sin(Math.toRadians(angle));
        int[] x = {
                width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col1/size + ((height - topBorder) - 2 * border)/size/2 + (int) (10 * c) - (int) (5 * s),
                width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col1/size + ((height - topBorder) - 2 * border)/size/2 + (int) (10 * c) + (int) (5 * s),
                width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col2/size + ((height - topBorder) - 2 * border)/size/2 - (int) (30 * c) + (int) (5 * s),
                width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col2/size + ((height - topBorder) - 2 * border)/size/2 - (int) (30 * c) + (int) (10 * s),
                width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col2/size + ((height - topBorder) - 2 * border)/size/2 - (int) (10 * c),
                width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col2/size + ((height - topBorder) - 2 * border)/size/2 - (int) (30 * c) - (int) (10 * s),
                width - (height - topBorder) + border + ((height - topBorder) - 2 * border) * col2/size + ((height - topBorder) - 2 * border)/size/2 - (int) (30 * c) - (int) (5 * s),
            };
        int[] y = {
                topBorder + border + ((height - topBorder) - 2 * border) * row1/size + ((height - topBorder) - 2 * border)/size/2 - (int) (10 * s) - (int) (5 * c),
                topBorder + border + ((height - topBorder) - 2 * border) * row1/size + ((height - topBorder) - 2 * border)/size/2 - (int) (10 * s) + (int) (5 * c),
                topBorder + border + ((height - topBorder) - 2 * border) * row2/size + ((height - topBorder) - 2 * border)/size/2 + (int) (30 * s) + (int) (5 * c),
                topBorder + border + ((height - topBorder) - 2 * border) * row2/size + ((height - topBorder) - 2 * border)/size/2 + (int) (30 * s) + (int) (10 * c),
                topBorder + border + ((height - topBorder) - 2 * border) * row2/size + ((height - topBorder) - 2 * border)/size/2 + (int) (10 * s),
                topBorder + border + ((height - topBorder) - 2 * border) * row2/size + ((height - topBorder) - 2 * border)/size/2 + (int) (30 * s) - (int) (10 * c),
                topBorder + border + ((height - topBorder) - 2 * border) * row2/size + ((height - topBorder) - 2 * border)/size/2 + (int) (30 * s) - (int) (5 * c)
            };
        g.setColor(color);
        g.fillPolygon(x, y, 7);
        g.setColor(new Color(0, 0, 0));
        g.drawPolygon(x, y, 7);
    }

    public void drawArrow(int row1, int col1, int row2, int col2, Color color, Graphics g)
    {
        double angle = Math.toDegrees(Math.asin((row1 - row2)/Math.hypot(row1 - row2, col1 - col2)));
        if(row1 <= row2 && col1 < col2)
            angle = 180 - angle;
        else if(row1 > row2 && col1 < col2)
            angle += 180;
        else if(row1 > row2 && col1 > col2)
            angle = 360 - angle;
        if(row1 == row2)
            angle += 180;
        angle %= 360;
        drawArrow(row1, col1, row2, col2, angle, color, g);
    }

    public void drawDeployArmiesScreen(Graphics g)
    {
        Font font = new Font("Verdana", Font.PLAIN, height/50);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        if(keyPressed && remainder == -1)
            remainder = frame % 10;
        else if(keyReleased)
            remainder = -1;
        if(keyReleased && key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            if(time == 0)
                time = frame;
            numDeployedString = numDeployedString.substring(0, Math.max(0, cursorPos - 1)) + numDeployedString.substring(cursorPos);
            cursorPos--;
        }
        else if(keyReleased && key.getKeyCode() == KeyEvent.VK_DELETE)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_DELETE)
        {
            if(time == 0)
                time = frame;
            numDeployedString = numDeployedString.substring(0, cursorPos) + numDeployedString.substring(Math.min(cursorPos + 1, numDeployedString.length()));
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_0)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 0 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_1)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 1 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_2)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 2 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_3)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 3 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_4)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 4 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_5)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 5 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_6)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 6 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_7)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 7 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_8)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 8 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && numDeployedString.length() < 9 && key.getKeyCode() == KeyEvent.VK_9)
        {
            numDeployedString = numDeployedString.substring(0, cursorPos) + 9 + numDeployedString.substring(Math.min(cursorPos, numDeployedString.length()));
            cursorPos++;
        }
        else if(keyReleased && key.getKeyCode() == KeyEvent.VK_LEFT)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if(time == 0)
                time = frame;
            cursorPos--;
        }
        else if(keyReleased && key.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if(time == 0)
                time = frame;
            cursorPos++;
        }
        if(mousePressed && !draggingArmiesAmount && mouseX >= deployArmiesScreenX + width/8 - height/60 - fm.stringWidth("0")/4 && mouseX <= deployArmiesScreenX + width/8 - fm.stringWidth("0")/4 + fm.stringWidth("000000000") && mouseY >= deployArmiesScreenY + height/16 - 9 * fm.getAscent()/16 && mouseY <= deployArmiesScreenY + height/16 + 9 * fm.getAscent()/16)
            cursorPos = (int) Math.round((mouseX - deployArmiesScreenX + width / 8.0 + fm.stringWidth("0") / 4.0) /fm.stringWidth("0")) + 1;
        cursorPos = Math.max(0, Math.min(cursorPos, numDeployedString.length()));
        int numAlreadyDeployed = 0;
        for(int r = 0; r < size; r++)
        {
            for(int c = 0; c < size; c++)
            {
                if(players[1].getArmies(r, c) - armiesCopy[r][c] > 0 && !(selectedRow == r && selectedCol == c))
                    numAlreadyDeployed += players[1].getArmies(r, c) - armiesCopy[r][c];
            }
        }
        if(!numDeployedString.isEmpty() && numDeployedString.length() < 10)
            ((Human) players[1]).setArmiesDeployed(Math.min(Integer.parseInt(numDeployedString) + numAlreadyDeployed, armiesPerTurn));
        g.setColor(new Color(8, 8, 8));
        g.fillRoundRect(deployArmiesScreenX - 3 * width/32, deployArmiesScreenY - 3 * height/32, 3 * width/16, 3 * height/16, height/30, height/30);
        g.setColor(new Color(255, 255, 255));
        g.drawRoundRect(deployArmiesScreenX - 3 * width/32, deployArmiesScreenY - 3 * height/32, 3 * width/16, 3 * height/16, height/30, height/30);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(deployArmiesScreenX - fm.stringWidth("000000000")/2 - fm.stringWidth("0")/4, deployArmiesScreenY + height/16 - 9 * fm.getAscent()/16, fm.stringWidth("000000000") + fm.stringWidth("0")/2, 9 * fm.getAscent()/8);
        g.setColor(new Color(0, 0, 0));
        g.drawString(numDeployedString, deployArmiesScreenX - fm.stringWidth("000000000")/2, deployArmiesScreenY + height/16 + fm.getAscent()/2 - 1);
        if(frame/100 % 2 == 0)
            g.drawLine(deployArmiesScreenX - fm.stringWidth("000000000")/2 + fm.stringWidth("0") * cursorPos, deployArmiesScreenY + height/16 - height/100, deployArmiesScreenX - fm.stringWidth("000000000")/2 + fm.stringWidth("0") * cursorPos, deployArmiesScreenY + height/16 + height/100 - 1);
        int deployed = 0;
        if(!numDeployedString.isEmpty())
            deployed = Math.max(0, Math.min(Integer.parseInt(numDeployedString), numAvailable));
        if(mousePressed && Math.hypot(mouseX - (deployArmiesScreenX - 5.0 * width/64 + (int) (5 * width/32.0 * deployed/numAvailable)), mouseY - deployArmiesScreenY) < height/120.0)
            draggingArmiesAmount = true;
        if(!mousePressed)
            draggingArmiesAmount = false;
        if(draggingArmiesAmount)
        {
            numDeployedString = "" + Math.max(0, Math.min((int) Math.round(((mouseX - (deployArmiesScreenX - 5.0 * width / 64)) /(5.0 * width/32)) * numAvailable), numAvailable));
            cursorPos = numDeployedString.length();
            ((Human) players[1]).setArmiesDeployed(Integer.parseInt(numDeployedString));
        }
        g.setColor(new Color(128, 128, 128));
        g.drawLine(deployArmiesScreenX - 5 * width/64, deployArmiesScreenY, deployArmiesScreenX + 5 * width/64, deployArmiesScreenY);
        g.fillOval(deployArmiesScreenX - 5 * width/64 + (int) (5 * width/32.0 * deployed/numAvailable) - height/120, deployArmiesScreenY - height/120, height/60, height/60);
        if(draggingArmiesAmount)
            g.fillOval(deployArmiesScreenX - 5 * width/64 + (int) (5 * width/32.0 * deployed/numAvailable) - height/60, deployArmiesScreenY - height/60, height/30, height/30);
        ((Human) players[1]).setArmiesDeployed(deployed + numAlreadyDeployed);
        players[1].setArmies(selectedRow, selectedCol, armiesCopy[selectedRow][selectedCol] + deployed);
        if(Math.hypot(mouseX - (deployArmiesScreenX + 3.0 * width/32 - height/30.0), mouseY - (deployArmiesScreenY - 3.0 * height/32 + height/30.0)) < height/60.0)
            g.setColor(new Color(32, 32, 32));
        else
            g.setColor(new Color(64, 64, 64));
        g.fillOval(deployArmiesScreenX + 3 * width/32 - height/20, deployArmiesScreenY - 3 * height/32 + height/60, height/30, height/30);
        g.setColor(new Color(0, 0, 0));
        g.drawOval(deployArmiesScreenX + 3 * width/32 - height/20, deployArmiesScreenY - 3 * height/32 + height/60, height/30, height/30);
        g.drawLine(deployArmiesScreenX + 3 * width/32 - height/30 - height/120, deployArmiesScreenY - 3 * height/32 + height/30 - height/120, deployArmiesScreenX + 3 * width/32 - height/30 + height/120, deployArmiesScreenY - 3 * height/32 + height/30 + height/120);
        g.drawLine(deployArmiesScreenX + 3 * width/32 - height/30 - height/120 + 1, deployArmiesScreenY - 3 * height/32 + height/30 - height/120, deployArmiesScreenX + 3 * width/32 - height/30 + height/120, deployArmiesScreenY - 3 * height/32 + height/30 + height/120 - 1);
        g.drawLine(deployArmiesScreenX + 3 * width/32 - height/30 - height/120, deployArmiesScreenY - 3 * height/32 + height/30 - height/120 + 1, deployArmiesScreenX + 3 * width/32 - height/30 + height/120 - 1, deployArmiesScreenY - 3 * height/32 + height/30 + height/120);
        g.drawLine(deployArmiesScreenX + 3 * width/32 - height/30 + height/120, deployArmiesScreenY - 3 * height/32 + height/30 - height/120, deployArmiesScreenX + 3 * width/32 - height/30 - height/120, deployArmiesScreenY - 3 * height/32 + height/30 + height/120);
        g.drawLine(deployArmiesScreenX + 3 * width/32 - height/30 + height/120 - 1, deployArmiesScreenY - 3 * height/32 + height/30 - height/120, deployArmiesScreenX + 3 * width/32 - height/30 - height/120, deployArmiesScreenY - 3 * height/32 + height/30 + height/120 - 1);
        g.drawLine(deployArmiesScreenX + 3 * width/32 - height/30 + height/120, deployArmiesScreenY - 3 * height/32 + height/30 - height/120 + 1, deployArmiesScreenX + 3 * width/32 - height/30 - height/120 + 1, deployArmiesScreenY - 3 * height/32 + height/30 + height/120);
        if(mouseClicked && Math.hypot(mouseX - (deployArmiesScreenX + 3.0 * width/32 - height/30.0), mouseY - (deployArmiesScreenY - 3.0 * height/32 + height/30.0)) < height/60.0)
        {
            deployArmiesScreen = false;
            draggingScreen = false;
            originalScreenX = deployArmiesScreenX;
            originalScreenY = deployArmiesScreenY;
        }
        else if(mousePressed && mouseX >= deployArmiesScreenX - 3 * width/32 && mouseX <= deployArmiesScreenX + 3 * width/32 && mouseY >= deployArmiesScreenY - 3 * height/32 && mouseY <= deployArmiesScreenY + 3 * height/32 && !draggingArmiesAmount && Math.hypot(mouseX - (deployArmiesScreenX - 5.0 * width/64 + (int) (5 * width/32.0 * deployed/numAvailable)), mouseY - deployArmiesScreenY) >= height/120.0)
        {
            if(!draggingScreen)
            {
                deployArmiesScreenClickX = mouseX;
                deployArmiesScreenClickY = mouseY;
                draggingScreen = true;
            }
            deployArmiesScreenX = originalScreenX + mouseX - deployArmiesScreenClickX;
            deployArmiesScreenY = originalScreenY + mouseY - deployArmiesScreenClickY;
        }
        else if(mousePressed && !draggingScreen && !draggingArmiesAmount && !(mouseX >= width - (height - topBorder - border) + (height - topBorder - 2 * border) * selectedCol/size && mouseX <= width - (height - topBorder - border) + (height - topBorder - 2 * border) * (selectedCol + 1)/size && mouseY >= topBorder + border + (height - topBorder - 2 * border) * selectedRow/size && mouseY <= topBorder + border + (height - topBorder - 2 * border) * (selectedRow + 1)/size))
        {
            deployArmiesScreen = false;
            originalScreenX = deployArmiesScreenX;
            originalScreenY = deployArmiesScreenY;
        }
        else if(mouseClicked)
        {
            draggingScreen = false;
            originalScreenX = deployArmiesScreenX;
            originalScreenY = deployArmiesScreenY;
        }
    }

    public void drawSelectArmiesScreen(Graphics g)
    {
        Font font = new Font("Verdana", Font.PLAIN, height/50);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        if(keyPressed && remainder == -1)
            remainder = frame % 10;
        else if(keyReleased)
            remainder = -1;
        if(keyReleased && key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            if(time == 0)
                time = frame;
            numAttackingString = numAttackingString.substring(0, Math.max(0, cursorPos - 1)) + numAttackingString.substring(cursorPos);
            cursorPos--;
        }
        else if(keyReleased && key.getKeyCode() == KeyEvent.VK_DELETE)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_DELETE)
        {
            if(time == 0)
                time = frame;
            numAttackingString = numAttackingString.substring(0, cursorPos) + numAttackingString.substring(Math.min(cursorPos + 1, numAttackingString.length()));
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_0)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 0 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_1)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 1 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_2)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 2 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_3)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 3 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_4)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 4 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_5)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 5 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_6)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 6 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_7)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 7 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_8)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 8 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && numAttackingString.length() < 9 && key.getKeyCode() == KeyEvent.VK_9)
        {
            numAttackingString = numAttackingString.substring(0, cursorPos) + 9 + numAttackingString.substring(Math.min(cursorPos, numAttackingString.length()));
            cursorPos++;
        }
        else if(keyReleased && key.getKeyCode() == KeyEvent.VK_LEFT)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if(time == 0)
                time = frame;
            cursorPos--;
        }
        else if(keyReleased && key.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            time = 0;
        }
        else if(keyPressed && frame >= time + 80 && (frame - remainder) % 10 == 0 && key.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if(time == 0)
                time = frame;
            cursorPos++;
        }
        if(mousePressed && !draggingArmiesAmount && mouseX >= 5 * width/8 - height/60 - fm.stringWidth("0")/4 && mouseX <= 5 * width/8 - fm.stringWidth("0")/4 + fm.stringWidth("000000000") && mouseY >= 9 * height/16 - 9 * fm.getAscent()/16 && mouseY <= 9 * height/16 + 9 * fm.getAscent()/16)
            cursorPos = (int) Math.round((mouseX - 5.0 * width / 8 + fm.stringWidth("0") / 4.0) /fm.stringWidth("0")) + 1;
        cursorPos = Math.max(0, Math.min(cursorPos, numAttackingString.length()));
        numAttacking = 0;
        if(!numAttackingString.isEmpty() && numAttackingString.length() < 10)
            numAttacking = Math.min(Integer.parseInt(numAttackingString), numAvailable);
        g.setColor(new Color(0, 0, 0, 192));
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(8, 8, 8));
        g.fillRoundRect(width/4, height/4, width/2, height/2, height/30, height/30);
        g.setColor(new Color(255, 255, 255));
        g.drawRoundRect(width/4, height/4, width/2, height/2, height/30, height/30);
        g.setColor(new Color(getR(players[1].getColor()), getG(players[1].getColor()), getB(players[1].getColor())));
        g.fillRect(width/3, height/3, height/10, height/10);
        g.setColor(new Color(getR(players[board[selectedRow][selectedCol]].getColor()), getG(players[board[selectedRow][selectedCol]].getColor()), getB(players[board[selectedRow][selectedCol]].getColor())));
        g.fillRect(2 * width/3 - height/10, height/3, height/10, height/10);
        int[] x = {width/2 - height/20, width/2 - height/20, width/2 + height/60, width/2 + height/60, width/2 + height/20, width/2 + height/60, width/2 + height/60};
        int[] y = {23 * height/60 - height/120, 23 * height/60 + height/120, 23 * height/60 + height/120, 23 * height/60 + height/60, 23 * height/60, 23 * height/60 - height/60, 23 * height/60 - height/120};
        g.setColor(new Color(255, 0, 0, 128));
        g.fillPolygon(x, y, 7);
        g.setColor(new Color(128, 128, 128, 128));
        g.drawPolygon(x, y, 7);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(5 * width/8 - height/60 - fm.stringWidth("0")/4, 9 * height/16 - 9 * fm.getAscent()/16, fm.stringWidth("000000000") + fm.stringWidth("0")/2, 9 * fm.getAscent()/8);
        g.setColor(new Color(0, 0, 0));
        g.drawString(numAttackingString, 5 * width/8 - height/60, 9 * height/16 + fm.getAscent()/2 - 1);
        if(frame/100 % 2 == 0)
            g.drawLine(5 * width/8 - height/60 + fm.stringWidth("0") * cursorPos, 9 * height/16 - height/100, 5 * width/8 - height/60 + fm.stringWidth("0") * cursorPos, 9 * height/16 + height/100 - 1);
        if(mousePressed && Math.hypot(mouseX - (3.0 * width/8 - height/12.0 + (int) (width/4.0 * numAttacking/numAvailable)), mouseY - 9.0 * height/16) < height/120.0)
            draggingArmiesAmount = true;
        if(!mousePressed)
            draggingArmiesAmount = false;
        if(draggingArmiesAmount)
        {
            numAttackingString = "" + Math.max(0, Math.min((int) Math.round(((mouseX - (3.0 * width / 8 - height / 12.0)) /(width/4.0)) * numAvailable), numAvailable));
            cursorPos = numAttackingString.length();
        }
        g.setColor(new Color(128, 128, 128));
        g.drawLine(3 * width/8 - height/12, 9 * height/16, 5 * width/8 - height/12, 9 * height/16);
        g.fillOval(3 * width/8 - height/12 + (int) (width/4.0 * numAttacking/numAvailable) - height/120, 9 * height/16 - height/120, height/60, height/60);
        if(draggingArmiesAmount)
            g.fillOval(3 * width/8 - height/12 + (int) (width/4.0 * numAttacking/numAvailable) - height/60, 9 * height/16 - height/60, height/30, height/30);
        if(mouseX >= 3 * width/8 - height/20 && mouseX <= 3 * width/8 + height/20 && mouseY >= 11 * height/16 - height/90 && mouseY <= 11 * height/16 + height/90)
            g.setColor(new Color(32, 32, 32));
        else
            g.setColor(new Color(64, 64, 64));
        g.fillRoundRect(3 * width/8 - height/15, 11 * height/16 - height/90, (int) (height/7.5), height/45, height/30, height/30);
        g.setColor(new Color(0, 0, 0));
        g.drawRoundRect(3 * width/8 - height/15, 11 * height/16 - height/90, (int) (height/7.), height/45, height/30, height/30);
        g.drawString("Cancel", 3 * width/8 - fm.stringWidth("Cancel")/2, 11 * height/16 + fm.getAscent()/2);
        if(mouseX >= 5 * width/8 - height/20 && mouseX <= 5 * width/8 + height/20 && mouseY >= 11 * height/16 - height/90 && mouseY <= 11 * height/16 + height/90)
            g.setColor(new Color(32, 32, 32));
        else
            g.setColor(new Color(64, 64, 64));
        g.fillRoundRect(5 * width/8 - height/15, 11 * height/16 - height/90, (int) (height/7.5), height/45, height/30, height/30);
        g.setColor(new Color(0, 0, 0));
        g.drawRoundRect(5 * width/8 - height/15, 11 * height/16 - height/90, (int) (height/7.5), height/45, height/30, height/30);
        g.drawString("Okay", 5 * width/8 - fm.stringWidth("Okay")/2, 11 * height/16 + fm.getAscent()/2);
        if(Math.hypot(mouseX - (3.0 * width/4 - height/30.0), mouseY - (height/4.0 + height/30.0)) < height/60.0)
            g.setColor(new Color(32, 32, 32));
        else
            g.setColor(new Color(64, 64, 64));
        g.fillOval(3 * width/4 - height/20, height/4 + height/60, height/30, height/30);
        g.setColor(new Color(0, 0, 0));
        g.drawOval(3 * width/4 - height/20, height/4 + height/60, height/30, height/30);
        int x1 = 3 * width / 4 - height / 30 - height / 120;
        int x2 = 3 * width / 4 - height / 30 + height / 120;
        int y1 = height/4 + height/30 - height/120;
        int y2 = height/4 + height/30 + height/120;
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x1 + 1, y1, x2, y2 - 1);
        g.drawLine(x1, y1+ 1, x2 - 1, y2);
        g.drawLine(x2, y1, x1, y2);
        g.drawLine(x2 - 1, y1, x1, y2 - 1);
        g.drawLine(x2, y1+ 1, x1 + 1, y2);
        if(mouseClicked && mouseX >= 3 * width/8 - height/20 && mouseX <= 3 * width/8 + height/20 && mouseY >= 11 * height/16 - height/90 && mouseY <= 11 * height/16 + height/90)
        {
            selectArmiesScreen = false;
            selectArmiesScreenClosed = true;
            numAttacking = -1;
        }
        else if((mouseClicked && mouseX >= 5 * width/8 - height/20 && mouseX <= 5 * width/8 + height/20 && mouseY >= 11 * height/16 - height/90 && mouseY <= 11 * height/16 + height/90) || (keyPressed && key.getKeyCode() == KeyEvent.VK_ENTER))
        {
            selectArmiesScreen = false;
            selectArmiesScreenClosed = true;
            numAttacking = Math.min(numAttacking, numAvailable);
        }
        else if(mouseClicked && Math.hypot(mouseX - (3.0 * width/4 - height/30.0), mouseY - (height/4.0 + height/30.0)) < height/60.0)
        {
            selectArmiesScreen = false;
            selectArmiesScreenClosed = true;
            numAttacking = -1;
        }
    }

    public void setArmiesPerTurn()
    {
        for(int i = 0; i < numPlayers; i++)
        {
            int numTerritories = 0;
            for(int r = 0; r < size; r++)
            {
                for(int c = 0; c < size; c++)
                {
                    if(board[r][c] == i + 1)
                        numTerritories++;
                }
            }
            if(i == 0)
                ((Human) players[i + 1]).setArmiesPerTurn(5 + numTerritories);
            else
                ((AI) players[i + 1]).setArmiesPerTurn(5 + numTerritories);
        }
    }

    public boolean connected(int r1, int c1, int r2, int c2)
    {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2) == 1;
    }

    public void doAttacks()
    {
        int maxAttacks = 0;
        for(int i = 0; i < numPlayers; i++)
        {
            maxAttacks = Math.max(attacks[i].size(), maxAttacks);
        }
        // change j to (j + first) % numPlayers
        // to not have same cycle every turn
        int temp = start;
        for(int i = 0; i < maxAttacks; i++)
        {
            for(int j = 0; j < numPlayers; j++)
            {
                if(i < attacks[start].size() && players[start + 1].getArmies(attacks[start].get(i).getRow1(), attacks[start].get(i).getCol1()) > 0 && attacks[start].get(i).getNum() <= players[board[attacks[start].get(i).getRow1()][attacks[start].get(i).getCol1()]].getArmies(attacks[start].get(i).getRow1(), attacks[start].get(i).getCol1()))
                {
                    if(players[start + 1] instanceof Human)
                        ((Human) players[start + 1]).attack(attacks[start].get(i).getRow1(), attacks[start].get(i).getCol1(), attacks[start].get(i).getRow2(), attacks[start].get(i).getCol2(), players[board[attacks[start].get(i).getRow2()][attacks[start].get(i).getCol2()]], attacks[start].get(i).getNum(), board);
                    else if(players[start + 1] instanceof AI)
                        ((AI) players[start + 1]).attack(attacks[start].get(i).getRow1(), attacks[start].get(i).getCol1(), attacks[start].get(i).getRow2(), attacks[start].get(i).getCol2(), players[board[attacks[start].get(i).getRow2()][attacks[start].get(i).getCol2()]], attacks[start].get(i).getNum(), board);
                }
                start++;
                start %= numPlayers;
            }
        }
        start = temp;
    }

    public void setAIDeployments()
    {
        for(int i = 1; i < numPlayers; i++)
        {
            int[] rows = new int[size * size];
            int[] cols = new int[size * size];
            int ctr = 0;
            for(int r = 0; r < size; r++)
            {
                for(int c = 0; c < size; c++)
                {
                    if(board[r][c] == i + 1 && ((c < size - 1 && board[r][c + 1] != i + 1) || (r > 0 && board[r - 1][c] != i + 1) || (c > 0 && board[r][c - 1] != i + 1) || (r < size - 1 && board[r + 1][c] != i + 1)))
                    {
                        rows[ctr] = r;
                        cols[ctr] = c;
                        ctr++;
                    }
                }
            }
            for(int k = 0; k < ((AI) players[i + 1]).getArmiesPerTurn(); k++)
            {
                int random = (int) (Math.random() * ctr);
                ((AI) players[i + 1]).deploy(rows[random], cols[random]);
            }
        }
    }

    public void setAIAttacks()
    {
        for(int i = 1; i < numPlayers; i++)
        {
            for(int r = 0; r < size; r++)
            {
                for(int c = 0; c < size; c++)
                {
                    if(players[i + 1].getArmies(r, c) > 0)
                    {
                        ArrayList<Integer> possibleAttacks = new ArrayList<>();
                        if(c < size - 1)
                            possibleAttacks.add(0);
                        if(r > 0)
                            possibleAttacks.add(1);
                        if(c > 0)
                            possibleAttacks.add(2);
                        if(r < size - 1)
                            possibleAttacks.add(3);
                        int numArmies = players[i + 1].getArmies(r, c);
                        int numNonTransfers = 0;
                        if(c < size - 1 && board[r][c + 1] != i + 1)
                            numNonTransfers++;
                        if(r > 0 && board[r - 1][c] != i + 1)
                            numNonTransfers++;
                        if(c > 0 && board[r][c - 1] != i + 1)
                            numNonTransfers++;
                        if(r < size - 1 && board[r + 1][c] != i + 1)
                            numNonTransfers++;
                        while(!possibleAttacks.isEmpty())
                        {
                            int random = (int) (Math.random() * possibleAttacks.size());
                            int dir = possibleAttacks.remove(random);
                            int num = 0;
                            if(dir == 0 && board[r][c + 1] != i + 1)
                            {
                                num = Math.max(1, (int) Math.round(players[board[r][c + 1]].getArmies(r, c + 1)/players[board[r][c]].getOffenseKillRate()));
                                if(numArmies < num)
                                    num = 0;
                                if(num > 0)
                                    attacks[i].add(new Attack(r, c, r, c + 1, num));
                            }
                            else if(dir == 1 && board[r - 1][c] != i + 1)
                            {
                                num = Math.max(1, (int) Math.round(players[board[r - 1][c]].getArmies(r - 1, c)/players[board[r][c]].getOffenseKillRate()));
                                if(numArmies < num)
                                    num = 0;
                                if(num > 0)
                                    attacks[i].add(new Attack(r, c, r - 1, c, num));
                            }
                            else if(dir == 2 && board[r][c - 1] != i + 1)
                            {
                                num = Math.max(1, (int) Math.round(players[board[r][c - 1]].getArmies(r, c - 1)/players[board[r][c]].getOffenseKillRate()));
                                if(numArmies < num)
                                    num = 0;
                                if(num > 0)
                                    attacks[i].add(new Attack(r, c, r, c - 1, num));
                            }
                            else if(dir == 3 && board[r + 1][c] != i + 1)
                            {
                                num = Math.max(1, (int) Math.round(players[board[r + 1][c]].getArmies(r + 1, c)/players[board[r][c]].getOffenseKillRate()));
                                if(numArmies < num)
                                    num = 0;
                                if(num > 0)
                                    attacks[i].add(new Attack(r, c, r + 1, c, num));
                            }
                            else if(numNonTransfers == 0)
                            {
                                num = numArmies;
                                int[] rows = new int[size * size];
                                int[] cols = new int[size * size];
                                int n = 0;
                                for(int r1 = 0; r1 < size; r1++)
                                {
                                    for(int c1 = 0; c1 < size; c1++)
                                    {
                                        if(board[r1][c1] == 1)
                                        {
                                            rows[n] = r1;
                                            cols[n] = c1;
                                            n++;
                                        }
                                    }
                                }
                                int[] dist = new int[n];
                                for(int k = 0; k < n; k++)
                                {
                                    dist[k] = Math.abs(rows[k] - r) + Math.abs(cols[k] - c);
                                }
                                quickSort(dist, rows, cols, 0, n - 1);
                                current = new Vector(c, r);
                                player = new Vector(cols[0], rows[0]);
                                Vector dif = Vector.sub(current, player);
                                double angle = 0;
                                if(dif.getX() > 0 && dif.getY() == 0)
                                    angle = 2;
                                else if(dif.getX() > 0 && dif.getY() > 0)
                                {
                                    int a = (int) (Math.random() * 2);
                                    if(a == 0)
                                        angle = 2;
                                    else
                                        angle = 1;
                                }
                                else if(dif.getX() == 0 && dif.getY() > 0)
                                    angle = 1;
                                else if(dif.getX() < 0 && dif.getY() > 0)
                                {
                                    int a = (int) (Math.random() * 2);
                                    if(a == 0)
                                        angle = 0;
                                    else
                                        angle = 1;
                                }
                                else if(dif.getX() < 0 && dif.getY() == 0)
                                    angle = 0;
                                else if(dif.getX() < 0 && dif.getY() < 0)
                                {
                                    int a = (int) (Math.random() * 2);
                                    if(a == 0)
                                        angle = 0;
                                    else
                                        angle = 3;
                                }
                                else if(dif.getX() == 0 && dif.getY() < 0)
                                    angle = 3;
                                else if(dif.getX() > 0 && dif.getY() < 0)
                                {
                                    int a = (int) (Math.random() * 2);
                                    if(a == 0)
                                        angle = 2;
                                    else
                                        angle = 3;
                                }
                                if(angle == 0 && num > 0 && c < size - 1)
                                    attacks[i].add(new Attack(r, c, r, c + 1, num));
                                else if(angle == 1 && num > 0 && r > 0)
                                    attacks[i].add(new Attack(r, c, r - 1, c, num));
                                else if(angle == 2 && num > 0 && c > 0)
                                    attacks[i].add(new Attack(r, c, r, c - 1, num));
                                else if(angle == 3 && num > 0 && r < size - 1)
                                    attacks[i].add(new Attack(r, c, r + 1, c, num));
                            }
                            numArmies -= num;
                            if(numArmies == 0)
                                break;
                        }
                    }
                }
            }
        }
    }

    public boolean win()
    {
        for(int r = 0; r < size; r++)
        {
            for(int c = 0; c < size; c++)
            {
                if(board[r][c] > 1)
                    return false;
            }
        }
        return true;
    }

    public boolean lose()
    {
        for(int r = 0; r < size; r++)
        {
            for(int c = 0; c < size; c++)
            {
                if(board[r][c] == 1)
                    return false;
            }
        }
        return true;
    }

    public void quickSort(int[] list, int[] rows, int[] cols, int first, int last)
    {
        int g = first, h = last;
        int midIndex = (first + last) / 2;
        int dividingValue = list[midIndex];
        do
        {
            while(list[g] < dividingValue)
                g++;
            while(list[h] > dividingValue)
                h--;
            if(g <= h)
            {
                int temp = list[g];
                list[g] = list[h];
                list[h] = temp;
                int tempRow = rows[g];
                rows[g] = rows[h];
                rows[h] = tempRow;
                int tempCol = cols[g];
                cols[g] = cols[h];
                cols[h] = tempCol;
                g++;
                h--;
            }
        }
        while(g < h);
        if(h > first)
            quickSort(list, rows, cols, first, h);
        if(g < last)
            quickSort(list, rows, cols, g, last);
    }

    public void mouseClicked(MouseEvent me)
    {
        mousePressed = true;
        mouseClicked = true;
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {

    }

    public void mousePressed(MouseEvent me)
    {
        mousePressed = true;
    }

    public void mouseReleased(MouseEvent me)
    {
        mousePressed = false;
        mouseClicked = true;
    }

    public void mouseDragged(MouseEvent me)
    {
        mousePressed = true;
        mouseX = me.getX() - 1;
        mouseY = me.getY() - 3;
    }

    public void mouseMoved(MouseEvent me)
    {
        mousePressed = false;
        mouseX = me.getX() - 1;
        mouseY = me.getY() - 3;
    }

    public void keyPressed(KeyEvent key)
    {
        keyPressed = true;
        this.key = key;
    }

    public void keyReleased(KeyEvent key)
    {
        keyPressed = false;
        keyReleased = true;
        this.key = key;
    }

    public void keyTyped(KeyEvent key)
    {

    }

    private int rgbNum(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        return r * 65536 + g * 256 + b;
    }

    private int getR(int color)
    {
        //gets r value from rgb decimal input
        return color/65536;
    }

    private int getG(int color)
    {
        //gets g value from rgb decimal input
        color -= color/65536 * 65536;
        return color/256;
    }

    private int getB(int color)
    {
        //gets b value from rgb decimal input
        color -= color/65536 * 65536;
        color -= color/256 * 256;
        return color;
    }

    public static void main(String[] args) {
        Risk r = new Risk();
        JFrame jf = new JFrame();
        jf.setTitle("Risk");
        jf.setSize(r.width, r.height + 28);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(r);
        jf.setVisible(true);
    }
}