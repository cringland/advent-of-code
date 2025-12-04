public interface Day<T> {
    T sampleAnswerP1();
    T sampleAnswerP2();
    T part1(Input input);
    T part2(Input input);
}
