package com.jaelse.seebtc.lib.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceHistoryBetweenHoursDto {

    private String startDatetime;
    private String endDatetime;
}
