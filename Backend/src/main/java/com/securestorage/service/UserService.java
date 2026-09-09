package com.securestorage.service;

import com.securestorage.dto.UserDTO;
import com.securestorage.dto.UserRequestDTO;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserRequestDTO userRequest);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserRequestDTO userRequest);
    void deleteUser(Long id);
}
