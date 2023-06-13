package org.mint.smallcloud.log.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserLogRepository {

    private final EntityManager em;

    public void saveAndFlush(UserLog userLog) {
        em.persist(userLog);
        em.flush();
    }
    public List<UserLog> findLogs(String userName, LocalDateTime startTime, LocalDateTime endTime, Boolean status, String action, Pageable pageable) {
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
            query = query.setParameter("endTime", endTime);
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
