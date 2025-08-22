package yes.shief.telegrambotspicesshop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yes.shief.telegrambotspicesshop.dto.SpiceDto;
import yes.shief.telegrambotspicesshop.entity.Spice;
import yes.shief.telegrambotspicesshop.exception.SpiceNotFoundException;
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

    @Override
    public Spice getSpiceById(Long spiceId) {
        return spiceRepository.findById(spiceId)
                .orElseThrow(() ->
                        new SpiceNotFoundException("Spice can't ve found fo id = " + spiceId));
    }

    @Override
    public Spice updateSpice(SpiceDto spiceDto) {
        Spice spice = getSpiceById(spiceDto.getId());
        if (spiceDto.getName())
        return spiceRepository.save(null);
    }

    @Override
    public Spice deleteSpiceById(Long spiceId) {
        return null;
    }
}
