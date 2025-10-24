package yes.shef.telegramshop.mapper;

import org.springframework.stereotype.Component;
import yes.shef.telegramshop.dto.CustomerDto;
import yes.shef.telegramshop.entity.Customer;

/**
 * Mapper for {@link Customer} in facade style.
 */
@Component
public class CustomerMapper {
    /**
     * Maps {@link Customer} to {@link CustomerDto}.
     *
     * @param customer to be mapped to {@link CustomerDto}.
     * @return new Dto.
     */
    public CustomerDto customerToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .telegramId(customer.getTelegramId())
                .firstName(customer.getFirstName())
                .secondName(customer.getSecondName())
                .username(customer.getUsername())
                .build();
    }
}
