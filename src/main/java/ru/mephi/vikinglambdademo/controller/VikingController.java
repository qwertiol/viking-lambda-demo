package ru.mephi.vikinglambdademo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikinglambdademo.model.Viking;
import ru.mephi.vikinglambdademo.service.VikingService;
import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    public List<Viking> getAllVikings() {
        return vikingService.findAll();
    }

    @GetMapping("/test")
    public List<String> test() {
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    public Viking addRandomViking() {
        Viking saved = vikingService.createRandomViking();
        vikingListener.onVikingAdded(saved);
        return saved;
    }

    @PostMapping
    public Viking addViking(@RequestBody Viking viking) {
        Viking saved = vikingService.addViking(viking);
        vikingListener.onVikingAdded(saved);
        return saved;
    }

    @DeleteMapping("/{id}")
    public boolean deleteViking(@PathVariable int id) {
        boolean deleted = vikingService.deleteViking(id);
        if (deleted) {
            vikingListener.onVikingDeleted(id);
        }
        return deleted;
    }

    @PutMapping("/{id}")
    public boolean updateViking(@PathVariable int id, @RequestBody Viking updatedViking) {
        boolean updated = vikingService.updateViking(id, updatedViking);
        if (updated) {
            vikingListener.onVikingUpdated(updatedViking);
        }
        return updated;
    }
}