package org.mint.smallcloud.security.user;

import lombok.Getter;


@Getter
public class Roles {
    public static final String PREFIX = "ROLE_";
    public static final String ADMIN = "ADMIN";
    public static final String S_ADMIN = PREFIX + ADMIN;
    public static final String COMMON = "COMMON";
    public static final String S_COMMON = PREFIX + COMMON;
    public static final String PRIVILEGE = "PRIVILEGE";
    public static final String S_PRIVILEGE = PREFIX + PRIVILEGE;
}
