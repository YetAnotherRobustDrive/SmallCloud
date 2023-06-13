package org.mint.smallcloud.log.user;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.user.domain.Member;
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

    public List<UserLog> findLogs(String nickname, LocalDateTime startTime, LocalDateTime endTime, Boolean status, String action, Pageable pageable) {
        String jpql = "select ul from UserLog ul join ul.member m";
        boolean isFirst = true;
        if (nickname != null) {
            if (isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " m.nickname = :nickname";
        }
        if (startTime != null && endTime == null) {
            if (isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.time >= :startTime";
        }
        if (startTime == null && endTime != null) {
            if (isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.time <= :endTime";
        }
        if (startTime != null && endTime != null) {
            if (isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.time between :startTime and :endTime";
        }
        if (status != null) {
            if (isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.status = :status";
        }
        if (action != null) {
            if (isFirst) {
                jpql += " where";
                isFirst = false;
            } else {
                jpql += " and";
            }
            jpql += " ul.action = :action";
        }

        TypedQuery<UserLog> query = em.createQuery(jpql, UserLog.class);
        if (nickname != null) {
            query = query.setParameter("nickname", nickname);
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
        if (status != null) {
            query = query.setParameter("status", status);
        }
        if (action != null) {
            query = query.setParameter("action", action);
        }
        return query.getResultList();
    }

    public List<UserLog> findByActionStartsWith(String action){
        return em.createQuery("select ul from UserLog ul where ul.action like :action", UserLog.class)
                .setParameter("action", "%" + action + "%")
                .getResultList();
    }
}
