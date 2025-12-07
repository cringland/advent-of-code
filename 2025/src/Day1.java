import java.util.List;

public class Day1 implements Day<Number> {

    public Number sampleAnswerP1() {
        return 3;
    }

    public Number sampleAnswerP2() {
        return 6;
    }

    public Number part1(Input input) {
        var start = 50;
        var total = 100;
        var zeroCount = 0;
        List<String> lines = input.immutableLines();

        var current = start;
        for (var line: lines) {
            var dir = line.charAt(0);
            var num = Integer.parseInt(line.substring(1));
            if (dir == 'L'){
                current = (current - num + total) % total;
            } else {
                current = (current + num) % total;
            }
            if (current == 0) {
                zeroCount++;
            }
        }
        return zeroCount;
    }

    public Number part2(Input input) {
        var start = 50;
        var total = 100;
        var zeroCount = 0;
        List<String> lines = input.immutableLines();

        var current = start;
        for (var line: lines) {
            var dir = line.charAt(0);
            var num = Integer.parseInt(line.substring(1));
            zeroCount += num / 100;
            var mod = num % total;
            for (int i = 0; i < mod; i++) {
                if (dir == 'L') {
                    current = (current - 1 + total) % total;
                } else {
                    current = (current + 1) % total;
                }
                if (current == 0) {
                    zeroCount++;
                }
            }
        }
        return zeroCount;
    }
}
