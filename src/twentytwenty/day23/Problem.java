package twentytwenty.day23;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/23
    public static void main(String[] args) {
//        var input1 = List.of(3, 8, 9, 1, 2, 5, 4, 6, 7);
        var input1 = List.of(1, 5, 8, 9, 3, 7, 4, 6, 2);

        var p1cup1 = play2(input1, 9, 100);
        final String p1Ans = p1String(p1cup1);
        System.out.println("Problem 1 Answer is: " + p1Ans);
        Util.assertEquals("69473825", p1Ans);

        var input2 = IntStream.concat(input1.stream().mapToInt(Integer::intValue),
                IntStream.range(max(input1) + 1, 1000001))
                .boxed().collect(Collectors.toList());

        var p2cup1 = play2(input2, 1000000, 10000000);
        var p2Ans = p2cup1.getNext().getValue().longValue() * p2cup1.getNext().getNext().getValue().longValue();
        System.out.println("Problem 2 Answer is: " + p2Ans);
        Util.assertEquals(96604396189L, p2Ans);
    }

    private static Cup play2(final List<Integer> input, final int max, final int iters) {
        var cups = new Cups(input);
        var currentCup = cups.start;
        for (int i = 0; i < iters; i++) {
            var pickUp1 = currentCup.popNext();
            var pickUp2 = currentCup.popNext();
            var pickUp3 = currentCup.popNext();
            var pickedUpVals = List.of(pickUp1.getValue(), pickUp2.getValue(), pickUp3.getValue());

            var destinationCupVal = currentCup.getValue() - 1;
            while (pickedUpVals.contains(destinationCupVal))
                destinationCupVal--;

            var min = 1;
            while (pickedUpVals.contains(min))
                min++;
            if (min > destinationCupVal) {
                int temp = max;
                while (pickedUpVals.contains(temp))
                    temp--;
                destinationCupVal = temp;
            }

            var destCup = cups.find(destinationCupVal);
            destCup.put(pickUp3);
            destCup.put(pickUp2);
            destCup.put(pickUp1);
            currentCup = currentCup.getNext();
        }

        return cups.find(1);
    }

    private static String p1String(final Cup oneCup) {
        var prev = oneCup.getNext();
        var sb = new StringBuilder();
        while (prev.getValue() != 1) {
            sb.append(prev.getValue());
            prev = prev.getNext();
        }
        return sb.toString();
    }

    private static Integer max(final List<Integer> cups) {
        return cups.stream().max(Integer::compareTo).get();
    }
}

class Cups {

    Cup start;

    Cup[] cache; //For looking up by label

    public Cups(List<Integer> input) {
        var cups = new ArrayList<>(input);
        cache = new Cup[input.size() + 1];
        start = new Cup(cups.remove(0));
        cache[start.getValue()] = start;
        var prev = start;
        for (var i : cups) {
            var temp = new Cup(i);
            cache[i] = temp;
            prev.setNext(temp);
            prev = temp;
        }
        prev.setNext(start); //Circular
    }

    public Cup find(int val) {
        return cache[val];
    }


}

class Cup {

    private Integer value;
    private Cup next;

    public Cup(Integer value) {
        this.value = value;
    }


    public Integer getValue() {
        return value;
    }

    public Cup getNext() {
        return next;
    }

    public void setNext(final Cup next) {
        this.next = next;
    }

    public Cup popNext() {
        var temp = next;
        this.next = next.next;
        temp.next = null;
        return temp;
    }

    public void put(final Cup put) {
        var temp = this.next;
        this.next = put;
        put.next = temp;
    }

    @Override
    public String toString() {
        return value + "";
    }
}

