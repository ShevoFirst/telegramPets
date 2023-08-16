package pro.sky.telegrampets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrampets.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByChatId(long chatId);

    User findUserByChatId(int chatId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.chatId = :chatId")
    boolean existsByChatId(int chatId);

    Optional<User> findByChatId(int chatId);

    Optional<User> findByDateTimeToTookBefore(LocalDateTime dateTimeToTook);
    @Transactional
    @Modifying
    @Query("UPDATE User u set u.number = ?2 where u.chatId = ?1")
    int updateNumber(int chatId, String num);


}
