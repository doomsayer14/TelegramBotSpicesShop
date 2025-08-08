package yes.shief.telegrambotspicesshop.service;

import yes.shief.telegrambotspicesshop.dto.SpiceDto;
import yes.shief.telegrambotspicesshop.entity.Spice;

import java.util.List;

/**
 * Business logic implementation for {@link Spice}
 */
public interface SpiceService {

    /**
     * Finds all the spices from DB.
     * @return list of all spices.
     */
   List<Spice> getAllSpices();

    /**
     * Creates new Spice from DTO.
     * @param spiceDto spice to be created.
     * @return created spice.
     */
    Spice createSpice(SpiceDto spiceDto);

    /**
     * Get
     * @param l
     * @return
     */
    Spice getSpiceById(Long l);
}
