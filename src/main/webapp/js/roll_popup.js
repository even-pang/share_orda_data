/*  ���˾��� �ڹ� ��ũ��Ʈ ���� (roll_popup.js)  */

var pop_rolling = function(popContainer){
	// �ð������� ms�� 1000�� 1��

	if(popContainer.nodeType==1){
		this.popContainer = popContainer;
	}else{
		this.popContainer = document.getElementById(popContainer);
	}
	this.is_rolling = false;
	this.counter = 0;
	this.pop_children = null;
	this.time_dealy = 3000; //�����̴� Ÿ�ӵ�����
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
				break;	// ù��° ��ũ(A)���� �̺�Ʈ �Ҵ�
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
					var child_1 = oRoll.pop_children[i].childNodes[k].childNodes[0];	//��ư�̹���
					child_1.src = child_1.src.replace(/_.gif/gi,".gif");

					if (i == oRoll.counter) {
						child_1.src = child_1.src.replace(/.gif/gi,"_.gif");
					}
				}
				else {
					var child_2 = oRoll.pop_children[i].childNodes[k].childNodes[0];	//����̹���
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


pop_rolling.prototype.start = function(){ //�Ѹ� ����
	var oRoll = this;
	this.stop();
	this.is_rolling = true;

	var act = function(){
		if(oRoll.is_rolling){
			oRoll.move_right();
		}
	}
	if (!this.time_timer) {
		act();	// ó�� �ε��� ù��° ��ư�� ��� ���õǵ���
	}
	this.time_timer = setInterval(act,this.time_dealy);
}

pop_rolling.prototype.pause = function(){ //�Ͻ� ����
	this.is_rolling = false;
}
pop_rolling.prototype.resume = function(){ //�Ͻ� ���� ����
	//if(!this.mouseover){
		this.is_rolling = true;
	//}
}

pop_rolling.prototype.stop = function(){ //�Ѹ��� ����
	this.is_rolling = false;
	if(!this.time_timer){
		clearInterval(this.time_timer);
	}
	this.time_timer = null;
}
//----------------------------------------------------------------------------------------------------------
