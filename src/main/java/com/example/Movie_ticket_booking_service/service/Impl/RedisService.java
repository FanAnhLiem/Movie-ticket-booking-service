package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.TxnInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final String BLACKLIST_PREFIX = "jwt:blacklist:";

    private String seatKey(long showtimeId, long seatId) {
        return "seat:showtime:" + showtimeId + ":seat:" + seatId;
    }
    private String txnKey(String vnp_TxnRef) {
        return "txnInfo:vnp_TxnRef:" + vnp_TxnRef;
    }

    public void blackList(String jwi, Instant exp) {
        if(jwi == null || exp == null) return;
        long ttlMillis =  exp.toEpochMilli() - Instant.now().toEpochMilli();
        if (ttlMillis <= 0) ttlMillis = 1000;
        String key = BLACKLIST_PREFIX + jwi;
        redisTemplate.opsForValue().set(key,"1", ttlMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isBlackList(String jwi) {
        if(jwi == null || jwi.isEmpty()) return false;
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jwi));
    }

    public void seatHolding(Long showTimeId, Long seatId, long ttl) {
        if(showTimeId == null || seatId == null) return;
        String key = seatKey(showTimeId, seatId);
        redisTemplate.opsForValue().set(key,"1", ttl, TimeUnit.MINUTES);
    }

    public boolean isSeatHoldingList(Long showTimeId, Long seatId) {
        if(showTimeId == null || seatId == null) return false;
        return Boolean.TRUE.equals(redisTemplate.hasKey(seatKey(showTimeId, seatId)));
    }

    public void txnRefInfo(String vnp_TxnRef, TxnInfo  txnInfo, long ttl) {
        if(vnp_TxnRef == null || txnInfo == null) return;
        String txnKey = txnKey(vnp_TxnRef);
        redisTemplate.opsForValue().set(txnKey, txnInfo, ttl, TimeUnit.MINUTES);
    }

    public TxnInfo getByTxnRef(String vnp_TxnRef) {
        if (vnp_TxnRef == null || vnp_TxnRef.isBlank()) return null;

        Object raw = redisTemplate.opsForValue().get(txnKey(vnp_TxnRef));
        if (raw == null) return null;

        if (raw instanceof TxnInfo) return (TxnInfo) raw; // trường hợp có type-info
        try {
            return objectMapper.convertValue(raw, TxnInfo.class); // Map -> TxnInfo
        } catch (IllegalArgumentException e) {
            log.warn("Cannot convert redis value for key {} to TxnInfo. Type={}", txnKey(vnp_TxnRef), raw.getClass(), e);
            return null;
        }
    }

    public void deleteSeatHold(Long showTimeId,  Long seatId) {
        String key = seatKey(showTimeId, seatId);
        redisTemplate.delete(key);
    }
}
