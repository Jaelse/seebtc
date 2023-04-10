package com.jaelse.seebtc.lib.dtos;


import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class CreateWalletDto {

    @NonNull
    @Builder.Default
    private Long initialBalance = 1000L;
}
