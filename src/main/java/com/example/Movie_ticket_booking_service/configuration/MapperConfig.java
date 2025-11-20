package com.example.Movie_ticket_booking_service.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Cấu hình ModelMapper để bỏ qua giá trị null
        modelMapper.getConfiguration()
                .setPropertyCondition(context -> context.getSource() != null) // Chỉ ánh xạ nếu nguồn không phải null
                .setMatchingStrategy(MatchingStrategies.STRICT); // Sử dụng chiến lược khớp nghiêm ngặt

        return modelMapper;
    }
}
