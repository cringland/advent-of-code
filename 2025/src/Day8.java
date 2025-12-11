import util.Point3;
import util.SamePair;
import util.Util;

import java.util.*;
import java.util.List;

public class Day8 implements Day<Number> {

    @Override
    public Number sampleAnswerP1() {
        return 40L;
    }

    @Override
    public Number sampleAnswerP2() {
        return 25272;
    }

    @Override
    public Number part1(Input input) {
        var sortedPairs = sortedUniquePairs(input);
        var circuits = new ArrayList<Set<Point3>>();
        var amountToSort = sortedPairs.size() <= 1000 ? 10 : 1000;
        for (int i = 0; i < amountToSort; i++) {
            var currentPair = sortedPairs.get(i);
            var matchedCircuits = circuits.stream().filter(it -> it.contains(currentPair.left()) || it.contains(currentPair.right())).toList();
            if (matchedCircuits.isEmpty()) {
                circuits.add(new HashSet<>());
                circuits.getLast().add(currentPair.left());
                circuits.getLast().add(currentPair.right());
            } else if (matchedCircuits.size() == 2) {
                var circuit1Idx = circuits.indexOf(matchedCircuits.getFirst());
                var circuit2Idx = circuits.indexOf(matchedCircuits.getLast());
                circuits.get(circuit1Idx).addAll(matchedCircuits.getLast());
                circuits.remove(circuit2Idx);
            } else if (matchedCircuits.size() == 1) {
                var circuit1Idx = circuits.indexOf(matchedCircuits.getFirst());
                circuits.get(circuit1Idx).add(currentPair.left());
                circuits.get(circuit1Idx).add(currentPair.right());
            } else {
                throw new RuntimeException();
            }
        }
        var sortedCircuitSizes = circuits.stream().map(Set::size).map(Long::valueOf).sorted(Comparator.reverseOrder()).toList();
        var threeLargest = sortedCircuitSizes.subList(0, 3);
        return Util.productLong(threeLargest);
    }

    @Override
    public Number part2(Input input) {
        var points = input.point3s().stream().map(it -> {
            var set = new HashSet<Point3>();
            set.add(it);
            return set;
        }).toList();
        var sortedPairs = sortedUniquePairs(input);
        var circuits = new ArrayList<>(points);
        var i = 0;
        var lastPair = sortedPairs.get(i);
        while(circuits.size() > 1) {
            lastPair = sortedPairs.get(i);
            var currentPair = lastPair;
            var matchedCircuits = circuits.stream().filter(it -> it.contains(currentPair.left()) || it.contains(currentPair.right())).toList();
            if (matchedCircuits.isEmpty()) {
                circuits.add(new HashSet<>());
                circuits.getLast().add(currentPair.left());
                circuits.getLast().add(currentPair.right());
            } else if (matchedCircuits.size() == 2) {
                var circuit1Idx = circuits.indexOf(matchedCircuits.getFirst());
                var circuit2Idx = circuits.indexOf(matchedCircuits.getLast());
                circuits.get(circuit1Idx).addAll(matchedCircuits.getLast());
                circuits.remove(circuit2Idx);
            } else if (matchedCircuits.size() == 1) {
                var circuit1Idx = circuits.indexOf(matchedCircuits.getFirst());
                circuits.get(circuit1Idx).add(currentPair.left());
                circuits.get(circuit1Idx).add(currentPair.right());
            } else {
                throw new RuntimeException();
            }
            i++;
        }
        return lastPair.left().x() * lastPair.right().x();
    }

    private List<SamePair<Point3>> sortedUniquePairs(Input input) {
        var points = input.point3s();
        var pairs = new HashSet<SamePair<Point3>>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i+1; j < points.size(); j++) {
                var p1 = points.get(i);
                var p2 = points.get(j);
                if (!p1.equals(p2)) {
                    var pair = new SamePair<>(p1, p2);
                    pairs.add(pair);
                }
            }
        }
        if (points.size() * (points.size()-1) / 2 != pairs.size()) {
            throw new RuntimeException();
        }
        return pairs.stream().sorted(Comparator.comparing(pair -> pair.left().distance(pair.right()))).toList();
    }
}
