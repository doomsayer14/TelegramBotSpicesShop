package yes.shef.telegramshop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yes.shef.telegramshop.dto.CustomerDto;
import yes.shef.telegramshop.entity.Customer;
import yes.shef.telegramshop.exception.CustomerNotFoundException;
import yes.shef.telegramshop.repository.CustomerRepository;
import yes.shef.telegramshop.service.CustomerService;

import java.util.List;

/**
 * Implementation for {@link CustomerService}.
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = Customer.builder()
                .firstName(customerDto.getFirstName())
                .secondName(customerDto.getSecondName())
                .username(customerDto.getUsername())
                .build();

        log.info("Saving new customer: {}", customer);
        return customerRepository.save(customer);
    }


    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer can't be found for id = " + customerId));
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        customerRepository.deleteById(customerId);
        log.info("Deleting customer: {}", customerId);
    }
}
