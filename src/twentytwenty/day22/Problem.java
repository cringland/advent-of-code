package twentytwenty.day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Problem {

    //https://adventofcode.com/2020/day/22
    public static void main(String[] args) throws IOException {
        var input = Files.readString(Paths.get("src/twentytwenty/day22/input")).split("\n\n");
        var p1Hand = getHand(input[0]);
        var p2Hand = getHand(input[1]);

        var result = playCombat(p1Hand, p2Hand);
        System.out.println("Problem 1 Answer is: " + result.getScore());

        var rResult = playRCombat(new HashSet<>(), p1Hand, p2Hand);
        System.out.println("Problem 2 Answer is: " + rResult.getScore());
    }

    private static GameResult playCombat(final List<Integer> p1HandOrg, final List<Integer> p2HandOrg) {
        var p1Hand = new ArrayList<>(p1HandOrg);
        var p2Hand = new ArrayList<>(p2HandOrg);

        int moveCount = 0;
        while (!p1Hand.isEmpty() && !p2Hand.isEmpty()) {
            moveCount++;
            int p1 = p1Hand.get(0);
            int p2 = p2Hand.get(0);
            p1Hand.remove(0);
            p2Hand.remove(0);

            if (p1 > p2) {
                p1Hand.add(p1);
                p1Hand.add(p2);
            } else {
                p2Hand.add(p2);
                p2Hand.add(p1);
            }
        }
        System.out.println("Combat Game took " + moveCount + " moves");
        return new GameResult(p1Hand, p2Hand);
    }

    private static GameResult playRCombat(final Set<Integer> hashes, final List<Integer> p1HandOrg, final List<Integer> p2HandOrg) {
        var p1Hand = new ArrayList<>(p1HandOrg);
        var p2Hand = new ArrayList<>(p2HandOrg);

        while (!p1Hand.isEmpty() && !p2Hand.isEmpty()) {
            var currentHash = Objects.hash(p1Hand, p2Hand);
            if (hashes.contains(currentHash)) {
                return GameResult.p1Wins(p1Hand);
            }
            hashes.add(currentHash);

            int p1 = p1Hand.get(0);
            int p2 = p2Hand.get(0);
            p1Hand.remove(0);
            p2Hand.remove(0);

            if (p1 <= p1Hand.size() && p2 <= p2Hand.size()) {
                var subResult = playRCombat(new HashSet<>(), p1Hand.subList(0, p1), p2Hand.subList(0, p2));
                if (subResult.p1Won()) {
                    p1Hand.add(p1);
                    p1Hand.add(p2);
                } else {
                    p2Hand.add(p2);
                    p2Hand.add(p1);
                }
            } else {
                if (p1 > p2) {
                    p1Hand.add(p1);
                    p1Hand.add(p2);
                } else {
                    p2Hand.add(p2);
                    p2Hand.add(p1);
                }
            }
        }

        return new GameResult(p1Hand, p2Hand);
    }

    private static List<Integer> getHand(final String input) {
        return input.lines().skip(1).map(Integer::valueOf).collect(Collectors.toList());
    }
}

class GameResult {

    private Long score;
    private boolean p1Won;

    public GameResult(final List<Integer> p1, final List<Integer> p2) {
        p1Won = p2.isEmpty();
        score = p1Won ? calcScore(p1) : calcScore(p2);
    }

    public static GameResult p1Wins(final List<Integer> p1) {
        var g = new GameResult();
        g.p1Won = true;
        g.score = calcScore(p1);
        return g;
    }

    private GameResult() {

    }

    public Long getScore() {
        return score;
    }

    public boolean p1Won() {
        return p1Won;
    }

    private static Long calcScore(List<Integer> hand) {
        var score = 0L;
        for (int i = hand.size() - 1; i >= 0; i--) {
            score += hand.get(i) * (hand.size() - i);
        }
        return score;
    }
}
