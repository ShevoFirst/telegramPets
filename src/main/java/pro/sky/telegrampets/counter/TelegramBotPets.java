package pro.sky.telegrampets.counter;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.Buttons;
import pro.sky.telegrampets.components.ButtonsVolunteer;
import pro.sky.telegrampets.components.GetPetReportButton;
import pro.sky.telegrampets.config.TelegramBotConfiguration;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.repository.UserRepository;
import org.telegram.telegrambots.meta.api.objects.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.io.FileUtils.getFile;

/**
 * класс реагирущий на реакции бота через telegram api
 */
@Component
@EnableScheduling
public class TelegramBotPets extends TelegramLongPollingBot {
    //подключение конфигуратора к нашему боту
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final Buttons buttons;
    private final GetPetReportButton getPetReportButton;
    protected boolean isACatShelter;
    private boolean isWaitNumber = false;
    private final UserRepository userRepository;
    private final ButtonsVolunteer buttonsVolunteer;
    private String namePhotoId;
    private ReportRepository reportRepository;
    int sendMessageReport; // сохраняем текстовую часть репорта для парсинга ID


    private boolean photoCheckButton = false; // флаг на проверку нажатия кнопки
    private boolean reportCheckButton = false; // флаг на проверку нажатия кнопки

    public TelegramBotPets(TelegramBotConfiguration telegramBotConfiguration, ReportRepository reportRepository, Buttons buttons, GetPetReportButton getPetReportButton, UserRepository userRepository, ButtonsVolunteer buttonsVolunteer) {
        this.telegramBotConfiguration = telegramBotConfiguration;
        this.buttons = buttons;
        this.getPetReportButton = getPetReportButton;
        this.userRepository = userRepository;
        this.buttonsVolunteer = buttonsVolunteer;
        this.reportRepository = reportRepository;
    }


