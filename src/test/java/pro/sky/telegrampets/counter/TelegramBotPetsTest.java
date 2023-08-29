package pro.sky.telegrampets.counter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import liquibase.pro.packaged.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatJoinRequest;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.ChatShared;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Dice;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageAutoDeleteTimerChanged;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.ProximityAlertTriggered;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.UserShared;
import org.telegram.telegrambots.meta.api.objects.Venue;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.WriteAccessAllowed;
import org.telegram.telegrambots.meta.api.objects.forum.ForumTopicClosed;
import org.telegram.telegrambots.meta.api.objects.forum.ForumTopicCreated;
import org.telegram.telegrambots.meta.api.objects.forum.ForumTopicEdited;
import org.telegram.telegrambots.meta.api.objects.forum.ForumTopicReopened;
import org.telegram.telegrambots.meta.api.objects.forum.GeneralForumTopicHidden;
import org.telegram.telegrambots.meta.api.objects.forum.GeneralForumTopicUnhidden;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.games.Game;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.passport.PassportData;
import org.telegram.telegrambots.meta.api.objects.payments.Invoice;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatEnded;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatParticipantsInvited;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatScheduled;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatStarted;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.Buttons;
import pro.sky.telegrampets.components.ButtonsVolunteer;
import pro.sky.telegrampets.components.GetPetReportButton;
import pro.sky.telegrampets.config.TelegramBotConfiguration;
import pro.sky.telegrampets.model.Volunteer;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.repository.UserRepository;
import pro.sky.telegrampets.repository.VolunteerRepository;

@ContextConfiguration(classes = {TelegramBotPets.class, TelegramBotConfiguration.class})
@ExtendWith(SpringExtension.class)
class TelegramBotPetsTest {


    private TelegramBotConfiguration telegramBotConfiguration1;
    private Buttons buttons1;
    private GetPetReportButton getPetReportButton1;
    private UserRepository userRepository1;
    private ButtonsVolunteer buttonsVolunteer1;
    private ReportRepository reportRepository1;
    private VolunteerRepository volunteerRepository1;

    private FakeTelegramBotPets telegramBot;

    @BeforeEach
    public void setUp() {
         telegramBot = spy(new FakeTelegramBotPets(telegramBotConfiguration1 = mock(TelegramBotConfiguration.class),
                reportRepository1 = mock(ReportRepository.class),
                buttons1 = mock(Buttons.class),
                getPetReportButton1 = mock(GetPetReportButton.class),
                userRepository1 = mock(UserRepository.class),
                buttonsVolunteer1 = mock(ButtonsVolunteer.class),
                volunteerRepository1 = mock(VolunteerRepository.class)));
    }


    @MockBean
    private Buttons buttons;

    @MockBean
    private ButtonsVolunteer buttonsVolunteer;

    @MockBean
    private GetPetReportButton getPetReportButton;

    @MockBean
    private ReportRepository reportRepository;

    @Autowired
    private TelegramBotPets telegramBotPets;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VolunteerRepository volunteerRepository;

    /**
     * Method under test: {@link TelegramBotPets#getBotUsername()}
     */
    @Test
    void testGetBotUsername() {
        assertEquals("${telegram.bot.name}", telegramBotPets.getBotUsername());
    }

    /**
     * Method under test: {@link TelegramBotPets#getBotToken()}
     */
    @Test
    void testGetBotToken() {
        assertEquals("${telegram.bot.token}", telegramBotPets.getBotToken());
    }

