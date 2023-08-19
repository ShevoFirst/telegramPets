package pro.sky.telegrampets.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.repository.ReportRepository;

public class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReportAdd() {
        Report report = new Report();
        Report savedReport = new Report();
        when(reportRepository.save(report)).thenReturn(savedReport);

        Report result = reportService.reportAdd(report);

        assertEquals(savedReport, result);
        verify(reportRepository, times(1)).save(report);
    }

    @Test
    public void testUpdateReport() {
        Report report = new Report();

        reportService.updateReport(report);

        verify(reportRepository, times(1)).save(report);
    }

}