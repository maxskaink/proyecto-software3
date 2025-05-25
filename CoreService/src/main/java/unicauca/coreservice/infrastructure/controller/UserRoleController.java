package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.infrastructure.dto.UserRoleDTO;

@RestController
@RequestMapping("/roles")
@AllArgsConstructor
public class UserRoleController {

    private final IAuthenticationService firebaseService;

    @PostMapping("/assign")
    public ResponseEntity<?> assignRole(@RequestBody UserRoleDTO request) 
    throws Exception {
            firebaseService.assignRole(request.getUid(), request.getRole());
            return ResponseEntity.ok("Role '" + request.getRole() + "' assigned to user.");
    }
}
