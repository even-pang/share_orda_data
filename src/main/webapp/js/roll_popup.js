/*  롤팝업용 자바 스크립트 파일 (roll_popup.js)  */

var pop_rolling = function(popContainer){
	// 시간단위는 ms로 1000이 1초

	if(popContainer.nodeType==1){
		this.popContainer = popContainer;
	}else{
		this.popContainer = document.getElementById(popContainer);
	}
	this.is_rolling = false;
	this.counter = 0;
	this.pop_children = null;
	this.time_dealy = 3000; //움직이는 타임딜레이
	this.time_timer = null;
	this.time_timer_pause=null;
	this.mouseover=false;
	this.init();
}

pop_rolling.prototype.init = function(){
	var pop_children = this.popContainer.childNodes;

	for(var i=(pop_children.length-1); 0<=i; i--){
		if(pop_children[i].nodeType==1){
			//pop_children[i].style.position='relative';
		}else{
			this.popContainer.removeChild(pop_children[i]);
		}
	}

	this.pop_children = this.popContainer.childNodes;
	var oRoll = this;

	for (var i=0; i<this.pop_children.length; i++) {

		for (var k=0; k<this.pop_children[i].childNodes.length; k++) {

			if (this.pop_children[i].childNodes[k].nodeType == 1) {

				this.pop_children[i].childNodes[k].onclick = function() {
					oRoll.moveAt(this.firstChild);
					return false;
				}
				this.pop_children[i].childNodes[k].onfocus = function() {
					oRoll.moveAt(this.firstChild);
					return false;
				}
				break;	// 첫번째 링크(A)에만 이벤트 할당
			}
		}
	}
}

pop_rolling.prototype.moveAt = function(oBtn){
	var i = oBtn.id.substring(6);
	this.mouseover = true;
	if(!this.time_timer_pause){
		this.counter = (i-1);
		this.move_right();
		this.pause();
	}
}

pop_rolling.prototype.move_right = function(){
	var oRoll = this;
	var nTemp = 0;
	for(var i=0, m=oRoll.pop_children.length; i<m; i++){

		nTemp = 0;
		for (var k=0; k<this.pop_children[i].childNodes.length; k++) {

			if (this.pop_children[i].childNodes[k].nodeType == 1) {
				nTemp++;
				if (nTemp == 1) {
					var child_1 = oRoll.pop_children[i].childNodes[k].childNodes[0];	//버튼이미지
					child_1.src = child_1.src.replace(/_.gif/gi,".gif");

					if (i == oRoll.counter) {
						child_1.src = child_1.src.replace(/.gif/gi,"_.gif");
					}
				}
				else {
					var child_2 = oRoll.pop_children[i].childNodes[k].childNodes[0];	//배너이미지
					child_2.style.display = "none";

					if (i == oRoll.counter) {
						child_2.style.display = "block";
					}
				}

			}
		}
	}

	oRoll.counter++;
	if (oRoll.counter >= oRoll.pop_children.length) {
		oRoll.counter = 0;
	}
}


pop_rolling.prototype.start = function(){ //롤링 시작
	var oRoll = this;
	this.stop();
	this.is_rolling = true;

	var act = function(){
		if(oRoll.is_rolling){
			oRoll.move_right();
		}
	}
	if (!this.time_timer) {
		act();	// 처음 로딩시 첫번째 버튼이 즉시 선택되도록
	}
	this.time_timer = setInterval(act,this.time_dealy);
}

pop_rolling.prototype.pause = function(){ //일시 멈춤
	this.is_rolling = false;
}
pop_rolling.prototype.resume = function(){ //일시 멈춤 해제
	//if(!this.mouseover){
		this.is_rolling = true;
	//}
}

pop_rolling.prototype.stop = function(){ //롤링을 끝냄
	this.is_rolling = false;
	if(!this.time_timer){
		clearInterval(this.time_timer);
	}
	this.time_timer = null;
}
//----------------------------------------------------------------------------------------------------------
