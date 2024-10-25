package nl.fontys.s3.rentride_be.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.user.*;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
import nl.fontys.s3.rentride_be.domain.user.CreateUserResponse;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UsersController {
    private GetUserUseCase getUserUseCase;
    private GetUsersUseCase getUsersUseCase;
    private DeleteUserUseCase deleteUserUseCase;
    private CreateUserUseCase createUserUseCase;
    private UpdateUserUseCase updateUserUseCase;

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") final long id) {
        User userOptional = getUserUseCase.getUser(id);

        if (userOptional == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userOptional);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.getUsersUseCase.getUsers());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateUserRequest request) {
        request.setId(id);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }
}
