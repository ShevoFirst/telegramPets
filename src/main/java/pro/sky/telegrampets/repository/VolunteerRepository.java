package pro.sky.telegrampets.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrampets.model.Volunteer;

@Repository
public interface VolunteerRepository extends JpaRepository <Volunteer,Long> {
}
