package twentytwenty.day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;

public class Problem {

    public static void main(String[] args) throws IOException {
        var groups = Stream.of(Files.readString(Paths.get("src/twentytwenty/day6/input")))
                .flatMap(string -> Stream.of(string.split("\n\n")))
                .map(group -> group.split("\n"))
                .map(strArr -> Stream.of(strArr)
                        .map(s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new))
                        .map(Set::of)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        final var unionSum = groups.stream()
                .map(group -> group.stream()
                        .reduce((set1, set2) -> {
                            var cset1 = new HashSet<>(set1);
                            cset1.addAll(set2);
                            return cset1;
                        }).get().size())
                .reduce(Math::addExact)
                .get();


        final var intersectSum = groups.stream()
                .map(group -> group.stream()
                        .reduce((set1, set2) -> {
                            var cset1 = new HashSet<>(set1);
                            cset1.retainAll(set2);
                            return cset1;
                        }).get().size())
                .reduce(Math::addExact)
                .get();

        System.out.println("Problem 1: Union size = " + unionSum);
        Util.assertEquals(6504, unionSum);

        System.out.println("Problem 2: Missing ID = " + intersectSum);
        Util.assertEquals(3351, intersectSum);
    }
}
