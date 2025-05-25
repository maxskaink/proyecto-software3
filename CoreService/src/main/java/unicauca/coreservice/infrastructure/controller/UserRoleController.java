package unicauca.coreservice.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import unicauca.coreservice.infrastructure.security.FirebaseService;
import unicauca.coreservice.infrastructure.dto.UserRoleDTO;

@RestController
@RequestMapping("/roles")
public class UserRoleController {

    private final FirebaseService firebaseService;

    public UserRoleController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignRole(@RequestBody UserRoleDTO request) 
    throws Exception {
            firebaseService.assignRole(request.getUid(), request.getRole());
            return ResponseEntity.ok("Role '" + request.getRole() + "' assigned to user.");
    }
}
