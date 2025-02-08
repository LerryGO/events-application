package com.lapdev.eventos_api.controller;

import com.lapdev.eventos_api.domain.coupon.Coupon;
import com.lapdev.eventos_api.domain.coupon.CouponRequestDTO;
import com.lapdev.eventos_api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO couponRequest){
        Coupon coupons = couponService.addCouponToEvent(eventId, couponRequest);
        return ResponseEntity.ok(coupons);
    }
}
