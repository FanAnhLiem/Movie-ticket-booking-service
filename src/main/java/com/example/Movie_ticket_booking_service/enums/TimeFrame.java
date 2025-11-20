package com.example.Movie_ticket_booking_service.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalTime;
@Getter
public enum TimeFrame {
    MORNING("Morning",LocalTime.of(8, 0), LocalTime.of(11, 59), 1.0),
    NOON("Noon",LocalTime.of(12, 0), LocalTime.of(16, 59), 1.05),
    AFTERNOON("Afternoon",LocalTime.of(17, 0), LocalTime.of(22, 59),1.1),
    EVENING("Evening",LocalTime.of(23, 0), LocalTime.of(2, 59), 1.15);


    private final String timeFrame;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final double priceFactor;

    TimeFrame(String timeFrame, LocalTime startTime, LocalTime endTime, double priceFactor) {
        this.timeFrame = timeFrame;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priceFactor = priceFactor;
    }

    public boolean isInSlot(LocalTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    public static TimeFrame from(LocalTime time) {
        for (TimeFrame s : values()) {
            if (s.isInSlot(time)) return s;
        }
        return MORNING;
    }

    public static TimeFrame fromLabel(String label) {
        for (TimeFrame tf : values()) {
            if (tf.getTimeFrame().equalsIgnoreCase(label)) {
                return tf;
            }
        }
        throw new IllegalArgumentException("Unknown time frame: " + label);
    }

}
