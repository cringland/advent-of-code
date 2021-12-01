package day7;

import static java.lang.String.format;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;

public class Problem {

    private static List<Rule> allRules = inputLines().stream()
            .map(Problem::parseRule)
            .collect(Collectors.toList());

    //https://adventofcode.com/2020/day/7
    public static void main(String[] args) {
        //Problem 1
        Set<String> currentList = new HashSet<>(Set.of("shiny gold"));
        var lastCount = 0;
        while (currentList.size() != lastCount) {
            lastCount = currentList.size();
            for (Rule rule : allRules) {
                if (rule.containsAny(currentList)) {
                    currentList.add(rule.bagColour);
                }
            }
        }

        //Problem 2
        Rule head = findRule("shiny gold");
        populateChildren(head);
        int goldInnerBags = countBags(head);

        System.out.println("Problem 1: Number of bags that can hold a gold bag = " + (lastCount - 1));
        Util.assertEquals(257, (lastCount - 1)); // -1 as it contains "shiny gold"

        System.out.println("Problem 2: Total bags in a gold bag = " + (goldInnerBags - 1));
        Util.assertEquals(1038, goldInnerBags - 1); // -1 as it contains the "shiny gold" bag
    }


    private static int countBags(Rule rule) {
        var currentBag = rule.number;
        int total = currentBag;
        for (var child : rule.children) {
            var childsCount = countBags(child);
            total += currentBag * childsCount;
        }
        return total;
    }

    private static void populateChildren(Rule rule) {
        for (var child : rule.children) {
            var childsRule = findRule(child.bagColour);
            child.setChildren(childsRule.children);
            populateChildren(child);
        }
    }

    private static Rule findRule(String colour) {
        return allRules.stream().filter(rule -> rule.bagColour.equals(colour)).findFirst().get();
    }

    private static Rule parseRule(final String str) {
        var colours = Stream.of(str.replaceAll("contain|\\.", "")
                .split("bags,?|bag,?"))
                .map(String::trim)
                .collect(Collectors.toList());
        var rootColour = colours.get(0);
        var childColours = colours
                .subList(1, colours.size()).stream()
                .filter(s -> !s.equals("no other"))
                .map(s -> {
                    var temp = s.split(" ", 2);
                    return new Rule(temp[1], Integer.valueOf(temp[0]));
                }).collect(Collectors.toList());
        return new Rule(rootColour, childColours);
    }

    private static List<String> inputLines() {
        try {
            return Files.readAllLines(Paths.get("src/twentytwenty/day7/input"));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

class Rule {

    String bagColour;
    int number = 1;
    List<Rule> children = new ArrayList<>();

    Rule(String bagColour, int number) {
        this.bagColour = bagColour;
        this.number = number;
    }

    Rule(String bagColour, List<Rule> childColours) {
        this.bagColour = bagColour;
        this.setChildren(childColours);
    }

    boolean containsAny(Collection<String> colours) {
        return colours.contains(bagColour) || children.stream()
                .anyMatch(rule -> rule.containsAny(colours));
    }

    void setChildren(List<Rule> childColours) {
        children = new ArrayList<>(childColours);
    }

    @Override
    public String toString() {
        return format("%s %s bag has %s", number, bagColour, children);
    }
}
