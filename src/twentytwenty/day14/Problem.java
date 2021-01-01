package twentytwenty.day14;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/14
    public static void main(String[] args) throws IOException {
        var actions = Arrays.stream(Files.readString(Paths.get("src/twentytwenty/day14/input")).split("mask = "))
                .skip(1)
                .flatMap(str -> {
                    var list = List.of(str.split("\n"));
                    var mask = list.get(0);
                    var acts = new ArrayList<Action>();
                    for (var s : list.subList(1, list.size())) {
                        var matcher = Pattern.compile("\\d+").matcher(s);
                        matcher.find();
                        var address = Integer.valueOf(matcher.group());
                        matcher.find();
                        var val = Long.valueOf(matcher.group());
                        acts.add(new Action(mask, address, val));
                    }
                    return acts.stream();
                })
                .collect(Collectors.toList());

        var problem1 = actions.stream()
                .map(action -> {
                    var maskedVal = maskP1(action.getMask(), action.getVal());
                    return new AbstractMap.SimpleEntry<>(action.getAddress(), maskedVal);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2))
                .values().stream().mapToLong(Long::longValue).sum();

        System.out.println("Problem 1 Answer is: " + problem1);
        Util.assertEquals(6386593869035L, problem1);

        var problem2 = actions.stream()
                .flatMap(action ->
                        maskP2(action.getMask(), action.getAddress().longValue())
                                .stream()
                                .map(addr -> new AbstractMap.SimpleEntry<>(addr, action.getVal())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2))
                .values().stream().mapToLong(Long::longValue).sum();

        System.out.println("Problem 2 Answer is: " + problem2);
        Util.assertEquals(4288986482164L, problem1);
    }

    private static Long maskP1(final String mask, final Long val) {
        String input = binaryPad(val, mask.length());

        final var charArray = mask.toCharArray();
        final var sb = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            final char c = charArray[i];
            switch (c) {
                case 'X':
                    sb.append(input.charAt(i));
                    break;
                default:
                    sb.append(c);
            }
        }
        return fromBinary(sb);
    }

    private static List<Long> maskP2(final String mask, final Long addr) {
        String input = binaryPad(addr, mask.length());

        final var charArray = mask.toCharArray();
        final var sb = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            final char c = charArray[i];
            switch (c) {
                case '0':
                    sb.append(input.charAt(i));
                    break;
                default:
                    sb.append(c);
            }
        }
        return getMutations(new ArrayList<>(), sb, 0);
    }

    private static List<Long> getMutations(List<Long> current, StringBuilder s, int index) {
        if (index >= s.length()) {
            return current;
        }

        if (s.charAt(index) == 'X') {
            StringBuilder s0 = with(s, index, '0');
            StringBuilder s1 = with(s, index, '1');
            getMutations(current, s0, index);
            getMutations(current, s1, index);
            return current;
        } else if (index >= s.length() - 1) {
            System.out.println("Adding " + s.toString());
            current.add(fromBinary(s));
            return current;
        }
        return getMutations(current, s, index + 1);
    }

    private static StringBuilder with(final StringBuilder s, final int index, final char c) {
        var s0 = new StringBuilder(s);
        s0.setCharAt(index, c);
        return s0;
    }

    private static long fromBinary(final StringBuilder sb) {
        return new BigInteger(sb.toString(), 2).longValue();
    }

    private static String binaryPad(final Long val, final int length) {
        String input = Long.toBinaryString(val);
        input = String.format("%1$" + length + "s", input).replace(' ', '0'); //Pad With 0s
        return input;
    }
}

class Action {

    private final String mask;
    private final Integer address;
    private final Long val;

    public Action(String mask, int address, long val) {
        this.mask = mask;
        this.address = address;
        this.val = val;
    }

    public String getMask() {
        return mask;
    }

    public Integer getAddress() {
        return address;
    }

    public Long getVal() {
        return val;
    }
}
