package core.spring.security.domain;

import core.spring.security.domain.dto.AccountDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String age;
    private String role;

    public Account(AccountDto accountDto) {
        username = accountDto.getUsername();
        password = accountDto.getPassword();
        email = accountDto.getEmail();
        age = accountDto.getAge();
        role = accountDto.getRole();
    }
}
