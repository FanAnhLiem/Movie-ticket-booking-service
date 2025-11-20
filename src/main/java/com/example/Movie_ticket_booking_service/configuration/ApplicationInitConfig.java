package com.example.Movie_ticket_booking_service.configuration;

import com.example.Movie_ticket_booking_service.entity.CinemaTypeEntity;
import com.example.Movie_ticket_booking_service.entity.RoleEntity;
import com.example.Movie_ticket_booking_service.entity.SeatTypeEntity;
import com.example.Movie_ticket_booking_service.entity.UserEntity;
import com.example.Movie_ticket_booking_service.enums.CinemaType;
import com.example.Movie_ticket_booking_service.enums.PredefinedRoles;
import com.example.Movie_ticket_booking_service.enums.SeatType;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.CinemaTypeRepository;
import com.example.Movie_ticket_booking_service.repository.RoleRepository;
import com.example.Movie_ticket_booking_service.repository.SeatTypeRepository;
import com.example.Movie_ticket_booking_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    private final PasswordEncoder  passwordEncoder;
    private final CinemaTypeRepository cinemaTypeRepository;
    private final SeatTypeRepository seatTypeRepository;

    @NonFinal
    private static final String ADMIN_USER_NAME = "admin@gmail.com";

    @NonFinal
    private static final String ADMIN_PASSWORD = "admin";


    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {

        return args -> {
            List<String> predefinedRoles = List.of(
                    PredefinedRoles.ADMIN,
                    PredefinedRoles.USER,
                    PredefinedRoles.STAFF
            );

            predefinedRoles.forEach(roleName -> {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            log.info("Creating role: {}", roleName);
                            return roleRepository.save(
                                    RoleEntity.builder()
                                            .name(roleName)
                                            .build()
                            );
                        });
            });

            for (CinemaType type : CinemaType.values()) {
                cinemaTypeRepository.findByName(type.getName())
                        .orElseGet(() -> {
                            return cinemaTypeRepository.save(
                                    CinemaTypeEntity.builder()
                                            .name(type.getName())
                                            .priceFactor(type.getPriceFactor())
                                            .build()
                            );
                        });
            }

            for (SeatType type : SeatType.values()) {
                seatTypeRepository.findByName(type.getName())
                        .orElseGet(() -> {
                            return seatTypeRepository.save(
                                    SeatTypeEntity.builder()
                                            .name(type.getName())
                                            .priceFactor(type.getPriceFactor())
                                            .build()
                            );
                        });
            }


            //Tạo tài khoản admin mặc định nếu chưa có
            userRepository.findByEmail(ADMIN_USER_NAME)
                    .or(() -> {
                        RoleEntity adminRole = roleRepository.findByName(PredefinedRoles.ADMIN)
                                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

                        UserEntity admin = UserEntity.builder()
                                .email(ADMIN_USER_NAME)
                                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                                .role(adminRole)
                                .status(true)
                                .build();

                        userRepository.save(admin);
                        log.warn(" Admin user has been created with default password: '{}'. Please change it!", ADMIN_PASSWORD);
                        return Optional.of(admin);
                    });
            log.info("Application initialization completed.");
        };
    }

}
