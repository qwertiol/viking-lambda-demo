package ru.mephi.vikinglambdademo.service;

import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import ru.mephi.vikinglambdademo.model.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class VikingFactory {
    private final Faker faker = new Faker();
    private final Random random = new Random();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Viking createRandomViking() {
        return new Viking(
                idGenerator.getAndIncrement(),
                faker.name().firstName(),
                18 + random.nextInt(43),
                160 + random.nextInt(41),
                HairColor.values()[random.nextInt(HairColor.values().length)],
                BeardStyle.values()[random.nextInt(BeardStyle.values().length)],
                createRandomEquipment()
        );
    }

    private List<EquipmentItem> createRandomEquipment() {
        return List.of(
                EquipmentFactory.createItem(),
                EquipmentFactory.createItem()
        );
    }
}