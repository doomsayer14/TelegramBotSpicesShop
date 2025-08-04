package yes.shief.telegrambotspicesshop.service;

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
}
