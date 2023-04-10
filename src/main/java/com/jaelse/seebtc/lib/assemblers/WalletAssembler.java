package com.jaelse.seebtc.lib.assemblers;


import com.jaelse.seebtc.lib.models.WalletModel;
import com.jaelse.seebtc.lib.models.WalletModelList;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WalletAssembler {

    public WalletModel assemble(WalletEntity entity) {
        var builder = WalletModel.builder();
        builder.id(entity.getId().toHexString());
        builder.balance(entity.getBalance().bigDecimalValue());

        return builder.build();
    }

    public List<WalletModel> assemble(List<WalletEntity> entities) {
        return entities.stream()
                .map(this::assemble)
                .collect(Collectors.toList());
    }
}
