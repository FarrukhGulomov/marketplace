package com.iphone.marketplace.bot.command;

import com.iphone.marketplace.model.Advertisement;
import com.iphone.marketplace.service.AdvertisementService;
import com.iphone.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final UserService userService;
    private final AdvertisementService advertisementService;

    public SendMessage handleCommand(Message message) {
        String command = message.getText();
        Long chatId = message.getChatId();

        switch (command) {
            case "/start":
                return handleStart(chatId, message.getFrom().getUserName());
            case "/sell":
                return handleSell(chatId);
            case "/browse":
                return handleBrowse(chatId);
            case "/profile":
                return handleProfile(chatId);
            case "/help":
                return handleHelp(chatId);
            default:
                return handleUnknownCommand(chatId);
        }
    }

    private SendMessage handleStart(Long chatId, String username) {
        try {
            userService.registerUser(chatId, username);
        } catch (RuntimeException e) {
            // User already exists, continue with welcome message
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Welcome to iPhone Marketplace! 📱\n\n" +
                "Here you can buy and sell iPhones safely.\n\n" +
                "Use the menu below to get started:");

        message.setReplyMarkup(createMainMenu());
        return message;
    }

    private SendMessage handleSell(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("To sell your iPhone, please provide the following information:\n\n" +
                "1. Model (e.g., iPhone 13 Pro)\n" +
                "2. Condition (New/Used)\n" +
                "3. Price\n" +
                "4. Description\n\n" +
                "Send the information in the format:\n" +
                "/post [model] | [condition] | [price] | [description]");
        return message;
    }

    private SendMessage handleBrowse(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        
        List<Advertisement> latestAds = advertisementService.searchAdvertisements(null, null, null);
        if (latestAds.isEmpty()) {
            message.setText("No advertisements available at the moment.\n\n" +
                    "Use these commands to filter when ads are available:\n" +
                    "/search [model] - Search by model\n" +
                    "/price [min]-[max] - Filter by price range\n" +
                    "/latest - Show latest listings");
        } else {
            StringBuilder adList = new StringBuilder("Available iPhones:\n\n");
            for (Advertisement ad : latestAds) {
                adList.append("📱 ").append(ad.getModel())
                      .append(" | ").append(ad.getCondition())
                      .append(" | $").append(ad.getPrice())
                      .append("\n").append(ad.getDescription())
                      .append("\n\n");
            }
            adList.append("Filter commands:\n")
                 .append("/search [model] - Search by model\n")
                 .append("/price [min]-[max] - Filter by price range\n")
                 .append("/latest - Show latest listings");
            message.setText(adList.toString());
        }
        return message;
    }

    private SendMessage handleProfile(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        
        var userOpt = userService.findByTelegramId(chatId);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            message.setText("Your Profile:\n\n" +
                    "Username: " + user.getUsername() + "\n" +
                    "Phone: " + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "Not set") + "\n" +
                    "Role: " + user.getRole() + "\n\n" +
                    "To update your phone number, use:\n" +
                    "/phone [number]");
        } else {
            message.setText("Profile not found. Please use /start to register.");
        }
        return message;
    }

    private SendMessage handleHelp(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Available commands:\n\n" +
                "/start - Start using the marketplace\n" +
                "/sell - Post a new advertisement\n" +
                "/browse - Browse available iPhones\n" +
                "/profile - View your profile\n" +
                "/help - Show this help message");
        return message;
    }

    private SendMessage handleUnknownCommand(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Unknown command. Use /help to see available commands.");
        return message;
    }

    private ReplyKeyboardMarkup createMainMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        
        KeyboardRow row1 = new KeyboardRow();
        row1.add("/sell");
        row1.add("/browse");
        
        KeyboardRow row2 = new KeyboardRow();
        row2.add("/profile");
        row2.add("/help");
        
        keyboard.add(row1);
        keyboard.add(row2);
        
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        
        return keyboardMarkup;
    }
}
