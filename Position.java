package minesweeper;

class Position {
    boolean visible;
    boolean armed;
    boolean numbered;
    boolean flagged;
    char display;
    int number;

    public Position() {
        this.visible = false;
        this.armed = false;
        this.numbered = false;
        this.flagged = false;
        this.display = '.';
    }

    public void toggleFlagged() {
        this.flagged = (!this.flagged);
        if(this.flagged) {
            setDisplay('*');
        } else {
            setDisplay('.');
        }
    }

    public void armBomb() {
        this.armed = true;
    }

    public boolean isArmed() {
        return this.armed;
    }

    public boolean isFlagged() {
        return this.flagged;
    }

    public void setDisplay(char toShow) {
        this.display = toShow;
    }

    public char getDisplay() {
        return this.display;
    }

    public boolean isNumbered() {
        return numbered;
    }

    public void setNumbered(int num) {
        this.numbered = true;
        this.number = num;
    }

    public int getNumber() {
        return number;
    }

    //reveals position and returns true if a bomb explodes
    public boolean revealWithExplosion() {
        this.visible = true;
        if(this.isNumbered()) {
            this.setDisplay((char) ('0' + this.getNumber()));
            return false;
        } else if (!this.isArmed()) {
            this.setDisplay('/');
            return false;
        } else {
            this.setDisplay('X');
            return true;
        }
    }

    public boolean isVisible() {
        return visible;
    }
}
