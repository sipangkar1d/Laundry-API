package com.gpt5.laundry.config;

import com.gpt5.laundry.entity.Admin;
import com.gpt5.laundry.entity.Role;
import com.gpt5.laundry.entity.UserCredential;
import com.gpt5.laundry.entity.constant.ERole;
import com.gpt5.laundry.security.BCryptUtils;
import com.gpt5.laundry.service.AdminService;
import com.gpt5.laundry.service.RoleService;
import com.gpt5.laundry.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppInit implements CommandLineRunner {
//    private final AdminService adminService;
//    private final RoleService roleService;
//    private final BCryptUtils bCryptUtils;
//    private final UserCredentialService userCredentialService;
//
//    @Value(value = "${geramed.email}")
//    String email;
//    @Value(value = "${geramed.password}")
//    String password;

    @Override
    public void run(String... args) throws Exception {
//        Optional<Admin> admin = adminService.findByEmail(email);
//        if (admin.isEmpty()) {
//            log.info("Creating first admin.");
//            Role role = roleService.getOrSave(ERole.ROLE_ADMIN);
//            UserCredential userCredential = userCredentialService.create(UserCredential.builder()
//                    .email(email)
//                    .password(bCryptUtils.hashPassword(password))
//                    .roles(List.of(role))
//                    .build());
//
//            adminService.create(Admin.builder()
//                    .email(userCredential.getEmail())
//                    .name(userCredential.getEmail().substring(0, userCredential.getEmail().indexOf("@")))
//                    .userCredential(userCredential)
//                    .build());
//        }
//        log.info("First Admin was created.");
    }
}
