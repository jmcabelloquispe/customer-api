package com.init.customers.rest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.init.customers.dao.CustomersDAO;
import com.init.customers.entities.Customer;
import com.init.customers.entities.Kpi;

@RestController
@RequestMapping("customers")
public class CustomersREST {
	
	@Autowired
	private CustomersDAO customerDAO;
	
	
	@RequestMapping(value = "/listclientes", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getCustomer(){
		List<Customer> customers = customerDAO.findAll();
		return ResponseEntity.ok(customers);
	}
	
	
	@RequestMapping(value = "/kpideclientes", method = RequestMethod.GET)
	public ResponseEntity<Kpi> getKpi(){
		List<Customer> customers = customerDAO.findAll();
		double[] ages = new double[customers.size()];
		double avg = 0;
		int index = 0;
		for(Customer customer : customers) {
			ages[index++] = customer.getAge();
			avg = avg + customer.getAge();
		}
		Kpi kpi = new Kpi();
		kpi.setAverage(avg);
		kpi.setStandardDeviation(Customer.calculateSD(ages));
		return ResponseEntity.ok(kpi);
	}
	
	
	@RequestMapping(value="{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId){
		Optional<Customer> optionslCustomer = customerDAO.findById(customerId);
		if(optionslCustomer.isPresent()) {
			return ResponseEntity.ok(optionslCustomer.get());
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	

	@RequestMapping(value = "/creacliente​", method = RequestMethod.POST)
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
		Customer newCustomer = customerDAO.save(customer);
		Calendar cal = Calendar.getInstance();
		cal.setTime(newCustomer.getBirthdate());
		cal.add(Calendar.YEAR, 72);
		newCustomer.setDeathDate(cal.getTime());
		return ResponseEntity.ok(newCustomer);
	}
	
	@DeleteMapping(value="{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId")  Long customerId){
		customerDAO.deleteById(customerId);
		return ResponseEntity.ok(null);
	}
	
	@PutMapping
	public ResponseEntity<Customer> updateProduct(@RequestBody Customer customer){
		Optional<Customer> optionslCustomer = customerDAO.findById(customer.getId());
		if(optionslCustomer.isPresent()) {
			Customer updateCustomer = optionslCustomer.get();
			updateCustomer.setName(customer.getName());
			return ResponseEntity.ok(updateCustomer);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
