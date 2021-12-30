<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/home/header.do"/>
    <script>

    </script>
</head>

<body>
<div id="wrap">

    <!-- header -->
    <jsp:include page="/home/topMenu.do"/>
    <!-- //header -->

    <!-- 중간 섹션 -->
    <section id="cont-left" class="o-tab-menu">
        <!-- 상단 헤더 -->
        <div class="cl-header">
            <ul class="ui-tab-menu tab-cont-setting">
                <li class="on">
                    <a href="#u-multiMode" class="txt">오르다데이터</a>
                </li>
                <%--                <li>--%>
                <%--                    <a href="#u-multiMode" class="txt">프리미엄/스프레드</a>--%>
                <%--                </li>--%>
                <%--                <li>--%>
                <%--                    <a href="#u-multiMode" class="txt">펀딩수수료</a>--%>
                <%--                </li>--%>
                <%--                <li>--%>
                <%--                    <a href="#u-multiMode" class="txt">리더보드</a>--%>
                <%--                </li>--%>
                <%--                <li>--%>
                <%--                    <a href="#u-multiMode" class="txt">온 체인</a>--%>
                <%--                </li>--%>
                <%--                <li>--%>
                <%--                    <a href="#u-multiMode" class="txt">백테스트 모드</a>--%>
                <%--                </li>--%>
            </ul>
        </div>
        <!-- //상단 헤더 -->

        <!-- 중간 하단 주문 테이블 -->
        <div class="cl-row1 cl-row1--main" id="mainList" onscroll="scrollChanging()">
            <div class="list-user-batting">
                <!-- 멀티거래 모드 -->
                <div class="ui-tab-cont tab-on u-multiMode" id="u-multiMode">
                    <div class="area-right">
                        <ul class="tab-currency" id="tab-division-time">
                            <li onclick="fTapTimeDivision('1');" class="on"><a href="#">1분</a></li>
                            <li onclick="fTapTimeDivision('15');"><a href="#">15분</a></li>
                            <li onclick="fTapTimeDivision('60');"><a href="#">1시간</a></li>
                            <li onclick="fTapTimeDivision('240');" ><a href="#">4시간</a></li>
                            <li onclick="fTapTimeDivision('day');"><a href="#">1일</a></li>
                        </ul>
                        <span class="select-base sel-time time-spen">
                            <select class="select-st1 time-select" onChange="fSelectTimeDivision(this.value);">
                                <option class="hideme">시간</option>
                                <option value="1">1분</option>
                                <option value="3">3분</option>
                                <option value="5">5분</option>
                                <option value="10">10분</option>
                                <option value="15">15분</option>
                                <option value="30">30분</option>
                                <option value="60">1시간</option>
                                <option value="240">4시간</option>
                                <option value="day">1일</option>
                                <option value="week">주</option>
                                <option value="month">달</option>
                            </select>
                        </span>
                        <span class="select-base sel-sort" style="display: none;">
                            <select class="select-st1 soft-select" onChange="fSelectrankbathDivision(this.value);">
                                <option value="buysell">정렬 기준 선택</option>
                                <option value="liquiditysell">매도 유동성</option>
                                <option value="countsell">매도 건수</option>
                                <option value="quantitysell">매도 수량</option>
                                <option value="pricesell">매도 금액</option>
                                <option value="liquiditybuy">매수 금액</option>
                                <option value="countbuy">매수 수량</option>
                                <option value="quantitybuy">매수 건수</option>
                                <option value="pricebuy">매수 유동성</option>
                            </select>
                        </span>
                        <ul class="tab-currency" id="tab-division-dataMode">
                            <li id="kindLi" onclick="dataModeDivision('kind');" class="on"><a href="#">종목별 데이터</a></li>
                            <li id="rankLi" onclick="dataModeDivision('rank');"><a href="#">순위별 데이터</a></li>
                        </ul>
                    </div>

                    <div class="tcs-cont tcs-on u-multi" id="u-multi">
                        <!-- 1 -->
                        <div class="row-table rt1">
                            <div class="tbl_scroll_wrap">
                                <table id="ordRltdLsTbl" class="c-table">
                                    <caption>주문 관련 리스트</caption>
                                    <colgroup>
                                        <col width="8%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
                                        <col width="12%"/>
                                        <col width="11%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
                                        <col width="11%"/>
                                        <col width="8%"/>
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th class="center">시&nbsp;&nbsp;&nbsp;간</th>
                                        <th class="c-red">유동성(KRW)</th>
                                        <th class="c-red">매도건수(건)</th>
                                        <th class="c-red">매도량(BTC)</th>
                                        <th class="c-red">매도금액(KRW)</th>
                                        <th class="c-green">매수금액(KRW)</th>
                                        <th class="c-green">매수량(BTC)</th>
                                        <th class="c-green">매수건수(건)</th>
                                        <th class="c-green">유동성(KRW)</th>
                                        <th class="center">
                                                    <span class="select-base sel-time">
                                                        <select class="select-st1">
                                                            <option class="hideme">지표</option>
                                                            <option>지표1</option>
                                                        </select>
                                                    </span>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="conclusion-listbox">
            <div class="cl-header">
                <ul class="ui-tab-menu tab-cont-setting">
                    <li class="on">
                        <a href="#" class="txt">체결 데이터</a>
                    </li>
                </ul>
            </div>
            <div class="conclusion-list">
                <div class="ncscroll">
                    <div class="row-table rt1">
                        <table id="tradeInfoTbl" class="c-table c-table--large">
                            <colgroup>
                                <col width="*" style="text-align:center;"/>
                                <col width="15.5%" style="text-align:center;"/>
                                <col width="21%"/>
                                <col width="21%"/>
                                <col width="21%"/>
                            </colgroup>
                            <thead>
                            <tr id="tradeInfo">
                                <th>거래 시간</th>
                                <th>거래 유형</th>
                                <th>체결 가격(KRW)</th>
                                <th>체결량(BTC)</th>
                                <th>체결금액(KRW)</th>
                            </tr>
                            </thead>
                            <tbody id="tradeList">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- //중간 하단 주문 테이블 -->
    </section>
    <!-- //중간 섹션 -->

    <!-- 오른쪽 섹션 -->
    <section id="cont-right" class="">

        <div class="cl-header">
            <ul class="ui-tab-menu tab-cont-setting">
                <li class="on">
                    <a href="#" class="txt">시세 리스트</a>
                </li>
                <li class="selectExchangeLi">
                    <select class="select-st1" id="exchange" onchange="selectChange(this.value)">
                        <option data-image="/images/logo-upbit.png" value="Upbit">UPBIT</option>
                        <option data-image="/images/logo-binance.png" value="BINANCE">BINANCE</option>
                        <%--                        <option data-image="/images/logo-huobi.png">HUOBI</option>--%>
                    </select>
                </li>
            </ul>
        </div>
        <div class="cr-row2">
            <div class="cr2-header">
                <ul class="tab-currency" id="quoteTab">

                </ul>
                <div class="user-fav">
                    <button><span class="icon-star">☆</span> 즐겨찾기</button>
                </div>
            </div>
            <div class="list-real-currency">
                <div class="area-search"><input type="text" class="input-base inp-st1" id="search" onkeyup="filter()"
                                                placeholder="찾으실 코인명 또는 심볼을 입력하세요."></div>
                <table id="nowCoinTheadTbl" class="table-st2">
                    <caption>코인별 현재가 현황</caption>
                    <colgroup>
                        <col width="24%">
                        <col width="24%">
                        <col width="26%">
                        <col width="31%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="left small">코인명</th>
                        <th>
                            <button class="tbtn-updn">현재가</button>
                        </th>
                        <th>
                            <button class="tbtn-updn">일간범위</button>
                        </th>
                        <th>
                            <button class="tbtn-updn">거래대금</button>
                        </th>
                    </tr>
                    </thead>
                </table>
                <div class="ncscroll">
                    <table id="nowCoinTbodyTbl" class="table-st2">
                        <caption>코인별 현재가 현황</caption>
                        <colgroup>
                            <col width="24%">
                            <col width="24%">
                            <col width="26%">
                            <col width="31%">
                        </colgroup>
                        <tbody id="quoteList">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="watch-list">
            <div class="cl-header">
                <ul class="ui-tab-menu tab-cont-setting">
                    <li class="on">
                        <a href="#" class="txt">와치 리스트</a>
                    </li>
                </ul>
            </div>
            <div class="ncscroll">
                <table id="watch_list_tbl" class="watch-list_table">
                    <colgroup>
                        <col width="*"/>
                        <col width="34%"/>
                        <col width="25%"/>
                    </colgroup>
                    <tbody>
                    <tr>
                        <td class="left">
                            <strong>BINANCE BTCUSDT</strong>
                            40678.14
                        </td>
                        <td class="up">+12.12 %</td>
                        <td class="right">99.99 K</td>
                    </tr>
                    <tr>
                        <td class="left">
                            <strong>BINANCE BTCUSDT</strong>
                            40678.14
                        </td>
                        <td class="up">+12.12 %</td>
                        <td class="right">99.99 K</td>
                    </tr>
                    <tr>
                        <td class="left">
                            <strong>BINANCE BTCUSDT</strong>
                            40678.14
                        </td>
                        <td class="down">-12.12 %</td>
                        <td class="right">99.99 K</td>
                    </tr>
                    <tr>
                        <td class="left">
                            <strong>BINANCE BTCUSDT</strong>
                            40678.14
                        </td>
                        <td class="up">+12.12 %</td>
                        <td class="right">99.99 K</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="watch-data">
            <div class="cl-header">
                <ul class="ui-tab-menu tab-cont-setting">
                    <li class="on">
                        <a href="#" class="txt">와치 데이터</a>
                    </li>
                </ul>
            </div>
            <div class="conclusion-list">
                <div class="ncscroll">
                    <table id="watch_tbl" class="watch-data_table">
                        <colgroup>
                            <col width="30%"/>
                            <col width="40%"/>
                            <col width="30%"/>
                        </colgroup>
                        <tbody>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left down">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left down">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left down">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left down">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        <tr>
                            <td class="left up">40678.14</td>
                            <td>123.12345678</td>
                            <td class="right">04 : 58 : 35</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!-- //오른쪽 섹션 하단(실시간 현황) -->
    </section>
    <!-- //오른쪽 섹션 -->
    <!-- footer -->
    <div class="footer">
        <div class="f-inner">
            <div class="f-box">
                <img class="footerLogo" src="/images/footer_logo.png" />
                <div class="f-menu">
                    <ul>
                        <li><a href="#">News</a></li>
                        <li><a href="#">About us</a></li>
                        <li><a href="#">Support</a></li>
                        <li><a href="#">Terms of use</a></li>
                        <li><a href="#">Privacy policy</a></li>
                    </ul>
                </div>
            </div>
            <p class="f-right">Ordadata@gmail.com</p>
            <p class="f-copyright">Copyright &copy;2021 ORDADATA Technologies Holding Company. All Rights Reserved</p>
        </div>
    </div>
    <!-- //footer -->
