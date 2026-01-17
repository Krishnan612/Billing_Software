package krishnan.billingsoftware.service;

import krishnan.billingsoftware.io.UserRequest;
import krishnan.billingsoftware.io.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    String getUserRole(String email);
    List<UserResponse> readUser();
    void deleteUser(String id);
}
