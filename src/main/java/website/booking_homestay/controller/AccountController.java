package website.booking_homestay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.create.UserCreate;
import website.booking_homestay.DTO.update.UserUpdate;
import website.booking_homestay.service.IAccountService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("")
    public ResponseEntity<?> getAllAccounts(@RequestParam(value = "roleId",defaultValue = "") Long id){
        if (id == null) return accountService.getAllAccounts();
        else return accountService.getAccountsRole(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAccountDetails(@PathVariable("id") Long accountId){
        return accountService.getAccountDetails(accountId);
    }

    @PostMapping("")
    public ResponseEntity<?> createAccount(@Valid @RequestBody UserCreate userCreate){
        return accountService.createAccount(userCreate);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateAccount(@PathVariable("id") Long accountId,@RequestBody UserUpdate userUpdate){
        return accountService.updateAccount(accountId,userUpdate);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAccount(@PathVariable("id") Long accountId){
        return accountService.removeAccount(accountId);
    }

    @GetMapping("roles")
    public ResponseEntity<?> getRoles(){
        return accountService.getRoles();
    }

}
