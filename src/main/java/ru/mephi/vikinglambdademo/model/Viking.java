package ru.mephi.vikinglambdademo.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Viking model")
public record Viking(
        @Schema(description = "Integer ID", example = "1") 
        int id, // новое поле от меня
        @Schema(description = "Viking name", example = "Bjorn")
        String name,
        @Schema(description = "Age", example = "31")
        int age,
        @Schema(description = "Height in cm", example = "184")
        int heightCm,
        @Schema(description = "Hair color", example = "Blond")
        HairColor hairColor,
        @Schema(description = "Beard style")
        BeardStyle beardStyle,
        @ArraySchema(schema = @Schema(implementation = EquipmentItem.class),
                     arraySchema = @Schema(description = "Viking Equipment"))
        List<EquipmentItem> equipment
) {}