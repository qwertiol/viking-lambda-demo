package ru.mephi.vikinglambdademo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikinglambdademo.model.Viking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {
    private final CopyOnWriteArrayList<Viking> vikings = new CopyOnWriteArrayList<>();
    private final VikingFactory vikingFactory;

    @Autowired
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

    public Viking addViking(Viking viking) {
        vikings.add(viking);
        return viking;
    }

    public boolean deleteViking(String id) {
        return vikings.removeIf(v -> v.id().equals(id));
    }

    public boolean updateViking(String id, Viking updatedViking) {
        for (int i = 0; i < vikings.size(); i++) {
            if (vikings.get(i).id().equals(id)) {
                vikings.set(i, updatedViking);
                return true;
            }
        }
        return false;
    }

    // добавлено
    public void generateMassVikings(int count) {
        if (count <= 0) return;
        List<Viking> newVikings = IntStream.range(0, count)
                .mapToObj(i -> vikingFactory.createRandomViking())
                .collect(Collectors.toList());
        vikings.addAll(newVikings);
    }
}