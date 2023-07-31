package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import website.booking_homestay.DTO.create.UserCreate;
import website.booking_homestay.DTO.update.UserUpdate;

public interface IAccountService {
    ResponseEntity<?> getAccountsRole(Long role);

    ResponseEntity<?> createAccount(UserCreate userCreate);

    ResponseEntity<?> getRoles();

    ResponseEntity<?> removeAccount(Long accountId);

    ResponseEntity<?> updateAccount(Long accountId,UserUpdate userUpdate);

    ResponseEntity<?> getManagerNoBranch();

    ResponseEntity<?> getAllAccounts();

    ResponseEntity<?> getAccountDetails(Long accountId);
}
