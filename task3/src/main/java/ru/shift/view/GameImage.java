package ru.shift.view;

import javax.swing.*;
import ru.shift.model.field.CellState;

public enum GameImage {
    CLOSED("cell.png"),
    MARKED("flag.png"),
    EMPTY("empty.png"),
    NUM_1("1.png"),
    NUM_2("2.png"),
    NUM_3("3.png"),
    NUM_4("4.png"),
    NUM_5("5.png"),
    NUM_6("6.png"),
    NUM_7("7.png"),
    NUM_8("8.png"),
    BOMB("mine.png"),
    TIMER("timer.png"),
    BOMB_ICON("mineImage.png"),
    ;

    private final String fileName;
    private ImageIcon imageIcon;

    GameImage(String fileName) {
        this.fileName = fileName;
    }

    public synchronized ImageIcon getImageIcon() {
        if (imageIcon == null) {
            imageIcon = new ImageIcon(ClassLoader.getSystemResource(fileName));
        }

        return imageIcon;
    }
    
    public synchronized static GameImage fromCellState(CellState cellState) {
       return switch (cellState) {
           case BOMB -> GameImage.BOMB;
           case CLOSED -> GameImage.CLOSED;
            case FLAG -> GameImage.MARKED;
            case EMPTY -> GameImage.EMPTY;
            case ONE -> GameImage.NUM_1;
            case TWO -> GameImage.NUM_2;
            case THREE -> GameImage.NUM_3;
            case FOUR -> GameImage.NUM_4;
            case FIVE -> GameImage.NUM_5;
            case SIX -> GameImage.NUM_6;
            case SEVEN -> GameImage.NUM_7;
            case EIGHT -> GameImage.NUM_8;
        };
    }
}
