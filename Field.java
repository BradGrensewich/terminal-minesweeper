package minesweeper;

import java.util.Random;
import java.util.Scanner;

class Field {

    static int FIELD_SIZE = 9;

    Position[][] map;
    int flags;
    int foundBombs;
    int mines;
    boolean exploded;

    public Field(int mines) {
        this.mines = mines;
        this.map = createField();
        this.flags = 0;
        this.foundBombs = 0;
        this.exploded = false;

    }

    private Position[][] createField() {

        int mines = this.mines;

        Position[][] field = new Position[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = new Position();
            }
        }
        Random random = new Random();
        for (int i = 0; i < mines; i++) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);
            if(field[x][y].isArmed()) {
                i--;
                continue;
            }
            field[x][y].armBomb();
        }
        makeWarnings(field);
        return field;
    }

    public void playerTurn(Scanner scanner) {
        System.out.println("Set/unset mines marks or claim a cell as free:");
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        String option = scanner.next();
        if (option.equals("mine")) {
            flag(x, y);
        } else if (option.equals("free")) {
            reveal(x, y);
        } else {
            System.out.println("Unrecognized command.(Use 'free' or 'mine')");
        }
        printField();
    }

    private void reveal(int x, int y) {
        if (this.map[y][x].isVisible()) {
            System.out.println("Position already revealed");
            return;
        }
        if(this.map[y][x].revealWithExplosion()) {
            explode();
        } else {
            if(!this.map[y][x].isNumbered()) {
                showSurrounding(x, y);
            }
        }
    }

    private void explode () {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if(!this.map[i][j].isVisible() && this.map[i][j].isArmed()) {
                    this.map[i][j].revealWithExplosion();
                }
            }
        }
        this.exploded = true;
        System.out.println("You stepped on a mine and failed!");
    }

    private void flag(int x , int y) {
        if(this.map[y][x].isNumbered() && this.map[y][x].isVisible()) {
            System.out.println("There is a number here!");
            return;
        }
        this.map[y][x].toggleFlagged();

    }

    private void makeWarnings(Position[][] field) {
        for(int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {

                if(field[i][j].isArmed()) {
                    continue;
                }
                int startI;
                int endI;
                int startJ;
                int endJ;

                //find i coordinates
                if(i == 0) {
                    startI = i;
                    endI = i + 1;
                } else if (i == FIELD_SIZE - 1) {
                    startI = i - 1;
                    endI = i;
                } else {
                    startI = i - 1;
                    endI = i + 1;
                }
                //find j coordinates
                if (j == 0) {
                    startJ = j;
                    endJ = j + 1;
                } else if(j == FIELD_SIZE -1) {
                    startJ = j - 1;
                    endJ = j;
                } else {
                    startJ = j - 1;
                    endJ = j + 1;
                }
                int bombs = countBombs(field, startI, endI, startJ, endJ);

                if(bombs != 0) {
                    //field[i][j].setDisplay ((char)('0' + bombs));
                    field[i][j].setNumbered(bombs);
                }
            }
        }
    }
    private int countBombs(Position[][] field, int iLow, int iHigh, int jLow, int jHigh) {
        int count = 0;
        for (int i = iLow; i <= iHigh; i++) {
            for (int j = jLow; j <= jHigh; j++) {
                if(field[i][j].isArmed()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void showSurrounding(int x, int y) {
        for (int i = y - 1; i <= y + 1; i++) {
            if (i < 0 || i >= FIELD_SIZE) {
                continue;
            }
            for (int j = x - 1; j <= x +1; j++) {
                if (j < 0 || j >= FIELD_SIZE) {
                    continue;
                }
                if(!this.map[i][j].isVisible()) {
                    this.map[i][j].revealWithExplosion();
                    if(!this.map[i][j].isNumbered()) {
                        showSurrounding(j, i);
                    }
                }
            }
        }
    }

    public void printField() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < FIELD_SIZE; j++) {
                System.out.print(this.map[i][j].getDisplay());
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public boolean isComplete() {
        if(this.exploded) {
            return true;
        }
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if(this.map[i][j].isArmed() && this.map[i][j].isFlagged()) {
                    continue;
                }
                if(this.map[i][j].isArmed() || this.map[i][j].isFlagged()) {
                    return false;
                }
            }
        }
        System.out.println("Congratulations! You found all the mines!");
        return true;
    }
}

