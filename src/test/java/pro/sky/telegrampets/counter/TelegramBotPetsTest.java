package pro.sky.telegrampets.counter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import liquibase.pro.packaged.M;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
    void testCallAVolunteer() {

        // Заглушки
        TelegramBotConfiguration config = Mockito.mock(TelegramBotConfiguration.class);
        ReportRepository reportRepository = Mockito.mock(ReportRepository.class);
        Buttons buttons = Mockito.mock(Buttons.class);
        GetPetReportButton getPetReportButton = Mockito.mock(GetPetReportButton.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ButtonsVolunteer buttonsVolunteer = Mockito.mock(ButtonsVolunteer.class);
        VolunteerRepository volunteerRepository = Mockito.mock(VolunteerRepository.class);

        TelegramBotPets botPets = new TelegramBotPets(config, reportRepository, buttons, getPetReportButton, userRepository, buttonsVolunteer, volunteerRepository);

        // Создание моков для тестирования
        Update mockUpdate = mock(Update.class);
        CallbackQuery mockCallbackQuery = mock(CallbackQuery.class);
        User mockUser = mock(User.class);
        Chat mockChat = mock(Chat.class);
        when(mockUpdate.getCallbackQuery()).thenReturn(mockCallbackQuery);
        when(mockCallbackQuery.getFrom()).thenReturn(mockUser);
        when(mockCallbackQuery.getMessage()).thenReturn(null); // В данном случае не используется
        when(mockCallbackQuery.getMessage()).thenReturn(mockChat.getPinnedMessage());
        when(mockUser.getUserName()).thenReturn("test_user");

        // Создание списка моков волонтеров
        List<Volunteer> mockVolunteerList = new ArrayList<>();
        Volunteer mockVolunteer1 = mock(Volunteer.class);
        Volunteer mockVolunteer2 = mock(Volunteer.class);
        when(mockVolunteer1.getChatId()).thenReturn(Long.valueOf("chatId1"));
        when(mockVolunteer2.getChatId()).thenReturn(Long.valueOf("chatId2"));
        mockVolunteerList.add(mockVolunteer1);
        mockVolunteerList.add(mockVolunteer2);

        // Подготовка моков репозитория и бота
        when(volunteerRepository.findAll()).thenReturn(mockVolunteerList);


        // Вызов метода для тестирования
        botPets.callAVolunteer(mockUpdate);

//        // Проверка, что был вызван метод execute() нужное количество раз
//        verify(mockSender, times(2)).execute(any(SendMessage.class));

        // Проверка, что был вызван метод findAll() у репозитория
        verify(volunteerRepository).findAll();

        // Проверка, что был получен chatId у волонтеров
        verify(mockVolunteer1).getChatId();
        verify(mockVolunteer2).getChatId();

//        // Проверка, что текст отправленного сообщения соответствует ожидаемому
//        verify(mockSender).execute(argThat(sendMessage ->
//                sendMessage.getText().contains("Пользователь: @test_user просит с ним связаться.")));
    }
}