    /**
     * получение имени бота
     */
    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getBotName();
    }

    /**
     * получение токена бота
     */
    @Override
    public String getBotToken() {
        return telegramBotConfiguration.getToken();
    }

    /**
     * Метод получащий все события внутри бота
     *
     * @param update Update received
     */
    @Override
    public void onUpdateReceived(Update update) {

        //в случае если пользователь введет команду "/start" выведутся кнопки 1ого уровня
        if (isStartCommand(update)) {
            long chatId = update.getMessage().getChatId();
            startSelection(chatId, update);
        } else if (update.hasCallbackQuery()) { //проверка на нажатие кнопки
            String callbackData = update.getCallbackQuery().getData();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                //Cтартовый блок
                case "Кошка" -> catSelection(messageId, chatId);
                case "Собака" -> dogSelection(messageId, chatId);

                //блок второго уровня
                case "Как взять животное из приюта?" -> takeAnimalSelection(messageId, chatId);
                case "Информация о приюте" -> shelterInformationSelection(messageId, chatId);
                case "Позвать волонтера" -> callAVolunteer(update);
                case "Прислать отчет о питомце" -> petReportSelection(messageId, chatId);
                case "В начало" -> buttonToStart(messageId, chatId);

                //блок “Прислать отчет о питомце”
                case "Форма ежедневного отчета" -> {
                    takeDailyReportFormPhoto(messageId, chatId, update);
                    photoCheckButton = true; // Устанавливаем флаг в true после нажатия кнопки
                    reportCheckButton = true; // Устанавливаем флаг в true после нажатия кнопки
                }

                //Блок "Информация о приюте"
                case "Информация о приюте для кошек" -> aboutCatShelterSelection(messageId, chatId);
                case "Расписание работы приюта для кошек" -> catShelterWorkingHoursSelection(messageId, chatId);
                case "Контакты охраны приюта для кошек" -> catShelterSecurityContactSelection(messageId, chatId);
                case "Информация о приюте для собак" -> aboutDogShelterSelection(messageId, chatId);
                case "Расписание работы приюта для собак" -> dogShelterWorkingHoursSelection(messageId, chatId);
                case "Контакты охраны приюта для собак" -> dogShelterSecurityContactSelection(messageId, chatId);
                case "Общие правила поведения" -> safetyRecommendationsSelection(messageId, chatId);
                case "Запись ваших контактов", "Запись контактов" -> recordingContactsSelection(messageId, chatId);

                //блок “Как взять животное из приюта”
                case "Правила знакомства" -> datingRulesSelection(messageId, chatId);
                case "Список документов" -> documentsSelection(messageId, chatId);
                case "Рекомендации по транспортировке" -> transportationSelection(messageId, chatId);
                case "Обустройство котенка" -> KittenArrangementSelection(messageId, chatId);
                case "Обустройство щенка" -> puppyArrangementSelection(messageId, chatId);
                case "Обустройство для взрослого кота" -> arrangementAdultSelectionCat(messageId, chatId);
                case "Обустройство для взрослой собаки" -> arrangementAdultSelectionDog(messageId, chatId);
                case "Обустройство для ограниченного" -> arrangementLimitedSelection(messageId, chatId);
                case "Cписок причин" -> listReasonsSelection(messageId, chatId);

                //блок Волонтера
                case "Отчеты" -> reviewListOfReports(update.getCallbackQuery().getMessage().getChatId());
                case "ОТЧЕТ СДАН" -> {
                    reportSubmitted(update);
                    reviewListOfReports(update.getCallbackQuery().getMessage().getChatId());
                }
            }

        }
        if (photoCheckButton) { // Проверяем флаг перед выполнением checkDailyReport(update) и проверяеем, что пользователь прислал фото
            if (update.hasMessage()) {
                PhotoSize photoSize = getPhoto(update);
                File file = null;
                file = downloadPhoto(photoSize.getFileId());
                savePhotoToLocalFolder(file, update);
                checkDailyReportPhoto(update);
                photoCheckButton = false;
            }
        }
        if (reportCheckButton) { // Проверяем флаг перед выполнением checkDailyReport(update) и проверяеем, что пользователь прислал текст отчета
            if (update.getMessage().hasText()) {
                checkDailyReportMessage(update);
                reportCheckButton = false;
            }
        }

        Pattern pattern = Pattern.compile("^(\\+7)([0-9]{10})$");
        if (update.hasMessage() && isWaitNumber && pattern.matcher(update.getMessage().getText()).matches()) {
            getPetReportButton.saveUser(update, false);
            userRepository.updateNumber(update.getMessage().getChatId().intValue(), update.getMessage().getText());

            executeSendMessage(new SendMessage(update.getMessage().getChatId().toString(), "номер записан вам обязательно позвонят"));
            isWaitNumber = false;
        } else if (update.hasMessage() && isWaitNumber && !pattern.matcher(update.getMessage().getText()).matches()) {
            executeSendMessage(new SendMessage(update.getMessage().getChatId().toString(), "не правильно набран номер повторите ещё раз"));
        }

        if (update.getMessage().getText().equals("volonter")) {
            sendButtonVolonter(update.getMessage().getChatId());
        }

    }

    //просмотр отчетов питомцев
    private void reviewListOfReports(long chatId) {
        System.out.println(sendMessageReport);
        sendMessageReport = buttonsVolunteer.parseReportNumber(buttonsVolunteer.reviewListOfReports(chatId).getText()); //Сохроняем ID отчета
        try {
            execute(buttonsVolunteer.reviewListOfReports(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //Если отчет сдан
    private void reportSubmitted(Update update) {
        changeMessage(update.getCallbackQuery().getMessage().getChatId(), "Отчет сдан");
        System.out.println(sendMessageReport);
        buttonsVolunteer.reportSubmitted((long) sendMessageReport);
    }

    //Провека на нажатия /start
    private boolean isStartCommand(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start");
    }

    private void sendButtonVolonter(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(buttonsVolunteer.buttonVolunteer());
        sendMessage.setText("Волонтерская панель");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    //проверка что пользователь прислал фото для отчета и сохранеяем в БД
    private void checkDailyReportPhoto(Update update) {
        long chatId = update.getMessage().getChatId();
        executeSendMessage(getPetReportButton.dailyReportCheckPhoto(chatId, update));
    }

    //проверка что пользователь прислал текстовую часть отчета и сохроняем в БД
    private void checkDailyReportMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        executeSendMessage(getPetReportButton.dailyReportCheckMessage(chatId, update, namePhotoId));
    }

    //вызов кннопки о просьбе "Прислать фото питомца"
    private void takeDailyReportFormPhoto(long messageId, long chatId, Update update) {
        SendMessage sendMessage = getPetReportButton.sendMessageDailyReportPhoto(chatId);
        executeSendMessage(sendMessage);
    }

    //метод кнопки "Как взять животное из приюта?"
    private void takeAnimalSelection(int messageId, long chatId) {
        String messageText = "Выберите, что вас интересует";
        InlineKeyboardMarkup catsButtons = buttons.takeAnimalButton(isACatShelter);
        changeMessage(messageId, chatId, messageText, catsButtons);
    }


    //метод кнопки "Информация о приюте"
    private void shelterInformationSelection(int messageId, long chatId) {
        String messageText = "Здравствуйте, дорогой пользователь.";
        InlineKeyboardMarkup shelterButtons = buttons.shelterInformationButton(isACatShelter);
        changeMessage(messageId, chatId, messageText, shelterButtons);
    }

    private void startSelection(long chatId, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(new Buttons().selectionAnimalButtons());
        sendMessage.setText("Привет! " + update.getMessage().getFrom().getFirstName() + " Выберите приют который Вас интересует:");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void buttonToStart(int messageId, long chatId) {
        String messageText = " Выберите приют который Вас интересует:";
        changeMessage(messageId, chatId, messageText, new Buttons().selectionAnimalButtons());
        isWaitNumber = false;
    }

    private void catSelection(int messageId, long chatId) {
        isACatShelter = true;
        String messageText = "Вы выбрали приют для кошек";
        InlineKeyboardMarkup catsButtons = buttons.secondLayerButtons();
        changeMessage(messageId, chatId, messageText, catsButtons);
    }

    private void dogSelection(int messageId, long chatId) {
        isACatShelter = false;
        InlineKeyboardMarkup dogButtons = buttons.secondLayerButtons();
        changeMessage(messageId, chatId, "Вы выбрали собачий приют", dogButtons);
    }

    //метод кнопки "Прислать отчет о питомце"
    private void petReportSelection(int messageId, long chatId) {
        InlineKeyboardMarkup reportButtons = getPetReportButton.sendMessageReportFromPet();
        changeMessage(messageId, chatId, "Выберите одну из кнопок", reportButtons);
    }


    private void handleInvalidCommand(long chatId) {
        SendMessage messageText = new SendMessage();
        messageText.setChatId(chatId);
        messageText.setText("не правильная команда");
        executeSendMessage(messageText);
    }

    private void executeSendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод для создания/изменения сообщения
     */
    private void changeMessage(int messageId, long chatIdInButton, String messageText, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatIdInButton));
        editMessageText.setText(messageText);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(keyboardMarkup);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeMessage(long chatIdInButton, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatIdInButton));
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeMessage(long chatIdInButton, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatIdInButton));
        sendMessage.setText(messageText);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Реализация кнопки "Позвать волонтера"
     * в List chatIdVolunteer добавляются chatId волонтеров, котормым рассылаются сообщения
     */
    public void callAVolunteer(Update update) {
        List<Long> chatIdVolunteer = List.of(931733272L, 590317122L);
        for (Long chat : chatIdVolunteer) {
            String user = update.getCallbackQuery().getFrom().getUserName();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chat);
            sendMessage.setText("Пользователь: @" + user + " просит с ним связаться.");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Получаеем объект File содержащий информацию о файле по его индефикатору.
     */
    private File downloadPhoto(String fileId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        try {
            return execute(getFile);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Скачиваем файл, генерируем уникальное имя для него,
     * перемещаем в целевую директорию и возвращаем путь к сохраненному файлу
     */
    private Path savePhotoToLocalFolder(File file, Update update) {
        PhotoSize photoSize = getPhoto(update);
        String filePath = file.getFilePath();
        java.io.File downloadedFile = null;
        try {
            downloadedFile = downloadFile(filePath);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        // Генерируем уникальное имя файла с сохранением расширения
        namePhotoId = photoSize.getFileId() + "." + "jpg";
        Path targetPath = Path.of("C:\\photoTG", namePhotoId);
        getPetReportButton.saveUser(update, true);
        getPetReportButton.saveReportPhotoId(update, namePhotoId);
        try {
            Files.move(downloadedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return targetPath;
    }

    /**
     * Извлекает из update список объектов PhotoSize, которые представляют разный размер фотографий
     * Через стрим ищет самую большую фотографию и возвращает её.
     */
    public PhotoSize getPhoto(Update update) {
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            List<PhotoSize> photos = update.getMessage().getPhoto();
            return photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);

        }
        return null;
    }

    //   Здесь можно реализовать логику для расширения файла
    private String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            return filePath.substring(dotIndex + 1);
        }
        return "jpg";
    }

    //   Здесь можно реализовать логику для генерации уникального имени файла
    private String generateUniqueFileName() {
        return "unique_filename";
    }

    private void aboutCatShelterSelection(int messageId, long chatId) {
        String messageText = """
                Мы стремимся обеспечить каждую кошку в нашем приюте ласковым убежищем, где они могут чувствовать себя в безопасности и защищенности.
                Наши просторные помещения созданы таким образом, чтобы кошки могли свободно перемещаться, играть и исследовать окружающую среду.\s
                Наша команда состоит из опытных сотрудников и добровольцев, которые уделяют каждой кошке индивидуальное внимание и заботу. Мы предлагаем регулярные медицинские осмотры, вакцинации и стерилизацию, чтобы убедиться, что все кошки живут в полном здоровье и благополучии.
                Наш приют для кошек также является активным членом местного сообщества. Мы проводим информационные мероприятия, образовательные программы и совместные акции, чтобы привлекать внимание к проблеме бездомности кошек и найти решения.
                Мы находимся по адресу: г.Москва, ул. Ленина, стр.17.

                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void aboutDogShelterSelection(int messageId, long chatId) {
        String messageText = """
                Наш приют для собак - это удивительное место, где каждый пушистый друг может найти свой дом и получить заботу, любовь и безопасность, которые он заслуживает.
                Открыв свои двери в 2023 году, наш приют уже успел стать оазисом для бездомных собак и их временным приютом.
                У нас работает дружелюбный и преданный персонал, состоящий из опытных ветеринаров, тренеров и волонтеров, которые уделяют особое внимание заботе о наших собаках. Мы предлагаем им полноценное питание, погружение в игры и развлечения, а также регулярные медицинские осмотры и ветеринарную помощь.
                Мы находимся по адресу: г.Москва, ул. Пушкина, стр.10 (вход со стороны магазина "Атлантида").
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void catShelterWorkingHoursSelection(int messageId, long chatId) {
        String messageText = """
                Мы работаем с 7:00 и до 23:00 ежедневно.
                В праздничные и предпраздничные дни график работы может меняться, для уточнения данных времени приема - просьба обратиться по контактам для связи с охраной приюта
                """;
        System.out.println(77);
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void dogShelterWorkingHoursSelection(int messageId, long chatId) {
        String messageText = """
                Мы работаем с 8:30 и до 22:00 ежедневно.
                В праздничные и предпраздничные дни график работы может меняться, для уточнения данных времени приема - просьба обратиться по контактам для связи с охраной приюта
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void catShelterSecurityContactSelection(int messageId, long chatId) {
        String messageText = """
                Уважаемые посетители нашего прекрасного приюта, убедительная просьба перед посещением связаться с сотрудниками охраны,так как у нас установлен пропускной режим
                Телефон для связи с постом охраны: 8-800-888-88-88.
                Просьба звонить заранее, в часы работы приюта.
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void dogShelterSecurityContactSelection(int messageId, long chatId) {
        String messageText = """
                Уважаемые посетители нашего прекрасного приюта, убедительная просьба перед посещением связаться с сотрудниками охраны,так как у нас установлен пропускной режим
                Телефон для связи с постом охраны: 8-777-666-55-44.
                Просьба звонить заранее, в часы работы приюта.
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void safetyRecommendationsSelection(int messageId, long chatId) {
        String messageText = """
                1. Уважайте животных: Обращайтесь с животными с добротой и состраданием. Используйте спокойный и мягкий тон голоса, чтобы не пугать их.
                2. Следуйте инструкциям персонала: Следуйте указаниям персонала приюта. Они знают особенности каждого животного и могут дать советы о безопасности и правильном обращении с ними.
                3. Не кормите животных без разрешения: Не предлагайте пищу животным без разрешения персонала. У них может быть специальная диета или определенные проблемы со здоровьем, и неправильная пища может причинить вред.
                4. Будьте аккуратны и осторожны: Поведение в приюте должно быть аккуратным и осторожным, чтобы не причинить вред животным или себе. Не шумите, не пугайте их и не оставляйте предметы, которые могут быть опасными.
                5. Не проникайте в запретные зоны приюта: Уважайте указанные запретные зоны, куда доступ запрещен. Некоторые животные могут быть в карантине или требовать специального ухода, поэтому важно соблюдать инструкции персонала.
                6. Поддерживайте чистоту: Следите за чистотой и гигиеной в приюте. Выбрасывайте мусор в специальные контейнеры, после общения с животными вымывайте руки.
                7. Предоставляйте информацию: Если замечаете какие-либо изменения в поведении или состоянии животных, сообщите об этом персоналу приюта. Ваши наблюдения могут быть важными для здоровья и ухода за животными.
                """;
        System.out.println(43);
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void arrangementAdultSelectionDog(int messageId, long chatId) {
        String messageText = """
                1. Место для сна, такое как кровать или мягкий матрас.
                2. Миски для еды и воды, предпочтительно из нержавеющей стали или керамики.
                3. Корм для взрослой собаки, соответствующий ее возрасту, размеру и потребностям.
                4. Игрушки для игр и развлечения, включая интерактивные игры и игрушки для охоты.
                5. Когтеточка или когтетренировочный материал, чтобы предотвратить повреждение мебели.
                6. Лоток для учения собаки делать свои нужды на определенном месте, если она живет в квартире.
                7. Шлейка и поводок для прогулок и тренировок, если собака привыкла к ним.
                8. Щетка для груминга и ухода за шерстью, чтобы предотвратить образование комков и перхоти.
                9. Дезинфицирующее средство для очистки мест, где собака делает свои нужды, а также для очистки поверхностей и предметов, которые собака может затронуть.
                """;
        System.out.println(43);
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void recordingContactsSelection(int messageId, long chatId) {
        String messageText = "Введите свой номер телефона в формате +71112223344";
        isWaitNumber = true;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void listReasonsSelection(int messageId, long chatId) {
        String messageText = """
                1. Несоответствие потенциального владельца требованиям приюта по уходу за животным.
                2. Недостаточный опыт и знания в уходе за собакой.
                3. Невозможность предоставить достаточное пространство для жизни собаки.
                4. Неспособность обеспечить ежедневные прогулки и физическую активность для собаки.
                5. Несоответствие возраста, размера и характера собаки потенциальному владельцу.
                6. Наличие других животных в доме, которые могут не совместимы с собакой.
                7. Непредоставление всех необходимых документов и разрешений для содержания собаки.
                8. Недостаточный доход или финансовые возможности для обеспечения здоровья и ухода за собакой.
                9. История жестокого обращения с животными или нарушений законодательства в отношении животных.
                10. Негативный результат при проверке на предмет аллергии к собакам у потенциального владельца или членов его семьи.
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void arrangementLimitedSelection(int messageId, long chatId) {
        String messageText = """
                1. Уютное место для сна, такое как кровать или мягкий матрас.
                2. Миски для еды и воды, предпочтительно из нержавеющей стали или керамики.
                3. Корм для взрослого питомца, соответствующий его возрасту, размеру и потребностям.
                4. Игрушки для игр и развлечения, включая интерактивные игры и игрушки для охоты.
                6. Лоток для кошек или лоток для учения собаки делать свои нужды на определенном месте, если она живет в квартире.
                7. Шлейка и поводок для прогулок и тренировок, если питомец привык к ним.
                8. Щетка для груминга и ухода за шерстью, чтобы предотвратить образование комков и перхоти.
                9. Дезинфицирующее средство для очистки мест, где питомец делает свои нужды, а также для очистки поверхностей и предметов, которые они могут затронуть.
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void arrangementAdultSelectionCat(int messageId, long chatId) {
        String messageText = """
                1. Место для сна, такое как кровать или мягкий матрас.
                2. Миски для еды и воды, предпочтительно из нержавеющей стали или керамики.
                3. Корм для взрослого кота, соответствующий его возрасту, размеру и потребностям.
                4. Игрушки для игр и развлечения, включая интерактивные игры и игрушки для охоты.
                5. Когтеточка или когтетренировочный материал, чтобы предотвратить повреждение мебели.
                6. Лоток для учения кота делать свои нужды на определенном месте, предпочтительно с запахоней или без запаха.
                7. Шлейка и поводок для прогулок и тренировок, если кот привык к ним.
                8. Щетка для груминга и ухода за шерстью, чтобы предотвратить образование комков и перхоти.
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void transportationSelection(int messageId, long chatId) {
        String messageText = """
                1. Клетка или переноска для транспортировки животного, соответствующая его размеру и весу.
                2. Постельное белье для клетки или переноски.
                3. Еда и вода на время транспортировки.
                4. Игрушки или другие предметы, которые помогут животному чувствовать себя комфортно во время перевозки.""";
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void documentsSelection(int messageId, long chatId) {
        String messageText = """
                1. Заявление на усыновление животного.
                2. Документ, удостоверяющий личность (паспорт или другой документ, содержащий фотографию и подпись).
                3. Документ, подтверждающий наличие разрешения на содержание животного в доме или квартире (в зависимости от законодательства страны).
                4. Документы, подтверждающие финансовую способность обеспечить животное достойными условиями жизни (например, выписки из банковских счетов или другие документы, подтверждающие стабильный доход).
                5. Документы, подтверждающие наличие опыта в уходе за животными (если это требуется при усыновлении конкретной породы).
                6. Документы, подтверждающие прохождение курсов обучения по уходу за животными (если это требуется при усыновлении конкретной породы).
                7. Документы, подтверждающие наличие места для проживания животного (например, договор аренды жилья или собственности).
                """;
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void datingRulesSelection(int messageId, long chatId) {
        String messageText = """
                1. Изучите информацию о животном: возраст, порода, характер, здоровье и особенности ухода.

                2. Проведите время с животным в приюте, чтобы понять, как он ведет себя в различных ситуациях.

                3. Обсудите с работниками приюта все вопросы, связанные с уходом за животным, его здоровьем и поведением.

                4. Убедитесь, что вы готовы к финансовым затратам на уход за животным и можете обеспечить ему достойные условия жизни.

                5. Подготовьте дом к прибытию нового члена семьи: оборудуйте место для сна и отдыха, купите необходимые принадлежности и игрушки.

                6. Соблюдайте все требования приюта по уходу за животным после его забора.\s

                7. Не забывайте о регулярных посещениях ветеринара и своевременном проведении всех необходимых процедур по уходу за животным.\s

                8. Относитесь к животному с любовью и заботой, чтобы он чувствовал себя дома и был счастлив.""";
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void puppyArrangementSelection(int messageId, long chatId) {
        String messageText = """
                1. Место для сна, такое как кровать или корзина.
                2. Миски для еды и воды.
                3. Корм для щенка, соответствующий его возрасту и размеру.
                4. Игрушки для жевания и развлечения.
                5. Подстилка или пеленки для учения щенка делать свои нужды на определенном месте.
                6. Шлейка и поводок для прогулок и тренировок.
                7. Туалет для щенка, если вы планируете держать его внутри дома.
                8. Щетка для груминга и ухода за шерстью.
                9. Дезинфицирующее средство для очистки мест, где щенок делает свои нужды.
                10. Контактные данные ветеринарного врача или клиники, которые можно обратиться в случае необходимости.""";
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }

    private void KittenArrangementSelection(int messageId, long chatId) {
        String messageText = """
                1. Место для сна, такое как кошачий домик или мягкая подушка.
                2. Миски для еды и воды.
                3. Корм для котенка, соответствующий его возрасту и размеру.
                4. Игрушки для игр и развлечения.
                5. Когтеточка или когтетренировочный материал.
                6. Подстилка или лоток для учения котенка делать свои нужды на определенном месте.
                7. Шлейка и поводок для прогулок и тренировок.
                8. Щетка для груминга и ухода за шерстью.
                9. Дезинфицирующее средство для очистки мест, где котенок делает свои нужды.
                10. Контактные данные ветеринарного врача или клиники, которые можно обратиться в случае необходимости.""";
        InlineKeyboardButton toStartButton = new InlineKeyboardButton("В начало");
        toStartButton.setCallbackData("В начало");
        changeMessage(messageId, chatId, messageText, new InlineKeyboardMarkup(List.of(List.of(toStartButton))));
    }
}