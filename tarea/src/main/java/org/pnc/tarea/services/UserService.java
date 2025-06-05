package org.pnc.tarea.services;

import org.pnc.tarea.entities.User;
import org.pnc.tarea.entities.dto.RegisterDto;
import org.pnc.tarea.entities.dto.UserDto;
import org.pnc.tarea.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(RegisterDto userInfo) {
        User user = new User();
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setPassword(userInfo.getPassword());
        userRepository.save(user);
    }

    public User getUser(String id){
        UUID userId = UUID.fromString(id);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return optionalUser.get();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public UserDto getUserByEmail(String email){
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return new UserDto(optionalUser.get().getId(), optionalUser.get().getName(), optionalUser.get().getEmail());

    }


}
