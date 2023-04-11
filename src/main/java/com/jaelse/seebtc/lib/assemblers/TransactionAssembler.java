package com.jaelse.seebtc.lib.assemblers;

import com.jaelse.seebtc.lib.models.TransactionModel;
import com.jaelse.seebtc.lib.queries.TransactionQuery;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionAssembler {

    public TransactionModel assemble(TransactionEntity entity) {
        TransactionModel model = TransactionModel.builder()
                .id(entity.getId().toHexString())
                .walletId(entity.getWalletId().toHexString())
                .dateTime(entity.getDateTime().toString())
                .amount(entity.getAmount().bigDecimalValue())
                .build();
        return model;
    }

    public List<TransactionModel> assemble(List<TransactionEntity> entity) {
        return entity.stream()
                .map(this::assemble)
                .collect(Collectors.toList());
    }


    public TransactionQuery assemble(MultiValueMap<String, String> params) {
        var queryBuilder = TransactionQuery.builder();

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

        if (params.get("walletIds") != null) {
            var walletIds = params.get("walletIds")
                    .stream()
                    .map(ObjectId::new)
                    .collect(Collectors.toSet());
            queryBuilder.walletIds(walletIds);
        }

        return queryBuilder.build();
    }
}
