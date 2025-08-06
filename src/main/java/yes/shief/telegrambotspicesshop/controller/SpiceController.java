package yes.shief.telegrambotspicesshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yes.shief.telegrambotspicesshop.service.SpiceService;

/**
 * Spices management via HTTP server.
 */
@RestController
@RequestMapping("/spice")
@RequiredArgsConstructor
public class SpiceController {

    private final SpiceService spiceService;

    @PostMapping
    public ResponseEntity<>

}
