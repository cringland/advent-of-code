package twentytwenty.day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/8
    public static void main(String[] args) throws IOException {
        var ops = Files.readAllLines(Paths.get("src/twentytwenty/day8/input")).stream()
                .map(s -> {
                    var sarr = s.split(" ");
                    return new Op(sarr[0], Integer.valueOf(sarr[1]));
                }).collect(Collectors.toList());

        List<List<Op>> permutations = new ArrayList<>();
        for (int i = 0; i < ops.size(); i++) {
            var currentOp = ops.get(i);
            if (currentOp.opType().equals("nop")) {
                continue;
            }
            var copy = new ArrayList<>(ops);
            switch (currentOp.opType) {
                case "nop":
                    copy.set(i, new Op("jmp", currentOp.number()));
                    break;
                case "jmp":
                    copy.set(i, new Op("nop", currentOp.number()));
                    break;
            }
            permutations.add(copy);
        }


        var problemOneAcc = getAcc(ops).acc;

        System.out.println("Problem 1: Accumulator before it loops = " + problemOneAcc);
        Util.assertEquals(1317, problemOneAcc);

        var problemTwoAcc = -1;
        for (var perm : permutations) {
            ops.forEach(Op::resetFlag);
            var currentAcc = getAcc(perm);
            if (currentAcc.success) {
                problemTwoAcc = currentAcc.acc;
                break;
            }
        }


        System.out.println("Problem 2: Accumulator of correct permutation = " + problemTwoAcc);
        Util.assertEquals(1033, problemTwoAcc);
    }

    private static Result getAcc(final List<Op> ops) {
        for (int i = 0, acc = 0; i < ops.size(); ) {
            var op = ops.get(i);
            if (op.beenRan()) {
                return new Result(acc, false);
            }
            switch (op.opType()) {
                case "nop":
                    i++;
                    break;
                case "acc":
                    i++;
                    acc += op.number();
                    break;
                case "jmp":
                    i += op.number();
                    break;
            }
            if (i == ops.size()) {
                return new Result(acc, true);
            }
            op.setFlag();
        }
        return new Result(0, false);
    }

}

class Result {

    int acc;
    boolean success;

    public Result(final int acc, final boolean success) {
        this.acc = acc;
        this.success = success;
    }
}

class Op {

    String opType;
    int number;
    boolean beenRan = false;

    Op(String opType, int number) {
        this.opType = opType;
        this.number = number;
    }

    public void resetFlag() {
        this.beenRan = false;
    }

    public void setFlag() {
        this.beenRan = true;
    }

    public boolean beenRan() {
        return beenRan;
    }

    public String opType() {
        return opType;
    }

    public int number() {
        return number;
    }
}
