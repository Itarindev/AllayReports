package allayplugins.stompado.dao;

import allayplugins.stompado.model.Report;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    @Getter
    private static final List<Report> reports = new ArrayList<>();

    public static Report findReportById(String reportId) {
        return reports.stream().filter(report -> report.getId().equalsIgnoreCase(reportId)).findFirst().orElse(null);
    }
}