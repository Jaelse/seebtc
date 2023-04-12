package com.jaelse.seebtc.lib.dtos;


import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWalletDto {
    private Long initialBalance;
}
