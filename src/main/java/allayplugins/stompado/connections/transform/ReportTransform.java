package allayplugins.stompado.connections.transform;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.connections.model.IDatabase;
import allayplugins.stompado.dao.ReportDAO;
import allayplugins.stompado.enuns.State;
import allayplugins.stompado.model.Report;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReportTransform {

    protected AllayReports main;

    private final IDatabase iDatabase;

    public ReportTransform(AllayReports main) {
        this.main = main;

        iDatabase = main.getIDatabase();
    }

    public Report reportTransform(ResultSet rs) throws SQLException {
        val id = rs.getString("id");
        val victim = rs.getString("victim");
        val reported = rs.getString("reported");
        val reason = rs.getString("reason");
        val state = State.fromPlainName(rs.getString("state"));
        val dateString = rs.getString("date");

        long date = 0L;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Report(id, victim, reported, reason, state, date);
    }

    public void loadReports() {
        try (val ps = iDatabase.getConnection().prepareStatement("SELECT * FROM `allayreports_reports`")) {
            val rs = ps.executeQuery();

            while (rs.next()) {
                val report = reportTransform(rs);
                if (report == null) continue;

                if (report.getState() == State.RESOLVED || report.getState() == State.FALSE_REPORT) continue;

                ReportDAO.getReports().add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}