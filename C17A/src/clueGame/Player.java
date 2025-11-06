package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected String name;
    protected Color color;
    protected int row;
    protected int column;
    protected List<Card> hand = new ArrayList<>();

    public Player(String name, Color color, int row, int column) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = column;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public abstract boolean isHuman();
}
