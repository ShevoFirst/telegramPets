package pro.sky.telegrampets.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportService;

    public ReportServiceImpl(ReportRepository reportService) {
        this.reportService = reportService;
    }

    @Override
    public Report reportAdd(Report report) {
        return reportService.save(report);
    }

}
