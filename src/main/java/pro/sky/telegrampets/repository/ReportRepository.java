package pro.sky.telegrampets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrampets.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findReportByPhotoNameId(String name);

    List<Report> findReportByCheckReportIsFalse();

    Report findReportById(Long id);
}
