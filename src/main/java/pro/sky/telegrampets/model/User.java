package pro.sky.telegrampets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
    private String number;

    @Column(name = "date_time_to_took")
    private LocalDateTime dateTimeToTook;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    public User(long id, String firstName, int chatId, String number) {
        this.id = id;
        this.firstName = firstName;
        this.chatId = chatId;
        this.number = number;
    }

    public User(String firstName, int chatId, String number) {
        this.firstName = firstName;
        this.chatId = chatId;
        this.number = number;
    }
}
