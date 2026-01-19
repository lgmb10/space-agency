package com.lgambier.spaceagency.controllers;

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
    public List<Ship> getAllShips() {
        return shipService.findAll();
    }

    @GetMapping("/ships/{shipId}")
    public Ship getOneShip(@PathVariable Integer  shipId) {
        return shipService.findById(shipId);
    }

    @PostMapping("/ships")
    @ResponseStatus(HttpStatus.CREATED)
    public Ship createShip(@RequestBody Ship ship) {
        return shipService.create(ship);
    }

    @PutMapping("/ships")
    public Ship updateShip(@RequestBody Ship ship) {
        return shipService.update(ship);
    }

    @PatchMapping("/ships/{shipId}")
    public Ship patchEmployee(@PathVariable Integer shipId, @RequestBody Map<String, Object> patchPayload){
        Ship ship = shipService.findById(shipId);

        if(ship == null){
            throw new RuntimeException("Employee id not found - "+shipId);
        }

        if(patchPayload.containsKey("id")){
            throw new RuntimeException("Employee id not allowed in request body - "+ shipId);
        }

        Ship patchedShip = jsonMapper.updateValue(ship, patchPayload);

        return shipService.update(patchedShip);

    }

    @DeleteMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShip(@PathVariable("id") Integer id){
        shipService.deleteById(id);
    }


}
