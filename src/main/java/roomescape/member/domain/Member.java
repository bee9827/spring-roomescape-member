package roomescape.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.AuthErrorStatus;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @Email
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String name, String email, String password,Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role != null ? role : Role.USER;
    }

    public void validateAdmin(){
        if(this.role != Role.ADMIN){
            throw new RestApiException(AuthErrorStatus.NOT_AUTHORIZED);
        }
    }
}
