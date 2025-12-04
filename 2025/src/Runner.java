import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Runner {
    public static final List<Day> DAYS = List.of(new Day1(),
            new Day2(),
            new Day3(),
            new Day4());
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;


    public static void main(String[] args) {
        var startTime = LocalDateTime.now();
        log("Program started", startTime);
        var day = DAYS.getLast();
        var input = Input.input(day.getClass());
        var test = Input.test(day.getClass());
        var lastTime = LocalDateTime.now();
        log("Inputs Read in %s ms", lastTime, ChronoUnit.MILLIS.between(startTime, lastTime));


        if (day.sampleAnswerP1() != null) {
            assertEquals(day.sampleAnswerP1(), day.part1(test));
            var p1TestTime = LocalDateTime.now();
            log("Part 1 Test done in %s ms", p1TestTime, ChronoUnit.MILLIS.between(lastTime, p1TestTime));
            lastTime = p1TestTime;
        }

        if (day.sampleAnswerP2() != null) {
            assertEquals(day.sampleAnswerP2(), day.part2(test));
            var p2TestTime = LocalDateTime.now();
            log("Part 2 Test done in %s ms", p2TestTime, ChronoUnit.MILLIS.between(lastTime, p2TestTime));
            lastTime = p2TestTime;
        }

        var p1Answer = day.part1(input);
        var p1Time = LocalDateTime.now();
        log("Part 1 done in %s ms : %s", p1Time, ChronoUnit.MILLIS.between(lastTime, p1Time), p1Answer);
        var p2Answer = day.part2(input);
        var p2Time = LocalDateTime.now();
        log("Part 1 done in %s ms : %s", p2Time, ChronoUnit.MILLIS.between(lastTime, p2Time), p2Answer);
    }

    public static void log(String s, LocalDateTime currentTime, Object... args) {
        System.out.println(currentTime.format(TIME_FORMAT) + ": " + s.formatted(args));
    }

    public static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("""
                    "%s" did not match expected value "%s"
                    """.formatted(actual, expected));
        }
    }
}
