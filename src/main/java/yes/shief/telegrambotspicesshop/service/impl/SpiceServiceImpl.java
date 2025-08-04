package yes.shief.telegrambotspicesshop.service.impl;

import org.springframework.stereotype.Service;
import yes.shief.telegrambotspicesshop.entity.Spice;
import yes.shief.telegrambotspicesshop.repository.SpiceRepository;
import yes.shief.telegrambotspicesshop.service.SpiceService;

import java.util.List;

/**
 * Implementation for {@link SpiceService}
 */
@Service
public class SpiceServiceImpl implements SpiceService {

    private final SpiceRepository spiceRepository;

    public SpiceServiceImpl(SpiceRepository spiceRepository) {
        this.spiceRepository = spiceRepository;
    }

    @Override
    public List<Spice> getAllSpices() {
        return spiceRepository.findAll();
    }
}
