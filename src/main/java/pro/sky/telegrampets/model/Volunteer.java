package pro.sky.telegrampets.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_volunteer", nullable = false, unique = true)
    private long id;
    @Column(name = "name_volunteer")
    private String name;
    @Column(name = "last_name_volunteer")
    private String lastName;
    @Column(name = "chat_id_volunteer")
    private long chatId;
}
