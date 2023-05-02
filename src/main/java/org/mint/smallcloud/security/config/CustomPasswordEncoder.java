package org.mint.smallcloud.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder extends BCryptPasswordEncoder {
}
