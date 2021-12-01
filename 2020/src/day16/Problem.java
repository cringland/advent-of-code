package day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;
import common.model.Range;

public class Problem {

    //https://adventofcode.com/2020/day/16
    public static void main(String[] args) throws IOException {
        var input = Files.readString(Paths.get("src/twentytwenty/day16/input")).split("\n\n");
        var validations = Arrays.stream(input[0].split("\n"))
                .map(str -> {
                    var split = List.of(str.split(":| or "));
                    var key = split.get(0);
                    var ranges = split.subList(1, split.size()).stream()
                            .map(range -> {
                                var matcher = Pattern.compile("\\d+").matcher(range);
                                matcher.find();
                                var lowerBound = Long.valueOf(matcher.group());
                                matcher.find();
                                var upperBound = Long.valueOf(matcher.group());
                                return Range.of(lowerBound, upperBound);
                            })
                            .map(range -> (Predicate<Long>) range::testInclusive)
                            .reduce(Predicate::or).get();
                    return new AbstractMap.SimpleEntry<>(key, ranges);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        var myTicket = ticket(input[1].split("\n")[1]);

        var nearbyTickets = Stream.of(input[2].split("\n")).skip(1)
                .map(Problem::ticket)
                .collect(Collectors.toList());

        var p1ErrRate = nearbyTickets.stream()
                .flatMap(ticket ->
                        ticket.stream().filter(val ->
                                validations.values().stream().noneMatch(pred -> pred.test(val))
                        )
                ).mapToLong(Long::longValue).sum();

        var validTickets = nearbyTickets.stream()
                .filter(ticket ->
                        ticket.stream().allMatch(val ->
                                validations.values().stream().anyMatch(pred -> pred.test(val))
                        )
                ).collect(Collectors.toList());

        List<List<String>> indexToMatches = new ArrayList<>();
        for (int i = 0; i < validTickets.get(0).size(); i++) {
            final var r = i;
            var vals = validTickets.stream().map(l -> l.get(r)).collect(Collectors.toList());
            var l = validations.entrySet().stream()
                    .filter(entry -> vals.stream().allMatch(val -> entry.getValue().test(val)))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            indexToMatches.add(l);
        }

        while (indexToMatches.stream().anyMatch(list -> list.size() > 1)) {
            for (List<String> strings : indexToMatches) {
                if (strings.size() == 1) {
                    indexToMatches.forEach(list -> {
                        if (list.size() != 1)
                            list.remove(strings.get(0));
                    });
                }
            }
        }

        long problem2Product = 1L;
        for (int i = 0; i < indexToMatches.size(); i++) {
            if (indexToMatches.get(i).get(0).startsWith("departure")) {
                problem2Product *= myTicket.get(i);
            }
        }

        System.out.println("Problem 1 Answer is: " + p1ErrRate);
        Util.assertEquals(20058L, p1ErrRate);
        System.out.println("Problem 2 Answer is: " + problem2Product);
        Util.assertEquals(366871907221L, problem2Product);
    }

    private static List<Long> ticket(String ticket) {
        return Stream.of(ticket.split(",")).map(Long::valueOf).collect(Collectors.toList());
    }
}
