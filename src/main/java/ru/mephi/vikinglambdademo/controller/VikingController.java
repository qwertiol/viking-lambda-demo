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
    @Operation(summary = "Get viking list")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Get test viking list")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    // Реализовать метод для добавления конкретного викинга
    @PostMapping
    @Operation(summary = "Create custom viking")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Викинг добавлен")})
    public Viking addViking(@RequestBody Viking viking) {
        System.out.println("POST /api/vikings called with: " + viking.name());
        Viking saved = vikingService.addViking(viking);
        vikingListener.onVikingAdded(saved);
        return saved;
    }

    // Реализовать метод для удаления викинга из таблицы
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete viking by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Удаление выполнено"),
        @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })
    public boolean deleteViking(@PathVariable String id) {
        System.out.println("DELETE /api/vikings/" + id);
        boolean deleted = vikingService.deleteViking(id);
        if (deleted) {
            vikingListener.onVikingDeleted(id);
        }
        return deleted;
    }

    // Реализовать метод для перезаписи параметров конкретного викинга
    @PutMapping("/{id}")
    @Operation(summary = "Update viking by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Обновление выполнено"),
        @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })
    public boolean updateViking(@PathVariable String id, @RequestBody Viking updatedViking) {
        System.out.println("PUT /api/vikings/" + id);
        boolean updated = vikingService.updateViking(id, updatedViking);
        if (updated) {
            vikingListener.onVikingUpdated(updatedViking);
        }
        return updated;
    }

    // НАДО // получается неоднородная (разная) реализация 
    @PostMapping("/post")
    @Operation(summary = "Create random viking")
    public void addRandomViking() {
        vikingListener.testAdd();
    }
}