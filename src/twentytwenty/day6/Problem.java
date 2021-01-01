package twentytwenty.day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.SetUtil;
import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/6
    public static void main(String[] args) throws IOException {
        var groups = Stream.of(Files.readString(Paths.get("src/twentytwenty/day6/input")).split("\n\n"))
                .map(str -> str.lines()
                        .map(s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new))
                        .map(Set::of)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        final var unionSum = groups.stream()
                .map(group -> group.stream()
                        .reduce(SetUtil::union)
                        .get().size())
                .reduce(0, Math::addExact);


        final var intersectSum = groups.stream()
                .map(group -> group.stream()
                        .reduce(SetUtil::interesect)
                        .get().size())
                .reduce(0, Math::addExact);

        System.out.println("Problem 1: Union size = " + unionSum);
        Util.assertEquals(6504, unionSum);

        System.out.println("Problem 2: Interesect sum = " + intersectSum);
        Util.assertEquals(3351, intersectSum);
    }
}
