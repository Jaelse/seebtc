package com.jaelse.seebtc.lib.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDto {

    private String datetime;
    private String amount;


}
