package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.booking_homestay.DTO.AccountInforSendMail;
import website.booking_homestay.DTO.create.UserCreate;
import website.booking_homestay.DTO.details.UserDetails;
import website.booking_homestay.DTO.update.UserUpdate;
import website.booking_homestay.DTO.view.UserView;
import website.booking_homestay.entity.Role;
import website.booking_homestay.entity.User;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.RoleRepository;
import website.booking_homestay.repository.UserRepository;
import website.booking_homestay.service.IAccountService;
import website.booking_homestay.service.IMailService;
import website.booking_homestay.utils.Characters;
import website.booking_homestay.utils.MessageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IMailService mailService;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Override
    public ResponseEntity<?> getAccountsRole(Long id) {
        List<User> users;
        users = userRepository.findAllByRole_Id(id);
        List<UserDetails> userViews = users.stream().map(user -> modelMapper.map(user,UserDetails.class)).collect(Collectors.toList());
        return ResponseEntity.ok(userViews);
    }

    @Override
    public ResponseEntity<?> getAllAccounts() {
        List<User> users;
        users = userRepository.findAll();
        List<UserDetails> userViews = users.stream().map(user -> modelMapper.map(user,UserDetails.class)).collect(Collectors.toList());
        return ResponseEntity.ok(userViews);
    }

    @Override
    public ResponseEntity<?> getAccountDetails(Long accountId) {
        User user = userRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        UserDetails userDetails = modelMapper.map(user,UserDetails.class);
        return ResponseEntity.ok(userDetails);
    }

    @Transactional
    @Override
    public ResponseEntity<?> createAccount(UserCreate userCreate) {
        if (userRepository.existsByUsername(userCreate.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.EXIST_USERNAME);
        }
        if (userRepository.existsByEmail(userCreate.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.EXIST_GMAIL);
        }

        if (userCreate.getRoleId() == null){
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST,MessageResponse.ERROR_ROLE));
            userCreate.setRoleId(userRole.getId());
        }
//        String strRole = userCreate.getRole();
//        Role role;
//        switch (strRole) {
//            case "ADMIN":
//                Role adminRole = roleRepository.findByName(ERole.ADMIN)
//                        .orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST,MessageResponse.ERROR_ROLE));
//                role = new Role(adminRole.getId(),adminRole.getName());
//                break;
//            case "MANAGER":
//                Role managerRole = roleRepository.findByName(ERole.MANAGER)
//                        .orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST,MessageResponse.ERROR_ROLE));
//                role = new Role(managerRole.getId(),managerRole.getName());
//                break;
//            default:
//                Role userRole = roleRepository.findByName(ERole.USER)
//                        .orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST,MessageResponse.ERROR_ROLE));
//                role = new Role(userRole.getId(),userRole.getName());
//        }
        String password = Characters.getStringRandom();
//        User user = new User(userCreate.getUsername(),
//                userCreate.getFullName(),
//                userCreate.getEmail(),
//                passwordEncoder.encode(password),
//                userCreate.getPhoneNumber(),
//                role);
        User user = modelMapper.map(userCreate,User.class);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(true);
        try {
            userRepository.save(user);
        }catch (Exception e){
            logger.error("Create account failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
        }
        mailService.sendMailRegister(new AccountInforSendMail(userCreate.getUsername(), password, userCreate.getEmail(), userCreate.getFullName()));
        return ResponseEntity.ok("Create account successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateAccount(Long accountId,UserUpdate userUpdate) {
        User user = userRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        if (user.getBranch() != null) {
            user.setBranch(null);
        }
        if (user.getRole() != null) {
            user.setRole(null);
        }
        modelMapper.map(userUpdate,user);
        try {
            userRepository.save(user);
        }catch (Exception e){
            logger.error("Update account failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
        }
        return ResponseEntity.ok("Update successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeAccount(Long accountId) {
        User user = userRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        user.setStatus(false);
        try {
            userRepository.save(user);
        }catch (Exception e){
            logger.error("Remove account failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
        }
        return ResponseEntity.ok("Remove account successfully!");
    }


    @Override
    public ResponseEntity<?> getManagerNoBranch(){
        List<User> users = userRepository.findAllByBranchIsNull();
        List<UserDetails> userViews = users.stream().map(user -> modelMapper.map(user,UserDetails.class)).collect(Collectors.toList());
        return ResponseEntity.ok(userViews);
    }


    @Override
    public ResponseEntity<?> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }


}
