package ai.ecma.server.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ButtonController {
    /*------------------------ inlineKeyboard---------------------------*/
    public static InlineKeyboardButton inlineButton(String text, String data){
        return new InlineKeyboardButton().setText(text).setCallbackData(data);
    }

    public static List<InlineKeyboardButton> inlineButtonList(InlineKeyboardButton... inlineButton){
        List<InlineKeyboardButton> inlineButtonList=new LinkedList<>();
        inlineButtonList.addAll(Arrays.asList(inlineButton));
        return inlineButtonList;
    }

    public static List<List<InlineKeyboardButton>> collection(List<InlineKeyboardButton>... inlineButtonList){
        List<List<InlineKeyboardButton>> collection=new LinkedList<>();
        collection.addAll(Arrays.asList(inlineButtonList));
        return collection;
    }

    public static InlineKeyboardMarkup inlineKeyboard(List<List<InlineKeyboardButton>> collection){
        InlineKeyboardMarkup inlineKeyboard=new InlineKeyboardMarkup();
        inlineKeyboard.setKeyboard(collection);
        return inlineKeyboard;
    }

    /*------------------------ Button---------------------------*/
    public static KeyboardRow button(ArrayList<String> text){
        KeyboardRow button=new KeyboardRow();
        for (String s : text) {
            button.add(s);
        }
        return button;
    }

    public static List<KeyboardRow> row(KeyboardRow... button){
        List<KeyboardRow> row = new LinkedList<>();
        row.addAll(Arrays.asList(button));
        return row;
    }

    public static ReplyKeyboardMarkup keyboardMarkup(List<KeyboardRow> row) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setKeyboard(row);
        return keyboardMarkup;
    }
}
