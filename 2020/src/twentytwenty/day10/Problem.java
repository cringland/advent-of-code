package twentytwenty.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;
import common.model.Range;

import static common.Util.range;

public class Problem {

    //https://adventofcode.com/2020/day/10
    public static void main(String[] args) throws IOException {
        var input = Stream.concat(Stream.of(0), Files.readAllLines(Paths.get("src/twentytwenty/day10/input")).stream()
                .map(Integer::valueOf))
                .sorted()
                .collect(Collectors.toList());
        input.add(input.get(input.size() - 1) + 3);

        final var diffs = new ArrayList<>(Arrays.asList(0, 0, 0));
        for (int i = 0; i < input.size() - 1; i++) {
            int diff = input.get(i + 1) - input.get(i) - 1;
            diffs.set(diff, diffs.get(diff) + 1);
        }
        var problem1Answer = diffs.get(0) * diffs.get(2);

        System.out.println("Problem 1 Answer is: " + problem1Answer);
        Util.assertEquals(2080, problem1Answer);

        Graph graph = new Graph();
        input.forEach(graph::addVertex);
        Range range = range(input.stream().map(Integer::longValue).collect(Collectors.toList()));

        for (int i = 0; i < input.size(); i++) {
            int currentVal = input.get(i);
            for (int j = i + 1; j <= (i + 3) && j < input.size(); j++) {
                int nextVal = input.get(j);
                int diff = nextVal - currentVal;
                if (diff <= 3) {
                    graph.addEdge(currentVal, nextVal);
                }
            }
        }
        var problem2Answer = graph.countPaths(range.lowest(), range.highest());
        System.out.println("Problem 1 Answer is: " + problem2Answer);
        Util.assertEquals(6908379398144L, problem2Answer);
    }
}

class Graph {

    private Map<Integer, List<Integer>> adjVertices = new HashMap<>();
    private Map<Integer, Long> cache = new HashMap<>();

    public void addVertex(Integer num) {
        adjVertices.putIfAbsent(num, new ArrayList<>());
    }

    public void addEdge(Integer num1, Integer num2) {
        adjVertices.get(num1).add(num2);
    }

    public long countPaths(long start, long fin) {
        long pathCount = 0;
        pathCount += countPathsRec(start, fin);
        return pathCount;
    }

    private long countPathsRec(Long start, Long finish) {
        if (start.equals(finish)) {
            return 1;
        } else {
            long count = 0;
            if (cache.containsKey(start.intValue()))
                return cache.get(start.intValue());
            else
                for (final Integer vertex : adjVertices.get(start.intValue())) {
                    long n = vertex;
                    count += countPaths(n, finish);
                    cache.put(start.intValue(), count);
                }

            return count;
        }
    }
}
