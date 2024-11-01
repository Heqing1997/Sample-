package Mappers;

import DTO.EventBookingDetail;
import Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventBookingMapper {

    public ArrayList<EventBookingDetail> findEventBookingDetailsByEventId(int eventId) {
        final String sql = "SELECT U.username AS customer_username, " +
                "B.booking_date_and_time, " +
                "T.id AS ticket_id, " +
                "T.ticket_price, " +
                "S.type AS section_type " +
                "FROM Customers C " +
                "JOIN Users U ON C.id = U.id " +
                "JOIN Booking B ON C.id = B.customer_id " +
                "JOIN Ticket T ON B.id = T.booking_id " +
                "JOIN Event E ON T.event_id = E.id " +
                "JOIN Section S ON T.section_id = S.id " +
                "WHERE E.id = ?";

        ArrayList<EventBookingDetail> bookingDetails = new ArrayList<>();

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                EventBookingDetail detail = new EventBookingDetail();

                detail.setCustomerUsername(rs.getString("customer_username"));
                detail.setBookingDateAndTime(rs.getTimestamp("booking_date_and_time"));
                detail.setTicketId(rs.getInt("ticket_id"));
                detail.setTicketPrice(rs.getDouble("ticket_price"));
                detail.setSectionType(rs.getString("section_type"));

                bookingDetails.add(detail);
            }

            return bookingDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}