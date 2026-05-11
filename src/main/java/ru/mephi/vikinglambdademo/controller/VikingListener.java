package ru.mephi.vikinglambdademo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.vikinglambdademo.gui.VikingDesktopFrame;
import ru.mephi.vikinglambdademo.model.Viking;
import ru.mephi.vikinglambdademo.service.VikingService;

@Component
public class VikingListener {
    private final VikingService service;
    private VikingDesktopFrame gui;

    @Autowired
    public VikingListener(VikingService service) {
        this.service = service;
    }

    public void setGui(VikingDesktopFrame gui) {
        this.gui = gui;
    }

    // Вызывается при добавлении случайного викинга через старую кнопку
    void testAdd() {
        Viking v = service.createRandomViking();
        gui.addNewViking(v);
    }

    // Реализовать метод для добавления конкретного викинга
    public void onVikingAdded(Viking viking) {
        if (gui != null) {
            gui.addNewViking(viking);
        }
    }

    // Реализовать метод для удаления викинга из таблицы
    public void onVikingDeleted(String id) {
        if (gui != null) {
            gui.removeVikingById(id);
        }
    }

    // Реализовать метод для перезаписи параметров конкретного викинга
    public void onVikingUpdated(Viking viking) {
        if (gui != null) {
            gui.updateViking(viking);
        }
    }
}