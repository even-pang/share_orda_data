<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/home/header.do"/>
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
                    <li >
                        <a href="#u-multiMode" class="txt">데이터 모드</a>
                    </li>
                    <li >
                        <a href="#u-multiMode" class="txt">시간별 거래순위</a>
                    </li>
                    <li >
                        <a href="#u-multiMode" class="txt">프리미엄/스프레드</a>
                    </li>
                    <li class="on">
                        <a href="#u-multiMode" class="txt">펀딩수수료</a>
                    </li>
                    <li >
                        <a href="#u-multiMode" class="txt">트레이딩 리더보드</a>
                    </li>
                    <li >
                        <a href="#u-multiMode" class="txt">기관 보유량</a>
                    </li>
                    <li >
                        <a href="#u-multiMode" class="txt">백테스트 모드</a>
                    </li>
                    <!-- <li>
                        <a href="#u-backtestMode" class="txt">백테스트 모드</a>
                    </li> -->
                </ul>
            </div>
            <!-- //상단 헤더 -->

            <!-- 중간 하단 주문 테이블 -->
            <div class="cl-row1 cl-row1--main">
                <div class="list-user-batting">
                    <!-- 멀티거래 모드 -->
                    <div class="ui-tab-cont tab-on u-multiMode" id="u-multiMode">
                        <div class="area-right">
                            <ul class="tab-currency">
                                <li><a href="#">15분</a></li>
                                <li><a href="#">30분</a></li>
                                <li><a href="#">1시간</a></li>
                                <li class="on"><a href="#">4시간</a></li>
                                <li><a href="#">1일</a></li>
                                <li><a href="#">1주</a></li>
                            </ul>
                            <span class="select-base sel-time">
								<select class="select-st1">
									<option class="hideme">시간</option>
									<option>1분</option>
									<option>3분</option>
									<option>5분</option>
									<option>15분</option>
									<option>30분</option>
									<option>1시간</option>
									<option>4시간</option>
									<option>1일</option>
									<option>1주</option>
									<option>1달</option>
								</select>
							</span>
                        </div>

                        <div class="tcs-cont tcs-on u-multi" id="u-multi">
                            <!-- 1 -->
                            <div class="row-table rt1">
                                <table id="ordRltdLsTbl" class="c-table">
                                    <caption>주문 관련 리스트</caption>
                                    <colgroup>
                                        <col width="9%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
                                        <col width="12%"/>
                                        <col width="12%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
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
                                        <th class="c-green">매수량(BTC) </th>
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
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td style="color:red">12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            08.09  12 : 12 <br />
                                            08.09  12 : 13
                                        </td>
                                        <td>12,123,123,123</td>
                                        <td>123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>12,123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>12123123123.12</td>
                                        <td>123,123,123</td>
                                        <td>12,123,123,123</td>
                                        <td>123</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- 트레일링 모드 -->
                        <div class="tcs-cont u-trailing" id="u-trailing">
                            트레일링
                        </div>

                        <!-- 원터치 모드 -->
                        <div class="tcs-cont u-onetouch" id="u-onetouch">
                            원터치
                        </div>
                    </div>

                    <!-- 백테스트 모드 -->
                    <div class="ui-tab-cont u-backtestMode" id="u-backtestMode">
                        <div class="msg-service">
                            <p class="tit">서비스 안내 메시지 !</p>
                            <p class="txt1">
                                선택하신 포지션은 현재 서비스<br />
                                지원 준비 중에 있습니다.<br />
                                불편함이 없도록 최대한 빠른<br />
                                시일 안에 오픈하겠습니다.
                            </p>
                            <p class="txt2">- CRYPTO BOX TEAM -</p>
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
                            <table class="c-table c-table--large">
                                <colgroup>
                                    <col style="width:15%;"/>
                                    <col style="width:15%;"/>
                                    <col style="width:20%;"/>
                                    <col style="width:20%;"/>
                                    <col style="width:20%;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th class="left">거래 시간</th>
                                    <th>거래 유형</th>
                                    <th>체결 가격(KRW)</th>
                                    <th>체결량(BTC) </th>
                                    <th>체결금액(KRW)</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
                                <tr>
                                    <td class="left">08.06  04 : 39</td>
                                    <td>매수</td>
                                    <td>47,113,000</td>
                                    <td>123.12345678</td>
                                    <td class="right">12,123,123,123</td>
                                </tr>
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
        <section id="cont-right" class=""> <!-- ly-cr-st2 ly-cr-st3 -->
            <!-- 오른쪽 섹션 하단(실시간 현황) -->
            <div class="cl-header">
                <ul class="ui-tab-menu tab-cont-setting">
                    <li class="on">
                        <a href="#" class="txt">시세 리스트</a>
                    </li>
                    <li>
                        <select class="select-st1">
                            <option class="hideme">거래소 선택</option>
                            <option data-image="/images/logo-upbit.png">UPBIT</option>
                            <option data-image="/images/logo-binance.png">BINANCE</option>
                            <option data-image="/images/logo-huobi.png">HUOBI</option>
                        </select>
                    </li>
                </ul>
            </div>
            <div class="cr-row2">
                <div class="cr2-header">
                    <ul class="tab-currency">
                        <li class="on"><a href="#">KRW</a></li>
                        <li><a href="#">BTC</a></li>
                        <li><a href="#">ETH</a></li>
                        <li><a href="#">USDT</a></li>
                    </ul>
                    <div class="user-fav"><button><span class="icon-star">☆</span> 즐겨찾기 ▶</button></div>
                </div>
                <div class="list-real-currency">
                    <div class="area-search"><input type="text" class="input-base inp-st1" placeholder="찾으실 코인명 또는 심볼을 입력하세요."></div>
                    <table class="table-st2">
                        <caption>코인별 현재가 현황</caption>
                        <colgroup>
                            <col width="19%">
                            <col width="27%">
                            <col width="26%">
                            <col width="28%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th class="left small">코인명</th>
                            <th><button class="tbtn-updn">현재가</button></th>
                            <th><button class="tbtn-updn">일간범위</button></th>
                            <th><button class="tbtn-updn">거래대금</button></th>
                        </tr>
                        </thead>
                    </table>
                    <div class="ncscroll">
                        <table class="table-st2">
                            <caption>코인별 현재가 현황</caption>
                            <colgroup>
                                <col width="19%">
                                <col width="27%">
                                <col width="26%">
                                <col width="28%">
                            </colgroup>
                            <tbody>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star">☆</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="down">-1.46%</span></td>
                                <td>123,456</td>
                            </tr>
                            <tr>
                                <td class="left"><a href="#" class="icon-star is-chk">★</a>BTC</td>
                                <td>7,111,000</td>
                                <td><span class="up">+2.46%</span></td>
                                <td>123,456</td>
                            </tr>
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
                    <table class="watch-list_table">
                        <colgroup>
                            <col style="width:40%"/>
                            <col />
                            <col />
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
                <div class="ncscroll">
                    <table class="watch-data_table">
                        <colgroup>
                            <col />
                            <col />
                            <col />
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
            <!-- //오른쪽 섹션 하단(실시간 현황) -->
        </section>
        <!-- //오른쪽 섹션 -->
        <!-- footer -->
        <div class="footer">
            <div class="f-inner">
                <div class="f-box">
                    <h4 class="logo"><span class="txt">ORD<em>Λ</em>DAT<em>Λ</em></span></h4>
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
                <p class="f-copyright">Copyright &copy;2021 ORDADATA Technologies Holding Company.  All Rights Reserved</p>
            </div>
        </div>
        <!-- //footer -->
    </div><!-- //wrap-inner -->
