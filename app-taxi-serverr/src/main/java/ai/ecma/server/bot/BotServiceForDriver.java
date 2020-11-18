//package ai.ecma.server.bot;
//
//import ai.ecma.server.entity.BotUser;
//import ai.ecma.server.entity.Car;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//import java.util.List;
//
///**
// * BY SIROJIDDIN on 12.11.2020
// */
//
//
//public interface BotServiceForDriver {
//
//    SendMessage selectLang(Update update);
//
//    SendMessage welcome(Update update);
//
//    SendMessage getPhoneNumber(Update update);
//
//    SendMessage checkSmsCode(Update update);
//
//    EditMessageText acceptOrCancelOrder(Update update);
//
//    DeleteMessage deleteMessage(Update update);
//
//    SendMessage sendOrder(List<Car> cars);
//
//    SendMessage sendOne(BotUser botUser);
//
//}
