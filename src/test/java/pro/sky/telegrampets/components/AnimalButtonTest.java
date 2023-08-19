package pro.sky.telegrampets.components;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

    public class AnimalButtonTest {

        @Test
        public void testTakeAnimalButtonCat() {
            Buttons button = new Buttons();
            InlineKeyboardMarkup markup = button.takeAnimalButton(true);
            assertNotNull(markup);
            assertEquals(10, markup.getKeyboard().size());
            assertEquals("Правила знакомства", markup.getKeyboard().get(0).get(0).getText());
            assertEquals("Список документов", markup.getKeyboard().get(1).get(0).getText());
            assertEquals("Рекомендации по транспортировке животных", markup.getKeyboard().get(2).get(0).getText());
            assertEquals("Обустройство для котенка", markup.getKeyboard().get(3).get(0).getText());
            assertEquals("Обустройство для взрослого кота", markup.getKeyboard().get(4).get(0).getText());
            assertEquals("Обустройство для животного\n с ограниченными возможностями", markup.getKeyboard().get(5).get(0).getText());
            assertEquals("Cписок причин, почему могут отказать", markup.getKeyboard().get(6).get(0).getText());
            assertEquals("Запись ваших контактов", markup.getKeyboard().get(7).get(0).getText());
            assertEquals("Позвать волонтера", markup.getKeyboard().get(8).get(0).getText());
            assertEquals("В начало", markup.getKeyboard().get(9).get(0).getText());
        }
        @Test
        public void testSecondLayerButtons() {
            Buttons buttons = new Buttons();
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            InlineKeyboardButton checkInfoButton = new InlineKeyboardButton("Информация о приюте");
            checkInfoButton.setCallbackData("Информация о приюте");
            InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
            callVolunteerButton.setCallbackData("Позвать волонтера");
            InlineKeyboardButton getReportAboutPet = new InlineKeyboardButton("Прислать отчет о питомце");
            getReportAboutPet.setCallbackData("Прислать отчет о питомце");
            InlineKeyboardButton howGetPet = new InlineKeyboardButton("Как взять животное из приюта?");
            howGetPet.setCallbackData("Как взять животное из приюта?");
            InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
            toStart.setCallbackData("В начало");

            keyboardMarkup.setKeyboard(List.of(
                    List.of(checkInfoButton),
                    List.of(callVolunteerButton),
                    List.of(getReportAboutPet),
                    List.of(howGetPet),
                    List.of(toStart)
            ));

            assertEquals(keyboardMarkup, buttons.secondLayerButtons());
        }
        @Test
        public void testTakeAnimalButtonDog() {
            Buttons button = new Buttons();
            InlineKeyboardMarkup markup = button.takeAnimalButton(false);
            assertNotNull(markup);
            assertEquals(10, markup.getKeyboard().size());
            assertEquals("Правила знакомства", markup.getKeyboard().get(0).get(0).getText());
            assertEquals("Список документов", markup.getKeyboard().get(1).get(0).getText());
            assertEquals("Рекомендации по транспортировке животных", markup.getKeyboard().get(2).get(0).getText());
            assertEquals("Обустройство для щенка", markup.getKeyboard().get(3).get(0).getText());
            assertEquals("Обустройство для взрослой собаки", markup.getKeyboard().get(4).get(0).getText());
            assertEquals("Обустройство для животного\n с ограниченными возможностями", markup.getKeyboard().get(5).get(0).getText());
            assertEquals("Cписок причин, почему могут отказать", markup.getKeyboard().get(6).get(0).getText());
            assertEquals("Запись ваших контактов", markup.getKeyboard().get(7).get(0).getText());
            assertEquals("Позвать волонтера", markup.getKeyboard().get(8).get(0).getText());
            assertEquals("В начало", markup.getKeyboard().get(9).get(0).getText());
        }
        @Test
        public void testSelectionAnimalButtons() {
            Buttons button = new Buttons();
            InlineKeyboardMarkup markup = button.selectionAnimalButtons();
            assertNotNull(markup);
            assertEquals(1, markup.getKeyboard().size());
            List<InlineKeyboardButton> keyboardButtons = markup.getKeyboard().get(0);
            assertEquals(2, keyboardButtons.size());
            assertEquals("Приют Кошек \uD83D\uDC08", keyboardButtons.get(0).getText());
            assertEquals("Кошка", keyboardButtons.get(0).getCallbackData());
            assertEquals("Приют Собак \uD83E\uDDAE", keyboardButtons.get(1).getText());
            assertEquals("Собака", keyboardButtons.get(1).getCallbackData());
        }
        @Test
        public void testShelterInformationButtonForCat() {
            Buttons buttons = new Buttons();
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton("Информация о приюте для кошек");
            aboutShelterButton.setCallbackData("Информация о приюте для кошек");
            InlineKeyboardButton shelterWorkingHoursButton = new InlineKeyboardButton("Расписание работы приюта для кошек");
            shelterWorkingHoursButton.setCallbackData("Расписание работы приюта для кошек");
            InlineKeyboardButton shelterSecurityContactButton = new InlineKeyboardButton("Контакты охраны приюта для кошек");
            shelterSecurityContactButton.setCallbackData("Контакты охраны приюта для кошек");
            InlineKeyboardButton safetyRecommendationsButton = new InlineKeyboardButton("Общие правила поведения");
            safetyRecommendationsButton.setCallbackData("Общие правила поведения");
            InlineKeyboardButton recordingContactsSelection = new InlineKeyboardButton("Запись ваших контактов");
            recordingContactsSelection.setCallbackData("Запись ваших контактов");
            InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
            toStart.setCallbackData("В начало");

            keyboardMarkup.setKeyboard(List.of(
                    List.of(aboutShelterButton),
                    List.of(shelterWorkingHoursButton),
                    List.of(shelterSecurityContactButton),
                    List.of(safetyRecommendationsButton),
                    List.of(recordingContactsSelection),
                    List.of(toStart)
            ));

            assertEquals(keyboardMarkup, buttons.shelterInformationButton(true));
        }

        @Test
        public void testShelterInformationButtonForDog() {
            Buttons buttons = new Buttons();
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton("Информация о приюте для собак");
            aboutShelterButton.setCallbackData("Информация о приюте для собак");
            InlineKeyboardButton shelterWorkingHoursButton = new InlineKeyboardButton("Расписание приюта для собак");
            shelterWorkingHoursButton.setCallbackData("Расписание приюта для собак");
            InlineKeyboardButton shelterSecurityContactButton = new InlineKeyboardButton("Контакты охраны приюта для собак");
            shelterSecurityContactButton.setCallbackData("Контакты охраны приюта для собак");
            InlineKeyboardButton safetyRecommendationsButton = new InlineKeyboardButton("Общие правила поведения");
            safetyRecommendationsButton.setCallbackData("Общие правила поведения");
            InlineKeyboardButton recordingContactsSelection = new InlineKeyboardButton("Запись ваших контактов");
            recordingContactsSelection.setCallbackData("Запись ваших контактов");
            InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
            toStart.setCallbackData("В начало");

            keyboardMarkup.setKeyboard(List.of(
                    List.of(aboutShelterButton),
                    List.of(shelterWorkingHoursButton),
                    List.of(shelterSecurityContactButton),
                    List.of(safetyRecommendationsButton),
                    List.of(recordingContactsSelection),
                    List.of(toStart)
            ));
            assertEquals(keyboardMarkup, buttons.shelterInformationButton(false));
        }
    }
