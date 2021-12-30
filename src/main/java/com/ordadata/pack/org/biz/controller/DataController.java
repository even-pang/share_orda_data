/*
 * Copyright (c) 2008  sosun . All rights reserved.
 */
package com.ordadata.pack.org.biz.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.CommonUtil;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * Class Summary. <br>
 * 硫붿씤 愿�由� class.
 *
 * @author �씠�쁽�룄
 * @version 1.00 - 2019. 09. 26
 * @see
 * @since 1.00
 */

@Controller
public class DataController {

    private final Gson gson = new Gson();

    @Resource(name = "dbSvc")
    private DbController dbSvc;

    @ResponseBody
    @RequestMapping(value = "/home/data/ordaDataList.do")
    public String ordaDataList(HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession(false);
        Map         reqMap  = CommonUtil.getRequestMap(servletRequest);
        String      strCode = "DUP";
        List        rsList;
        try {
            if (CommonUtil.nvl(reqMap.get("group")).equals("1")) {
                rsList = dbSvc.dbList(reqMap, "data.ordaDataCoinOneList");
            } else {
                rsList = dbSvc.dbList(reqMap, "data.ordaDataCoinList");
            }
            final JsonArray jsonObject = new JsonArray();
            rsList.forEach(data -> {
                final JsonObject object = new JsonObject();
                Map              temp   = (Map) data;
                object.addProperty("BUY_COUNT", CommonUtil.nvl(temp.get("BUY_COUNT")));
                object.addProperty("BUY_QUANTITY", CommonUtil.nvl(temp.get("BUY_QUANTITY")));
                object.addProperty("BUY_PR", CommonUtil.nvl(temp.get("BUY_PR")));
                object.addProperty("BUY_LIQUIDITY", CommonUtil.nvl(temp.get("BUY_LIQUIDITY")));
                object.addProperty("SELL_COUNT", CommonUtil.nvl(temp.get("SELL_COUNT")));
                object.addProperty("SELL_QUANTITY", CommonUtil.nvl(temp.get("SELL_QUANTITY")));
                object.addProperty("SELL_PR", CommonUtil.nvl(temp.get("SELL_PR")));
                object.addProperty("SELL_LIQUIDITY", CommonUtil.nvl(temp.get("SELL_LIQUIDITY")));
                object.addProperty("START_DATE", CommonUtil.nvl(temp.get("START_DATE")));
                object.addProperty("COIN_KIND", CommonUtil.nvl(temp.get("COIN_KINDS")));
                if (CommonUtil.nvl(reqMap.get("group")).equals("1")) {
                    object.addProperty("COUNT", 1);
                } else {
                    object.addProperty("COUNT", CommonUtil.nvl(temp.get("COUNT")));
                }

                jsonObject.add(object);
            });
            strCode = gson.toJson(jsonObject);
        } catch (Exception e) {
            System.out.println(e.toString());
            strCode = "ERROR";
        }

        return strCode;
    }


