package ai.ecma.server.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class BotTaxiDriver1 extends TelegramLongPollingBot {
    @Autowired
    BotServiceImplForDriverTX botService;



    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            if (update.getMessage().hasText()) {
                switch (update.getMessage().getText()){
                    case "/start":
                        execute(botService.selectLang(update));
                        break;
                }
            }else if (update.getMessage().hasContact()){
                execute(botService.checkContact(update));
            }
        }else if (update.hasCallbackQuery()){
            String data = update.getCallbackQuery().getData();
            if (data!=null){
                if (data.startsWith("Lang#")){
                    execute(botService.deleteMessage(update));
                    execute(botService.welcome(update));
                }else if (data.startsWith("Accept#")){
                    execute(botService.acceptOrder(update));
                }else if (data.startsWith("Cancel#")){
//                    execute(botService.acceptOrder(update));
                }
            }
        }
    }










    @Override
    public String getBotUsername() {
        return "taxiborg8bot";
    }

    @Override
    public String getBotToken() {
        return "1403599792:AAGErtHTdjIw6AH9RGiLl3Tl3DmntX2Km6U";
    }
}
