package ru.mephi.vikinglambdademo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikinglambdademo.model.Viking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

    // Реализовать метод для добавления конкретного викинга
    public Viking addViking(Viking viking) {
        vikings.add(viking);
        return viking;
    }

    // Реализовать метод для удаления викинга из таблицы
    public boolean deleteViking(String id) {
        return vikings.removeIf(v -> v.id().equals(id));
    }

    // Реализовать метод для перезаписи параметров конкретного викинга
    public boolean updateViking(String id, Viking updatedViking) {
        for (int i = 0; i < vikings.size(); i++) {
            if (vikings.get(i).id().equals(id)) {
                vikings.set(i, updatedViking);
                return true;
            }
        }
        return false;
    }
}