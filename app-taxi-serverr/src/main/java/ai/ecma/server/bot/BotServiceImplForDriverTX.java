package ai.ecma.server.bot;

import ai.ecma.server.bot.enums.LangEnums;
import ai.ecma.server.entity.*;
import ai.ecma.server.entity.enums.LangEnum;
import ai.ecma.server.entity.enums.OrderStatus;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BotServiceImplForDriverTX implements BotServiceForDriverTX {

    @Autowired
    BotUserRepository botUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    MissedRepository missedRepository;

    @Override
    public SendMessage selectLang(Update update) {
        SendMessage sendMessage = makeSendMessageMarkdown(update);
        Optional<BotUser> optionalBotUser = botUserRepository.findByChatId(update.getMessage().getChatId());
        if (!optionalBotUser.isPresent()) {
            BotUser botUser = new BotUser();
            botUser.setChatId(update.getMessage().getChatId());
            botUser.setState(BotStateXt.SELECT_LANG);
            botUserRepository.save(botUser);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("\n")
                .append(BotConst.SELECT_LANG_UZ)
                .append("\n")
                .append(BotConst.SELECT_LANG_OZ)
                .append("\n")
                .append(BotConst.SELECT_LANG_RU)
                .append("\n")
                .append(BotConst.SELECT_LANG_EN);
        sendMessage.setText(stringBuilder.toString());
        sendMessage.setReplyMarkup(ButtonController.inlineKeyboard(
                ButtonController.collection(ButtonController.inlineButtonList(ButtonController.inlineButton(BotConst.LANG_UZ, "Lang#" + LangEnums.UZ)),
                        ButtonController.inlineButtonList(ButtonController.inlineButton(BotConst.LANG_OZ, "Lang#" + LangEnums.OZ)),
                        ButtonController.inlineButtonList(ButtonController.inlineButton(BotConst.LANG_RU, "Lang#" + LangEnums.RU)),
                        ButtonController.inlineButtonList(ButtonController.inlineButton(BotConst.LANG_EN, "Lang#" + LangEnums.EN)))));
        return sendMessage;
    }

    @Override
    public DeleteMessage deleteMessage(Update update) {
        return new DeleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
    }

    @Override
    public SendMessage welcome(Update update) {
        Long data = update.getCallbackQuery().getMessage().getChatId();
        BotUser botUser = botUserRepository.findByChatId(data).orElseThrow(() -> new ResourceNotFoundException("BotUser", "chatId", data));
        SendMessage sendMessage = makeCallbackQuerySendMessageMarkdown(update);
        String lang = update.getCallbackQuery().getData().substring(5);
        botUser.setLang(LangEnums.valueOf(lang));
        botUser.setState(BotStateXt.SHARE_CONTACT);
        botUserRepository.save(botUser);

        //User tanlagan tilga qarab text chiqaradi
        StringBuilder stringBuilder = getInlineKeyboardConstValue(update);

        String shareText=botUser.getLang().equals(LangEnums.UZ)?BotConst.SHARE_CONTACT_UZ:
                botUser.getLang().equals(LangEnums.OZ)?BotConst.SHARE_CONTACT_OZ:
                        botUser.getLang().equals(LangEnums.RU)?BotConst.SHARE_CONTACT_RU:
                                BotConst.SHARE_CONTACT_EN;

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText(shareText).setRequestContact(true);
        keyboardButtons.add(keyboardButton);
        keyboardRows.add(keyboardButtons);
        keyboardMarkup.setKeyboard(keyboardRows);

        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText(stringBuilder.toString());

        return sendMessage;
    }

    @Override
    public SendMessage checkContact(Update update) {
        String phoneNumber = update.getMessage().getContact().getPhoneNumber();
        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
        Long chatId = update.getMessage().getChatId();
        Optional<User> optionalUser = userRepository.findByPhoneNumberAndRoleName(phoneNumber);
        BotUser botUser = botUserRepository.findByChatId(chatId).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
        SendMessage sendMessage = makeSendMessageMarkdown(update);
        StringBuilder stringBuilder = new StringBuilder();
        if (!optionalUser.isPresent()) {
            stringBuilder
                    .append(botUser.getLang().equals(LangEnums.UZ) ? BotConst.DRIVER_COBINET_UZ_X :
                            botUser.getLang().equals(LangEnums.OZ) ? BotConst.DRIVER_COBINET_OZ_X :
                                    botUser.getLang().equals(LangEnums.RU) ? BotConst.DRIVER_COBINET_RU_X :
                                            BotConst.DRIVER_COBINET_EN_X);
            sendMessage.setText(stringBuilder.toString());
            return sendMessage;
        }
        botUser.setPhoneNumber(phoneNumber);
        botUserRepository.save(botUser);
        stringBuilder
                .append(botUser.getLang().equals(LangEnums.UZ) ? BotConst.DRIVER_COBINET_UZ :
                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.DRIVER_COBINET_OZ :
                                botUser.getLang().equals(LangEnums.RU) ? BotConst.DRIVER_COBINET_RU :
                                        BotConst.DRIVER_COBINET_EN);
        sendMessage.setText(stringBuilder.toString());
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        return sendMessage;
    }

    public SendMessage makeEditMessageForOrder(BotUser botUser, Order order) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(botUser.getChatId());

        StringBuilder stringBuilder = getOrderInformation(order);
        String makeButton1=botUser.getLang().equals(LangEnums.UZ)?BotConst.ACCEPT_ORDER_UZ:
                botUser.getLang().equals(LangEnums.OZ)?BotConst.ACCEPT_ORDER_OZ:
                        botUser.getLang().equals(LangEnums.RU)?BotConst.ACCEPT_ORDER_RU:
                                BotConst.ACCEPT_ORDER_EN;
        String makeButton2=botUser.getLang().equals(LangEnums.UZ)?BotConst.CANCEL_ORDER_UZ:
                botUser.getLang().equals(LangEnums.OZ)?BotConst.CANCEL_ORDER_OZ:
                        botUser.getLang().equals(LangEnums.RU)?BotConst.CANCEL_ORDER_RU:
                                BotConst.CANCEL_ORDER_EN;

        sendMessage.setReplyMarkup(getInlineKeyboardMarkupInButton2(makeButton1, "Accept#", makeButton2, "Cancel#", order.getId().toString()));
        sendMessage.setText(stringBuilder.toString());
        return sendMessage;
    }

    @Override
    public EditMessageText acceptOrder(Update update) {
        Long chatId=update.getCallbackQuery().getMessage().getChatId();

        EditMessageText editMessageText=new EditMessageText();
        editMessageText.setParseMode(ParseMode.MARKDOWN);
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setChatId(chatId);

        String data = update.getCallbackQuery().getData();
        String orderId=data.substring(7);
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() -> new ResourceNotFoundException("order", "orderId", orderId));
        BotUser botUser = botUserRepository.findByChatId(chatId).orElseThrow(() -> new ResourceNotFoundException("BotUser", "butUser", update.getCallbackQuery().getMessage().getChatId()));
        User user = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "createdBy", order.getCreatedBy()));
        String phoneNumber=botUser.getPhoneNumber();
        User driver = userRepository.findByPhoneNumberAndRoleName(phoneNumber).orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));
        Car car = carRepository.findByDriverId(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("Car", "driverId", driver.getId()));
        boolean missed = missedRepository.existsByCarIdAndOrderId(car.getId(), UUID.fromString(orderId));
        StringBuilder aboutOrder = getOrderInformation(order);
        if (missed) {
            aboutOrder
                    .append("\n")
                    .append("Oka bu zakaz o'tib ketdi");
            editMessageText.setText(aboutOrder.toString());
            return editMessageText;
        }
        order.setCar(car);
        order.setStatus(OrderStatus.WAITING);
        order.setAcceptedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);

        editMessageText.setText(aboutOrder.toString());
        aboutOrder.append("\n");
        aboutOrder.append("*Buyurtmachi telephone raqami: "+user.getPhoneNumber()+"*");

        String text=getInlineKeyboardConstValue(update).toString();
        String text1=text.substring(0,text.indexOf("#"));
        String text2=text.substring(text.indexOf("#")+1);

        editMessageText.setReplyMarkup(getInlineKeyboardMarkupInButton2(text1, "Arrived#", text2, "Reject#", orderId));
        return editMessageText;
    }

    @Override
    public EditMessageText cancelOrder(Update update) {
        return null;
    }




    public static SendMessage makeSendMessageMarkdown(Update update) {
        return new SendMessage().setChatId(update.getMessage().getChatId()).setParseMode(ParseMode.MARKDOWN);
    }

    public static SendMessage makeSendMessageHtml(Update update) {
        return new SendMessage().setChatId(update.getMessage().getChatId()).setParseMode(ParseMode.HTML);
    }

    public static SendMessage makeCallbackQuerySendMessageMarkdown(Update update) {
        return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setParseMode(ParseMode.MARKDOWN);
    }

    public static SendMessage makeCallbackQuerySendMessageHtml(Update update) {
        return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setParseMode(ParseMode.HTML);
    }



    private InlineKeyboardMarkup getInlineKeyboardMarkupInButton2(String button1text, String button1Data, String button2text, String button2Data, String orderId) {
        return ButtonController.inlineKeyboard(ButtonController.collection(
                ButtonController.inlineButtonList(ButtonController.inlineButton(button1text, button1Data + orderId)),
                ButtonController.inlineButtonList(ButtonController.inlineButton(button2text, button2Data + orderId))));
    }

    private StringBuilder getOrderInformation(Order order){
        return new StringBuilder()
                .append(BotConstant.ALL_SUM_UZ)
                .append(order.getFare())
                .append("\n")
                .append(BotConstant.FROM_LOCATION)
                .append(order.getRoutes().get(0).getFromLat())
                .append(", ")
                .append(order.getRoutes().get(0).getFromLon())
                .append("\n")
                .append(BotConstant.TO_LOCATION)
                .append(order.getRoutes().get(order.getRoutes().size() - 1).getToLat())
                .append(", ")
                .append(order.getRoutes().get(order.getRoutes().size() - 1).getToLon());
    }

    private StringBuilder getInlineKeyboardConstValue(Update update) {
        StringBuilder stringBuilder = new StringBuilder();
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                BotUser botUser = botUserRepository.findByChatId(update.getMessage().getChatId()).orElseThrow(() -> new ResourceNotFoundException("BotUser", "chatId", update.getMessage().getChatId()));
                switch (update.getMessage().getText()) {
                    case "/start":
                        break;
                }
            } else if (update.getMessage().hasContact()) {

            }
        } else if (update.hasCallbackQuery()) {
            BotUser botUser = botUserRepository.findByChatId(update.getCallbackQuery().getMessage().getChatId()).orElseThrow(() -> new ResourceNotFoundException("BotUser", "chatId", update.getCallbackQuery().getMessage().getChatId()));
            String data = update.getCallbackQuery().getData();
            if (data.startsWith("Lang#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.WELCOME_FOR_DRIVER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.WELCOME_FOR_DRIVER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.WELCOME_FOR_DRIVER_RU :
                                                        BotConst.WELCOME_FOR_DRIVER_EN
                        );

            } else if (data.startsWith("Accept#")) {
                stringBuilder
//                        .append(
//                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.ACCEPT_ORDER_UZ :
//                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.ACCEPT_ORDER_OZ :
//                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.ACCEPT_ORDER_RU :
//                                                        BotConst.ACCEPT_ORDER_EN
//                        )
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.ARRIVED_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.ARRIVED_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.ARRIVED_ORDER_RU :
                                                        BotConst.ARRIVED_ORDER_EN
                        )
                        .append("#")
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.REJECT_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.REJECT_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.REJECT_ORDER_Ru :
                                                        BotConst.REJECT_ORDER_EN
                        );
            } else if (data.startsWith("Cancel#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.CANCEL_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.CANCEL_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.CANCEL_ORDER_RU :
                                                        BotConst.CANCEL_ORDER_EN
                        );
            } else if (data.startsWith("Arrived#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.ARRIVED_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.ARRIVED_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.ARRIVED_ORDER_RU :
                                                        BotConst.ARRIVED_ORDER_EN
                        );
            } else if (data.startsWith("Reject#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.REJECT_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.REJECT_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.REJECT_ORDER_Ru :
                                                        BotConst.REJECT_ORDER_EN
                        );
            } else if (data.startsWith("Start#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.STARTED_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.STARTED_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.STARTED_ORDER_RU :
                                                        BotConst.STARTED_ORDER_EN
                        );
            } else if (data.startsWith("StartWaiting#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.START_WAITING_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.START_WAITING_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.START_WAITING_ORDER_RU :
                                                        BotConst.START_WAITING_ORDER_EN
                        );
            } else if (data.startsWith("StopWaiting#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.STOP_WAITING_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.STOP_WAITING_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.STOP_WAITING_ORDER_RU :
                                                        BotConst.STOP_WAITING_ORDER_EN
                        );
            } else if (data.startsWith("Closed#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.CLOSED_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.CLOSED_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.CLOSED_ORDER_RU :
                                                        BotConst.CLOSED_ORDER_EN
                        );
            } else if (data.startsWith("Rate#")) {
                stringBuilder
                        .append(
                                botUser.getLang().equals(LangEnums.UZ) ? BotConst.RATE_ORDER_UZ :
                                        botUser.getLang().equals(LangEnums.OZ) ? BotConst.RATE_ORDER_OZ :
                                                botUser.getLang().equals(LangEnums.RU) ? BotConst.RATE_ORDER_RU :
                                                        BotConst.RATE_ORDER_EN
                        );
            }
//            else if (data.startsWith("Stars#")) {
//
//            }
        }
        return stringBuilder;
    }

}
