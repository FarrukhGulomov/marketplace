package com.iphone.marketplace.bot;

import com.iphone.marketplace.bot.command.CommandHandler;
import com.iphone.marketplace.config.TelegramBotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class MarketplaceBot extends TelegramWebhookBot {
    private final TelegramBotConfig config;
    private final CommandHandler commandHandler;

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotPath() {
        return config.getWebhookUrl();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                return commandHandler.handleCommand(update.getMessage());
            }
        } catch (Exception e) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), 
                "Sorry, an error occurred. Please try again later.");
        }
        return null;
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
