package allayplugins.stompado.manager;

import allayplugins.stompado.connections.model.IDatabase;
import allayplugins.stompado.dao.ReportDAO;
import allayplugins.stompado.enuns.State;
import allayplugins.stompado.model.Report;
import lombok.val;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    private final IDatabase iDatabase;

    public ReportManager(IDatabase iDatabase) {
        this.iDatabase = iDatabase;
    }

    public void insert(Report report) {
        iDatabase.executeUpdate("INSERT INTO allayreports_reports (id, victim, reported, reason, state, date) VALUES (?,?,?,?,?,?)",
                report.getId(), report.getVictim(),report.getReported(), report.getReason(), report.getState().getPlainName(), report.getFormattedDate());
    }

    public void remove(Report report) {
        iDatabase.executeUpdate("DELETE FROM allayreports_reports WHERE id = ?", report.getId());
        ReportDAO.getReports().remove(report);
    }

    public void save(Report report) {
        iDatabase.executeUpdate("UPDATE allayreports_reports SET victim = ?, reported = ?, reason = ?, state = ?, date = ? WHERE id = ?",
                report.getVictim(), report.getReported(), report.getReason(), report.getState().getPlainName(), report.getFormattedDate(), report.getId());
    }

    public void updateState(String reportId, State newState) {
        iDatabase.executeUpdate(
                "UPDATE allayreports_reports SET state = ? WHERE id = ?",
                newState.getPlainName(), reportId);
    }

    public List<Report> getReportsAgainstPlayer(String playerName) {
        List<Report> reports = new ArrayList<>();
        try (val ps = iDatabase.getConnection().prepareStatement("SELECT * FROM allayreports_reports WHERE reported = ?")) {
            ps.setString(1, playerName);
            try (val rs = ps.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report(
                            rs.getString("id"),
                            rs.getString("victim"),
                            rs.getString("reported"),
                            rs.getString("reason"),
                            State.fromPlainName(rs.getString("state")),
                            new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(rs.getString("date")).getTime()
                    );
                    reports.add(report);
                }
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return reports;
    }

}