package org.mint.smallcloud.log.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserLogRepository {

    private final EntityManager em;

    public void saveAndFlush(UserLog userLog) {
        em.persist(userLog);
        em.flush();
    }

    public Page<UserLog> findLogs(String nickname, LocalDateTime startTime, LocalDateTime endTime, Boolean status, String action, Pageable pageable) {
        String selectJpql = "select ul";
        String countingJpqlSelect = "select count(ul)";
        String fromJpql = " from UserLog ul left join ul.member m";
        String orderByJpql = " order by ul.time desc";
        List<String> conditionsWhereJpql = new ArrayList<>();
        if (nickname != null)
            conditionsWhereJpql.add("m.nickname = :nickname");
        if (startTime != null && endTime == null)
            conditionsWhereJpql.add("ul.time >= :startTime");
        if (startTime == null && endTime != null)
            conditionsWhereJpql.add("ul.time <= :endTime");
        if (startTime != null && endTime != null)
            conditionsWhereJpql.add("ul.time between :startTime and :endTime");
        if (status != null)
            conditionsWhereJpql.add("ul.status = :status");
        if (action != null)
             conditionsWhereJpql.add("ul.action like :action");
        String whereJpql = conditionsWhereJpql.isEmpty() ? "" : " where " + String.join(" and ", conditionsWhereJpql);

        TypedQuery<UserLog> query = em.createQuery(
                selectJpql + fromJpql + whereJpql + orderByJpql,
                UserLog.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize());

        TypedQuery<Long> countingQuery= em.createQuery(
            countingJpqlSelect + fromJpql + whereJpql,
            Long.class);
        if (nickname != null) {
            query = query.setParameter("nickname", nickname);
            countingQuery = countingQuery.setParameter("nickname", nickname);
        }
        if (startTime != null && endTime == null) {
            query = query.setParameter("startTime", startTime);
            countingQuery = countingQuery.setParameter("startTime", startTime);
        }
        if (startTime == null && endTime != null) {
            query = query.setParameter("endTime", endTime);
            countingQuery = countingQuery.setParameter("endTime", endTime);
        }
        if (startTime != null && endTime != null) {
            query = query.setParameter("startTime", startTime);
            query = query.setParameter("endTime", endTime);
            countingQuery = countingQuery.setParameter("startTime", startTime);
            countingQuery = countingQuery.setParameter("endTime", endTime);
        }
        if (status != null) {
            query = query.setParameter("status", status);
            countingQuery = countingQuery.setParameter("status", status);
        }
        if (action != null) {
            // convert
            // from /group/{groupName}/add-user/{username}
            // to /group/%/remove-user/%
            String actionLike = action.replaceAll("\\{.*?\\}", "%");
            query = query.setParameter("action", actionLike);
            countingQuery = countingQuery.setParameter("action", actionLike);
        }
        List<UserLog> content = query.getResultList();
        Long total = countingQuery.getSingleResult();
        return new PageImpl<>(content, pageable, total);
    }

    public List<UserLog> findByActionStartsWith(String action){
        return em.createQuery("select ul from UserLog ul where ul.action like :action", UserLog.class)
                .setParameter("action", "%" + action + "%")
                .getResultList();
    }
}
