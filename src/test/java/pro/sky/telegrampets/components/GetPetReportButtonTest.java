package pro.sky.telegrampets.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

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
import org.telegram.telegrambots.meta.api.objects.Chat;
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
import org.telegram.telegrambots.meta.api.objects.passport.PassportData;
import org.telegram.telegrambots.meta.api.objects.payments.Invoice;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatEnded;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatParticipantsInvited;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatScheduled;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatStarted;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import pro.sky.telegrampets.impl.ReportServiceImpl;
import pro.sky.telegrampets.impl.UserServiceImpl;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.repository.UserRepository;

@ContextConfiguration(classes = {GetPetReportButton.class})
@ExtendWith(SpringExtension.class)
class GetPetReportButtonTest {
    @Autowired
    private GetPetReportButton getPetReportButton;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private ReportServiceImpl reportServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link GetPetReportButton#sendMessageReportFromPet()}
     */
    @Test
    void testSendMessageReportFromPet() {
        assertEquals(3, getPetReportButton.sendMessageReportFromPet().getKeyboard().size());
    }

    /**
     * Method under test: {@link GetPetReportButton#sendMessageDailyReportPhoto(long)}
     */
    @Test
    void testSendMessageDailyReportPhoto() {
        SendMessage actualSendMessageDailyReportPhotoResult = getPetReportButton.sendMessageDailyReportPhoto(1L);
        assertEquals("Пришлите фото питомца", actualSendMessageDailyReportPhotoResult.getText());
        assertEquals("1", actualSendMessageDailyReportPhotoResult.getChatId());
    }

    /**
     * Method under test: {@link GetPetReportButton#dailyReportCheckPhoto(long, Update)}
     */
    @Test
    void testDailyReportCheckPhoto2() {
        Update update = new Update();
        update.setMessage(new Message());
        SendMessage actualDailyReportCheckPhotoResult = getPetReportButton.dailyReportCheckPhoto(1L, update);
        assertEquals("Вы прислали не фото!", actualDailyReportCheckPhotoResult.getText());
        assertEquals("1", actualDailyReportCheckPhotoResult.getChatId());
    }

