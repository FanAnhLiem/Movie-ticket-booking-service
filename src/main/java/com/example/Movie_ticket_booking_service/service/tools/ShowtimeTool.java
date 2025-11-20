package com.example.Movie_ticket_booking_service.service.tools;

import com.example.Movie_ticket_booking_service.dto.chatResponse.ShowTimeDto;
import com.example.Movie_ticket_booking_service.service.Impl.ShowTimeServiceImpl;
import com.example.Movie_ticket_booking_service.service.ShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.lang.Nullable;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowtimeTool {

    private final ShowTimeService showTimeService;

    @Tool(description = """
    Tìm các suất chiếu phim trong hệ thống.
    Dùng khi người dùng hỏi về giờ chiếu / suất chiếu / lịch chiếu.
    """)
    public List<ShowTimeDto> searchShowtimes(
            @ToolParam(description = "Tên phim mà người dùng muốn xem") String movieName,
            @ToolParam(description = "Tên rạp ") String cinemaName,
            @ToolParam(description = "Ngày chiếu dạng yyyy-MM-dd (có thể null nếu không chỉ rõ) ") @Nullable String showDate
    ){
        LocalDate date = null;
        if (showDate != null && !showDate.isBlank()) {
            date = LocalDate.parse(showDate);
        }else {
            date = LocalDate.now();
        }

        List<ShowTimeDto> showTimeList = showTimeService.getShowTimeDto(movieName, cinemaName, date);
        return showTimeList;
    }

}
