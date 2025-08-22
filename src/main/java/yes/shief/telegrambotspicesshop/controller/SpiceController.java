package yes.shief.telegrambotspicesshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yes.shief.telegrambotspicesshop.dto.SpiceDto;
import yes.shief.telegrambotspicesshop.entity.Spice;
import yes.shief.telegrambotspicesshop.mapper.SpiceMapper;
import yes.shief.telegrambotspicesshop.service.SpiceService;

import java.util.List;

/**
 * Spices management via HTTP server, crud operations.
 */
@RestController
@RequestMapping("/spice")
@RequiredArgsConstructor
public class SpiceController {

    private final SpiceService spiceService;

    private final SpiceMapper spiceMapper;

    @PostMapping("")
    public ResponseEntity<SpiceDto> createSpice(@RequestBody SpiceDto spiceDto) {
        Spice spice = spiceService.createSpice(spiceDto);
        SpiceDto createdSpice = spiceMapper.spiceToSpiceDto(spice);

        return new ResponseEntity<>(createdSpice, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SpiceDto>> getAllSpices() {
        List<SpiceDto> spiceDtoList = spiceService.getAllSpices()
                .stream()
                .map(spiceMapper::spiceToSpiceDto)
                .toList();
        return ResponseEntity.ok(spiceDtoList);
    }

    @GetMapping("/{spiceId}")
    public ResponseEntity<SpiceDto> getSpice(@PathVariable String spiceId) {
        Spice spice = spiceService.getSpiceById(Long.parseLong(spiceId));
        SpiceDto spiceDto = spiceMapper.spiceToSpiceDto(spice);
        return ResponseEntity.ok(spiceDto);
    }

    @PutMapping("/")
    public ResponseEntity<SpiceDto> updateSpice(@RequestBody SpiceDto spiceDto) {
        Spice updatedSpice = spiceService.updateSpice(spiceDto);
        SpiceDto updatedSpiceDto = spiceMapper.spiceToSpiceDto(updatedSpice);
        return ResponseEntity.ok(updatedSpiceDto);
    }

    @DeleteMapping("/{spiceId}")
    public ResponseEntity<SpiceDto> deleteSpice(@PathVariable String spiceId) {
        Spice spice = spiceService.deleteSpiceById(Long.parseLong(spiceId));
        return ResponseEntity.noContent().build();
    }
}
