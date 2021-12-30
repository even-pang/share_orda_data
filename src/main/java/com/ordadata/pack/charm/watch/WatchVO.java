package com.ordadata.pack.charm.watch;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class WatchVO {
    @SerializedName("marketUpdate") private WatchMarketUpdate marketUpdate;
}

@Getter
@ToString
class WatchMarketUpdate {
    private WatchMarket market;
    private WatchSummaryUpdate summaryUpdate;
    private WatchTradesUpdate tradesUpdate;
}

@Getter
@ToString
class WatchMarket {
    private String exchangeId;
    private String currencyPairId;
    private String marketId;
}

@Getter
@ToString
class WatchSummaryUpdate {
    private String lastStr;
    private String highStr;
    private String lowStr;
    private String volumeBaseStr;
    private String volumeQuoteStr;
    private String changeAbsoluteStr;
    private String changePercentStr;
}

@Getter
@ToString
class WatchTradesUpdate {
    private List<WatchTrade> trades;
}

@Getter
@ToString
class WatchTrade {
    private String externalId;
    private String timestamp;
    private String timestampNano;
    private String priceStr;
    private String amountStr;
    private String amountQuoteStr;
    private String orderSide;
}