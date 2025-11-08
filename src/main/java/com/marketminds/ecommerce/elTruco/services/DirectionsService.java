package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.dtos.DirectionRequest;
import com.marketminds.ecommerce.elTruco.models.Directions;
import com.marketminds.ecommerce.elTruco.models.Users;
import com.marketminds.ecommerce.elTruco.repositories.DirectionsRepository;
import com.marketminds.ecommerce.elTruco.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectionsService {

    private final DirectionsRepository directionsRepository;
    private final UsersRepository usersRepository;

    public List<Directions> getAllDirections() {
        return directionsRepository.findAll();
    }

    public List<Directions> getDirectionsByUserId(Long id){
        Optional<Users> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()) throw new IllegalArgumentException("El usuario con el id: " + id + " no tiene direcciones o no existe");
        Users user =  optionalUser.get();
        return user.getDirections();
    }

    public Users addDirectionUser(Long userId, DirectionRequest directionRequest) {
        Users user = usersRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("No existe el usuario con el id: " + userId)
        );

        Directions direction = new Directions();
        if (directionRequest.getStreet() != null) direction.setStreet(directionRequest.getStreet());
        if (directionRequest.getSuburb() != null) direction.setSuburb(directionRequest.getSuburb());
        if (directionRequest.getCountry() != null) direction.setCountry(directionRequest.getCountry());
        if (directionRequest.getZipCode() != null) direction.setZipCode(directionRequest.getZipCode());

        // asociando el usuario a la direccion
        direction.setUser(user);
        // guardando la direccion en la db
        directionsRepository.save(direction);
        // guardando la direccion en el list del usuario
        user.getDirections().add(direction);

        // actualizando el usuario en la db
        return usersRepository.save(user);
    }

    public Directions updateDirection(Long userId,Long directionId, DirectionRequest directionRequest){
        if(!usersRepository.existsById(userId)) throw new IllegalArgumentException("No existe el usuario con el id " + userId);
        Directions direction = directionsRepository.findById(directionId).orElseThrow(
                () ->  new IllegalArgumentException("No existe una direccion con el id " + directionId)
        );
        if(!direction.getUser().getId().equals(userId))throw new IllegalArgumentException("El id del usuario no coincide con el de la direccion") ;
        if(directionRequest.getStreet() != null) direction.setStreet(directionRequest.getStreet());
        if(directionRequest.getCountry() != null) direction.setCountry(directionRequest.getCountry());
        if(directionRequest.getSuburb() != null) direction.setSuburb(directionRequest.getSuburb());
        if(directionRequest.getZipCode() != null) direction.setZipCode(directionRequest.getZipCode());
        return directionsRepository.save(direction);
    }

    public Directions getDirectionById(Long userId, Long directionId){
        if(!usersRepository.existsById(userId)) throw new IllegalArgumentException("No existe el usuario con el id " + userId);
        Directions direction = directionsRepository.findById(directionId).orElseThrow(
                () ->  new IllegalArgumentException("No existe una direccion con el id " + directionId)
        );
        if(!direction.getUser().getId().equals(userId))throw new IllegalArgumentException("El id del usuario no coincide con el de la direccion") ;
        return direction;
    }

    public Directions deleteDirectionById(Long userId, Long directionId){
        if(!usersRepository.existsById(userId)) throw new IllegalArgumentException("No existe el usuario con el id " + userId);
        Directions direction = directionsRepository.findById(directionId).orElseThrow(
                () ->  new IllegalArgumentException("No existe una direccion con el id " + directionId)
        );
        if(!direction.getUser().getId().equals(userId))throw new IllegalArgumentException("El id del usuario no coincide con el de la direccion") ;
        directionsRepository.deleteById(directionId);
        return direction;
    }
}
