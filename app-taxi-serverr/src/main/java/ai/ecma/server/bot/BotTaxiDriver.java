//package ai.ecma.server.bot;
//
//import ai.ecma.server.repository.BotUserRepository;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//@Component
//public class BotTaxiDriver extends TelegramLongPollingBot {
//    @Autowired
//    BotServiceForDriver botServiceForDriver;
//    @Autowired
//    BotUserRepository botUserRepository;
//
//    @SneakyThrows
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage()) {
//            if (update.getMessage().hasText()) {
//                switch (update.getMessage().getText()) {
//                    case "/start":
//                        execute(botServiceForDriver.selectLang(update));
//                        break;
//                }
//            } else if (update.getMessage().hasContact()) {
//                execute(botServiceForDriver.getPhoneNumber(update));
//            }
//        } else if (update.hasCallbackQuery()) {
//            String data = update.getCallbackQuery().getData();
//            if (data.startsWith("Lang#")) {
//                execute(botServiceForDriver.deleteMessage(update));
//                execute(botServiceForDriver.welcome(update));
//            } else if (data.startsWith("Accept#") || data.startsWith("Cancel#")) {
//                execute(botServiceForDriver.acceptOrCancelOrder(update));
//            }
//        }
//
//
//    }
//
//
//    @Override
//    public String getBotUsername() {
//        return botUsername;
//    }
//
//    @Override
//    public String getBotToken() {
//        return botToken;
//    }
//
//
//    @Value("${bot.token}")
//    private String botToken;
//    @Value("${bot.username}")
//    private String botUsername;
//
//}