    /**
     * Method under test: {@link TelegramBotPets#onUpdateReceived(Update)}
     */
    @Test
    void testOnUpdateReceived1() {
        Update update = new Update();
        telegramBotPets.onUpdateReceived(update);
        assertFalse(update.hasShippingQuery());
        assertFalse(update.hasPreCheckoutQuery());
        assertFalse(update.hasPollAnswer());
        assertFalse(update.hasPoll());
        assertFalse(update.hasMyChatMember());
        assertFalse(update.hasMessage());
        assertFalse(update.hasInlineQuery());
        assertFalse(update.hasEditedMessage());
        assertFalse(update.hasEditedChannelPost());
        assertFalse(update.hasChosenInlineQuery());
        assertFalse(update.hasChatMember());
        assertFalse(update.hasChatJoinRequest());
        assertFalse(update.hasChannelPost());
        assertEquals(0, telegramBotPets.sendMessageReport);
        assertFalse(telegramBotPets.isACatShelter);
    }

    /**
     * Method under test: {@link TelegramBotPets#onUpdateReceived(Update)}
     */
    @Test
    void testOnUpdateReceived2() {
        Message message = mock(Message.class);
        when(message.getText()).thenThrow(new RuntimeException("/start"));
        when(message.hasText()).thenReturn(true);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        assertThrows(RuntimeException.class, () -> telegramBotPets.onUpdateReceived(update));
        verify(update).hasMessage();
        verify(update, atLeast(1)).getMessage();
        verify(message).hasText();
        verify(message).getText();
    }

    /**
     * Method under test: {@link TelegramBotPets#isStartCommand(Update)}
     */
    @Test
    void testIsStartCommand() {
        assertFalse(telegramBotPets.isStartCommand(new Update()));
    }

    /**
     * Method under test: {@link TelegramBotPets#isStartCommand(Update)}
     */
    @Test
    void testIsStartCommand1() {
        Message message = new Message();
        InlineQuery.InlineQueryBuilder chatTypeResult = InlineQuery.builder().chatType("Chat Type");
        InlineQuery.InlineQueryBuilder idResult = chatTypeResult.from(new User()).id("42");
        InlineQuery inlineQuery = idResult.location(new Location()).offset("Offset").query("Query").build();
        ChosenInlineQuery.ChosenInlineQueryBuilder builderResult = ChosenInlineQuery.builder();
        ChosenInlineQuery.ChosenInlineQueryBuilder inlineMessageIdResult = builderResult.from(new User())
                .inlineMessageId("42");
        ChosenInlineQuery chosenInlineQuery = inlineMessageIdResult.location(new Location())
                .query("Query")
                .resultId("42")
                .build();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message editedMessage = new Message();
        Message channelPost = new Message();
        Message editedChannelPost = new Message();
        ShippingQuery shippingQuery = new ShippingQuery();
        PreCheckoutQuery preCheckoutQuery = new PreCheckoutQuery();
        Poll poll = new Poll();
        PollAnswer pollAnswer = new PollAnswer();
        ChatMemberUpdated myChatMember = new ChatMemberUpdated();
        ChatMemberUpdated chatMember = new ChatMemberUpdated();
        assertFalse(telegramBotPets.isStartCommand(new Update(1, message, inlineQuery, chosenInlineQuery, callbackQuery,
                editedMessage, channelPost, editedChannelPost, shippingQuery, preCheckoutQuery, poll, pollAnswer,
                myChatMember, chatMember, new ChatJoinRequest())));
    }

