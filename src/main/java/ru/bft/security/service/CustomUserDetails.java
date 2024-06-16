package ru.bft.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bft.security.model.SecurityCustomer;
import ru.bft.security.entity.Customers;
import ru.bft.security.repository.CustomersRepository;

import java.util.List;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private CustomersRepository customersRepository;

   /* Позволяет получить из источника данных объект пользователя и сформировать из него
      объект UserDetails который будет использоваться контекстом Spring Security.*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Customers> customerList = customersRepository.findByEmail(username);
        if(customerList.size() == 0) {
            throw new UsernameNotFoundException("User not found : " + username);
        }
        return new SecurityCustomer(customerList.get(0));
    }
}
