package yes.shief.telegrambotspicesshop.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO class for {@link yes.shief.telegrambotspicesshop.entity.Spice}
 */
@Data
@Builder
public class SpiceDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private byte[] imageBytes;
}
