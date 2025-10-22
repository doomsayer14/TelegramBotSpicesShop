package yes.shef.telegramshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import yes.shef.telegramshop.dto.CustomerDto;
import yes.shef.telegramshop.entity.Customer;
import yes.shef.telegramshop.mapper.CustomerMapper;
import yes.shef.telegramshop.service.CustomerService;

import java.util.List;


/**
 * {@link Customer} management via HTTP server, crud operations.
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final CustomerMapper customerMapper;

    @PostMapping("/")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = customerService.createCustomer(customerDto);
        CustomerDto createdCustomer = customerMapper.customerToCustomerDto(customer);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{customerId}")
                        .buildAndExpand(createdCustomer.getId())
                        .toUri())
                .body(createdCustomer);
    }

    @GetMapping("/")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customerDtoList = customerService.getAllCustomers()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .toList();
        return ResponseEntity.ok(customerDtoList);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getProduct(@PathVariable String customerId) {
        Customer customer = customerService.getCustomerById(Long.parseLong(customerId));
        CustomerDto customerDto = customerMapper.customerToCustomerDto(customer);
        return ResponseEntity.ok(customerDto);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomerById(Long.parseLong(customerId));
        return ResponseEntity.noContent().build();
    }
}
