package com.ordadata.pack.charm.watch;

import com.google.gson.Gson;
import com.ordadata.pack.charm.db.DbController;
import okhttp3.*;
import okio.ByteString;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Component
public class watchSocket extends WebSocketListener {

    @Resource(name = "dbSvc")
    private DbController dbSvc;

    @Autowired
    private WatchSocketClient watchSocketClient;

    private Gson gson = new Gson();
    static List dataList = new ArrayList();
    static List summaryList = new ArrayList();

    static String[] keys = {"binance/btcusdt", "bitfinex/btcusd", "bitflyer/btcjpy", "bithumb/btckrw", "bitmex/btcusd-perpetual-future-inverse", "coinbase-pro/btcusd", "kraken/btceur", "kraken/btcusd", "kraken-futures/btcusd-perpetual-future-inverse", "bitfinex/ethusd", "kraken/ethbtc", "kraken/etheur", "bitfinex/xrpusd", "kraken/xrpeur", "binance/eosusdt"};
    static String[] ids = {"579", "1", "215", "373", "247", "65", "86", "87", "5545", "49678", "95", "97", "25", "138", "1650"};
    OkHttpClient client = new OkHttpClient();

    @PostConstruct
    public void getData() {
        try {
            for (int i = 0; i < keys.length; i++) {
                String url = "https://api.cryptowat.ch/markets/" + keys[i] + "/trades?apikey=BTEDVWWYTD69PFRJSV15&limit=1";
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder().url(url).get();
                Request request = builder.build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String str = body.string();
                        String[] resultArr = str.split(":\\[\\[")[1].split("]]")[0].split("],\\[");
                        Map<String, List> temp = new HashMap<String, List>();
                        List tmpList = new ArrayList();
                        int k = 0;
                        while (resultArr.length > k) {
                            tmpList.add(resultArr[k].substring(2));
                            k++;
                        }
                        temp.put("trades", tmpList);
                        dataList.add(temp);
                    }
                } else
                    System.err.println("Error Occurred");
            }

            for (int i = 0; i < keys.length; i++) {
                String url = "https://api.cryptowat.ch/markets/" + keys[i] + "/summary?apikey=BTEDVWWYTD69PFRJSV15";
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder().url(url).get();
                Request request = builder.build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String str = body.string();
                        //System.out.println(str);
                        String last = str.split("last\":")[1].split(",")[0];
                        String percent = str.split("percentage\":")[1].split(",")[0];
                        String volume = str.split("volume\":")[1].split(",")[0];
                        Map temp = new HashMap();
                        temp.put("summary", last + "," + percent + "," + volume);
                        summaryList.add(temp);
                    }
                } else
                    System.err.println("Error Occurred");
            }

            webSocketConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void webSocketConnect() throws Exception {
        try {
            Request request = new Request.Builder().url("wss://stream.cryptowat.ch/connect?apikey=BTEDVWWYTD69PFRJSV15").build();
            client.newWebSocket(request, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        String subscribe = "";
        for (String id : ids) {
            subscribe += "{\"streamSubscription\":{\"resource\":\"markets:" + id + ":trades\"}},";
            subscribe += "{\"streamSubscription\":{\"resource\":\"markets:" + id + ":summary\"}},";
        }
        subscribe = subscribe.substring(0, subscribe.length() - 1);
        webSocket.send("{\"subscribe\":{\"subscriptions\":[" + subscribe + "]}}");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("MESSAGE: " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        try {
            byte[] byteString = Hex.decodeHex(bytes.hex().toCharArray());
            String data = new String(byteString, "UTF-8");
            if (!data.contains("marketUpdate")) return;
            WatchVO watchVO = gson.fromJson(data, WatchVO.class);
            int index = 0;
            for (int i = 0; i < ids.length; i++) if (ids[i].equals(watchVO.getMarketUpdate().getMarket().getMarketId())) {index = i;break;}
            if (watchVO.getMarketUpdate().getTradesUpdate() != null) {
                if (watchVO.getMarketUpdate().getTradesUpdate().getTrades().size() > 0) {
                    for (WatchTrade wt : watchVO.getMarketUpdate().getTradesUpdate().getTrades()) {
                        String temp = "";
                        temp += wt.getTimestamp() + ",";  //시간
                        temp += wt.getPriceStr() + ",";   //가격
                        temp += wt.getAmountStr() + ",";  //수량
                        temp += wt.getOrderSide() + ","; //매수매도
                        temp += keys[index]; //키

                        Map detail = (Map) dataList.get(index);
                        List trades = (List) detail.get("trades");
                        if(trades.size() == 50) trades.remove(0);
                        trades.add(temp);
                        watchSocketClient.sendMarketData(new TextMessage("2" + (String) ((List)((Map) dataList.get(index)).get("trades")).get(((List)((Map) dataList.get(index)).get("trades")).size()-1) ) );
                    }
                }
            } else if (watchVO.getMarketUpdate().getSummaryUpdate() != null) {
                String temp = watchVO.getMarketUpdate().getSummaryUpdate().getLastStr() + ",";
                temp += watchVO.getMarketUpdate().getSummaryUpdate().getChangePercentStr() + ",";
                temp += watchVO.getMarketUpdate().getSummaryUpdate().getVolumeBaseStr();

                Map detail = new HashMap();
                detail.put("summary", temp);
                summaryList.set(index, detail);

                temp = keys[index] + "," + temp;
                watchSocketClient.sendMarketData(new TextMessage("3" + temp ));
            }
            //watchSocketClient.sendMarketData(new TextMessage(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }
}