package yes.shief.telegrambotspicesshop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yes.shief.telegrambotspicesshop.dto.SpiceDto;
import yes.shief.telegrambotspicesshop.entity.Spice;
import yes.shief.telegrambotspicesshop.repository.SpiceRepository;
import yes.shief.telegrambotspicesshop.service.SpiceService;

import java.util.List;

/**
 * Implementation for {@link SpiceService}
 */
@Slf4j
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

    @Override
    public Spice createSpice(SpiceDto spiceDto) {
        Spice spice = Spice.builder()
                .name(spiceDto.getName())
                .description(spiceDto.getDescription())
                .price(spiceDto.getPrice())
                .imageBytes(spiceDto.getImageBytes())
                .build();

        log.info("Saving new spice: {}", spice);
        return spiceRepository.save(spice);
    }
}
