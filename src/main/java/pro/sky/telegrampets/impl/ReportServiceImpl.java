package pro.sky.telegrampets.impl;
import org.springframework.stereotype.Service;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.service.ReportService;

@Service
public class ReportServiceImpl   {
    private final ReportRepository reportService;

    public ReportServiceImpl(ReportRepository reportService) {
        this.reportService = reportService;
    }

    public Report reportAdd(Report report) {
        return reportService.save(report);
    }

    public void updateReport(Report report) {
        reportService.save(report);
    }

}
