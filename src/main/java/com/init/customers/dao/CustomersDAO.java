package com.init.customers.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.init.customers.entities.Customer;

public interface CustomersDAO extends JpaRepository<Customer, Long> {

}
