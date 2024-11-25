package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.user.*;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserEmailSettingsRequest;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UsersController {
    private GetUserUseCase getUserUseCase;
    private GetUsersUseCase getUsersUseCase;
    private UpdateUserUseCase updateUserUseCase;
    private UpdateUserEmailsUseCase updateUserEmailsUseCase;

    @GetMapping("currentUserProfile")
    public ResponseEntity<User> getCurrentUserProfile() {
        User userOptional = getUserUseCase.getSessionUser();

        if (userOptional == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userOptional);
    }

    @PostMapping("updateUserEmailSettings")
        public ResponseEntity updateUserEmailSettings(@RequestBody @Valid UpdateUserEmailSettingsRequest request){
        updateUserEmailsUseCase.updateUserEmails(request);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.getUsersUseCase.getUsers());
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateUserRequest request) {
        request.setId(id);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }
}
