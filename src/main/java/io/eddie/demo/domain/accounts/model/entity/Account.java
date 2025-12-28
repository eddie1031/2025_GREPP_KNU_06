package io.eddie.demo.domain.accounts.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import io.eddie.demo.domain.accounts.model.vo.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    @Column(unique = true, nullable = false)
    private String cartCode;

    @Setter
    @Column(unique = true, nullable = false)
    private String depositCode;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @Builder
    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles.add(Role.BUYER);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

}
