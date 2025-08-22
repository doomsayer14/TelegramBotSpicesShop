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
     * Get spice by id.
     * @param spiceId id of spice to be got.
     * @return spice with specified id.
     */
    Spice getSpiceById(Long spiceId);

    /**
     * Updates Spice from DTO.
     * @param spiceDto spice to be updated.
     * @return updated spice
     */
    Spice updateSpice(SpiceDto spiceDto);

    /**
     * Deletes spice by id.
     * @param spiceId id of spice to be deleted.
     * @return deleted spice.
     */
    Spice deleteSpiceById(Long spiceId);
}
