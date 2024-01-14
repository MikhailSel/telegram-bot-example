package com.example.telegrambotexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final MessageRepository messageRepository;

    public TelegramBot(final MessageRepository messageRepository) {
        super("change-me-token");
        this.messageRepository = messageRepository;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        log.info(update.getMessage().getFrom().getFirstName());
        log.info(update.getMessage().getFrom().getId().toString());

        messageRepository.save(Message.builder()
            .text(update.getMessage().getText())
            .build());

        try {
            SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Echo: " + update.getMessage().getText())
                .build();
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "change-me-name";
    }

}
