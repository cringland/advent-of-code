package twentytwenty.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/2
    public static void main(String[] args) throws IOException {
        long problem1 = Files.readAllLines(Paths.get("src/twentytwenty/day2/input")).stream()
                .filter(input -> {
                    int lowerBound = Integer.valueOf(input.split("-")[0]);
                    int upperBound = Integer.valueOf(input.split("-")[1].split(" ")[0]);
                    String requiredChar = input.split(" ")[1].split(":")[0];
                    String password = input.substring(7);
                    int count = password.length() - password.replaceAll(requiredChar, "").length();
                    return count >= lowerBound && count <= upperBound;
                }).count();

        long problem2 = Files.readAllLines(Paths.get("src/twentytwenty/day2/input")).stream()
                .filter(input -> {
                    int pos1 = Integer.valueOf(input.split("-")[0]) - 1;
                    int pos2 = Integer.valueOf(input.split("-")[1].split(" ")[0]) - 1;
                    String requiredChar = input.split(" ")[1].split(":")[0];
                    String password = input.split(" ")[2];
                    return requiredChar.equals(String.valueOf(password.charAt(pos1)))
                            ^ requiredChar.equals(String.valueOf(password.charAt(pos2)));
                }).count();
        System.out.println("Problem 1: Number of valid Passwords: " + problem1);
        Util.assertEquals(456L, problem1);
        System.out.println("Problem 2: Number of valid Passwords: " + problem2);
        Util.assertEquals(308L, problem2);

    }
}
