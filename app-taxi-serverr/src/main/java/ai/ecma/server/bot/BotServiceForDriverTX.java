package ai.ecma.server.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageLiveLocation;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotServiceForDriverTX {
    SendMessage selectLang(Update update);
    DeleteMessage deleteMessage(Update update);
    SendMessage welcome(Update update);
    SendMessage checkContact(Update update);
    EditMessageText acceptOrder(Update update);
    EditMessageText cancelOrder(Update update);
}
