package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.dtos.DirectionRequest;
import com.marketminds.ecommerce.elTruco.models.Directions;
import com.marketminds.ecommerce.elTruco.models.Users;
import com.marketminds.ecommerce.elTruco.services.DirectionsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/directions")
@AllArgsConstructor
public class DirectionsController {

    /**
     * Directions controller de Josue:
     * https://gist.github.com/Omiced/eeff0bc70d54b59d6499c91c4adb3283
     */

    private final DirectionsService directionsService;

    @GetMapping
    public List<Directions> getDirectionsByUserId(@RequestParam(required = true) Long userId){
        return this.directionsService.getDirectionsByUserId(userId);
    }

    @GetMapping(path = "/single")
    public Directions getDirectionById(@RequestParam(required = true) Long userId, @RequestParam(required = true) Long directionId){
        return this.directionsService.getDirectionById(userId, directionId);
    }

    @PostMapping
    public Users addDirectionUser(@RequestParam(required = true) Long userId, @RequestBody DirectionRequest direction) {
        return directionsService.addDirectionUser(userId, direction);
    }

    @PutMapping
    public Directions updateDirection(@RequestParam(required = true) Long userId, @RequestParam(required = true) Long directionId,@RequestBody DirectionRequest direction){
        return  directionsService.updateDirection(userId,directionId, direction);
    }

    @DeleteMapping
    public Directions deleteDirectionById(@RequestParam(required = true) Long userId, @RequestParam(required = true) Long directionId){
        return directionsService.deleteDirectionById(userId,directionId);
    }
}
