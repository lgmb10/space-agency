package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.services.ShipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/ships")
@RequiredArgsConstructor
@Tag(name = "Ships", description = "Gestion des vaisseaux")
public class ShipController {

    private final ShipService shipService;

    @GetMapping
    @Operation(summary = "Récupérer la liste des vaisseaux")
    public List<ShipDTO> getAllShips() {
        return shipService.findAll();
    }

    @GetMapping("/{shipId}")
    @Operation(summary = "Récupérer les informations  d'un vaisseau")
    public ShipDTO getOneShip(@PathVariable Integer  shipId) {
        return shipService.findById(shipId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un vaisseau")
    public ShipDTO createShip(@RequestBody Ship ship) {
        return shipService.create(ship);
    }

    @PutMapping
    @Operation(summary = "Mettre à jour les informations d'un vaisseau")

    public ShipDTO updateShip(@RequestBody Ship ship) {
        return shipService.update(ship);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un vaisseau")
    public void deleteShip(@PathVariable("id") Integer id){
        shipService.deleteById(id);
    }

}
