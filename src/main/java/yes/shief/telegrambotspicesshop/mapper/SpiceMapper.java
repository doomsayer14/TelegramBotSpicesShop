package yes.shief.telegrambotspicesshop.mapper;

import org.springframework.stereotype.Component;
import yes.shief.telegrambotspicesshop.dto.SpiceDto;
import yes.shief.telegrambotspicesshop.entity.Spice;

/**
 * Mapper for Spice in facade style.
 */
@Component
public class SpiceMapper {
    /**
     * Maps {@link Spice} to {@link SpiceDto}.
     * @param spice to be mapped to {@link SpiceDto}.
     * @return new Dto.
     */
    public SpiceDto spiceToSpiceDto(Spice spice) {
        return SpiceDto.builder()
                .id(spice.getId())
                .name(spice.getName())
                .description(spice.getDescription())
                .price(spice.getPrice())
                .imageBytes(spice.getImageBytes())
                .build();
    }
}