    @ResponseBody
    @RequestMapping(value = "/home/data/ordaDataRankList.do")
    public String ordaDatarankingList(HttpServletRequest servletRequest,
                                      HttpServletResponse servletResponse) throws Exception {
        HttpSession session = servletRequest.getSession(false);
        Map         reqMap  = CommonUtil.getRequestMap(servletRequest);
        String      strCode = "";
        List        rsList;
        try {
            if (reqMap != null) {
                String table = CommonUtil.nvl(reqMap.get("table"));
                String time  = CommonUtil.nvl(reqMap.get("group"));
                String kind  = CommonUtil.nvl(reqMap.get("coin_kind"));
                System.out.println(CommonUtil.nvl(reqMap.get("table")) + "////" + CommonUtil.nvl(reqMap.get("group")) + "////" +
                                   CommonUtil.nvl(reqMap.get("coin_kind")));

                Instant baseTime = Instant.now().truncatedTo(ChronoUnit.MINUTES);
                int     min      = baseTime.atZone(ZoneOffset.UTC).getMinute();
                int     hour     = baseTime.atZone(ZoneOffset.UTC).getHour();
                int     week     = baseTime.atZone(ZoneOffset.UTC).getDayOfWeek().getValue(); // 0 - 일, ..., 6 - 토
                int     date     = baseTime.atZone(ZoneOffset.UTC).getDayOfMonth();

                switch (time) {
                    case "1":  // 1분
                        break;
                    case "3":  // 3분
                        baseTime = baseTime.minus(min % 3, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
                        break;
                    case "5":  // 5분
                        baseTime = baseTime.minus(min % 5, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
                        break;
                    case "10":  // 10분
                        baseTime = baseTime.minus(min % 10, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
                        break;
                    case "15":  // 15분
                        baseTime = baseTime.minus(min % 15, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
                        break;
                    case "30":  // 30분
                        baseTime = baseTime.minus(min % 30, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
                        break;
                    case "60":  // 1시간
                        baseTime = baseTime.minus(min % 60, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
                        break;
                    case "240":  // 4시간
                        baseTime = baseTime.minus(hour % 4, ChronoUnit.HOURS).truncatedTo(ChronoUnit.HOURS);
                        break;
                    case "day":  // 일
                        baseTime = baseTime.minus(hour % 24, ChronoUnit.HOURS).truncatedTo(ChronoUnit.HOURS);
                        break;
                    case "week":  // 주
                        baseTime = baseTime.minus(week % 6, ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
                        break;
                    case "month":  // 월
                        baseTime = baseTime.minus(date - 1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
                        break;
                }
                reqMap.put("STARTDATE", toStringDate(baseTime));
                rsList = dbSvc.dbList(reqMap, "data.ordaDataKindList");

                final JsonArray                jsonObject     = new JsonArray();
                rsList.forEach(data -> {
                    final JsonObject object = new JsonObject();
                    Map              temp   = (Map) data;
                    object.addProperty("RANK", CommonUtil.nvl(temp.get("RANK")));
                    object.addProperty("COIN_KR_NAME",CommonUtil.nvl(temp.get("COIN_KR_NAME")));
                    object.addProperty("COIN_NAME", CommonUtil.nvl(temp.get("COIN_NAME")));
                    object.addProperty("BUY_COUNT", CommonUtil.nvl(temp.get("BUY_COUNT")));
                    object.addProperty("BUY_QUANTITY", CommonUtil.nvl(temp.get("BUY_QUANTITY")));
                    object.addProperty("BUY_PR", CommonUtil.nvl(temp.get("BUY_PR")));
                    object.addProperty("BUY_LIQUIDITY", CommonUtil.nvl(temp.get("BUY_LIQUIDITY")));
                    object.addProperty("SELL_COUNT", CommonUtil.nvl(temp.get("SELL_COUNT")));
                    object.addProperty("SELL_QUANTITY", CommonUtil.nvl(temp.get("SELL_QUANTITY")));
                    object.addProperty("SELL_PR", CommonUtil.nvl(temp.get("SELL_PR")));
                    object.addProperty("SELL_LIQUIDITY", CommonUtil.nvl(temp.get("SELL_LIQUIDITY")));
                    object.addProperty("START_DATE", CommonUtil.nvl(temp.get("START_DATE")));
                    object.addProperty("COUNT", CommonUtil.nvl(temp.get("COUNT")));
                    object.addProperty("COIN_KIND", CommonUtil.nvl(temp.get("COIN_KINDS")));
                    jsonObject.add(object);
                });
                strCode = gson.toJson(jsonObject);
            }


        } catch (Exception e) {
            e.printStackTrace();
            strCode = "ERROR";
        }

        return strCode;
    }

    @NonNull
    private String toStringDate(@NonNull final Instant time) {
        return time.toString().replaceAll("[TZ]", " ").trim();
    }

}
