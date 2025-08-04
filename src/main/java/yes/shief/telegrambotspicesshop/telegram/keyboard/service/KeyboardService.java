package yes.shief.telegrambotspicesshop.telegram.keyboard.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import yes.shief.telegrambotspicesshop.telegram.keyboard.entity.StartKeyboardButton;
import yes.shief.telegrambotspicesshop.telegram.keyboard.repository.StartKeyboardButtonRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to manage ReplyKeyboard anywhere in program
 */

@Getter
@Component
public class KeyboardService {

    private List<KeyboardRow> startKeyboard;

    private final StartKeyboardButtonRepository startKeyboardButtonRepository;

    public KeyboardService(StartKeyboardButtonRepository startKeyboardButtonRepository) {
        this.startKeyboardButtonRepository = startKeyboardButtonRepository;

        this.startKeyboard = new ArrayList<>();
    }





}
