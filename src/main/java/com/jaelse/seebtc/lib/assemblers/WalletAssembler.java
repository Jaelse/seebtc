package com.jaelse.seebtc.lib.assemblers;


import com.jaelse.seebtc.lib.models.WalletModel;
import com.jaelse.seebtc.lib.models.WalletModelList;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

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


    public WalletQuery assemble(MultiValueMap<String, String> params) {
        var queryBuilder = WalletQuery.builder();

        if (params.get("offset") != null) {
            queryBuilder.offset(Long.parseLong(params.get("offset").get(0)));
        }

        if (params.get("limit") != null) {
            queryBuilder.limit(Integer.getInteger(params.get("limit").get(0)));
        }

        if (params.get("ids") != null) {
            var ids = params.get("ids")
                    .stream()
                    .map(ObjectId::new)
                    .collect(Collectors.toSet());
            queryBuilder.ids(ids);
        }

        return queryBuilder.build();
    }
}
