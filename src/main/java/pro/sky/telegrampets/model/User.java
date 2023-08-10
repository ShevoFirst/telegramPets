package pro.sky.telegrampets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", nullable = false, unique = true)
    private long id;
    @Column(name = "first_name_user")
    private String firstName;
    @Column(name = "last_name_user")
    private String lastName;
    @Column(name = "chat_id_user")
    private long chatId;
}
