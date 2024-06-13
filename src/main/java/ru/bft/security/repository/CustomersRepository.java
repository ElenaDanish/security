package ru.bft.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bft.security.entity.Customers;

import java.util.List;

@Repository
public interface CustomersRepository extends CrudRepository<Customers, Long> {
    List<Customers> findByEmail(String email);
}
