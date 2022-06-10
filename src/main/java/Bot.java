import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot {
    TaskGenerator taskGenerator;
    Test test;
    String previousMessage;
    int taskNumber;
    boolean isInTask, isInMainMenu, isInChooseTask, isInTest, isInChooseTest;
    Logger logger = Logger.getLogger(Bot.class.getName());

    public static void main(String[] args) {
        Bot bot = new Bot();
        bot.botConnect();
        Logger logger = Logger.getLogger(Bot.class.getName());
        logger.info("bot connected");

    }

    @Override
    public String getBotUsername() {
        return "ege_masterbot";
    }

    @Override
    public String getBotToken() {
        return "5109926976:AAG_gjb-X9Z-cUdPFcm27lxeRcJ3JHsZG4Y"; }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            logger.info(message);

            switch (message) {
                case "/start":
                    isInMainMenu = true;
                    sendMsg(chatId, "Привет! Этот бот был создан для школьников, которые хотят подготовиться к ЕГЭ. " +
                            "Удачной учебы и успешной сдачи экзаменов!");
                    break;

                case "Начать решать":
                     isInChooseTask = true;
                     isInTask = false;
                     isInMainMenu = false;
                     isInTest = false;
                     sendMsg(chatId, "Выберите номер задания: ");
                     break;

                case "Сгенерировать тест":
                    isInChooseTask = false;
                    isInTask = false;
                    isInMainMenu = false;
                    isInTest = false;
                    isInChooseTest = true;
                    sendMsg(chatId, "Выберете номер задания, из которых будет состоять тест, и их кол-во. \nПример: '4 5' - четвертый номер ЕГЭ, 5 заданий");

                case "4":
                    if (previousMessage.equals("Начать решать")) {
                        taskNumber = 4;
                        isInTask = true;
                        isInChooseTask = false;
                        isInMainMenu = false;
                        taskGenerator = new TaskGenerator();
                        taskGenerator.generate(taskNumber);
                        sendMsg(chatId, taskGenerator.getTask());
                    }
                    break;

                case "6":
                    if (previousMessage.equals("Начать решать")) {
                        taskNumber = 6;
                        isInTask = true;
                        isInChooseTask = false;
                        isInMainMenu = false;
                        taskGenerator = new TaskGenerator();
                        taskGenerator.generate(6);
                        sendMsg(chatId, taskGenerator.getTask());
                    }
                    break;

                case "7":
                    if (previousMessage.equals("Начать решать")) {
                        taskNumber = 7;
                        isInTask = true;
                        isInChooseTask = false;
                        isInMainMenu = false;
                        taskGenerator = new TaskGenerator();
                        taskGenerator.generate(taskNumber);
                        sendMsg(chatId, taskGenerator.getTask());
                    }
                    break;

                case "4 5":
                    if (isInChooseTest) {
                        isInChooseTest = false;
                        isInTest = true;
                        taskNumber = 4;
                        test = new Test();
                        taskGenerator = new TaskGenerator();
                        taskGenerator.generate(taskNumber);
                        sendMsg(chatId, taskGenerator.getTask());
                    }
                    isInTask = false;
                    isInMainMenu = false;
                    isInChooseTask = false;
                    break;

                case "7 5":
                    if (isInChooseTest) {
                        isInChooseTest = false;
                        isInTest = true;
                        taskNumber = 7;
                        test = new Test();
                        taskGenerator = new TaskGenerator();
                        taskGenerator.generate(taskNumber);
                        sendMsg(chatId, taskGenerator.getTask());
                    }
                    isInTask = false;
                    isInMainMenu = false;
                    isInChooseTask = false;
                    break;

                case "6 5":
                    if (isInChooseTest) {
                        isInChooseTest = false;
                        isInTest = true;
                        taskNumber = 6;
                        test = new Test();
                        taskGenerator = new TaskGenerator();
                        taskGenerator.generate(taskNumber);
                        sendMsg(chatId, taskGenerator.getTask());
                    }
                    isInTask = false;
                    isInMainMenu = false;
                    isInChooseTask = false;
                    break;


                case "Следующее задание":
                    if (isInTask) {
                        taskGenerator.generate(taskNumber);
                        sendMsg(chatId, taskGenerator.getTask());
                    }
                    break;

                case "Справка":
                    if (isInTask) {
                        switch (taskNumber) {
                            case 4 -> sendMsg(chatId, "В одном из приведённых ниже слов допущена ошибка в постановке ударения: НЕВЕРНО выделена буква, обозначающая ударный гласный звук. Выпишите это слово.");
                            case 6 -> sendMsg(chatId, "Отредактируйте предложение: исправьте лексическую ошибку, исключив лишнее слово. Выпишите это слово.");
                            case 7 -> sendMsg(chatId, "В одном из выделенных ниже слов допущена ошибка в образовании формы слова. Исправьте ошибку и запишите слово правильно.");
                        }
                    }
                    break;

                case "В меню":
                    taskNumber = -1;
                    isInMainMenu = true;
                    isInTask = false;
                    isInTest = false;
                    isInChooseTask = false;
                    sendMsg(chatId, "Вы в меню");
                    break;

                case "Показать ответ":
                    if (isInTask) {
                        sendMsg(chatId, taskGenerator.getAnswer());
                    }
                    break;

                default:
                    if (taskGenerator.getAnswer().equals(message.toLowerCase()) & isInTask) {
                        sendMsg(chatId, "Правильно!");
                    } else if (!taskGenerator.getAnswer().equals(message.toLowerCase()) & isInTask){
                        sendMsg(chatId, "Неверно");
                    }

                    if (taskGenerator.getAnswer().equals(message.toLowerCase()) & isInTest) {
                        test.plusScore();
                        test.plusCount();
                        if (test.getCount() == 5) {
                            sendMsg(chatId, "Вы правильно ответили на " + test.getScore() + " из 5 вопросов.");
                        } else {
                            taskGenerator.generate(taskNumber);
                            sendMsg(chatId, taskGenerator.getTask());
                        }
                    } else if (!taskGenerator.getAnswer().equals(message.toLowerCase()) & isInTest) {
                        test.plusCount();
                        if (test.getCount() == 5) {
                            sendMsg(chatId, "Вы правильно ответили на " + test.getScore() + " из 5 вопросов.");
                        } else {
                            taskGenerator.generate(taskNumber);
                            sendMsg(chatId, taskGenerator.getTask());
                        }
                    }
                    break;
            }
            previousMessage = message;
        }
    }

    public void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        setKeyboard(sendMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setKeyboard(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();
        KeyboardButton button2 = new KeyboardButton();
        KeyboardButton button3 = new KeyboardButton();
        KeyboardButton button4 = new KeyboardButton();

        if (isInMainMenu) {
            button1.setText("Начать решать");
            button2.setText("Сгенерировать тест");
            keyboardRow1.add(button1);
            keyboardRow2.add(button2);
        } else if (isInTask) {
            button1.setText("В меню");
            button3.setText("Следующее задание");
            button2.setText("Показать ответ");
            button4.setText("Справка");
            keyboardRow1.add(button3);
            keyboardRow1.add(button4);
            keyboardRow2.add(button1);
            keyboardRow2.add(button2);
        } else if (isInChooseTask) {
            button1.setText("4");
            button2.setText("В меню");
            button3.setText("6");
            button4.setText("7");
            keyboardRow1.add(button1);
            keyboardRow1.add(button4);
            keyboardRow2.add(button2);
            keyboardRow1.add(button3);
        } else if (isInTest) {
            button1.setText("В меню");
            keyboardRow1.add(button1);
        } else if (isInChooseTest) {
            button1.setText("4 5");
            button2.setText("В меню");
            button3.setText("6 5");
            button4.setText("7 5");
            keyboardRow1.add(button4);
            keyboardRow1.add(button1);
            keyboardRow2.add(button2);
            keyboardRow1.add(button3);
        }

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void botConnect(){
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
