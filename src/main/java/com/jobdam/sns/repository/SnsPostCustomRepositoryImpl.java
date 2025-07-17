package com.jobdam.sns.repository;

import com.jobdam.sns.dto.SnsPostFilterDto;
import com.jobdam.sns.entity.SnsPost;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SnsPostCustomRepositoryImpl implements SnsPostCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SnsPost> searchFilteredPosts(SnsPostFilterDto filter) {
        String baseQuery = """
            SELECT p FROM SnsPost p
            JOIN FETCH p.user u
            JOIN FETCH u.memberTypeCode mt
            LEFT JOIN p.likes l
        """;

        StringBuilder whereClause = new StringBuilder("WHERE 1=1 ");
        if (filter.getMemberType() != null && !filter.getMemberType().isBlank()) {
            whereClause.append("AND mt.code = :memberType ");
        }

        String orderClause;
        if ("likes".equalsIgnoreCase(filter.getSort())) {
            orderClause = "GROUP BY p ORDER BY COUNT(l) DESC";
        } else {
            orderClause = "ORDER BY p.createdAt DESC";
        }

        String finalQuery = baseQuery + whereClause + orderClause;

        TypedQuery<SnsPost> query = em.createQuery(finalQuery, SnsPost.class);

        if (filter.getMemberType() != null && !filter.getMemberType().isBlank()) {
            query.setParameter("memberType", filter.getMemberType());
        }

        return query.getResultList();
    }
}
