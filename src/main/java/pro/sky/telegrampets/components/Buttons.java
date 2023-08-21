package pro.sky.telegrampets.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * class for creating first and second level buttons
 */

@Component
public class Buttons {
    /**
     * @return returns dog button and cat button
     */
    /*
    SendMessage отправляет сообщение пользователю
    InlineKeyboardButton линейные кнопки под прикрепленным сообщением
    ReplyKeyboardMarkup кнопки внутри интерфейса телеграмма при нажатии которых возвращается определенный текст
     */
    public InlineKeyboardMarkup selectionAnimalButtons() {
        InlineKeyboardButton catButton = new InlineKeyboardButton("Приют Кошек \uD83D\uDC08");
        InlineKeyboardButton dogButton = new InlineKeyboardButton("Приют Собак \uD83E\uDDAE");
        //присваивание "бирок" которые будут возвращатся при нажатии
        catButton.setCallbackData("Кошка");
        dogButton.setCallbackData("Собака");
        //Разметка кнопок
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtons = List.of(catButton, dogButton);
        //Для добавления кнопок в одну линию стоит в массив добавить только один массив со всеми кнопками
        List<List<InlineKeyboardButton>> rowsInLine = List.of(keyboardButtons);
        //Добавление в разметку массив с кнопками
        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

    /**
     * @return returns dog button and cat button
     */

    //аналогичный методу первого уровня за исключением того что кнопок 4 и находятся они друг под другом
    public InlineKeyboardMarkup secondLayerButtons() {
        InlineKeyboardButton checkInfoButton = new InlineKeyboardButton("Информация о приюте");
        InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
        InlineKeyboardButton getReportAboutPet = new InlineKeyboardButton("Прислать отчет о питомце");
        InlineKeyboardButton howGetPet = new InlineKeyboardButton("Как взять животное из приюта?");
        InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
        checkInfoButton.setCallbackData("Информация о приюте");
        callVolunteerButton.setCallbackData("Позвать волонтера");
        getReportAboutPet.setCallbackData("Прислать отчет о питомце");
        howGetPet.setCallbackData("Как взять животное из приюта?");
        toStart.setCallbackData("В начало");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = List.of(List.of(checkInfoButton), List.of(callVolunteerButton), List.of(getReportAboutPet), List.of(howGetPet),List.of(toStart));
        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup takeAnimalButton(boolean isACat) {
        InlineKeyboardButton datingRulesButton = new InlineKeyboardButton("Правила знакомства");
        InlineKeyboardButton listOfDocumentsButton = new InlineKeyboardButton("Список документов");
        InlineKeyboardButton TransportationButton = new InlineKeyboardButton("Рекомендации по транспортировке животных");
        InlineKeyboardButton arrangementPuppyButton = new InlineKeyboardButton();
        InlineKeyboardButton arrangementAdultButton = new InlineKeyboardButton();
        if (isACat) {
            arrangementAdultButton.setText("Обустройство для взрослого кота");
            arrangementAdultButton.setCallbackData("Обустройство для взрослого кота");

            arrangementPuppyButton.setText("Обустройство для котенка");
            arrangementPuppyButton.setCallbackData("Обустройство котенка");
        } else {
            arrangementAdultButton.setText("Обустройство для взрослой собаки");
            arrangementAdultButton.setCallbackData("Обустройство для взрослой собаки");

            arrangementPuppyButton.setText("Обустройство для щенка");
            arrangementPuppyButton.setCallbackData("Обустройство щенка");
        }
        InlineKeyboardButton arrangemenDisabledButton = new InlineKeyboardButton("Обустройство для животного\n с ограниченными возможностями");
        InlineKeyboardButton whyRefuseButton = new InlineKeyboardButton("Cписок причин, почему могут отказать");
        InlineKeyboardButton contactButton = new InlineKeyboardButton("Запись ваших контактов");
        InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
        InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");

        datingRulesButton.setCallbackData("Правила знакомства");
        listOfDocumentsButton.setCallbackData("Список документов");
        TransportationButton.setCallbackData("Рекомендации по транспортировке");
        arrangemenDisabledButton.setCallbackData("Обустройство для ограниченного");
        whyRefuseButton.setCallbackData("Cписок причин");
        contactButton.setCallbackData("Запись контактов");
        callVolunteerButton.setCallbackData("Позвать волонтера");
        toStart.setCallbackData("В начало");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = List.of(
                                                      List.of(datingRulesButton), List.of(listOfDocumentsButton),
                                                      List.of(TransportationButton), List.of(arrangementPuppyButton),List.of(arrangementAdultButton),
                                                      List.of(arrangemenDisabledButton), List.of(whyRefuseButton),List.of(contactButton),
                                                      List.of(callVolunteerButton), List.of(toStart)
                                                    );
        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup shelterInformationButton(boolean isACat) {
        InlineKeyboardButton safetyRecommendationsButton = new InlineKeyboardButton("Общие правила поведения");
        InlineKeyboardButton recordingContactsSelection = new InlineKeyboardButton("Запись ваших контактов");
        InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton();
        InlineKeyboardButton shelterWorkingHoursButton = new InlineKeyboardButton();
        InlineKeyboardButton shelterSecurityContactButton = new InlineKeyboardButton();

        if (isACat) {
            aboutShelterButton.setText("Информация о приюте для кошек");
            aboutShelterButton.setCallbackData("Информация о приюте для кошек");
            shelterWorkingHoursButton.setText("Расписание работы приюта для кошек");
            shelterWorkingHoursButton.setCallbackData("Расписание работы приюта для кошек");
            shelterSecurityContactButton.setText("Контакты охраны приюта для кошек");
            shelterSecurityContactButton.setCallbackData("Контакты охраны приюта для кошек");
        } else {
            aboutShelterButton.setText("Информация о приюте для собак");
            aboutShelterButton.setCallbackData("Информация о приюте для собак");
            shelterWorkingHoursButton.setText("Расписание приюта для собак");
            shelterWorkingHoursButton.setCallbackData("Расписание приюта для собак");
            shelterSecurityContactButton.setText("Контакты охраны приюта для собак");
            shelterSecurityContactButton.setCallbackData("Контакты охраны приюта для собак");
        }

        safetyRecommendationsButton.setCallbackData("Общие правила поведения");
        recordingContactsSelection.setCallbackData("Запись ваших контактов");
        toStart.setCallbackData("В начало");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = List.of(
                List.of(aboutShelterButton), List.of(shelterWorkingHoursButton),
                List.of(shelterSecurityContactButton),
                List.of(safetyRecommendationsButton), List.of(recordingContactsSelection), List.of(toStart)
        );
        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }
}
