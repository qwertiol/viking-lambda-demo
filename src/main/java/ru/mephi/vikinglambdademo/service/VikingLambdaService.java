package ru.mephi.vikinglambdademo.service;

import ru.mephi.vikinglambdademo.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.BinaryOperator;
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
                .filter(v -> v.equipment().stream()
                        .anyMatch(item -> "Legendary".equalsIgnoreCase(item.quality())))
                .collect(Collectors.toList());
    }
    public List<Viking> getRedBeardedSortedByAge(List<Viking> vikings) {
        return vikings.stream()
                .filter(v -> v.hairColor() == HairColor.Red && v.beardStyle() != BeardStyle.CLEAN_SHAVEN)
                .sorted(Comparator.comparingInt(Viking::age))
                .collect(Collectors.toList());
    }

    public Integer[] toIdArray(List<Viking> vikings) {
        Integer[] ids = new Integer[vikings.size()];
        for (int i = 0; i < vikings.size(); i++) {
            ids[i] = vikings.get(i).id();
        }
        return ids;
    }

    public Optional<Integer> findMaxId(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return Optional.empty();
        }
        BinaryOperator<Integer> maxBy = BinaryOperator.maxBy(Comparator.naturalOrder());
        Integer result = ids[0];
        for (int i = 1; i < ids.length; i++) {
            result = maxBy.apply(result, ids[i]);
        }
        return Optional.of(result);
    }

    public List<Integer> getEvenIds(Integer[] ids) {
        Predicate<Integer> isEven = id -> id % 2 == 0;
        List<Integer> result = new ArrayList<>();
        for (Integer id : ids) {
            if (isEven.test(id)) {
                result.add(id);
            }
        }
        return result;
    }
}