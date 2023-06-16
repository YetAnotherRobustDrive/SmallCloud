package org.mint.smallcloud.admin.repository;

import org.mint.smallcloud.admin.domain.AdminConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminConfigRepository extends JpaRepository<AdminConfig, Long> {
    AdminConfig findByCode(String code);
}
