package org.mint.smallcloud.log.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {

    @PersistenceContext
    private EntityManager em;
    List<UserLog> findLogs(String userName, LocalDateTime startTime, LocalDateTime endTime, Boolean status, String action) {
        String jpql = "select ul from UserLog ul join ul.member m";
        boolean isFirst = true;
        if(userName != null) {
            if(isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " m.userName = :userName";
        }
        if(startTime != null && endTime == null) {
            if(isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.localDateTime >= :startTime";
        }
        if(startTime == null && endTime != null) {
            if(isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.localDateTime <= :endTime";
        }
        if(startTime != null && endTime != null) {
            if(isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.localDateTime between :startTime and :endTime";
        }
        if(status != null) {
            if(isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.status = :status";
        }
        if(action != null) {
            if(isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.action = :action";
        }

        TypedQuery<UserLog> query = em.createQuery(jpql, UserLog.class);
        if(userName != null) {
            query = query.setParameter("userName", userName);
        }
        if (startTime != null && endTime == null) {
            query = query.setParameter("startTime", startTime);
        }
        if (startTime == null && endTime != null) {
            query = query.setParameter("endTime", endTime);
        }
        if (startTime != null && endTime != null) {
            query = query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
        }
        if(status != null) {
            query = query.setParameter("status", status);
        }
        if(action != null) {
            query = query.setParameter("action", action);
        }
        return query.getResultList();
    }
}
