package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.booking_homestay.entity.Role;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.RoleRepository;
import website.booking_homestay.utils.MessageResponse;

import java.util.List;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleRepository roleRepository;

    @GetMapping("")
    public ResponseEntity<?> getAllRoles(){
        List<Role> role = roleRepository.findAll();
        return ResponseEntity.ok(role);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getRoleDetails(@PathVariable("id") Long roleId){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, MessageResponse.NOT_FOUND));
        return ResponseEntity.ok(role);
    }
}
