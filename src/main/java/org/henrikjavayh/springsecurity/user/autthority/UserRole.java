package org.henrikjavayh.springsecurity.user.autthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum UserRole {

    GUEST(UserRoleName.GUEST.getRoleName(),
            Set.of()

    ),
    USER(UserRoleName.USER.getRoleName(),
            Set.of(
                    UserPermission.WRITE,
                    UserPermission.READ
            )
    ),


    ADMIN(UserRoleName.ADMIN.getRoleName(),
            Set.of(
                    UserPermission.READ,
                    UserPermission.DELETE,
                    UserPermission.WRITE

            )
    );

    private final String roleName;
    private final Set<UserPermission> userPermission;

    UserRole(String roleName, Set<UserPermission> userPermission) {
        this.roleName = roleName;
        this.userPermission = userPermission;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<UserPermission> getUserPermission() {
        return userPermission;
    }

    //A list that spring understands for both role and permission
    public List<SimpleGrantedAuthority> getUserAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        authorityList.add(new SimpleGrantedAuthority(this.roleName));
        authorityList.addAll(
                this.userPermission.stream().map(
                        userPermission -> new SimpleGrantedAuthority(userPermission.getUserPermission())
                ).toList()


        );
        return authorityList;
    }
}
