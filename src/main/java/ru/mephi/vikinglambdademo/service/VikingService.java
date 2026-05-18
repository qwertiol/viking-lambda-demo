package ru.mephi.vikinglambdademo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikinglambdademo.model.Viking;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class VikingService {
    private final CopyOnWriteArrayList<Viking> vikings = new CopyOnWriteArrayList<>();
    private final VikingFactory vikingFactory;

    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public List<Viking> findAll() {
        return List.copyOf(vikings);
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        vikings.add(viking);
        return viking;
    }

    // новый метод дз4
    public Viking addViking(Viking viking) {
        vikings.add(viking);
        return viking;
    }

    // новый метод дз4
    public boolean deleteViking(int id) {
        return vikings.removeIf(v -> v.id() == id);
    }

    // новый метод дз4
    public boolean updateViking(int id, Viking updatedViking) {
        for (int i = 0; i < vikings.size(); i++) {
            if (vikings.get(i).id() == id) {
                vikings.set(i, updatedViking);
                return true;
            }
        }
        return false;
    }

    // новый метод дз5
    public void generateMassVikings(int count) {
        if (count <= 0) return;
        List<Viking> newVikings = IntStream.range(0, count) // создает поток чисел от 0 до count-1
                .mapToObj(i -> vikingFactory.createRandomViking()) // для каждого числа вызывает фабрику и создает викинга
                .collect(Collectors.toList()); // собирает в промежуточный список
        vikings.addAll(newVikings);
    }
}