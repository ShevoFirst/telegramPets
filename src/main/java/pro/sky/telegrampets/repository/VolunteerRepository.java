package pro.sky.telegrampets.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrampets.model.Volunteer;

public interface VolunteerRepository extends JpaRepository <Volunteer,Long> {
}
