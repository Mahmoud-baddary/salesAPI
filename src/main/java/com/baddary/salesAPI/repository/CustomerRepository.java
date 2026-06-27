package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("select c from Customer c LEFT JOIN FETCH c.phones where lower(c.name) = lower(:name)")
    Optional<Customer> findByName(String name);

    @Query("select c from Customer c LEFT JOIN FETCH c.phones")
    List<Customer> findAll();

    @Query("""
                select c
                from Customer c
                left join fetch c.phones
                where lower(c.name) like lower(concat('%', :name, '%'))
            """)
    List<Customer> searchByName(String name);

    @Query("select c.name from Customer c")
    List<String> findNameBy();

    @Query("SELECT c from Customer c left join fetch c.phones where c.id = :id")
    Optional<Customer> findById(Long id);

    @Query("SELECT c FROM Customer c join fetch c.phones p where p.phoneNum = :phoneNum")
    Optional<Customer> findByPhone(String phoneNum);

}
