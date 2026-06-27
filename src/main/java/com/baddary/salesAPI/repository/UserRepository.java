package com.baddary.salesAPI.repository;


import com.baddary.salesAPI.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
public interface UserRepository extends CrudRepository<User, Long> {


    @Query("""
            SELECT u FROM User u WHERE lower(u.name) = lower(:name)
            """)
    Optional<User> findByNameIgnoreCase(String name);
}
