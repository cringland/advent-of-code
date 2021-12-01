package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import common.SetUtil;
import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/21
    public static void main(String[] args) throws IOException {
        var allFoods = Files.readAllLines(Paths.get("src/twentytwenty/day21/input")).stream()
                .map(str -> {
                    var split = str.replaceAll("[,)]", "").split(" \\(contains ");
                    var ingredients = Set.of(split[0].split(" "));
                    var allergens = Set.of(split[1].split(" "));
                    return new Food(ingredients, allergens);
                })
                .collect(Collectors.toList());

        var allAllergens = getAll(allFoods, Food::getAllergens);
        var allIngredients = getAll(allFoods, Food::getIngredients);

        var maybesL = allAllergens.stream()
                .map(allergen -> new AbstractMap.SimpleEntry<>(allergen, allFoods.stream()
                        .filter(food -> food.hasAllergen(allergen))
                        .map(Food::getIngredients)
                        .reduce(SetUtil::interesect).get())
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        var maybes = maybesL.values().stream().reduce(new HashSet<>(), SetUtil::union);

        var nopes = SetUtil.difference(allIngredients, maybes);

        var nopeCount = allFoods.stream()
                .map(food -> nopes.stream().filter(food::hasIngredient).count())
                .reduce(0L, Math::addExact);

        System.out.println("Problem 1 Answer is: " + nopeCount);
        Util.assertEquals(2262L, nopeCount);
        var maybesMap = maybesL.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> new HashSet<>(entry.getValue())));

        while (maybesMap.values().stream().anyMatch(set -> set.size() > 1)) {
            for (var entry : maybesMap.entrySet()) {
                if (entry.getValue().size() == 1) {
                    maybesMap.forEach((key, value) -> {
                        if (value.size() != 1)
                            value.remove(new ArrayList<>(entry.getValue()).get(0));
                    });
                }
            }
        }

        List<Map.Entry<String, String>> allergenIngred = maybesMap.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), new ArrayList<>(entry.getValue()).get(0)))
                .collect(Collectors.toList());

        var p2Ans = allergenIngred.stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .reduce((s1,s2) -> s1 + "," + s2).get();

        System.out.println("Problem 2 Answer is: " + p2Ans);
        Util.assertEquals("cxsvdm,glf,rsbxb,xbnmzr,txdmlzd,vlblq,mtnh,mptbpz", p2Ans);
    }

    private static Set<String> getAll(final List<Food> foods, final Function<Food, Set<String>> getter) {
        return foods.stream().map(getter).reduce(SetUtil::union).get();
    }

    private static <T> boolean isNotEmpty(final Collection<T> collection) {
        return !collection.isEmpty();
    }
}

class Food {

    private Set<String> ingredients;
    private Set<String> allergens;

    public Food(Set<String> ingredients, Set<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public Set<String> getAllergens() {
        return allergens;
    }

    public boolean hasAllergen(String s) {
        return allergens.contains(s);
    }

    public boolean hasIngredient(String s) {
        return ingredients.contains(s);
    }

    public Food withoutIngredients(Collection<String> filterIngreds) {
        var newIngredients = this.ingredients.stream().filter(o -> !filterIngreds.contains(o)).collect(Collectors.toSet());
        return new Food(newIngredients, allergens);
    }
}
