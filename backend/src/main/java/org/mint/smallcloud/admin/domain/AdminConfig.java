package org.mint.smallcloud.admin.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "ADMIN_CONFIG")
@Entity
public class AdminConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_CONFIG_ID")
    private Long id;

    private String code;

    private String value;

    private LocalDateTime localDateTime;

    protected AdminConfig(String code, String value) {
        this.code = code;
        this.value = value;
        this.localDateTime = LocalDateTime.now();
    }

    protected AdminConfig() {
    }

    public static AdminConfig of(String code, String value) {
        return new AdminConfig(code, value);
    }

    public Long getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }
}
