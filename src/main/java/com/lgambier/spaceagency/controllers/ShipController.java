package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.services.ShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    @GetMapping
    public List<ShipDTO> getAllShips() {
        return shipService.findAll();
    }

    @GetMapping("/{shipId}")
    public ShipDTO getOneShip(@PathVariable Integer  shipId) {
        return shipService.findById(shipId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipDTO createShip(@RequestBody Ship ship) {
        return shipService.create(ship);
    }

    @PutMapping
    public ShipDTO updateShip(@RequestBody Ship ship) {
        return shipService.update(ship);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShip(@PathVariable("id") Integer id){
        shipService.deleteById(id);
    }

}
