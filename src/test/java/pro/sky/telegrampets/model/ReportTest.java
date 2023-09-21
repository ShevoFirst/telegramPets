package pro.sky.telegrampets.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ReportTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Report#Report()}
     *   <li>{@link Report#setCheckReport(boolean)}
     *   <li>{@link Report#setDateAdded(LocalDateTime)}
     *   <li>{@link Report#setGeneralWellBeing(String)}
     *   <li>{@link Report#setId(long)}
     *   <li>{@link Report#setUser(User)}
     *   <li>{@link Report#setPhotoNameId(String)}
     *   <li>{@link Report#getDateAdded()}
     *   <li>{@link Report#getGeneralWellBeing()}
     *   <li>{@link Report#getId()}
     *   <li>{@link Report#getPhotoNameId()}
     *   <li>{@link Report#getUser()}
     *   <li>{@link Report#isCheckReport()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Report actualReport = new Report();
        actualReport.setCheckReport(true);
        LocalDateTime dateAdded = LocalDate.of(1970, 1, 1).atStartOfDay();
        actualReport.setDateAdded(dateAdded);
        actualReport.setGeneralWellBeing("General Well Being");
        actualReport.setId(1L);
        User user = new User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        actualReport.setUser(user);
        actualReport.setPhotoNameId("Bella");
        LocalDateTime actualDateAdded = actualReport.getDateAdded();
        String actualGeneralWellBeing = actualReport.getGeneralWellBeing();
        long actualId = actualReport.getId();
        String actualPhotoNameId = actualReport.getPhotoNameId();
        User actualUser = actualReport.getUser();
        assertSame(dateAdded, actualDateAdded);
        assertEquals("General Well Being", actualGeneralWellBeing);
        assertEquals(1L, actualId);
        assertEquals("Bella", actualPhotoNameId);
        assertSame(user, actualUser);
        assertTrue(actualReport.isCheckReport());
    }

    /**
     * Method under test: {@link Report#Report(long, LocalDateTime, String, String, boolean, User)}
     */
    @Test
    void testConstructor2() {
        LocalDateTime dateAdded = LocalDate.of(1970, 1, 1).atStartOfDay();

        User user = new User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        Report actualReport = new Report(1L, dateAdded, "General Well Being", "Bella", true, user);

        assertTrue(actualReport.isCheckReport());
        assertEquals("00:00", actualReport.getDateAdded().toLocalTime().toString());
        assertSame(user, actualReport.getUser());
        assertEquals("Bella", actualReport.getPhotoNameId());
        assertEquals("General Well Being", actualReport.getGeneralWellBeing());
        assertEquals(1L, actualReport.getId());
    }
}

