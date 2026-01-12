package com.ecommerce.project.reporting.repository;

import com.ecommerce.project.auth.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;


import java.util.List;


public interface UserCountReportRepository extends Repository<User, Long> {

    // Day
    @Query("""
        SELECT DATE(u.createdAt), COUNT(u)
        FROM User u
        GROUP BY DATE(u.createdAt)
        ORDER BY DATE(u.createdAt)
    """)
    List<Object[]> countNewUsersPerDay();

    // Week
    @Query("""
        SELECT YEAR(u.createdAt), WEEK(u.createdAt), COUNT(u)
        FROM User u
        GROUP BY YEAR(u.createdAt), WEEK(u.createdAt)
        ORDER BY YEAR(u.createdAt), WEEK(u.createdAt)
    """)
    List<Object[]> countNewUsersPerWeek();

    // Month
    @Query("""
        SELECT YEAR(u.createdAt), MONTH(u.createdAt), COUNT(u)
        FROM User u
        GROUP BY YEAR(u.createdAt), MONTH(u.createdAt)
        ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)
    """)
    List<Object[]> countNewUsersPerMonth();

    // Year
    @Query("""
        SELECT YEAR(u.createdAt), COUNT(u)
        FROM User u
        GROUP BY YEAR(u.createdAt)
        ORDER BY YEAR(u.createdAt)
    """)
    List<Object[]> countNewUsersPerYear();
}
