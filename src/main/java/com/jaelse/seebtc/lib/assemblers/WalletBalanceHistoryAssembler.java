package com.jaelse.seebtc.lib.assemblers;

import com.jaelse.seebtc.lib.models.WalletBalanceBetweenHoursModelList;
import com.jaelse.seebtc.lib.models.WalletBalanceHistoryModel;
import com.jaelse.seebtc.lib.models.WalletBalanceHistoryModelList;
import com.jaelse.seebtc.lib.queries.WalletBalanceHistoryQuery;
import com.jaelse.seebtc.resources.WalletBalanceHistory.domain.WalletBalanceHistoryEntity;
import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WalletBalanceHistoryAssembler {


    public WalletBalanceHistoryModel assemble(WalletBalanceHistoryEntity entity) {
        WalletBalanceHistoryModel model = WalletBalanceHistoryModel.builder()
                .id(entity.getId().toHexString())
                .walletId(entity.getWalletId().toHexString())
                .year(entity.getYear())
                .dayOfMonth(entity.getDayOfMonth())
                .hour(entity.getHour())
                .amount(entity.getAmount().bigDecimalValue())
                .dateTime(entity.getDateTime())
                .build();

        return model;
    }

    public WalletBalanceBetweenHoursModelList assembleBalance(List<WalletBalanceHistoryEntity> entities) {
        var builder = WalletBalanceBetweenHoursModelList.builder();
        var balances = entities.stream()
                .map(entity -> WalletBalanceBetweenHoursModelList.Balance.builder()
                        .amount(entity.getAmount().bigDecimalValue())
                        .datetime(LocalDateTime.of(
                                entity.getYear(),
                                entity.getDateTime().getMonth(),
                                entity.getDayOfMonth(),
                                entity.getHour(), 0, 0).toString())
                        .build())
                .toList();
        return builder.history(balances).build();
    }

    public List<WalletBalanceHistoryModel> assemble(List<WalletBalanceHistoryEntity> entities) {
        return entities.stream()
                .map(this::assemble)
                .collect(Collectors.toList());
    }


    public WalletBalanceHistoryQuery assemble(MultiValueMap<String, String> params) {
        var builder = WalletBalanceHistoryQuery.builder();


        if (params.get("offset") != null) {
            builder.offset(Long.parseLong(params.get("offset").get(0)));
        }

        if (params.get("limit") != null) {
            builder.limit(Integer.getInteger(params.get("limit").get(0)));
        }

        if (params.get("ids") != null) {
            var ids = params.get("ids")
                    .stream()
                    .map(ObjectId::new)
                    .collect(Collectors.toSet());
            builder.ids(ids);
        }

        if (params.get("walletIds") != null) {
            var walletIds = params.get("walletIds")
                    .stream()
                    .map(ObjectId::new)
                    .collect(Collectors.toSet());
            builder.walletIds(walletIds);
        }

        if (params.get("years") != null) {
            var years = params.get("years")
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            builder.years(years);
        }

        if (params.get("days") != null) {
            var days = params.get("days")
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            builder.days(days);
        }

        if (params.get("hours") != null) {
            var hours = params.get("hours")
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            builder.hours(hours);
        }

        return builder.build();
    }
}
