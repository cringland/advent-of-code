package twentytwenty.day13;

import static java.util.AbstractMap.Entry;
import static java.util.AbstractMap.SimpleEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
        for (int i = earliestTime; problem1Answer == 0.0; i++) {
            for (var busId : busIds) {
                if (busId != 0 && i % busId == 0) {
                    problem1Answer = (i - earliestTime) * busId;
                    break;
                }
            }
        }
        System.out.println("Problem 1 Answer is: " + problem1Answer);
//        Util.assertEquals(136.0, problem1Answer);


        var vals = new ArrayList<Entry<Long, Long>>();
//        vals.add(new SimpleEntry<>(2L, 3L));
//        vals.add(new SimpleEntry<>(2L, 4L));
//        vals.add(new SimpleEntry<>(2L, 3L));
        for (Long i = 0L; i < busIds.size(); i++) {
            long mod = busIds.get(i.intValue());
            if (mod != 0) {
                long rem = mod - i;
                if(rem == mod)
                    rem = 0;
//                while (rem > mod)
//                    rem -= mod;
                vals.add(new SimpleEntry<>(rem, mod));
            }
        }

        final var sorted = vals.stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
//                .peek(entry -> System.out.println("GCD for " + entry.toString() + " is " + gcd(entry)))
                .collect(Collectors.toList());

        long M = sorted.stream().map(Entry::getValue).reduce(Math::multiplyExact).get();

        long result = 0;
        for (var entry : sorted) {
            Long pp = M / entry.getValue();
            result += entry.getKey() * inv(pp, entry.getValue()) * pp;
        }

//        for (long i = 1; ; i++) {
//            var match = true;
//            for (var entry : sorted) {
//                long rem = entry.getKey();
//                long mod = entry.getValue();
//                if(rem != i % mod) {
//                    match = false;
//                    break;
//                }
//            }
//            if(match) {
//                System.out.println("Match = " + i);
//                break;
//            }
//        }


// I guessed 739320315996301
        System.out.println("Problem 2 Answer is: " + (result % M));
//        Util.assertEquals(29401L, problem1Answer);
    }

    static long gcd(Entry<Long, Long> entry) {
        
        return gcd(entry.getKey(), entry.getValue());

    }

    static long gcd(long num1, long num2) {
        if (num1 == 0 || num2 == 0)
            return 0;
        while (num1 != num2) {
            if (num1 > num2)
                num1 = num1 - num2;
            else
                num2 = num2 - num1;
        }
        return num1;
    }

    static long inv(long a, long m) {
        long m0 = m, t, q;
        long x0 = 0, x1 = 1;

        if (m == 1)
            return 0;

        // Apply extended Euclid Algorithm 
        while (a > 1) {
            // q is quotient 
            q = a / m;

            t = m;

            // m is remainder now, process 
            // same as euclid's algo 
            m = a % m;
            a = t;

            t = x0;

            x0 = x1 - q * x0;

            x1 = t;
        }

        // Make x1 positive 
        if (x1 < 0)
            x1 += m0;

        return x1;
    }

    private static Map<Integer, Integer> relate(final Map<Integer, Integer> map, final Entry<Integer, Integer> largestEntry) {
        return map.entrySet().stream()
                .map(entry -> new SimpleEntry<>(entry.getKey() - largestEntry.getKey(), entry.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }


    private static List<Long> primes;

    static {
        primes = LongStream.range(2, 613).filter(i -> {
            for (var j = i - 1; j > 1; j--) {
                if ((i % j) == 0)
                    return false;
            }
            return true;
        }).boxed().collect(Collectors.toList());
    }

    private static long lcm(List<Long> ls) {
        return ls.stream()
                .map(Problem::primeFactors)
                .reduce((map1, map2) -> {
                    Set<Long> prims = map1.keySet();
                    prims.addAll(map2.keySet());
                    var newMap = new HashMap<Long, Long>();
                    for (var prime : prims) {
                        long count = Math.max(map1.getOrDefault(prime, 0L), map2.getOrDefault(prime, 0L));
                        newMap.put(prime, count);
                    }
                    return newMap;
                }).get().entrySet().stream()
                .map(entry -> Math.pow(entry.getKey(), entry.getValue()))
                .reduce((l1, l2) -> l1 * l2)
                .map(Double::longValue)
                .get();
    }

    private static Map<Long, Long> primeFactors(final Long l1) {
        long reduce = l1;
        var set = new HashMap<Long, Long>();
        for (var prime : primes) {
            long count = 0;
            while ((reduce % prime) == 0) {
                reduce = reduce / prime;
                set.put(prime, ++count);
            }
        }
        return set;
    }
}
