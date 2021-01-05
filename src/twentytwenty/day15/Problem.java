package twentytwenty.day15;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.List;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/15
    public static void main(String[] args) {
        var input = List.of(19, 20, 14, 0, 9, 1);

        var map = new HashMap<Integer, T>();
        for (int i = 0; i < input.size(); i++)
            map.put(input.get(i), new T(i));


        int prevNum = input.get(input.size() - 1);

        for (var i = input.size(); i != 30000000; i++) {
            var currentNum = 0;
            var range = map.get(prevNum);
            if (range.getSecondLastIndex() != -1) {
                currentNum = range.getDiff();
            }

            if (!map.containsKey(currentNum)) {
                map.put(currentNum, new T(i));
            } else {
                map.get(currentNum).update(i);
            }

            prevNum = currentNum;
            if (i == 2019) {
                System.out.println(format("Turn %s: %s", i + 1, currentNum));
                Util.assertEquals(1325, currentNum);
            } else if (i == 30000000 - 1) {
                System.out.println(format("Turn %s: %s", i + 1, currentNum));
                Util.assertEquals(59006, currentNum);
            }
        }
    }
}

class T {

    private int lastIndex;
    private int secondLastIndex = -1;

    public T(int index) {
        this.lastIndex = index;
    }

    public void update(int index) {
        this.secondLastIndex = this.lastIndex;
        this.lastIndex = index;
    }

    public int getSecondLastIndex() {
        return secondLastIndex;
    }

    public int getDiff() {
        return lastIndex - secondLastIndex;
    }
}
