package com.movieBackend.services.concretes;

import com.movieBackend.dtos.DtoUser;
import com.movieBackend.dtos.DtoUserIU;
import com.movieBackend.entities.User;
import com.movieBackend.repositories.UserRepository;
import com.movieBackend.services.abstracts.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;


    @Override
    public User saveUser(DtoUserIU dtoUserIU) {
        User user = new User();
        user.setUsername(dtoUserIU.getUsername());
        user.setEmail(dtoUserIU.getEmail());
        user.setPassword(dtoUserIU.getPassword());
        return userRepository.save(user);
    }


    @Override
    public List<DtoUser> getAllUser() {
        List<DtoUser> dtoUserList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            DtoUser dtoUser = new DtoUser();
            BeanUtils.copyProperties(user, dtoUser);
            dtoUserList.add(dtoUser);
        }
        return dtoUserList;
    }

    @Override
    public ResponseEntity<DtoUser> getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            DtoUser dtoUser = new DtoUser();
            BeanUtils.copyProperties(user, dtoUser);
            return ResponseEntity.ok(dtoUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @Override
    public String deleteUser(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return "User delete successfully.";
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public DtoUser updateUser(Integer id, DtoUserIU dtoUpdateUser) {
        DtoUser dtoUser = new DtoUser();
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User dbUser = optionalUser.get();
            dbUser.setUsername(dtoUpdateUser.getUsername());
            dbUser.setEmail(dtoUpdateUser.getEmail());
            dbUser.setPassword(dtoUpdateUser.getPassword());
            dbUser.setBirtOfDate(dtoUpdateUser.getBirtOfDate());
            User updatedUser = userRepository.save(dbUser);
            BeanUtils.copyProperties(updatedUser, dtoUser);
            return dtoUser;
        }
        return null;
    }

    @Override
    public DtoUserIU getCurrentUserProfile(String username) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        DtoUserIU dtoUser = new DtoUserIU();
        BeanUtils.copyProperties(user.get(), dtoUser);

        return dtoUser;
    }

    @Transactional
    @Override
    public void updateUserProfile(String currentUsername, DtoUser dtoUser) {
        User user = userRepository.findByUsername(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(dtoUser.getUsername());
        user.setEmail(dtoUser.getEmail());
        userRepository.save(user);
    }

}
