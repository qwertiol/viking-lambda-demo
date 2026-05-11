package ru.mephi.vikinglambdademo.service;

import ru.mephi.vikinglambdademo.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class VikingLambdaService {

    public long countByAgeCondition(List<Viking> vikings, Predicate<Integer> agePredicate) {
        return vikings.stream().filter(v -> agePredicate.test(v.age())).count();
    }

    public long countOlderThan(List<Viking> vikings, int age) {
        return countByAgeCondition(vikings, a -> a > age);
    }

    public long countYoungerThan(List<Viking> vikings, int age) {
        return countByAgeCondition(vikings, a -> a < age);
    }

    public long countAgeBetween(List<Viking> vikings, int min, int max) {
        return countByAgeCondition(vikings, a -> a >= min && a <= max);
    }

    public long countAgeOutside(List<Viking> vikings, int min, int max) {
        return countByAgeCondition(vikings, a -> a < min || a > max);
    }

    public long countByBeardAndHair(List<Viking> vikings, BeardStyle beard, HairColor hair) {
        return vikings.stream()
                .filter(v -> v.beardStyle() == beard && v.hairColor() == hair)
                .count();
    }

    public long countWithOneOrTwoAxes(List<Viking> vikings) {
        return vikings.stream()
                .filter(v -> {
                    long axeCount = v.equipment().stream()
                            .filter(item -> item.name().equalsIgnoreCase("Axe"))
                            .count();
                    return axeCount == 1 || axeCount == 2;
                })
                .count();
    }

    public Optional<Viking> getRandomVikingHeightAbove180(List<Viking> vikings) {
        List<Viking> tall = vikings.stream().filter(v -> v.heightCm() > 180).collect(Collectors.toList());
        if (tall.isEmpty()) return Optional.empty();
        Random rand = new Random();
        return Optional.of(tall.get(rand.nextInt(tall.size())));
    }

    public List<Viking> getVikingsWithLegendaryEquipment(List<Viking> vikings) {
        return vikings.stream()
                .filter(v -> v.equipment().stream().anyMatch(item -> "Legendary".equalsIgnoreCase(item.quality())))
                .collect(Collectors.toList());
    }

    public List<Viking> getRedHairedSortedByAge(List<Viking> vikings) {
        return vikings.stream()
                .filter(v -> v.hairColor() == HairColor.Red)
                .sorted(Comparator.comparingInt(Viking::age))
                .collect(Collectors.toList());
    }

    public Optional<Integer> findMaxId(List<Viking> vikings) {
        return vikings.stream().map(Viking::id).max(Comparator.naturalOrder());
    }

    public List<Integer> getEvenIds(List<Viking> vikings) {
        return vikings.stream().map(Viking::id).filter(id -> id % 2 == 0).collect(Collectors.toList());
    }
}