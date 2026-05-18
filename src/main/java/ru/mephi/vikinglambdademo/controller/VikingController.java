package ru.mephi.vikinglambdademo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikinglambdademo.model.BeardStyle;
import ru.mephi.vikinglambdademo.model.HairColor;
import ru.mephi.vikinglambdademo.model.Viking;
import ru.mephi.vikinglambdademo.service.VikingLambdaService;
import ru.mephi.vikinglambdademo.service.VikingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;
    private final VikingLambdaService lambdaService;

    public VikingController(VikingService vikingService,
                            VikingListener vikingListener,
                            VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
        this.lambdaService = lambdaService;
    }
    
    @GetMapping
    @Operation(summary = "Получить список всех викингов", operationId = "getAllVikings")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<Viking> getAllVikings() {
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Тестовый endpoint", operationId = "getTest")
    public List<String> test() {
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    @Operation(summary = "Создать случайного викинга", operationId = "addRandomViking")
    public Viking addRandomViking() {
        Viking saved = vikingService.createRandomViking();
        vikingListener.onVikingAdded(saved);
        return saved;
    }

    @PostMapping
    @Operation(summary = "Добавить викинга из тела запроса", operationId = "addViking")
    public Viking addViking(@RequestBody Viking viking) {
        Viking saved = vikingService.addViking(viking);
        vikingListener.onVikingAdded(saved);
        return saved;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить викинга по ID", operationId = "deleteViking")
    public boolean deleteViking(@PathVariable int id) {
        boolean deleted = vikingService.deleteViking(id);
        if (deleted) {
            vikingListener.onVikingDeleted(id);
        }
        return deleted;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить викинга по ID", operationId = "updateViking")
    public boolean updateViking(@PathVariable int id, @RequestBody Viking updatedViking) {
        boolean updated = vikingService.updateViking(id, updatedViking);
        if (updated) {
            vikingListener.onVikingUpdated(updatedViking);
        }
        return updated;
    }
    
    @PostMapping("/generate")
    @Operation(summary = "Массовая генерация викингов", operationId = "generateMassVikings")
    public String generateMassVikings(@RequestParam int count) {
        vikingService.generateMassVikings(count);
        return "Generated " + count + " vikings";
    }

    @GetMapping("/stats/older-than")
    @Operation(summary = "Количество викингов старше заданного возраста")
    public long countOlderThan(@RequestParam int age) {
        return lambdaService.countOlderThan(vikingService.findAll(), age);
    }

    @GetMapping("/stats/younger-than")
    @Operation(summary = "Количество викингов младше заданного возраста")
    public long countYoungerThan(@RequestParam int age) {
        return lambdaService.countYoungerThan(vikingService.findAll(), age);
    }

    @GetMapping("/stats/age-between")
    @Operation(summary = "Количество викингов в диапазоне возраста [min, max]")
    public long countAgeBetween(@RequestParam int min, @RequestParam int max) {
        return lambdaService.countAgeBetween(vikingService.findAll(), min, max);
    }

    @GetMapping("/stats/age-outside")
    @Operation(summary = "Количество викингов вне диапазона возраста (min, max)")
    public long countAgeOutside(@RequestParam int min, @RequestParam int max) {
        return lambdaService.countAgeOutside(vikingService.findAll(), min, max);
    }

    @GetMapping("/stats/beard-hair")
    @Operation(summary = "Количество викингов с заданной бородой И цветом волос")
    public long countByBeardAndHair(@RequestParam BeardStyle beard,
                                    @RequestParam HairColor hair) {
        return lambdaService.countByBeardAndHair(vikingService.findAll(), beard, hair);
    }

    @GetMapping("/stats/axes-count")
    @Operation(summary = "Количество викингов, имеющих ровно 1 или 2 топора")
    public long countWithOneOrTwoAxes() {
        return lambdaService.countWithOneOrTwoAxes(vikingService.findAll());
    }

    @GetMapping("/stats/random-tall")
    @Operation(summary = "Случайный викинг ростом выше 180 см")
    public Optional<Viking> getRandomVikingHeightAbove180() {
        return lambdaService.getRandomVikingHeightAbove180(vikingService.findAll());
    }

    @GetMapping("/stats/legendary")
    @Operation(summary = "Все викинги с легендарным снаряжением")
    public List<Viking> getVikingsWithLegendaryEquipment() {
        return lambdaService.getVikingsWithLegendaryEquipment(vikingService.findAll());
    }

    @GetMapping("/stats/red-haired")
    @Operation(summary = "Рыжие викинги, отсортированные по возрасту")
    public List<Viking> getRedHairedSortedByAge() {
        return lambdaService.getRedBeardedSortedByAge(vikingService.findAll());
    }

    @GetMapping("/stats/max-id")
    @Operation(summary = "Максимальный ID среди викингов")
    public Optional<Integer> findMaxId() {
        Integer[] ids = lambdaService.toIdArray(vikingService.findAll());
        return lambdaService.findMaxId(ids);
    }

    @GetMapping("/stats/even-ids")
    @Operation(summary = "Список чётных ID викингов")
    public List<Integer> getEvenIds() {
        Integer[] ids = lambdaService.toIdArray(vikingService.findAll());
        return lambdaService.getEvenIds(ids);
    }
}