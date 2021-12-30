//<![CDATA[
//메뉴 네비게이션
function initNavigation(seq, seq2) {		
	var nav = document.getElementById("head_menu");
	nav.menu = new Array();
	nav.current = null;
	nav.menuseq = 0;
	var navLen = nav.childNodes.length;
	var menuImg;
	
	var allA = nav.getElementsByTagName("div");
	for(var k = 0; k < allA.length; k++) {
		allA.item(k).onmouseover = allA.item(k).onfocus = function () {
			nav.isOver = true;
		}
		allA.item(k).onmouseout = allA.item(k).onblur = function () {
			nav.isOver = false;
			setTimeout(function () {
				if (nav.isOver == false) {
					if (nav.menu[seq]) {
						nav.menu[seq].onmouseover();
					} else if(nav.current) {
						menuImg = nav.current.childNodes.item(0);
						menuImg.src = menuImg.src.replace("_on.gif", ".gif");
						if (nav.current.submenu) {
							nav.current.submenu.style.display = "none";
						}
						nav.current = null;
					}
				}
			}, 500);
		}
	}
	
	var allB = nav.getElementsByTagName("div");
	for(var k = 0; k < allB.length; k++) {
		var str = allB.item(k) + "";
		if(str.substr(str.length-10,4) == seq2){
			allB.item(k).className = "selected";
		}
	}

	for (var i = 0; i < navLen; i++) {
		var navItem = nav.childNodes.item(i);
		if (navItem.tagName != "LI") {
			continue;
		}
		var navAnchor = navItem.getElementsByTagName("a").item(0);
		navAnchor.submenu = navItem.getElementsByTagName("div").item(0);
	
		navAnchor.onmouseover = navAnchor.onfocus = function () {
			if (nav.current) {
				menuImg = nav.current.childNodes.item(0);
				menuImg.src = menuImg.src.replace("_on.gif", ".gif");
				if (nav.current.submenu) {
					nav.current.submenu.style.display = "none";
				}
				nav.current = null;
			}
			if (nav.current != this) {
				menuImg = this.childNodes.item(0);
				menuImg.src = menuImg.src.replace(".gif", "_on.gif");
				if (this.submenu) {
					this.submenu.style.display = "block";
				}
				nav.current = this;
			}
			nav.isOver = true;
		}
		nav.menuseq++;
		nav.menu[nav.menuseq] = navAnchor;
	}
	if (nav.menu[seq]) {
		//nav.menu[seq].onmouseover();
	}
}

//메뉴 네비게이션

//leftMenu_ 또는 submenu 각 좌측메뉴class가 들어가는 아이디앞 자리여야함 <a에 class가들어가면 a태그에 id값을 줘야되고
//li면 li에 줘야됨

 function left3Depth01(menuIdx) { //왼쪽메뉴숨김펼침//하위메뉴없을시
	// 보이게 처리
	
	vObj = document.getElementById("leftMenu_" + menuIdx);
	
	if (vObj.className == "leftMenuOff" || vObj.className == "") {
	  	vObj.className = "leftMenuOn";
	  	vObj.parentNode.className = "on";
	}	
	
	/*
	if ($("leftMenu_" + menuIdx).className == "leftMenuOff" || $("leftMenu_" + menuIdx).className == "") {
	  	$("leftMenu_" + menuIdx).className = "leftMenuOn";	
	}
	*/
	// 보이지 않게 처리
	else {
	
	  vObj.style.display = "none";
	
		//$("leftMenu_" + menuIdx).style.display = "none";
		//$("submenu" + menuIdx).style.display = "none";
	}
}

 function left3Depth02(menuIdx) { //왼쪽메뉴 숨김펼침
 
 	vObj = document.getElementById("submenu" + menuIdx);
 
	// 보이게 처리	
	if (vObj.className == "subOff" || vObj.className == "submenu" || vObj.className == "") {
		 // $("submenu" + menuIdx).className = "leftMenuSubOn";	
		 vObj.className = "leftMenuSubOn";
		 $("#submenu" + menuIdx).show();
	
	}
	// 보이지 않게 처리
	else {
		//$("submenu" + menuIdx).style.display = "none";
		//$("submenu" + menuIdx).className = "subOff";
		vObj.className = "subOff";	
	}
	
	
}

 function left3DepthFont02(menuIdx) { //왼쪽메뉴 숨김펼침
 
  	vObj = document.getElementById("leftSub_" + menuIdx);
  
	// 보이게 처리		
/*	
	if (vObj.className == "leftMenuFontNormal" || vObj.className == "") {
		//$("leftSub_" + menuIdx).className = "leftMenuFontBold";	
		vObj.className = "leftMenuFontBold";	
	
	}
*/	
	if (vObj.className == "fontNormal" || vObj.className == "") {
		vObj.className = "leftMenuFontBold";
		vObj.parentNode.className="on2";
	}
	// 보이지 않게 처리
	else {
		//$("leftSub_" + menuIdx).className = "fontNormal";
		vObj.className = "fontNormal";
	}
 
}

