package ru.mephi.vikinglambdademo.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import ru.mephi.vikinglambdademo.model.BeardStyle;
import ru.mephi.vikinglambdademo.model.EquipmentItem;
import ru.mephi.vikinglambdademo.model.HairColor;
import ru.mephi.vikinglambdademo.model.Viking;
import java.util.Locale;

@Component
public class VikingFactory {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    public Viking createRandomViking() {
        return new Viking(
                UUID.randomUUID().toString(),
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