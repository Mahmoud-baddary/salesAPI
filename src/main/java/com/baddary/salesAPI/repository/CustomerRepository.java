package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c LEFT JOIN FETCH c.phones where lower(c.name) = lower(:name)")
    Optional<Customer> findByName(@Param("name") String name);

    @Query("select distinct c from Customer c LEFT JOIN FETCH c.phones")
    List<Customer> findAll();

    @Query("""
                select distinct c
                from Customer c
                left join fetch c.phones
                where lower(c.name) like lower(concat('%', :name, '%'))
            """)
    List<Customer> searchByName(@Param("name") String name);

    @Query("select distinct c.name from Customer c")
    List<String> findNameBy();

    @Query("SELECT c from Customer c left join fetch c.phones where c.id = :id")
    Optional<Customer> findById(@Param("id") Long id);

    @Query("SELECT c FROM Customer c join fetch c.phones p where p.phoneNum = :phoneNum")
    Optional<Customer> findByPhone(@Param("phoneNum") String phoneNum);

    @Query("SELECT DISTINCT c FROM Customer c left join fetch c.phones WHERE " +
            "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "AND (" +
            "   (:balanceStatus = 'ALL') OR " +
            "   (:balanceStatus = 'OWES' AND c.balance > 0) OR " +
            "   (:balanceStatus = 'DESERVES' AND c.balance < 0) OR " +
            "   (:balanceStatus = 'SETTLED' AND c.balance = 0)" +
            ")")
    List<Customer> searchByNameAndBalanceStatus(@Param("name") String name,
            @Param("balanceStatus") String balanceStatus);
}