/* 검색박스 배경처리
function hideBG(f){ f.className = f.className.replace(""," BackgroundNone"); }
function showBG(f){ if(f.value == ""){ f.className = f.className.replace(" BackgroundNone"," "); }} */

/* 화면 확대,축소

var nowZoom = 100; // 현재비율
var maxZoom = 150; // 최대비율(500으로하면 5배 커진다)
var minZoom = 80; // 최소비율

function zoomInOut(value) {
	var browser = navigator.userAgent.toLowerCase();
	if(browser.indexOf('msie') == -1){ alert("익스플로러에서만 동작합니다. \n브라우저 메뉴의 확대축소 기능을 이용하세요.") }
	else {

		if(value == "in") {
			if (nowZoom < maxZoom) {
				nowZoom += 10;
			} else {
				alert("더이상 커지지 않습니다.");
				return;
			}
		}
		else if(value == "out") {
			if (nowZoom > minZoom) {
				nowZoom -= 10;
			} else {
				alert("더이상 작아지지 않습니다.");
				return;
			}
		}
		else if(value == "base") {
			nowZoom = 100;
		}
		else {
			return;
		}

		document.getElementsByTagName("body").item(0).style.position = "relative";
		document.getElementsByTagName("body").item(0).style.zoom = nowZoom + "%";
	}
}
-----------------------------------------------*/
/*퀵메뉴 스크립트

jQuery(function(){
	// 퀵메뉴 스크롤
		var sidebar = $('#sub_container #quick') // 해당 요소
	    b_width = $(document.body).width(); //페이지 전체크기
	    d_width = $("#sub_container").width(); //본문 전체 크기임
	    window_height = $(window).height(); //본문 전체 크기임
	    t_height = $(document.body).scrollTop(); //상단 높이
	    //width = (b_width - d_width) <= 0 ? 1000 : b_width/2 + 0; //중앙에서 어느 위치에 놓을지 적는다.
	    //900은 가운데 정렬한 div 값을 적는다.
	    //-610은 왼쪽으로 +는 오른쪽으로 놓는것이다.
	    width = 1000
	    height = t_height + 200; //상단부터 띄워야 하는 높이
	    sidebar.css({top:height, left:width, display:'block'});
	    sidebar.css({
			top:height,
			display:'block',
			position:'absolute'
			});
	    var currentPosition = parseInt(sidebar.css("top"));
	    $(window).scroll(function() {
	        if(window_height < 560){
	            sidebar.css({top:height, display:'block'});
	        }else{
	    var position = $(window).scrollTop(); // 현재 스크롤바의 위치값을 반환합니다.
	    sidebar.stop().animate({"top":position+currentPosition+"px"},1000); //무빙속도
	    }
	    });
	// 스크롤 끝
})
*/
//]]>

