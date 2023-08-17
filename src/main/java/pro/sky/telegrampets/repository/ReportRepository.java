package pro.sky.telegrampets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrampets.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findReportByPhotoNameId(String name);

}
