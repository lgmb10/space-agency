package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.services.ShipService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    private final JsonMapper jsonMapper;

    @GetMapping("/ships")
    public List<ShipDTO> getAllShips() {
        return shipService.findAll();
    }

    @GetMapping("/ships/{shipId}")
    public ShipDTO getOneShip(@PathVariable Integer  shipId) {
        return shipService.findById(shipId);
    }

    @PostMapping("/ships")
    @ResponseStatus(HttpStatus.CREATED)
    public ShipDTO createShip(@RequestBody Ship ship) {
        return shipService.create(ship);
    }

    @PutMapping("/ships")
    public ShipDTO updateShip(@RequestBody Ship ship) {
        return shipService.update(ship);
    }


    @DeleteMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShip(@PathVariable("id") Integer id){
        shipService.deleteById(id);
    }


}
