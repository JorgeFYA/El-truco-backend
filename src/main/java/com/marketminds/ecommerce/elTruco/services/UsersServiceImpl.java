package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.models.Role;
import com.marketminds.ecommerce.elTruco.models.Users;
import com.marketminds.ecommerce.elTruco.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService, UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("El usuario con el id: " + id + " no existe")
        );
    }

    @Override
    public Users addUser(Users user) {
        String encriptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encriptedPassword);

        user.setRole(Role.USER);

        return usersRepository.save(user);
    }

    @Override
    public Users deleteUserById(Long id) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()) throw new IllegalArgumentException("El usuario con el id: " + id + " no existe");

        usersRepository.deleteById(id);
        return optionalUser.get();
    }

    @Override
    public Users updateUserById(Long id, Users updatedUser) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()) throw new IllegalArgumentException("El usuario con el id: " + id + " no existe");

        Users userDB = optionalUser.get();
        if (updatedUser.getName() != null) userDB.setName(updatedUser.getName());
        if (updatedUser.getLastName() != null) userDB.setLastName(updatedUser.getLastName());
        if (updatedUser.getPhoneNumber() != null) userDB.setPhoneNumber(updatedUser.getPhoneNumber());
        if (updatedUser.getEmail() != null) userDB.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null) userDB.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        return usersRepository.save(userDB);
    }

    @Override
    public boolean validateUser(Users user) {
        Optional<Users> optionalUser = usersRepository.findByEmail(user.getEmail());
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("El correo o la contraseña son incorrectos");

        // devuelve true or false si la contraseña entrante coincide con la almacenada en la db
        return passwordEncoder.matches(user.getPassword(), optionalUser.get().getPassword());
    }

    // Spring Security lo usa para hacer la carga de los usuarios.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("No se encontró el usuario con el email: " + email)
        );
    }
}
