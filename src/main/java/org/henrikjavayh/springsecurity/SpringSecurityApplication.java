package org.henrikjavayh.springsecurity;

import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);

        System.out.println(UserRole.GUEST.getRoleName());

        System.out.println(UserRole.ADMIN.getUserPermission());

        System.out.println(UserRole.USER.getUserPermission());

        System.out.println(
                UserRole.GUEST.getUserAuthorities() + "\n" +
                UserRole.ADMIN.getUserAuthorities() + "\n" +
                UserRole.USER.getUserAuthorities());
    }


}
