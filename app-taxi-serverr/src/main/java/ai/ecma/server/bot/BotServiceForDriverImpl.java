//package ai.ecma.server.bot;
//
//import ai.ecma.server.entity.*;
//import ai.ecma.server.entity.enums.LangEnum;
//import ai.ecma.server.entity.enums.OrderStatus;
//import ai.ecma.server.entity.enums.RoleName;
//import ai.ecma.server.exceptions.ResourceNotFoundException;
//import ai.ecma.server.repository.BotUserRepository;
//import ai.ecma.server.repository.OrderRepository;
//import ai.ecma.server.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.meta.api.methods.ParseMode;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
//
//import java.util.*;
//
///**
// * BY SIROJIDDIN on 12.11.2020
// */
//
//@Service
//public class BotServiceForDriverImpl implements BotServiceForDriver {
//    @Autowired
//    BotUserRepository botUserRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Override
//    public SendMessage selectLang(Update update) {
//        Long chatId = update.getMessage().getChatId();
//        Optional<BotUser> optionalBotUser = botUserRepository.findByChatId(chatId);
//        if (!optionalBotUser.isPresent()) {
//            BotUser botUser = new BotUser();
//            botUser.setChatId(chatId);
//            botUser.setState(BotState.SELECT_LANG);
//            botUserRepository.save(botUser);
//        }
//        SendMessage sendMessage = makeSendMessageMarkdownHasMessage(update);
//        InlineKeyboardMarkup inlineKeyboardMarkup = makeLangInline();
//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder
//                .append(BotConstant.SELECT_UZ)
//                .append("\n")
//                .append(BotConstant.SELECT_OZ)
//                .append("\n")
//                .append(BotConstant.SELECT_RU)
//                .append("\n")
//                .append(BotConstant.SELECT_EN);
//        sendMessage.setText(stringBuilder.toString());
//        return sendMessage;
//    }
//
//    @Override
//    public SendMessage welcome(Update update) {
//        Long chatId = update.getCallbackQuery().getMessage().getChatId();
//        BotUser botUser = botUserRepository.findByChatId(chatId).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
//        String data = update.getCallbackQuery().getData();
//        String langSubstr = data.substring(data.indexOf("Lang#") + 5);
//        botUser.setLang(LangEnum.valueOf(langSubstr));
//        botUser.setState(BotState.SHARE_CONTACT);
//        botUserRepository.save(botUser);
//        SendMessage sendMessage = makeSendMessageMarkdownHasCallback(update);
//        StringBuilder builder = new StringBuilder();
//        LangEnum lang = botUser.getLang();
//        builder
//                .append(lang.equals(LangEnum.UZ) ? BotConstant.WELCOME_DRIVER_UZ
//                        : lang.equals(LangEnum.OZ) ? BotConstant.WELCOME_DRIVER_OZ
//                        : lang.equals(LangEnum.RU) ? BotConstant.WELCOME_DRIVER_RU :
//                        BotConstant.WELCOME_DRIVER_EN);
//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        replyKeyboardMarkup.setSelective(true);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(true);
//        List<KeyboardRow> keyboard = new ArrayList<>();
//        KeyboardRow keyboardFirstRow = new KeyboardRow();
//        KeyboardButton keyboardButton = new KeyboardButton();
//        keyboardButton.setText("Share your number >").setRequestContact(true);
//        keyboardFirstRow.add(keyboardButton);
//        keyboard.add(keyboardFirstRow);
//        replyKeyboardMarkup.setKeyboard(keyboard);
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        sendMessage.setText(builder.toString());
//        return sendMessage;
//    }
//
//    @Override
//    public SendMessage getPhoneNumber(Update update) {
//        Long chatId = update.getMessage().getChatId();
//        SendMessage sendMessage = makeSendMessageMarkdownHasMessage(update);
//        BotUser botUser = botUserRepository.findByChatId(chatId).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
//        String phoneNumber = update.getMessage().getContact().getPhoneNumber();
//        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
//        Optional<User> optionalDriver = userRepository.findByPhoneNumberAndRoleName(phoneNumber, RoleName.ROLE_DRIVER.name());
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
//        if (!optionalDriver.isPresent()) {
//            sendMessage.setText(BotConstant.NOT_DRIVER);
//            return sendMessage;
//        }
//        botUser.setPhoneNumber(phoneNumber);
//        botUser.setState(BotState.DRIVER_CABINET);
//        botUserRepository.save(botUser);
//        sendMessage.setText(BotConstant.WELCOME_CABINET_UZ);
//        return sendMessage;
//    }
//
//    @Override
//    public SendMessage checkSmsCode(Update update) {
//        return null;
//    }
//
//    @Override
//    public EditMessageText acceptOrCancelOrder(Update update) {
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
//        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
//        String data = update.getCallbackQuery().getData();
//        boolean accept = false;
//        if (data.startsWith("Accept#")) {
//            data = data.substring(data.indexOf("Accept#") + 7);
//            accept = true;
//        } else {
//            data = data.substring(data.indexOf("Cancel#") + 7);
//        }
//        Optional<Order> optionalOrder = orderRepository.findByIdAndStatus(UUID.fromString(data), OrderStatus.NEW);
//        if (!optionalOrder.isPresent()) {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder
//                    .append("Buyurtma allaqachon olingan");
//            editMessageText.setText(stringBuilder.toString());
//            return editMessageText;
//        }
//        Order order = optionalOrder.get();
//        List<Route> routes = order.getRoutes();
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder
//                .append(BotConstant.ALL_SUM_UZ)
//                .append(order.getFare())
//                .append("\n")
//                .append(BotConstant.FROM_LOCATION)
//                .append(routes.get(0).getFromLat())
//                .append(", ")
//                .append(routes.get(0).getFromLon())
//                .append("\n")
//                .append(BotConstant.TO_LOCATION)
//                .append(routes.get(routes.size() - 1).getToLat())
//                .append(", ")
//                .append(routes.get(routes.size() - 1).getToLon());
//        User user = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getCreatedBy()));
//        stringBuilder
//                .append(user.getPhoneNumber());
//        editMessageText.setText(stringBuilder.toString());
//        InlineKeyboardMarkup inlineKeyboardMarkup = makeRejectInlineOrArrived(order);
//        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
//        if (accept){
//            //TODO TASK TUGAT homework
////            order.setCar();
//        }
//        return editMessageText;
//    }
//
//
//    @Override
//    public SendMessage sendOrder(List<Car> cars) {
//        return null;
//    }
//
//    @Override
//    public SendMessage sendOne(BotUser botUser) {
//        return new SendMessage(botUser.getChatId(), "Qalay?");
//    }
//
//    private SendMessage makeSendMessageHtmlHasMessage(Update update) {
//        return new SendMessage()
//                .setChatId(update.getMessage().getChatId()).setParseMode(ParseMode.HTML);
//    }
//
//    private SendMessage makeSendMessageMarkdownHasMessage(Update update) {
//        return new SendMessage()
//                .setChatId(update.getMessage().getChatId()).setParseMode(ParseMode.MARKDOWN);
//    }
//
//    private SendMessage makeSendMessageHtmlHasCallback(Update update) {
//        return new SendMessage()
//                .setChatId(update.getCallbackQuery().getMessage().getChatId()).setParseMode(ParseMode.HTML);
//    }
//
//    private SendMessage makeSendMessageMarkdownHasCallback(Update update) {
//        return new SendMessage()
//                .setChatId(update.getCallbackQuery().getMessage().getChatId()).setParseMode(ParseMode.MARKDOWN);
//    }
//
//    private InlineKeyboardMarkup makeLangInline() {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//        List<InlineKeyboardButton> firstRow = new ArrayList<>();
//        List<InlineKeyboardButton> secondRow = new ArrayList<>();
//        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
//        List<InlineKeyboardButton> fourthRow = new ArrayList<>();
//        InlineKeyboardButton uz = new InlineKeyboardButton();
//        InlineKeyboardButton oz = new InlineKeyboardButton();
//        InlineKeyboardButton ru = new InlineKeyboardButton();
//        InlineKeyboardButton en = new InlineKeyboardButton();
//        uz.setText(BotConstant.LANG_UZ);
//        oz.setText(BotConstant.LANG_OZ);
//        ru.setText(BotConstant.LANG_RU);
//        en.setText(BotConstant.LANG_EN);
//        uz.setCallbackData("Lang#" + LangEnum.UZ);
//        oz.setCallbackData("Lang#" + LangEnum.OZ);
//        ru.setCallbackData("Lang#" + LangEnum.RU);
//        en.setCallbackData("Lang#" + LangEnum.EN);
//        firstRow.add(uz);
//        secondRow.add(oz);
//        thirdRow.add(ru);
//        fourthRow.add(en);
//        rows.addAll(Arrays.asList(firstRow, secondRow, thirdRow, fourthRow));
//        inlineKeyboardMarkup.setKeyboard(rows);
//        return inlineKeyboardMarkup;
//    }
//
//    private InlineKeyboardMarkup makeRejectInlineOrArrived(Order order) {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//        List<InlineKeyboardButton> firstRow = new ArrayList<>();
//        List<InlineKeyboardButton> secondRow = new ArrayList<>();
//        InlineKeyboardButton reject = new InlineKeyboardButton();
//        InlineKeyboardButton arrived = new InlineKeyboardButton();
//        reject.setText(BotConstant.REJECT_ORDER_UZ);
//        arrived.setText(BotConstant.ARRIVED_ORDER_UZ);
//        reject.setCallbackData("Reject#" + order.getId());
//        arrived.setCallbackData("Arrived#" + order.getId());
//        firstRow.add(reject);
//        secondRow.add(arrived);
//        rows.addAll(Arrays.asList(firstRow, secondRow));
//        inlineKeyboardMarkup.setKeyboard(rows);
//        return inlineKeyboardMarkup;
//    }
//
//    public DeleteMessage deleteMessage(Update update) {
//        return new DeleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
//    }
//}