    /**
     * Method under test: {@link GetPetReportButton#dailyReportCheckPhoto(long, Update)}
     */
    @Test
    void testDailyReportCheckPhoto4() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(new Message());
        SendMessage actualDailyReportCheckPhotoResult = getPetReportButton.dailyReportCheckPhoto(1L, update);
        assertEquals("Вы прислали не фото!", actualDailyReportCheckPhotoResult.getText());
        assertEquals("1", actualDailyReportCheckPhotoResult.getChatId());
        verify(update).getMessage();
    }

    /**
     * Method under test: {@link GetPetReportButton#dailyReportCheckPhoto(long, Update)}
     */
    @Test
    void testDailyReportCheckPhoto5() {
        Message message = mock(Message.class);
        when(message.hasPhoto()).thenReturn(true);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        SendMessage actualDailyReportCheckPhotoResult = getPetReportButton.dailyReportCheckPhoto(1L, update);
        assertEquals("Фото сохранено, пришлите текстовую часть отчета.", actualDailyReportCheckPhotoResult.getText());
        assertEquals("1", actualDailyReportCheckPhotoResult.getChatId());
        verify(update).getMessage();
        verify(message).hasPhoto();
    }

    /**
     * Method under test: {@link GetPetReportButton#dailyReportCheckMessage(long, Update, String)}
     */
    @Test
    void testDailyReportCheckMessage2() {
        Update update = new Update();
        update.setMessage(new Message());
        SendMessage actualDailyReportCheckMessageResult = getPetReportButton.dailyReportCheckMessage(1L, update, "Bella");
        assertEquals("Вы не прислали текстовую часть отчета!", actualDailyReportCheckMessageResult.getText());
        assertEquals("1", actualDailyReportCheckMessageResult.getChatId());
    }


    /**
     * Method under test: {@link GetPetReportButton#dailyReportCheckMessage(long, Update, String)}
     */
    @Test
    void testDailyReportCheckMessage4() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(new Message());
        SendMessage actualDailyReportCheckMessageResult = getPetReportButton.dailyReportCheckMessage(1L, update, "Bella");
        assertEquals("Вы не прислали текстовую часть отчета!", actualDailyReportCheckMessageResult.getText());
        assertEquals("1", actualDailyReportCheckMessageResult.getChatId());
        verify(update).getMessage();
    }

    /**
     * Method under test: {@link GetPetReportButton#dailyReportCheckMessage(long, Update, String)}
     */
    @Test
    void testDailyReportCheckMessage5() {
        doNothing().when(reportServiceImpl).updateReport(Mockito.<Report>any());

        pro.sky.telegrampets.model.User user = new pro.sky.telegrampets.model.User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);

        Report report = new Report();
        report.setCheckReport(true);
        report.setDateAdded(LocalDate.of(1970, 1, 1).atStartOfDay());
        report.setGeneralWellBeing("General Well Being");
        report.setId(1L);
        report.setUser(user);
        Optional<Report> ofResult = Optional.of(report);
        when(reportRepository.findReportByPhotoNameId(Mockito.<String>any())).thenReturn(ofResult);
        Update update = mock(Update.class);
        org.telegram.telegrambots.meta.api.objects.User from = new org.telegram.telegrambots.meta.api.objects.User();
        Chat chat = new Chat();
        org.telegram.telegrambots.meta.api.objects.User forwardFrom = new org.telegram.telegrambots.meta.api.objects.User();
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
        ArrayList<org.telegram.telegrambots.meta.api.objects.User> newChatMembers = new ArrayList<>();
        org.telegram.telegrambots.meta.api.objects.User leftChatMember = new org.telegram.telegrambots.meta.api.objects.User();
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
        org.telegram.telegrambots.meta.api.objects.User viaBot = new org.telegram.telegrambots.meta.api.objects.User();
        Chat senderChat = new Chat();
        ProximityAlertTriggered proximityAlertTriggered = new ProximityAlertTriggered();
        MessageAutoDeleteTimerChanged messageAutoDeleteTimerChanged = new MessageAutoDeleteTimerChanged();
        WebAppData webAppData = new WebAppData("Вы не прислали текстовую часть отчета!",
                "Вы не прислали текстовую часть отчета!");

        VideoChatStarted videoChatStarted = new VideoChatStarted();
        VideoChatEnded videoChatEnded = new VideoChatEnded();
        VideoChatParticipantsInvited videoChatParticipantsInvited = new VideoChatParticipantsInvited();
        VideoChatScheduled videoChatScheduled = new VideoChatScheduled();
        ForumTopicCreated forumTopicCreated = new ForumTopicCreated();
        ForumTopicClosed forumTopicClosed = new ForumTopicClosed();
        ForumTopicReopened forumTopicReopened = new ForumTopicReopened();
        ForumTopicEdited forumTopicEdited = new ForumTopicEdited("Вы не прислали текстовую часть отчета!", "42");

        GeneralForumTopicHidden generalForumTopicHidden = new GeneralForumTopicHidden();
        GeneralForumTopicUnhidden generalForumTopicUnhidden = new GeneralForumTopicUnhidden();
        WriteAccessAllowed writeAccessAllowed = new WriteAccessAllowed("Вы не прислали текстовую часть отчета!");
        UserShared userShared = new UserShared();
        when(update.getMessage()).thenReturn(new Message(1, 1, from, 1, chat, forwardFrom, forwardFromChat, 1,
                "Вы не прислали текстовую часть отчета!", entities, captionEntities, audio, document, photo, sticker, video,
                contact, location, venue, animation, pinnedMessage, newChatMembers, leftChatMember, "Dr", newChatPhoto, true,
                true, replyToMessage, voice, "Вы не прислали текстовую часть отчета!", true, true, 1L, 1L, 1, game, 1,
                invoice, successfulPayment, videoNote, "JaneDoe", "Вы не прислали текстовую часть отчета!", "42",
                "Вы не прислали текстовую часть отчета!", passportData, "Вы не прислали текстовую часть отчета!", poll,
                replyMarkup, dice, viaBot, senderChat, proximityAlertTriggered, messageAutoDeleteTimerChanged, true, true,
                webAppData, videoChatStarted, videoChatEnded, videoChatParticipantsInvited, videoChatScheduled, true,
                forumTopicCreated, forumTopicClosed, forumTopicReopened, forumTopicEdited, generalForumTopicHidden,
                generalForumTopicUnhidden, writeAccessAllowed, true, userShared, new ChatShared()));
        SendMessage actualDailyReportCheckMessageResult = getPetReportButton.dailyReportCheckMessage(1L, update, "Bella");
        assertEquals("Отчет сохранен", actualDailyReportCheckMessageResult.getText());
        assertEquals("1", actualDailyReportCheckMessageResult.getChatId());
        verify(reportServiceImpl).updateReport(Mockito.<Report>any());
        verify(reportRepository).findReportByPhotoNameId(Mockito.<String>any());
        verify(update, atLeast(1)).getMessage();
    }

    /**
     * Method under test: {@link GetPetReportButton#dailyReportCheckMessage(long, Update, String)}
     */
    @Test
    void testDailyReportCheckMessage6() {
        doNothing().when(reportServiceImpl).updateReport(Mockito.<Report>any());

        pro.sky.telegrampets.model.User user = new pro.sky.telegrampets.model.User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        Report report = mock(Report.class);
        doNothing().when(report).setCheckReport(anyBoolean());
        doNothing().when(report).setDateAdded(Mockito.<LocalDateTime>any());
        doNothing().when(report).setGeneralWellBeing(Mockito.<String>any());
        doNothing().when(report).setId(anyLong());
        doNothing().when(report).setUser(Mockito.<pro.sky.telegrampets.model.User>any());
        report.setCheckReport(true);
        report.setDateAdded(LocalDate.of(1970, 1, 1).atStartOfDay());
        report.setGeneralWellBeing("General Well Being");
        report.setId(1L);
        report.setUser(user);
        Optional<Report> ofResult = Optional.of(report);
        when(reportRepository.findReportByPhotoNameId(Mockito.<String>any())).thenReturn(ofResult);
        Update update = mock(Update.class);
        org.telegram.telegrambots.meta.api.objects.User from = new org.telegram.telegrambots.meta.api.objects.User();
        Chat chat = new Chat();
        org.telegram.telegrambots.meta.api.objects.User forwardFrom = new org.telegram.telegrambots.meta.api.objects.User();
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
        ArrayList<org.telegram.telegrambots.meta.api.objects.User> newChatMembers = new ArrayList<>();
        org.telegram.telegrambots.meta.api.objects.User leftChatMember = new org.telegram.telegrambots.meta.api.objects.User();
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
        org.telegram.telegrambots.meta.api.objects.User viaBot = new org.telegram.telegrambots.meta.api.objects.User();
        Chat senderChat = new Chat();
        ProximityAlertTriggered proximityAlertTriggered = new ProximityAlertTriggered();
        MessageAutoDeleteTimerChanged messageAutoDeleteTimerChanged = new MessageAutoDeleteTimerChanged();
        WebAppData webAppData = new WebAppData("Вы не прислали текстовую часть отчета!",
                "Вы не прислали текстовую часть отчета!");

        VideoChatStarted videoChatStarted = new VideoChatStarted();
        VideoChatEnded videoChatEnded = new VideoChatEnded();
        VideoChatParticipantsInvited videoChatParticipantsInvited = new VideoChatParticipantsInvited();
        VideoChatScheduled videoChatScheduled = new VideoChatScheduled();
        ForumTopicCreated forumTopicCreated = new ForumTopicCreated();
        ForumTopicClosed forumTopicClosed = new ForumTopicClosed();
        ForumTopicReopened forumTopicReopened = new ForumTopicReopened();
        ForumTopicEdited forumTopicEdited = new ForumTopicEdited("Вы не прислали текстовую часть отчета!", "42");

        GeneralForumTopicHidden generalForumTopicHidden = new GeneralForumTopicHidden();
        GeneralForumTopicUnhidden generalForumTopicUnhidden = new GeneralForumTopicUnhidden();
        WriteAccessAllowed writeAccessAllowed = new WriteAccessAllowed("Вы не прислали текстовую часть отчета!");
        UserShared userShared = new UserShared();
        when(update.getMessage()).thenReturn(new Message(1, 1, from, 1, chat, forwardFrom, forwardFromChat, 1,
                "Вы не прислали текстовую часть отчета!", entities, captionEntities, audio, document, photo, sticker, video,
                contact, location, venue, animation, pinnedMessage, newChatMembers, leftChatMember, "Dr", newChatPhoto, true,
                true, replyToMessage, voice, "Вы не прислали текстовую часть отчета!", true, true, 1L, 1L, 1, game, 1,
                invoice, successfulPayment, videoNote, "JaneDoe", "Вы не прислали текстовую часть отчета!", "42",
                "Вы не прислали текстовую часть отчета!", passportData, "Вы не прислали текстовую часть отчета!", poll,
                replyMarkup, dice, viaBot, senderChat, proximityAlertTriggered, messageAutoDeleteTimerChanged, true, true,
                webAppData, videoChatStarted, videoChatEnded, videoChatParticipantsInvited, videoChatScheduled, true,
                forumTopicCreated, forumTopicClosed, forumTopicReopened, forumTopicEdited, generalForumTopicHidden,
                generalForumTopicUnhidden, writeAccessAllowed, true, userShared, new ChatShared()));
        SendMessage actualDailyReportCheckMessageResult = getPetReportButton.dailyReportCheckMessage(1L, update, "Bella");
        assertEquals("Отчет сохранен", actualDailyReportCheckMessageResult.getText());
        assertEquals("1", actualDailyReportCheckMessageResult.getChatId());
        verify(reportServiceImpl).updateReport(Mockito.<Report>any());
        verify(reportRepository).findReportByPhotoNameId(Mockito.<String>any());
        verify(report).setCheckReport(anyBoolean());
        verify(report).setDateAdded(Mockito.<LocalDateTime>any());
        verify(report, atLeast(1)).setGeneralWellBeing(Mockito.<String>any());
        verify(report).setId(anyLong());
        verify(report).setUser(Mockito.<pro.sky.telegrampets.model.User>any());
        verify(update, atLeast(1)).getMessage();
    }

    /**
     * Method under test: {@link GetPetReportButton#saveUser(Update, boolean)}
     */
    @Test
    void testSaveUser5() {
        pro.sky.telegrampets.model.User user = new pro.sky.telegrampets.model.User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        Optional<pro.sky.telegrampets.model.User> ofResult = Optional.of(user);

        pro.sky.telegrampets.model.User user2 = new pro.sky.telegrampets.model.User();
        user2.setChatId(1);
        user2.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setNumber("42");
        user2.setReports(new ArrayList<>());
        user2.setTookAPet(true);
        when(userServiceImpl.updateUser(Mockito.<pro.sky.telegrampets.model.User>any())).thenReturn(user2);
        when(userServiceImpl.getUserByChatId(anyInt())).thenReturn(ofResult);
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(1L);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        getPetReportButton.saveUser(update, true);
        verify(userServiceImpl).getUserByChatId(anyInt());
        verify(userServiceImpl).updateUser(Mockito.<pro.sky.telegrampets.model.User>any());
        verify(update).getMessage();
        verify(message).getChatId();
    }

    /**
     * Method under test: {@link GetPetReportButton#saveUser(Update, boolean)}
     */
    @Test
    void testSaveUser6() {
        pro.sky.telegrampets.model.User user = new pro.sky.telegrampets.model.User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        when(userServiceImpl.userAdd(Mockito.<pro.sky.telegrampets.model.User>any())).thenReturn(user);
        Optional<pro.sky.telegrampets.model.User> emptyResult = Optional.empty();
        when(userServiceImpl.getUserByChatId(anyInt())).thenReturn(emptyResult);
        Message message = mock(Message.class);
        when(message.getFrom()).thenReturn(new org.telegram.telegrambots.meta.api.objects.User());
        when(message.getChatId()).thenReturn(1L);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        getPetReportButton.saveUser(update, true);
        verify(userServiceImpl).getUserByChatId(anyInt());
        verify(userServiceImpl).userAdd(Mockito.<pro.sky.telegrampets.model.User>any());
        verify(update, atLeast(1)).getMessage();
        verify(message).getChatId();
        verify(message).getFrom();
    }

    /**
     * Method under test: {@link GetPetReportButton#saveReportMessage(Update, String)}
     */
    @Test
    void testSaveReportMessage3() {
        doNothing().when(reportServiceImpl).updateReport(Mockito.<Report>any());

        pro.sky.telegrampets.model.User user = new pro.sky.telegrampets.model.User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);

        Report report = new Report();
        report.setCheckReport(true);
        report.setDateAdded(LocalDate.of(1970, 1, 1).atStartOfDay());
        report.setGeneralWellBeing("General Well Being");
        report.setId(1L);
        report.setUser(user);
        Optional<Report> ofResult = Optional.of(report);
        when(reportRepository.findReportByPhotoNameId(Mockito.<String>any())).thenReturn(ofResult);

        Update update = new Update();
        update.setMessage(new Message());
        getPetReportButton.saveReportMessage(update, "Bella");
        verify(reportServiceImpl).updateReport(Mockito.<Report>any());
        verify(reportRepository).findReportByPhotoNameId(Mockito.<String>any());
    }

    /**
     * Method under test: {@link GetPetReportButton#saveReportMessage(Update, String)}
     */
    @Test
    void testSaveReportMessage4() {
        doNothing().when(reportServiceImpl).updateReport(Mockito.<Report>any());

        pro.sky.telegrampets.model.User user = new pro.sky.telegrampets.model.User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        Report report = mock(Report.class);
        doNothing().when(report).setCheckReport(anyBoolean());
        doNothing().when(report).setDateAdded(Mockito.<LocalDateTime>any());
        doNothing().when(report).setGeneralWellBeing(Mockito.<String>any());
        doNothing().when(report).setId(anyLong());
        doNothing().when(report).setUser(Mockito.<pro.sky.telegrampets.model.User>any());
        report.setCheckReport(true);
        report.setDateAdded(LocalDate.of(1970, 1, 1).atStartOfDay());
        report.setGeneralWellBeing("General Well Being");
        report.setId(1L);
        report.setUser(user);
        Optional<Report> ofResult = Optional.of(report);
        when(reportRepository.findReportByPhotoNameId(Mockito.<String>any())).thenReturn(ofResult);

        Update update = new Update();
        update.setMessage(new Message());
        getPetReportButton.saveReportMessage(update, "Bella");
        verify(reportServiceImpl).updateReport(Mockito.<Report>any());
        verify(reportRepository).findReportByPhotoNameId(Mockito.<String>any());
        verify(report).setCheckReport(anyBoolean());
        verify(report).setDateAdded(Mockito.<LocalDateTime>any());
        verify(report, atLeast(1)).setGeneralWellBeing(Mockito.<String>any());
        verify(report).setId(anyLong());
        verify(report).setUser(Mockito.<pro.sky.telegrampets.model.User>any());
    }

    /**
     * Method under test: {@link GetPetReportButton#saveReportPhotoId(Update, String)}
     */
    @Test
    void testSaveReportPhotoId5() {
        pro.sky.telegrampets.model.User user = new pro.sky.telegrampets.model.User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);

        Report report = new Report();
        report.setCheckReport(true);
        report.setDateAdded(LocalDate.of(1970, 1, 1).atStartOfDay());
        report.setGeneralWellBeing("General Well Being");
        report.setId(1L);
        report.setUser(user);
        when(reportServiceImpl.reportAdd(Mockito.<Report>any())).thenReturn(report);

        pro.sky.telegrampets.model.User user2 = new pro.sky.telegrampets.model.User();
        user2.setChatId(1);
        user2.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setNumber("42");
        user2.setReports(new ArrayList<>());
        user2.setTookAPet(true);
        when(userRepository.findUserByChatId(anyInt())).thenReturn(user2);
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(1L);
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        getPetReportButton.saveReportPhotoId(update, "Bella");
        verify(reportServiceImpl).reportAdd(Mockito.<Report>any());
        verify(userRepository).findUserByChatId(anyInt());
        verify(update).getMessage();
        verify(message).getChatId();
    }
}

