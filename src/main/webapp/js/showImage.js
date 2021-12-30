/*********************************************************************
 * 이미지 새창으로 열어서 보기
 * 파일명 : showImage.htm
 * 작성자 : 서비큐라(subicura@nate.com)
 * 작성일 : 06/01/27
 * 상업적인 싸이트에서는 무단으로 사용할 수 없습니다.
 *********************************************************************/
/*********************************************************************
 * 이미지를 볼때 하나의 윈도우만 사용할 것인가?
 * isUsePerText : % 표시 유무
 * newWndMargin : 윈도우 해상도에서 이미지 창의 여유(margin)값
 * newWndMinSize : 창의 최소 크기
 *********************************************************************/
var isUsePerText = true;
var newWndMargin = 160;
var newWndMinSize = 250;

/*********************************************************************
 * 각종변수
 *********************************************************************/
var ImgWnd, isUseZoom, isZoom ;
var img_original_width, img_original_height, img_modify_width, img_modify_height;
var pop_mouse_x, pop_mouse_y, pop_scroll_x, pop_scroll_y, pop_mouseStatus;
var zoombox_x, zoombox_y;

/*********************************************************************
 * 시작함수
 *********************************************************************/
function showImage(filename)
{
	image_copy = new Image();
	image_copy.src = filename;

	if(image_copy.width == 0 || image_copy.height == 0)
		setTimeout("showImage('"+filename+"');",50);
	else
		popupImage(image_copy);
}

/*********************************************************************
 * 이미지 팝업창 생성
 *********************************************************************/
