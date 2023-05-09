package org.mint.smallcloud.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "ADMINS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 15, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 15)
    private String password;

    @Column(name = "IS_LOCKED")
    @Getter(AccessLevel.NONE)
    private boolean isLocked = true;

    protected Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Admin of(String username, String password) {
        return new Admin(username, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        return obj instanceof Admin
            && ((Admin) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void unlock() {
        isLocked = false;
    }

    public void lock() {
        isLocked = true;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
