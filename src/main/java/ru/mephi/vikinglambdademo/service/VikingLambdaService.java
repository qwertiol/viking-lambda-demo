package ru.mephi.vikinglambdademo.service;

import ru.mephi.vikinglambdademo.model.BeardStyle;
import ru.mephi.vikinglambdademo.model.HairColor;
import ru.mephi.vikinglambdademo.model.Viking;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class VikingLambdaService {

    public long countByAgeCondition(List<Viking> vikings, Predicate<Integer> agePredicate) {
        return vikings.stream()
                .filter(v -> agePredicate.test(v.age()))
                .count();
    }

    public long countOlderThan(List<Viking> vikings, int age) {
        return countByAgeCondition(vikings, a -> a > age);
    }

    public long countYoungerThan(List<Viking> vikings, int age) {
        return countByAgeCondition(vikings, a -> a < age);
    }

    public long countAgeBetween(List<Viking> vikings, int minAge, int maxAge) {
        return countByAgeCondition(vikings, a -> a >= minAge && a <= maxAge);
    }

    public long countAgeOutside(List<Viking> vikings, int minAge, int maxAge) {
        return countByAgeCondition(vikings, a -> a < minAge || a > maxAge);
    }

    public long countByBeardAndHair(List<Viking> vikings, BeardStyle beard, HairColor hair) {
        return vikings.stream()
                .filter(v -> v.beardStyle() == beard && v.hairColor() == hair)
                .count();
    }

    public long countWithAxes(List<Viking> vikings, int axeCount) {
        return vikings.stream()
                .filter(v -> countAxes(v) == axeCount)
                .count();
    }

    private long countAxes(Viking v) {
        return v.equipment().stream()
                .filter(item -> item.name().equalsIgnoreCase("Axe"))
                .count();
    }

    public Optional<Viking> getRandomVikingHeightAbove180(List<Viking> vikings) {
        List<Viking> tall = vikings.stream()
                .filter(v -> v.heightCm() > 180)
                .collect(Collectors.toList());
        if (tall.isEmpty()) return Optional.empty();
        Random random = new Random();
        return Optional.of(tall.get(random.nextInt(tall.size())));
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

    public Optional<Integer> findMaxId(List<Integer> ids) {
        return ids.stream().max(Comparator.naturalOrder());
    }

    public List<Integer> getEvenIds(List<Integer> ids) {
        return ids.stream().filter(id -> id % 2 == 0).collect(Collectors.toList());
    }
}