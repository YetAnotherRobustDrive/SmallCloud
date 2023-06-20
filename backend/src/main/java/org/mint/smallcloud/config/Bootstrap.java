package org.mint.smallcloud.config;

import org.mint.smallcloud.user.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class Bootstrap {
    @PersistenceContext
    private EntityManager em;

    @Value("${smallcloud.admin.username}")
    private String adminUsername;

    @Value("${smallcloud.admin.password}")
    private String adminPassword;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        Member admin = Member.createAdmin(adminUsername, adminPassword, "root");
        try {
            em.createQuery("select m from Member m where m.username = ?1", Member.class)
                .setParameter(1, admin.getUsername())
                .getSingleResult();
        } catch (Exception ignored) {
            em.persist(admin);
        }
    }
}
