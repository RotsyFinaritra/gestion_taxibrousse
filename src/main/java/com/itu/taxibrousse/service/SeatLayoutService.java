package com.itu.taxibrousse.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeatLayoutService {
    /**
     * Generate seat layout HTML for the vehicle passenger seats and driver seat.
     * The returned HTML is intended to be injected with Thymeleaf th:utext (unescaped).
     *
     * @param capacity number of passenger seats (driver NOT included)
     * @param reservedSeats list of reserved seat numbers (1-based)
     * @return HTML string representing the seat map
     */
    public String generateSeatLayoutHtml(int capacity, List<Integer> reservedSeats) {
        return generateSeatLayoutHtml(capacity, reservedSeats, null);
    }

    /**
     * Generate seat layout HTML allowing to mark seats as belonging to a class.
     * @param capacity passenger seat count
     * @param reservedSeats list of reserved seat numbers
     * @param seatToClass mapping seatNumber -> classId (nullable)
     * @return HTML string
     */
    public String generateSeatLayoutHtml(int capacity, List<Integer> reservedSeats, java.util.Map<Integer,Integer> seatToClass) {
        StringBuilder sb = new StringBuilder();
        java.util.Set<Integer> reserved = new java.util.HashSet<>();
        if (reservedSeats != null) reserved.addAll(reservedSeats);
        java.util.Map<Integer,Integer> classMap = seatToClass != null ? seatToClass : new java.util.HashMap<>();
        sb.append("<div class=\"seat-rows\">\n");
        // front row: driver + two passenger seats beside the driver (if available)
        sb.append("  <div class=\"seat-row front-row\">\n");
        sb.append("    <div class=\"driver-seat\">Ch</div>\n");
        sb.append("    <div class=\"seat-block front-block\">\n");
        int seatIndex = 1;
        // put up to 2 seats beside the driver
        for (int i = 0; i < 2; i++) {
            appendSeatHtml(sb, seatIndex, capacity, reserved, classMap);
            seatIndex++;
        }
        sb.append("    </div>\n");
        sb.append("  </div>\n");
        // remaining seats: rows with 2 left + aisle + 2 right
        int remaining = capacity - (seatIndex - 1);
        if (remaining <= 0) {
            sb.append("</div>\n");
            return sb.toString();
        }
        int rows = (int) Math.ceil((double) remaining / 4);
        for (int r = 0; r < rows; r++) {
            sb.append("  <div class=\"seat-row\">\n");
            // left block: up to 2 seats
            sb.append("    <div class=\"seat-block left-block\">\n");
            for (int i = 0; i < 2; i++) {
                appendSeatHtml(sb, seatIndex, capacity, reserved, classMap);
                seatIndex++;
            }
            sb.append("    </div>\n");
            // aisle
            sb.append("    <div class=\"aisle\"></div>\n");
            // right block: up to 2 seats
            sb.append("    <div class=\"seat-block right-block\">\n");
            for (int i = 0; i < 2; i++) {
                appendSeatHtml(sb, seatIndex, capacity, reserved, classMap);
                seatIndex++;
            }
            sb.append("    </div>\n");
            sb.append("  </div>\n");
        }
        sb.append("</div>\n");
        return sb.toString();
    }
    private void appendSeatHtml(StringBuilder sb, int seatIndex, int capacity, java.util.Set<Integer> reserved, java.util.Map<Integer,Integer> classMap) {
        if (seatIndex > capacity) {
            sb.append("      <div style=\"width:44px;visibility:hidden\"></div>\n");
            return;
        }
        Integer clsId = classMap.get(seatIndex);
        String classAttr = "";
        // Reserved seats must not be selectable: treat reserved as highest priority
        if (reserved.contains(seatIndex)) {
            classAttr = " class=\"seat reserved\"";
        } else if (clsId != null) {
            classAttr = " class=\"seat available class-" + clsId + "\"";
        } else {
            classAttr = " class=\"seat available\" style=\"background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%); border: 2px solid #86efac;\"";
        }
            sb.append("      <div" + classAttr + " data-seat=\"").append(seatIndex).append("\">").append(seatIndex).append("</div>\n");
    }
    private int seatsPerRowFor(int cap) {
        if (cap >= 20) return 4;
        if (cap >= 12) return 3;
        if (cap >= 6) return 2;
        return Math.max(1, cap);
    }
}
