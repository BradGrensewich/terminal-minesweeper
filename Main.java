package minesweeper;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many mines do you want on the field?");
        Field field = new Field(scanner.nextInt());
        field.printField();
        while(!field.isComplete()) {
            field.playerTurn(scanner);
        }



    }
}