function popupImage(img_obj)
{
	// 변수 초기화
	initValue();

	// 이미지 width, height 저장
	img_original_width = img_obj.width;
	img_original_height = img_obj.height;

	// 생성될 창의 크기 조정
	if(img_original_width + newWndMargin > screen.width)
	{
		img_modify_width = screen.width - newWndMargin;
		img_modify_height = (img_original_height*img_modify_width/img_original_width);
		if(img_modify_height + newWndMargin > screen.height)
		{
			img_modify_height = screen.height - newWndMargin;
			img_modify_width = Math.ceil(img_original_width*img_modify_height/img_original_height);
		}
	}
	else if(img_original_height + newWndMargin > screen.height)
	{
			img_modify_height = screen.height - newWndMargin;
			img_modify_width = Math.ceil(img_original_width*img_modify_height/img_original_height);
	}
	else
	{
		img_modify_width = img_original_width;
		img_modify_height = img_original_height;
	}
	
	// 위치 조정
	var i_left = Math.ceil((screen.width - img_modify_width)/2);
	var i_top = Math.ceil((screen.height - img_modify_height)/2);
	if(i_top > newWndMargin)
		i_top = newWndMargin;

	// 확대 축소 사용여부
	if(img_original_width != img_modify_width && img_original_height != img_modify_height)
		isUseZoom = true;
	else
		isUseZoom = false;

	// 새창 만들기
	if(ImgWnd) ImgWnd.close(); // 초기화
	if(img_original_width > newWndMinSize || img_original_height > newWndMinSize)
	{
		ImgWnd = window.open('', 'viewImage', 'width='+img_modify_width+', height='+img_modify_height+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
		if(!ImgWnd.document.body)
			ImgWnd = window.open('', 'viewImage', 'width='+img_modify_width+', height='+img_modify_height+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
	}
	else
	{
		ImgWnd = window.open('', 'viewImage', 'width='+newWndMinSize+', height='+newWndMinSize+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
		if(!ImgWnd.document.body)
			ImgWnd = window.open('', 'viewImage', 'width='+newWndMinSize+', height='+newWndMinSize+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
	}

	if(ImgWnd.document.body.innerHTML)
	{
		ImgWnd.close();
		if(img_original_width > newWndMinSize || img_original_height > newWndMinSize)
		{
			ImgWnd = window.open('', 'viewImage', 'width='+img_modify_width+', height='+img_modify_height+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
			if(!ImgWnd.document.body)
				ImgWnd = window.open('', 'viewImage', 'width='+img_modify_width+', height='+img_modify_height+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
		}
		else
		{
			ImgWnd = window.open('', 'viewImage', 'width='+newWndMinSize+', height='+newWndMinSize+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
			if(!ImgWnd.document.body)
				ImgWnd = window.open('', 'viewImage', 'width='+newWndMinSize+', height='+newWndMinSize+', top='+i_top+', left='+i_left+', scrollbars=0,resizable=no');
		}
	}

	// 새창 초기화
	initImgWnd(ImgWnd);

	// 이미지 생성
	var doc = ImgWnd.document;
	var doc_style = doc.body.style;
	var resize_width, resize_height;
	
	if(img_original_width > newWndMinSize || img_original_height > newWndMinSize)
	{
			var new_img = doc.createElement('IMG');
			new_img.id = 'showImage';
			new_img.src = img_obj.src;
			new_img.width = img_modify_width;
			new_img.height = img_modify_height;
			doc.body.appendChild(new_img);
	}
	else
	{
		doc_style.background='url('+img_obj.src+')';
		doc_style.backgroundRepeat = 'no-repeat';
		doc_style.backgroundPosition = 'center';
		//doc_style.backgroundColor = "#000000";
		doc_style.backgroundColor = "#FFFFFF";
		doc.body.innerHTML = "<table width=100% height=100%><tr><td></td></tr></table>";
	}

	// 새창에 각종객체 삽입
	if(isUseZoom == true)
	{
		var old_zoom_box = ImgWnd.document.getElementById('zoom_box');

		// zoom 관련 객체를 묶음
		var zoom_box = doc.createElement('DIV');
		zoom_box.id = 'zoom_box';
		zoom_box.style.position = 'absolute';
		zoom_box.style.top = img_modify_height - 60;
		zoom_box.style.left = img_modify_width - 64;
		zoom_box.style.width = 38;
		zoom_box.style.height = 43;
		doc.body.appendChild(zoom_box);

		// zoom 버튼 생성
		var zoom_button = doc.createElement('IMG');
		zoom_button.id = 'zoom_button';
		zoom_button.src = '/images/common/100_icon.gif';
		zoom_button.style.position = 'absolute';
		zoom_button.style.width = 38;
		zoom_button.style.height = 43;
		zoom_button.style.top = '0px';
		zoom_button.style.left = '0px';
		zoom_box.appendChild(zoom_button);

		if(isUsePerText == true)
		{
			// 비율 글자 생성
			var zoom_percent = doc.createElement('DIV');
			zoom_percent.id = 'zoom_percent';
			zoom_percent.style.position = 'absolute';
			zoom_percent.style.top = '29px';
			zoom_percent.style.left = '3px';
			zoom_percent.style.width = '30px';
			zoom_percent.style.textAlign = 'center';
			zoom_percent.style.fontSize = '8pt';
			zoom_percent.style.fontFamily = '돋움';
			zoom_percent.innerHTML = Math.round(img_modify_width/img_original_width*100)+'%';
			zoom_box.appendChild(zoom_percent);
		}
		zoombox_x = eval(zoom_box.style.left.substring(0,zoom_box.style.left.length-2));
		zoombox_y = eval(zoom_box.style.top.substring(0,zoom_box.style.top.length-2));

		setButtonAlpha(70);
	}

	// 마우스 이벤트
	doc.onmousedown=ImageMouseDown;
	doc.onmousemove=ImageMouseDrag;
	doc.onmouseup=ImageMouseUp;
	doc.onmousewheel=ImageMouseWheel; // IE 전용 FF는?

	// 포커스
	ImgWnd.focus();
}

/*********************************************************************
 * 창 초기화
 *********************************************************************/
function initImgWnd()
{
	var doc = ImgWnd.document;
	var doc_style = doc.body.style;

	doc_style.margin = 0;
	doc_style.background='url("http://comedu.korea.ac.kr/~subicura/java/loading.jpg")';
	doc_style.backgroundRepeat = 'no-repeat';
	doc_style.backgroundPosition = 'center';
	//doc_style.backgroundColor = "#000000";
	doc_style.backgroundColor = "#FFFFFF";
	doc_style.cursor = "pointer";
	doc.title = '이미지';
}

/*********************************************************************
 * 창 삭제
 *********************************************************************/
function destroyImgWnd()
{
	initValue();
	ImgWnd.document.body.style.cursor = '';
	setTimeout("ImgWnd.close(); ImgWnd='';",10);
	ImgWnd.document.onmousedown=null;
	ImgWnd.document.onmousemove=null;
	ImgWnd.document.onmouseup=null;
	ImgWnd.document.onmousewheel=null;
}

// 각종변수 초기화
function initValue()
{
	isZoom = false;
	wheelCount = 0;
	pop_scroll_x = pop_scroll_y = 0;
	pop_mouseStatus = '';
}

/*********************************************************************
 * 마우스 이벤트
 *********************************************************************/
function ImageMouseDown(e)
{
	if (ImgWnd.window.event) // IE
	{
		pop_mouse_x = ImgWnd.window.event.clientX;
		pop_mouse_y = ImgWnd.window.event.clientY;
		if(ImgWnd.window.event.srcElement.id == 'zoom_button' || ImgWnd.window.event.srcElement.id == 'zoom_percent')
		{
			pop_mouseStatus = '';
			zoomImg();
		}
		else if(ImgWnd.window.event.button != 2 && ImgWnd.window.event.button != 4)
			pop_mouseStatus = 'drag';
	}
	else
	{
		pop_mouse_x = e.clientX;
		pop_mouse_y = e.clientY;
		if(e.target.getAttribute('id') == 'zoom_button' || e.target.getAttribute('id') == 'zoom_percent')
		{
			pop_mouseStatus = '';
			zoomImg();
		}
		else if(e.button != 2 && e.button != 1)
			pop_mouseStatus = 'drag';
	}

	// 클릭순간의 좌표 기억
	pop_scroll_x = ImgWnd.document.body.scrollLeft;
	pop_scroll_y = ImgWnd.document.body.scrollTop;

	// 커서 모양 바꿈
	if(isZoom == true)
		ImgWnd.document.body.style.cursor = "move";

	return false;
}

function ImageMouseUp(e)
{
	ImgWnd.document.body.style.cursor = "pointer";
	// 종료
	if (ImgWnd.window.event) // IE
	{
		if(pop_mouse_x == ImgWnd.window.event.clientX && pop_mouse_y == ImgWnd.window.event.clientY && pop_mouseStatus == 'drag')
			destroyImgWnd();
	}
	else
	{
		if(pop_mouse_x == e.clientX && pop_mouse_y == e.clientY && pop_mouseStatus == 'drag')
			destroyImgWnd();
	}

	// 아이콘 위치 이동
	if(isUseZoom == true)
	{
		if(pop_mouseStatus == 'drag' && isZoom == true)
		{
			var zoom_box = ImgWnd.document.getElementById('zoom_box');
			zoom_box.style.left = (ImgWnd.document.body.scrollLeft + zoombox_x)+'px';
			zoom_box.style.top = (ImgWnd.document.body.scrollTop + zoombox_y)+'px';
			zoom_box.style.display = '';
		}
	}
	
	// 스크롤 위치 기억/휠 위치 초기화
	pop_scroll_y = ImgWnd.document.body.scrollTop;
	wheelCount = 0;

	pop_mouseStatus = '';
	return false;
}

function ImageMouseDrag(e)
{
	// 드래그할때 스크롤링
	if(pop_mouseStatus == 'drag' && isZoom == true)
	{
		// 줌버튼 숨김
		ImgWnd.document.getElementById('zoom_box').style.display = 'none';

		if(ImgWnd.window.event) // IE
			ImgWnd.scroll(pop_scroll_x + (pop_mouse_x - ImgWnd.window.event.clientX)*2, pop_scroll_y+(pop_mouse_y - ImgWnd.window.event.clientY)*2);
		else
			ImgWnd.scroll(pop_scroll_x + (pop_mouse_x - e.clientX)*2, pop_scroll_y+(pop_mouse_y - e.clientY)*2);
		
	} else if(pop_mouseStatus == 'drag' && isZoom == false) {

	} else{ 
		ImgWnd.document.body.style.cursor = "pointer";
	}
	// zoom 롤오버 체크
	if(isUseZoom == true)
	{
		if (ImgWnd.window.event) // IE
		{
			if(ImgWnd.window.event.srcElement.id == 'zoom_button' || ImgWnd.window.event.srcElement.id == 'zoom_percent')
				setButtonAlpha(95);
			else
				setButtonAlpha(70);
		}
		else
		{
			if(e.target.getAttribute('id') == 'zoom_button' || e.target.getAttribute('id') == 'zoom_percent')
				setButtonAlpha(95);
			else
				setButtonAlpha(70);
		}
	}

	return false;
}

function ImageMouseWheel(e)
{
	if(isZoom == true)
	{
		var zoom_box = ImgWnd.document.getElementById('zoom_box');
		var temp = ImgWnd.document.body.scrollTop;

		wheelCount += (ImgWnd.window.event.wheelDelta > 0) ? 1 : -1;
		zoom_box.style.display = 'none';
		ImgWnd.scroll(pop_scroll_x, (pop_scroll_y-wheelCount*120));

		if(temp != ImgWnd.document.body.scrollTop)
		{
			zoom_box.style.left = (ImgWnd.document.body.scrollLeft + zoombox_x)+'px';
			zoom_box.style.top = (ImgWnd.document.body.scrollTop + zoombox_y)+'px';
		}
		else
		{
			wheelCount -= (ImgWnd.window.event.wheelDelta > 0) ? 1 : -1;
		}
		zoom_box.style.display = '';
	}
}

/*********************************************************************
 * 버튼 투명도 조정
 *********************************************************************/
function setButtonAlpha(opacity)
{
	var zoom_box;
	zoom_box = ImgWnd.document.getElementById('zoom_box');
	zoom_box.style.filter = "alpha(opacity=" + opacity + ")";
	zoom_box.style.KhtmlOpacity = (opacity / 100);
	zoom_box.style.MozOpacity = (opacity / 100);
}

/*********************************************************************
 * 이미지 줌인/줌아웃
 *********************************************************************/
function zoomImg()
{
	// 이미지 관련
	var zoom_img = ImgWnd.document.getElementById('showImage');
	var per_text = ImgWnd.document.getElementById('zoom_percent');
	per_text.innerHTML = Math.ceil(zoom_img.style.width/img_original_width*100)+'%';

	// 윈도우 창 크기 조정 관련
	var resize_width=0, resize_height=0;

	if(zoom_img.clientWidth == img_original_width)
	{
		zoom_img.style.width = img_modify_width;
		zoom_img.style.height = img_modify_height;
		per_text.innerHTML = Math.ceil(img_modify_width/img_original_width*100)+'%';
		isZoom = false;

		// 창 크기 조정
		if(img_modify_width == screen.width - newWndMargin)
		{
			if(!ImgWnd.window.innerHeight) // IE
				resize_height = img_modify_height - ImgWnd.window.document.body.offsetHeight;
			else
				resize_height = img_modify_height - ImgWnd.window.innerHeight;
		}
		else
		{
			if(!ImgWnd.window.innerWidth) // IE
				resize_width = img_modify_width - ImgWnd.window.document.body.offsetWidth;
			else
				resize_width = img_modify_width - ImgWnd.window.innerWidth;
		}

		// 버튼 위치 초기화
		var zoom_box = ImgWnd.document.getElementById('zoom_box');
		zoom_box.style.left = zoombox_x + 'px';
		zoom_box.style.top = zoombox_y + 'px';

		// 버튼 이미지 변경
		var zoombutton = ImgWnd.document.getElementById('zoom_button');
		zoombutton.src = '/images/common/100_icon.gif';

	}
	else
	{
		zoom_img.style.width = img_original_width;
		zoom_img.style.height = img_original_height;
		per_text.innerHTML = '100%';
		isZoom = true;

		var zoombutton = ImgWnd.document.getElementById('zoom_button');
		zoombutton.src = '/images/common/00_icon.gif';

		// 창 크기 조정
		if(img_modify_width+newWndMargin == screen.width)
		{
			if(img_original_height+newWndMargin > screen.height)
				resize_height = screen.height-newWndMargin-img_modify_height;
			else
				resize_height = img_original_height-img_modify_height;
		}
		else
		{
			if(img_original_width+newWndMargin > screen.width)
				resize_width = screen.width-newWndMargin-img_modify_width;
			else
				resize_width = img_original_width-img_modify_width;
		}
	}

	//  창 크기 조정
	if(Math.abs(resize_width) > 70 || Math.abs(resize_height) > 70)
	{
		if(navigator.appName.indexOf("Microsoft") != -1) // IE Bug
			ImgWnd.moveTo(0,0);
		ImgWnd.resizeBy(resize_width,resize_height);

		// 윈도우 창 위치 조정
		if(!ImgWnd.window.innerHeight) // ie
		{
			ImgWnd.moveTo(Math.ceil((screen.width - ImgWnd.window.document.body.offsetWidth)/2), Math.ceil((screen.height - ImgWnd.window.document.body.offsetHeight)/2));
		}
		else
		{
			ImgWnd.moveTo(Math.ceil((screen.width - ImgWnd.window.innerWidth)/2), Math.ceil((screen.height - ImgWnd.window.innerHeight)/2));
		}

		// 줌 버튼 위치 이동
		var zoom_box = ImgWnd.document.getElementById('zoom_box');
		zoombox_x += resize_width;
		zoombox_y += resize_height;
		zoom_box.style.left = zoombox_x+'px';
		zoom_box.style.top = zoombox_y+'px';
	}
}
// 여기까지 showImage 관련함수 끝
