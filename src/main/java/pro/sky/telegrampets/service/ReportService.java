package pro.sky.telegrampets.service;

import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.model.User;

public interface ReportService {
    Report reportAdd(Report report);

    void updateUser(Report report);
}
