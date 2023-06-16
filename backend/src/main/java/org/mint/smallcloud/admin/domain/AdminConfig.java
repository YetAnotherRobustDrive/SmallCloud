package org.mint.smallcloud.admin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "ADMIN_CONFIG")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_CONFIG_ID")
    private Long id;

    private String code;

    private String value;

    private LocalDateTime localDateTime;

    protected AdminConfig(String code, String value){
        this.code = code;
        this.value = value;
        this.localDateTime = LocalDateTime.now();
    }

    public static AdminConfig of(String code, String value) {
        return new AdminConfig(code, value);
    }
}
