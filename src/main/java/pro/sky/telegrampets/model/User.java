package pro.sky.telegrampets.model;

import liquibase.pro.packaged.S;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_tg")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(name = "name")
    private String firstName;
   // @Column(name = "took_a_pet")
    //private Boolean tookAPet;
   // @Column(name = "chat_id_user")
   // private long chatId;
    @Column(name = "msg_numb")
    private String number;
   // @Column(name = "reports")
   // private Reports reports;
   // @Column(name = "date_time_to_took")
  //  private int dateTimeToTook;

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


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
