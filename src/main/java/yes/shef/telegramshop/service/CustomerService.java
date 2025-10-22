package yes.shef.telegramshop.service;

import yes.shef.telegramshop.dto.CustomerDto;
import yes.shef.telegramshop.entity.Customer;
import yes.shef.telegramshop.entity.Product;

import java.util.List;

/**
 * Business logic implementation for {@link Customer}
 */
public interface CustomerService {

    /**
     * Finds all the customers from DB.
     *
     * @return list of all customers.
     */
    List<Customer> getAllCustomers();

    /**
     * Creates new {@link Customer} from DTO.
     *
     * @param customerDto customer to be created.
     * @return created customer.
     */
    Customer createCustomer(CustomerDto customerDto);

    /**
     * Get customer by id.
     *
     * @param customerId id of customer to be got.
     * @return customer with specified id.
     */
    Customer getCustomerById(Long customerId);

    /**
     * Deletes customer by id.
     *
     * @param customerId id of customer to be deleted.
     * @return customer product.
     */
    void deleteCustomerById(Long customerId);
}
