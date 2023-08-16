package pro.sky.telegrampets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_tg")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "took_a_pet")
    private Boolean tookAPet;

    @Column(name = "chat_id")
    private int chatId;

    @Column(name = "telephone_number")
    private int number;

    @Column(name = "date_time_to_took")
    private LocalDateTime dateTimeToTook;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getTookAPet() {
        return tookAPet;
    }

    public void setTookAPet(Boolean tookAPet) {
        this.tookAPet = tookAPet;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LocalDateTime getDateTimeToTook() {
        return dateTimeToTook;
    }

    public void setDateTimeToTook(LocalDateTime dateTimeToTook) {
        this.dateTimeToTook = dateTimeToTook;
    }
}
