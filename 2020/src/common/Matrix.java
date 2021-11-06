package common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Matrix<T> {

    private List<List<T>> table;

    public Matrix(final List<List<T>> table) {
        this.table = table;
    }

    public Matrix<T> flippedHorizontal() {
        var newTable = table.stream()
                .map(l -> {
                    List<T> copy = new ArrayList<>(l);
                    Collections.reverse(copy);
                    return copy;
                })
                .collect(Collectors.toList());
        return new Matrix<>(newTable);
    }

    public Matrix<T> rotated90() {
        List<List<T>> newTable = copy();
        for (int layer = 0; layer < newTable.size() / 2; ++layer) {
            int last = newTable.size() - 1 - layer;
            for (int i = layer; i < last; ++i) {
                int offset = i - layer;
                T temp = newTable.get(layer).get(i);
                newTable.get(layer).set(i, newTable.get(last - offset).get(layer));
                final T temp2 = newTable.get(last).get(last - offset);
                newTable.get(last - offset).set(layer, temp2);
                newTable.get(last).set(last - offset, newTable.get(i).get(last));
                newTable.get(i).set(last, temp);
            }
        }
        return new Matrix<>(newTable);
    }

    public T get(int x, int y) {
        return table.get(y).get(x);
    }

    public Matrix<T> subMatrix(int startX, int endX, int startY, int endY) {
        return new Matrix<>(table.subList(startY, endY).stream()
                .map(l -> l.subList(startX, endX))
                .collect(Collectors.toList()));
    }

    public List<T> getFirstCol() {
        return table.stream()
                .map(row -> row.get(0))
                .collect(Collectors.toList());
    }

    public List<T> getLastCol() {
        return table.stream()
                .map(row -> row.get(row.size() - 1))
                .collect(Collectors.toList());
    }

    public List<T> getFirstRow() {
        return table.get(0);
    }

    public List<T> getLastRow() {
        return table.get(table.size() - 1);
    }

    public List<List<T>> copy() {
        return table.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix)) return false;
        final Matrix<?> matrix = (Matrix<?>) o;
        return Objects.equals(table, matrix.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table);
    }
}
