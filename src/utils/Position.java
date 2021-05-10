package utils;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
    int row;
    int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getRow() == position.getRow() && getColumn() == position.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    public String toString() {
        return "(" + row + "," + column + ")";
    }
}