</div><!-- //wrap -->

<script>
    $(function(){
        //custom scroll
        $('.ncscroll').niceScroll('table', frontScript.nsOption());

        //API 펼쳐보기
        $('.s-reg-api-key .rak-base .btn-more').on('click', function(){
            $('#cont-right').addClass('ly-cr-st2');
            $('.list-real-currency .ncscroll').getNiceScroll().resize();
            $('.s-reg-api-key').addClass('mode-reg');
        });

        //API 등록하기
        $('.s-reg-api-key .rak-pop .btn-add-api').on('click', function(){
            $('#cont-right').removeClass('ly-cr-st2');
            $('.list-real-currency .ncscroll').getNiceScroll().resize();
            $('.s-reg-api-key').removeClass('mode-reg');
            $('.s-reg-api-key').addClass('mode-list');
            $('.s-reg-api-key .rak-list').niceScroll(frontScript.nsOption());
        });

        //거래소 select 없는경우 팝업
        $('.sel-exchange select').on('change', function(){
            if($(this).val() == 'none'){
                //service-lpu.html
                $.magnificPopup.open({
                    items: {
                        src: 'service-lpu.html'
                    },
                    type: 'ajax',
                    showCloseBtn: false,
                    callbacks: {
                        parseAjax: function(mfpResponse) {
                            mfpResponse.data = $(mfpResponse.data).filter('.wrap-popup');
                        },
                        ajaxContentAdded: function() {
                            //console.log('ajaxContentAdded = ', this.content);
                        }
                    }
                });
            }
        });

        //실시간 리스트 레이아웃 변경
        /*$('.cr2-header .user-fav .btn-full').on('click', function(){
            if ( $('#cont-right').hasClass('ly-cr-st3')){
                $('#cont-right').removeClass('ly-cr-st3');
                $('.s-reg-api-key.mode-list .rak-list').show();
            }else {
                $('#cont-right').addClass('ly-cr-st3');
                $('.s-reg-api-key.mode-list .rak-list').hide();
            }
            $('.list-real-currency .ncscroll').getNiceScroll().resize();
        });*/

        //실시간 리스트 ☆ 클릭
        $('.list-real-currency .icon-star').on('click', function(e){
            if($(this).hasClass('is-chk')){
                $(this).text('☆');
                $(this).removeClass('is-chk');
            }
            else{
                $(this).text('★');
                $(this).addClass('is-chk');
            }
            e.preventDefault();
        });

        //실시간 리스트 현재가, 일간범위, 거래대금 클릭
        $('.list-real-currency .tbtn-updn').on('click', function(){
            if(!$(this).hasClass('down') && !$(this).hasClass('up')){
                $('.list-real-currency .tbtn-updn.down').removeClass('down');
                $('.list-real-currency .tbtn-updn.up').removeClass('up');
            }

            if($(this).hasClass('down')){
                $(this).removeClass('down');
                $(this).addClass('up');
            }
            else if($(this).hasClass('up')){
                $(this).removeClass('up');
                $(this).addClass('down');
            }
            else{
                $(this).addClass('down');
            }
        });

        //테이블 주문포지션 변경1
        $(document).on('click', '.u-multi .rt1 .tbtn-st.b-change', function(){
            var txtCh = $(this).closest('tr').find('.txt-ch');
            if(txtCh.text() == '이익실현 지정 시장가 매도'){
                txtCh.text('이익실현 지정가 매도');
            }
            else if(txtCh.text() == '이익실현 지정가 매도'){
                txtCh.text('이익실현 지정 시장가 매도');
            }
        });
        //테이블 주문포지션 변경1 BTCUSD
        $(document).on('click', '.u-multi .rt1-BTCUSD .tbtn-st.b-change', function(){
            var txtCh = $(this).closest('tr').find('.txt-ch');
            if(txtCh.text() == 'BTCUSD 기준 이익실현 지정 시장가 매도'){
                txtCh.text('BTCUSD 기준 이익실현 지정가 매도');
            }
            else if(txtCh.text() == 'BTCUSD 기준 이익실현 지정가 매도'){
                txtCh.text('BTCUSD 기준 이익실현 지정 시장가 매도');
            }
        });

        //테이블 주문포지션 변경2
        $(document).on('click', '.u-multi .rt2 .tbtn-st.b-change', function(){
            var txtCh = $(this).closest('tr').find('.txt-ch');
            if(txtCh.text() == '지정가 매수'){
                txtCh.text('지정 시장가 매수');
            }
            else if(txtCh.text() == '지정 시장가 매수'){
                txtCh.text('지정가 매수');
            }
        });

        //테이블 주문포지션 변경3
        $(document).on('click', '.u-multi .rt3 .tbtn-st.b-change', function(){
            var txtCh = $(this).closest('tr').find('.txt-ch');
            if(txtCh.text() == '감시 지정 시장가 매도'){
                txtCh.text('감시 지정가 매도');
            }
            else if(txtCh.text() == '감시 지정가 매도'){
                txtCh.text('감시 지정 시장가 매도');
            }
        });
        //테이블 주문포지션 변경3 BTCUSD
        $(document).on('click', '.u-multi .rt3-BTCUSD .tbtn-st.b-change', function(){
            var txtCh = $(this).closest('tr').find('.txt-ch');
            if(txtCh.text() == 'BTCUSD 기준 감시 지정 시장가 매도'){
                txtCh.text('BTCUSD 기준 감시 지정가 매도');
            }
            else if(txtCh.text() == 'BTCUSD 기준 감시 지정가 매도'){
                txtCh.text('BTCUSD 기준 감시 지정 시장가 매도');
            }
        });

        //테이블 주문포지션 삭제1
        $(document).on('click', '.u-multi .rt1 .tbtn-st.b-delete', function(){
            var $tbody = $(this).closest('tbody');
            $(this).closest('tr').remove();
            if($tbody.find('tr').length < 2){
                $tbody.closest('table').removeClass('is-add');
            }
            $($tbody.find('.tnum').get().reverse()).each(function(index){
                $(this).text(index+1);
            });

            $tbody.closest('.ncscroll').getNiceScroll().resize();
        });

        //테이블 주문포지션 삭제2
        $(document).on('click', '.u-multi .rt2 .tbtn-st.b-delete', function(){
            var $tbody = $(this).closest('tbody');
            $(this).closest('tr').remove();
            if($tbody.find('tr').length < 2){
                $tbody.closest('table').removeClass('is-add');
            }
            $tbody.find('.tnum').each(function(index){
                $(this).text(index+1);
            });

            $tbody.closest('.ncscroll').getNiceScroll().resize();
        });

        //테이블 주문포지션 삭제3
        $(document).on('click', '.u-multi .rt3 .tbtn-st.b-delete', function(){
            var $tbody = $(this).closest('tbody');
            $(this).closest('tr').remove();
            if($tbody.find('tr').length < 2){
                $tbody.closest('table').removeClass('is-add');
            }
            $tbody.find('.tnum').each(function(index){
                $(this).text(index+1);
            });

            $tbody.closest('.ncscroll').getNiceScroll().resize();
        });

        //테이블 주문포지션 추가1
        $('.u-multi .rt1 .tbtn-st.b-add').on('click', function(){
            var $rtbody = $(this).closest('.row-table.rt1').find('.rt-body');
            var num = $rtbody.find('tr').length + 1;
            var temp = '<tr><td><button class="tbtn-st b-delete"><span class="a11y">삭제</span></button></td><td class="left"><span class="tnum">'+num+'</span>차 <span class="txt-ch">이익실현 지정 시장가 매도</span></td><td><button class="tbtn-st b-change"><span class="a11y">전환</span></button></td><td><input type="text" class="input-base inp-st3" value="0"> .KRW</td><td><input type="text" class="input-base inp-st3" value="0"> .XRP</td><td>0 .%</td><td><input type="text" class="input-base inp-st3" value="0"> .KRW</td></tr>';
            $rtbody.find('tbody').prepend(temp);
            if(!$rtbody.hasClass('is-add')){
                $rtbody.addClass('is-add');
            }

            $rtbody.closest('.ncscroll').getNiceScroll().resize();
        });

        //테이블 주문포지션 추가2
        $('.u-multi .rt2 .tbtn-st.b-add').on('click', function(){
            var $rtbody = $(this).closest('.row-table.rt2').find('.rt-body');
            var num = $rtbody.find('tr').length + 1;
            var temp = '<tr><td><button class="tbtn-st b-delete"><span class="a11y">삭제</span></button></td><td class="left"><span class="tnum">'+num+'</span>차 <span class="txt-ch">지정가 매수</span></td><td><button class="tbtn-st b-change"><span class="a11y">전환</span></button></td><td><input type="text" class="input-base inp-st3" value="0"> .KRW</td><td><input type="text" class="input-base inp-st3" value="0"> .XRP</td><td>0 .%</td><td><input type="text" class="input-base inp-st3" value="0"> .KRW</td></tr>';
            $rtbody.find('tbody').append(temp);
            if(!$rtbody.hasClass('is-add')){
                $rtbody.addClass('is-add');
            }

            $rtbody.closest('.ncscroll').getNiceScroll().resize();
        });

        //테이블 주문포지션 추가3
        $('.u-multi .rt3 .tbtn-st.b-add').on('click', function(){
            var $rtbody = $(this).closest('.row-table.rt3').find('.rt-body');
            var num = $rtbody.find('tr').length + 1;
            var temp = '<tr><td><button class="tbtn-st b-delete"><span class="a11y">삭제</span></button></td><td class="left"><span class="tnum">'+num+'</span>차 <span class="txt-ch">감시 지정 시장가 매도</span></td><td><button class="tbtn-st b-change"><span class="a11y">전환</span></button></td><td><input type="text" class="input-base inp-st3" value="0"> .KRW</td><td><input type="text" class="input-base inp-st3" value="0"> .XRP</td><td>0 .%</td><td><input type="text" class="input-base inp-st3" value="0"> .KRW</td></tr>';
            $rtbody.find('tbody').append(temp);
            if(!$rtbody.hasClass('is-add')){
                $rtbody.addClass('is-add');
            }

            $rtbody.closest('.ncscroll').getNiceScroll().resize();
        });

        //탭 버튼 클릭 이벤트(단순 클래스 on 컨트롤)
        $('.cr-header .tab-currency a, .list-user-batting .area-right .tab-currency a').on('click', function(e){
            if(!$(this).closest('li').hasClass('on')){
                $(this).closest('ul').find('.on').removeClass('on');
                $(this).parent().addClass('on')
            }
            e.preventDefault();
        });

        //상세보기
        $('.tbtn-detail').on('click', function(){
            $(this).closest('td').addClass('isDetail');
            $(this).closest('tr').next().find('.table-detail').closest('tr').show();
            //$(this).closest('tr').next().show();
        });

        //상세보기 접기
        $('.tbtn-close-detail').on('click', function(){
            $(this).closest('td').removeClass('isDetail');
            $(this).closest('tr').next().find('.table-detail').closest('tr').hide();
            //$(this).closest('tr').next().hide();
        });

    });
</script>

</body>
</html>