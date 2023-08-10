package pro.sky.telegrampets.model;

import javax.persistence.*;

@Entity
@Table(name = "reports")
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