</div><!-- //wrap-inner -->
</div><!-- //wrap -->

<script>
    $(function () {
        //custom scroll
        $('.ncscroll').niceScroll('table', frontScript.nsOption());


        //거래소 select 없는경우 팝업
        $('.sel-exchange select').on('change', function () {
            if ($(this).val() == 'none') {
                //service-lpu.html
                $.magnificPopup.open({
                    items: {
                        src: 'service-lpu.html'
                    },
                    type: 'ajax',
                    showCloseBtn: false,
                    callbacks: {
                        parseAjax: function (mfpResponse) {
                            mfpResponse.data = $(mfpResponse.data).filter('.wrap-popup');
                        },
                        ajaxContentAdded: function () {
                            //console.log('ajaxContentAdded = ', this.content);
                        }
                    }
                });
            }
        });

        //실시간 리스트 ☆ 클릭
        $('.list-real-currency .icon-star').on('click', function (e) {
            if ($(this).hasClass('is-chk')) {
                $(this).text('☆');
                $(this).removeClass('is-chk');
            } else {
                $(this).text('★');
                $(this).addClass('is-chk');
            }
            e.preventDefault();
        });

        //실시간 리스트 현재가, 일간범위, 거래대금 클릭
        $('.list-real-currency .tbtn-updn').on('click', function () {
            if (!$(this).hasClass('down') && !$(this).hasClass('up')) {
                $('.list-real-currency .tbtn-updn.down').removeClass('down');
                $('.list-real-currency .tbtn-updn.up').removeClass('up');
            }

            if ($(this).hasClass('down')) {
                $(this).removeClass('down');
                $(this).addClass('up');

            } else if ($(this).hasClass('up')) {
                $(this).removeClass('up');
                $(this).addClass('down');
            } else {
                $(this).addClass('down');
            }

            updowncoin(this);
        });



        //탭 버튼 클릭 이벤트(단순 클래스 on 컨트롤)
        $('.cr-header .tab-currency a, .list-user-batting .area-right .tab-currency a').on('click', function (e) {
            if (!$(this).closest('li').hasClass('on')) {
                $(this).closest('ul').find('.on').removeClass('on');
                $(this).parent().addClass('on')
            }
            e.preventDefault();
        });




    });
</script>
<script src="/js/particles.min.js"></script>
<script src="/js/particles.app.js"></script>
</body>
</html>