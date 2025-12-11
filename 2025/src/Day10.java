import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Day10 implements Day<Number> {

    record Button(List<Integer> indices) {}

    record Machine(List<Boolean> desiredState, List<Button> buttons, List<Integer> joltageReqs) {}


    @Override
    public Number sampleAnswerP1() {
        return 7L;
    }

    @Override
    public Number sampleAnswerP2() {
        return null;
    }

    @Override
    public Number part1(Input input) {
        var machines = parseInput(input);
        var minButtonPresses = machines.stream().map(machine -> {
//             1  function Dijkstra(Graph, source):
// 2
// 3      for each vertex v in Graph.Vertices:
// 4          dist[v] ← INFINITY
// 5          prev[v] ← UNDEFINED
// 6          add v to Q
// 7      dist[source] ← 0
// 8
// 9      while Q is not empty:
//10          u ← vertex in Q with minimum dist[u]
//11          Q.remove(u)
//12
//13          for each arc (u, v) in Q:
//14              alt ← dist[u] + Graph.Edges(u, v)
//15              if alt < dist[v]:
//16                  dist[v] ← alt
//17                  prev[v] ← u
//18
//19      return dist[], prev[]
//            var states = new ArrayList<List<Boolean>>();
//            for (var button: machine.buttons) {
//                states.add(emptyState(machine.desiredState.size()));
//            }

//            var numPresses = 0;
//            while(states.stream().noneMatch(machine.desiredState()::equals)) {
//
//                numPresses++;
//            }
//            return numPresses;
        })
        return null;
    }

    private static List<Boolean> emptyState(int size) {
        var state = new ArrayList<Boolean>();
        for (int i = 0; i < size; i++) {
            state.add(false);
        }
        return state;
    }

    @Override
    public Number part2(Input input) {
        return null;
    }

    private static List<Machine> parseInput(Input input) {
        return input.linesStream().map(line -> {
            var desiredState = Util.toCharList(line.substring(1, line.indexOf(']'))).stream()
                    .map(c -> c != '.')
                    .toList();
            var buttonPattern = Pattern.compile("\\((.*?)\\)");
            var matcher = buttonPattern.matcher(line);
            var buttons = matcher.results()
                    .map(matchResult -> new Button(Arrays.stream(matchResult.group().substring(1, matchResult.group().length()-1).split(","))
                            .map(Integer::valueOf)
                            .toList())
                    ).toList();
            return new Machine(desiredState, buttons, null);
        }).toList();
    }

}
