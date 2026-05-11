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

    public void onVikingAdded(Viking viking) {
        if (gui != null) gui.addNewViking(viking);
    }

    public void onVikingDeleted(int id) {
        if (gui != null) gui.removeVikingById(id);
    }

    public void onVikingUpdated(Viking viking) {
        if (gui != null) gui.updateViking(viking);
    }
}