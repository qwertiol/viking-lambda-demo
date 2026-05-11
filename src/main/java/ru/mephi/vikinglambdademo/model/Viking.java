package ru.mephi.vikinglambdademo.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Viking model")
public record Viking(
        @Schema(description = "UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,
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
) {
}