    /**
     * Method under test: {@link TelegramBotPets#isStartCommand(Update)}
     */
    @Test
    void testIsStartCommand2() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(new Message());
        when(update.hasMessage()).thenReturn(true);
        assertFalse(telegramBotPets.isStartCommand(update));
        verify(update).hasMessage();
        verify(update).getMessage();
    }

    /**
     * Method under test: {@link TelegramBotPets#isStartCommand(Update)}
     */
    @Test
    void testIsStartCommand3() {
        Update update = mock(Update.class);
        User from = new User();
        Chat chat = new Chat();
        User forwardFrom = new User();
        Chat forwardFromChat = new Chat();
        ArrayList<MessageEntity> entities = new ArrayList<>();
        ArrayList<MessageEntity> captionEntities = new ArrayList<>();
        Audio audio = new Audio();
        Document document = new Document();
        ArrayList<PhotoSize> photo = new ArrayList<>();
        Sticker sticker = new Sticker();
        Video video = new Video();
        Contact contact = new Contact();
        Location location = new Location();
        Venue venue = new Venue();
        Animation animation = new Animation();
        Message pinnedMessage = new Message();
        ArrayList<User> newChatMembers = new ArrayList<>();
        User leftChatMember = new User();
        ArrayList<PhotoSize> newChatPhoto = new ArrayList<>();
        Message replyToMessage = new Message();
        Voice voice = new Voice();
        Game game = new Game();
        Invoice invoice = new Invoice();
        SuccessfulPayment successfulPayment = new SuccessfulPayment();
        VideoNote videoNote = new VideoNote();
        PassportData passportData = new PassportData();
        Poll poll = new Poll();
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        Dice dice = new Dice();
        User viaBot = new User();
        Chat senderChat = new Chat();
        ProximityAlertTriggered proximityAlertTriggered = new ProximityAlertTriggered();
        MessageAutoDeleteTimerChanged messageAutoDeleteTimerChanged = new MessageAutoDeleteTimerChanged();
        WebAppData webAppData = new WebAppData("Data", "Button Text");

        VideoChatStarted videoChatStarted = new VideoChatStarted();
        VideoChatEnded videoChatEnded = new VideoChatEnded();
        VideoChatParticipantsInvited videoChatParticipantsInvited = new VideoChatParticipantsInvited();
        VideoChatScheduled videoChatScheduled = new VideoChatScheduled();
        ForumTopicCreated forumTopicCreated = new ForumTopicCreated();
        ForumTopicClosed forumTopicClosed = new ForumTopicClosed();
        ForumTopicReopened forumTopicReopened = new ForumTopicReopened();
        ForumTopicEdited forumTopicEdited = new ForumTopicEdited("Name", "42");

        GeneralForumTopicHidden generalForumTopicHidden = new GeneralForumTopicHidden();
        GeneralForumTopicUnhidden generalForumTopicUnhidden = new GeneralForumTopicUnhidden();
        WriteAccessAllowed writeAccessAllowed = new WriteAccessAllowed("Web App Name");
        UserShared userShared = new UserShared();
        when(update.getMessage()).thenReturn(new Message(1, 1, from, 1, chat, forwardFrom, forwardFromChat, 1, "Text",
                entities, captionEntities, audio, document, photo, sticker, video, contact, location, venue, animation,
                pinnedMessage, newChatMembers, leftChatMember, "Dr", newChatPhoto, true, true, replyToMessage, voice,
                "Caption", true, true, 1L, 1L, 1, game, 1, invoice, successfulPayment, videoNote, "JaneDoe",
                "Forward Signature", "42", "Connected Website", passportData, "Forward Sender Name", poll, replyMarkup, dice,
                viaBot, senderChat, proximityAlertTriggered, messageAutoDeleteTimerChanged, true, true, webAppData,
                videoChatStarted, videoChatEnded, videoChatParticipantsInvited, videoChatScheduled, true, forumTopicCreated,
                forumTopicClosed, forumTopicReopened, forumTopicEdited, generalForumTopicHidden, generalForumTopicUnhidden,
                writeAccessAllowed, true, userShared, new ChatShared()));
        when(update.hasMessage()).thenReturn(true);
        assertFalse(telegramBotPets.isStartCommand(update));
        verify(update).hasMessage();
        verify(update, atLeast(1)).getMessage();
    }

    /**
     * Method under test: {@link TelegramBotPets#isStartCommand(Update)}
     */
    @Test
    void testIsStartCommand4() {
        Message message = mock(Message.class);
        when(message.getText()).thenThrow(new RuntimeException("/start"));
        when(message.hasText()).thenReturn(true);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        assertThrows(RuntimeException.class, () -> telegramBotPets.isStartCommand(update));
        verify(update).hasMessage();
        verify(update, atLeast(1)).getMessage();
        verify(message).hasText();
        verify(message).getText();
    }

    /**
     * Method under test: {@link TelegramBotPets#changeMessage(long, String)}
     */
    @Test
    void testChangeMessage() {
        assertThrows(RuntimeException.class, () -> telegramBotPets.changeMessage(1L, ""));
    }

    @Test
    void testGetPhoto() {
        assertNull(telegramBotPets.getPhoto(new Update()));
    }

    /**
     * Method under test: {@link TelegramBotPets#getPhoto(Update)}
     */
    @Test
    void testGetPhoto1() {
        Message message = new Message();
        InlineQuery.InlineQueryBuilder chatTypeResult = InlineQuery.builder().chatType("Chat Type");
        InlineQuery.InlineQueryBuilder idResult = chatTypeResult.from(new User()).id("42");
        InlineQuery inlineQuery = idResult.location(new Location()).offset("Offset").query("Query").build();
        ChosenInlineQuery.ChosenInlineQueryBuilder builderResult = ChosenInlineQuery.builder();
        ChosenInlineQuery.ChosenInlineQueryBuilder inlineMessageIdResult = builderResult.from(new User())
                .inlineMessageId("42");
        ChosenInlineQuery chosenInlineQuery = inlineMessageIdResult.location(new Location())
                .query("Query")
                .resultId("42")
                .build();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message editedMessage = new Message();
        Message channelPost = new Message();
        Message editedChannelPost = new Message();
        ShippingQuery shippingQuery = new ShippingQuery();
        PreCheckoutQuery preCheckoutQuery = new PreCheckoutQuery();
        Poll poll = new Poll();
        PollAnswer pollAnswer = new PollAnswer();
        ChatMemberUpdated myChatMember = new ChatMemberUpdated();
        ChatMemberUpdated chatMember = new ChatMemberUpdated();
        assertNull(telegramBotPets.getPhoto(new Update(1, message, inlineQuery, chosenInlineQuery, callbackQuery,
                editedMessage, channelPost, editedChannelPost, shippingQuery, preCheckoutQuery, poll, pollAnswer,
                myChatMember, chatMember, new ChatJoinRequest())));
    }

    /**
     * Method under test: {@link TelegramBotPets#getPhoto(Update)}
     */
    @Test
    void testGetPhoto2() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(new Message());
        when(update.hasMessage()).thenReturn(true);
        assertNull(telegramBotPets.getPhoto(update));
        verify(update).hasMessage();
        verify(update).getMessage();
    }

    /**
     * Method under test: {@link TelegramBotPets#getPhoto(Update)}
     */
    @Test
    void testGetPhoto3() {
        Message message = mock(Message.class);
        when(message.getPhoto()).thenReturn(new ArrayList<>());
        when(message.hasPhoto()).thenReturn(true);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        assertNull(telegramBotPets.getPhoto(update));
        verify(update).hasMessage();
        verify(update, atLeast(1)).getMessage();
        verify(message).hasPhoto();
        verify(message).getPhoto();
    }

    /**
     * Method under test: {@link TelegramBotPets#getPhoto(Update)}
     */
    @Test
    void testGetPhoto4() {
        Message message = mock(Message.class);
        when(message.getPhoto()).thenThrow(new RuntimeException("foo"));
        when(message.hasPhoto()).thenReturn(true);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        assertThrows(RuntimeException.class, () -> telegramBotPets.getPhoto(update));
        verify(update).hasMessage();
        verify(update, atLeast(1)).getMessage();
        verify(message).hasPhoto();
        verify(message).getPhoto();
    }

    @Test
    public void testCatShelterWorkingHoursSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.catShelterWorkingHoursSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testCatShelterMessageTextFormat() {
        // Вызываем метод catShelterWorkingHoursSelection
        telegramBot.catShelterWorkingHoursSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("Мы работаем с 7:00 и до 23:00 ежедневно.\n" +
                        "В праздничные и предпраздничные дни график работы может меняться, для уточнения данных времени приема - просьба обратиться по контактам для связи с охраной приюта"),

                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDogShelterWorkingHoursSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.dogShelterWorkingHoursSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDogShelterMessageTextFormat() {

        // Вызываем метод dogShelterWorkingHoursSelection
        telegramBot.dogShelterWorkingHoursSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("Мы работаем с 8:30 и до 22:00 ежедневно.\n" +
                        "В праздничные и предпраздничные дни график работы может меняться, для уточнения данных времени приема - просьба обратиться по контактам для связи с охраной приюта"),

                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testCatShelterSecurityContactSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.catShelterSecurityContactSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testCatShelterSecurityContactSelectionMessageTextFormat() {
        // Вызываем метод dogShelterWorkingHoursSelection
        telegramBot.catShelterSecurityContactSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                Уважаемые посетители нашего прекрасного приюта, убедительная просьба перед посещением связаться с сотрудниками охраны,так как у нас установлен пропускной режим
                Телефон для связи с постом охраны: 8-800-888-88-88.
                Просьба звонить заранее, в часы работы приюта.
                """),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDogShelterSecurityContactSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.dogShelterSecurityContactSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDogShelterSecurityContactSelectionMessageTextFormat() {
        // Вызываем метод dogShelterWorkingHoursSelection
        telegramBot.dogShelterSecurityContactSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("Уважаемые посетители нашего прекрасного приюта, убедительная просьба перед посещением связаться с сотрудниками охраны,так как у нас установлен пропускной режим\n" +
                        "Телефон для связи с постом охраны: 8-777-666-55-44.\n" +
                        "Просьба звонить заранее, в часы работы приюта."),

                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testSafetyRecommendationsSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.safetyRecommendationsSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testSafetyRecommendationsSelectionMessageTextFormat() {
        // Вызываем метод safetyRecommendationsSelection
        telegramBot.safetyRecommendationsSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Уважайте животных: Обращайтесь с животными с добротой и состраданием. Используйте спокойный и мягкий тон голоса, чтобы не пугать их.
                2. Следуйте инструкциям персонала: Следуйте указаниям персонала приюта. Они знают особенности каждого животного и могут дать советы о безопасности и правильном обращении с ними.
                3. Не кормите животных без разрешения: Не предлагайте пищу животным без разрешения персонала. У них может быть специальная диета или определенные проблемы со здоровьем, и неправильная пища может причинить вред.
                4. Будьте аккуратны и осторожны: Поведение в приюте должно быть аккуратным и осторожным, чтобы не причинить вред животным или себе. Не шумите, не пугайте их и не оставляйте предметы, которые могут быть опасными.
                5. Не проникайте в запретные зоны приюта: Уважайте указанные запретные зоны, куда доступ запрещен. Некоторые животные могут быть в карантине или требовать специального ухода, поэтому важно соблюдать инструкции персонала.
                6. Поддерживайте чистоту: Следите за чистотой и гигиеной в приюте. Выбрасывайте мусор в специальные контейнеры, после общения с животными вымывайте руки.
                7. Предоставляйте информацию: Если замечаете какие-либо изменения в поведении или состоянии животных, сообщите об этом персоналу приюта. Ваши наблюдения могут быть важными для здоровья и ухода за животными.
                """),

                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testArrangementAdultSelectionDog() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.arrangementAdultSelectionDog(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testArrangementAdultSelectionDogMessageTextFormat() {
        // Вызываем метод arrangementAdultSelectionDog
        telegramBot.arrangementAdultSelectionDog(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Место для сна, такое как кровать или мягкий матрас.
                2. Миски для еды и воды, предпочтительно из нержавеющей стали или керамики.
                3. Корм для взрослой собаки, соответствующий ее возрасту, размеру и потребностям.
                4. Игрушки для игр и развлечения, включая интерактивные игры и игрушки для охоты.
                5. Когтеточка или когтетренировочный материал, чтобы предотвратить повреждение мебели.
                6. Лоток для учения собаки делать свои нужды на определенном месте, если она живет в квартире.
                7. Шлейка и поводок для прогулок и тренировок, если собака привыкла к ним.
                8. Щетка для груминга и ухода за шерстью, чтобы предотвратить образование комков и перхоти.
                9. Дезинфицирующее средство для очистки мест, где собака делает свои нужды, а также для очистки поверхностей и предметов, которые собака может затронуть.
                """),

                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testRecordingContactsSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.recordingContactsSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testRecordingContactsSelectionTextFormat() {
        // Вызываем метод arrangementAdultSelectionDog
        telegramBot.recordingContactsSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("Введите свой номер телефона в формате +71112223344"),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testListReasonsSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.listReasonsSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testListReasonsSelectionTextFormat() {
        // Вызываем метод arrangementAdultSelectionDog
        telegramBot.listReasonsSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
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
                """),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testArrangementLimitedSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.arrangementLimitedSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testArrangementLimitedSelectionTextFormat() {
        // Вызываем метод arrangementAdultSelectionDog
        telegramBot.arrangementLimitedSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Уютное место для сна, такое как кровать или мягкий матрас.
                2. Миски для еды и воды, предпочтительно из нержавеющей стали или керамики.
                3. Корм для взрослого питомца, соответствующий его возрасту, размеру и потребностям.
                4. Игрушки для игр и развлечения, включая интерактивные игры и игрушки для охоты.
                6. Лоток для кошек или лоток для учения собаки делать свои нужды на определенном месте, если она живет в квартире.
                7. Шлейка и поводок для прогулок и тренировок, если питомец привык к ним.
                8. Щетка для груминга и ухода за шерстью, чтобы предотвратить образование комков и перхоти.
                9. Дезинфицирующее средство для очистки мест, где питомец делает свои нужды, а также для очистки поверхностей и предметов, которые они могут затронуть.
                """),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testArrangementAdultSelectionCat() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.arrangementAdultSelectionCat(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testArrangementAdultSelectionCatTextFormat() {
        // Вызываем метод arrangementAdultSelectionCat
        telegramBot.arrangementAdultSelectionCat(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Место для сна, такое как кровать или мягкий матрас.
                2. Миски для еды и воды, предпочтительно из нержавеющей стали или керамики.
                3. Корм для взрослого кота, соответствующий его возрасту, размеру и потребностям.
                4. Игрушки для игр и развлечения, включая интерактивные игры и игрушки для охоты.
                5. Когтеточка или когтетренировочный материал, чтобы предотвратить повреждение мебели.
                6. Лоток для учения кота делать свои нужды на определенном месте, предпочтительно с запахоней или без запаха.
                7. Шлейка и поводок для прогулок и тренировок, если кот привык к ним.
                8. Щетка для груминга и ухода за шерстью, чтобы предотвратить образование комков и перхоти.
                """),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testTransportationSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.transportationSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testTransportationSelectionTextFormat() {
        // Вызываем метод transportationSelection
        telegramBot.transportationSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Клетка или переноска для транспортировки животного, соответствующая его размеру и весу.
                2. Постельное белье для клетки или переноски.
                3. Еда и вода на время транспортировки.
                4. Игрушки или другие предметы, которые помогут животному чувствовать себя комфортно во время перевозки."""),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDocumentsSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.documentsSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDocumentsSelectionTextFormat() {
        // Вызываем метод documentsSelection
        telegramBot.documentsSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Заявление на усыновление животного.
                2. Документ, удостоверяющий личность (паспорт или другой документ, содержащий фотографию и подпись).
                3. Документ, подтверждающий наличие разрешения на содержание животного в доме или квартире (в зависимости от законодательства страны).
                4. Документы, подтверждающие финансовую способность обеспечить животное достойными условиями жизни (например, выписки из банковских счетов или другие документы, подтверждающие стабильный доход).
                5. Документы, подтверждающие наличие опыта в уходе за животными (если это требуется при усыновлении конкретной породы).
                6. Документы, подтверждающие прохождение курсов обучения по уходу за животными (если это требуется при усыновлении конкретной породы).
                7. Документы, подтверждающие наличие места для проживания животного (например, договор аренды жилья или собственности).
                """),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDatingRulesSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.datingRulesSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testDatingRulesSelectionTextFormat() {
        // Вызываем метод datingRulesSelection
        telegramBot.datingRulesSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Изучите информацию о животном: возраст, порода, характер, здоровье и особенности ухода.

                2. Проведите время с животным в приюте, чтобы понять, как он ведет себя в различных ситуациях.

                3. Обсудите с работниками приюта все вопросы, связанные с уходом за животным, его здоровьем и поведением.

                4. Убедитесь, что вы готовы к финансовым затратам на уход за животным и можете обеспечить ему достойные условия жизни.

                5. Подготовьте дом к прибытию нового члена семьи: оборудуйте место для сна и отдыха, купите необходимые принадлежности и игрушки.

                6. Соблюдайте все требования приюта по уходу за животным после его забора.\s

                7. Не забывайте о регулярных посещениях ветеринара и своевременном проведении всех необходимых процедур по уходу за животным.\s

                8. Относитесь к животному с любовью и заботой, чтобы он чувствовал себя дома и был счастлив."""),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testPuppyArrangementSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.puppyArrangementSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testPuppyArrangementSelectionTextFormat() {
        // Вызываем метод puppyArrangementSelection
        telegramBot.puppyArrangementSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Место для сна, такое как кровать или корзина.
                2. Миски для еды и воды.
                3. Корм для щенка, соответствующий его возрасту и размеру.
                4. Игрушки для жевания и развлечения.
                5. Подстилка или пеленки для учения щенка делать свои нужды на определенном месте.
                6. Шлейка и поводок для прогулок и тренировок.
                7. Туалет для щенка, если вы планируете держать его внутри дома.
                8. Щетка для груминга и ухода за шерстью.
                9. Дезинфицирующее средство для очистки мест, где щенок делает свои нужды.
                10. Контактные данные ветеринарного врача или клиники, которые можно обратиться в случае необходимости."""),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testKittenArrangementSelection() {
        // Вызываем метод, который мы хотим протестировать
        telegramBot.KittenArrangementSelection(123, 456L);

        // Проверяем, что метод changeMessage был вызван с ожидаемыми аргументами
        verify(telegramBot).changeMessage(
                eq(123),
                eq(456L),
                anyString(),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    public void testKittenArrangementSelectionTextFormat() {
        // Вызываем метод KittenArrangementSelection
        telegramBot.KittenArrangementSelection(123, 456L);

        // Проверяем, что текст сообщения соответствует ожидаемому формату
        verify(telegramBot).changeMessage(
                anyInt(),
                anyLong(),
                matches("""
                1. Место для сна, такое как кошачий домик или мягкая подушка.
                2. Миски для еды и воды.
                3. Корм для котенка, соответствующий его возрасту и размеру.
                4. Игрушки для игр и развлечения.
                5. Когтеточка или когтетренировочный материал.
                6. Подстилка или лоток для учения котенка делать свои нужды на определенном месте.
                7. Шлейка и поводок для прогулок и тренировок.
                8. Щетка для груминга и ухода за шерстью.
                9. Дезинфицирующее средство для очистки мест, где котенок делает свои нужды.
                10. Контактные данные ветеринарного врача или клиники, которые можно обратиться в случае необходимости."""),
                any(InlineKeyboardMarkup.class)
        );
    }

}


