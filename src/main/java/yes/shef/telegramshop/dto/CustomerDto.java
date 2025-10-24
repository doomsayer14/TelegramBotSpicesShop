package yes.shef.telegramshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yes.shef.telegramshop.entity.Customer;

/**
 * DTO class for {@link Customer}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long id;
    private Long telegramId;
    private String firstName;
    private String secondName;
    private String username;
}
