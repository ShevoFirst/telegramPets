package pro.sky.telegrampets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report_tg")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "date_added")
    private LocalDateTime dateAdded;

    @Column(name = "general_well_being")
    private String generalWellBeing;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
