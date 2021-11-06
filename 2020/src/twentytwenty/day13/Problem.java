package twentytwenty.day13;

import static java.util.AbstractMap.Entry;
import static java.util.AbstractMap.SimpleEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/13
    public static void main(String[] args) throws IOException {

        final var input = Files.readAllLines(Paths.get("src/twentytwenty/day13/input"));
        final var earliestTime = Integer.valueOf(input.get(0));
        final var busIds = Stream.of(input.get(1).split(","))
                .map(s -> s.equals("x") ? "0" : s)
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        var problem1Answer = 1.0;
        for (int i = earliestTime; problem1Answer == 1.0; i++) {
            for (var busId : busIds) {
                if (busId != 0 && i % busId == 0) {
                    problem1Answer = (i - earliestTime) * busId;
                    break;
                }
            }
        }
        System.out.println("Problem 1 Answer is: " + problem1Answer);
        Util.assertEquals(136.0, problem1Answer);


        var vals = new ArrayList<Entry<Long, Long>>();
        for (Long i = 0L; i < busIds.size(); i++) {
            long mod = busIds.get(i.intValue());
            if (mod != 0) {
                long rem = mod - i;
                if (rem == mod)
                    rem = 0;
                vals.add(new SimpleEntry<>(rem, mod));
            }
        }

        final var sorted = vals.stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toList());

        long M = sorted.stream().map(Entry::getValue).reduce(Math::multiplyExact).get();

        long result = 0;
        for (var entry : sorted) {
            Long pp = M / entry.getValue();
            result += entry.getKey() * inv(pp, entry.getValue()) * pp;
        }

        var problem2Answer = result % M;
        System.out.println("Problem 2 Answer is: " + problem2Answer);
        Util.assertEquals(305068317272992L, problem2Answer);
    }

    static long inv(long a, long m) {
        long m0 = m, t, q;
        long x0 = 0, x1 = 1;

        if (m == 1)
            return 0;

        while (a > 1) {
            q = a / m;

            t = m;

            m = a % m;
            a = t;

            t = x0;

            x0 = x1 - q * x0;

            x1 = t;
        }

        if (x1 < 0)
            x1 += m0;

        return x1;
    }
}
