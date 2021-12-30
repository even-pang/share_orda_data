/*=======  �ѹ�ʿ� �ڹ� ��ũ��Ʈ ���� (rolling.js)=================================
#���ܼ���
<div><img /><img /></div>
��� ���� ��� div���� img�� ��,������,�Ʒ�,�������� �Ѹ� ��Ų��.

# ����
<script type="text/javascript" charset='utf-8' src="js_rolling.js"></script> 
//JS��ũ��Ʈ �ε�</P><P><div id='div1'><img /><img /><img /><img /><img /></div>
//ó�� ������ div�� �ʺ�� ���̴� �� �����ֱ� �ٶ��ϴ�.
<div>
 <div>1<img />etc</div>
 <div>2</div>
 <div>3</div>
 <div>4</div>
</div>
�̷��� ������ ��� ������ ��,�Ʒ��θ� �����մϴ�</P><P>
var roll = new js_rolling('rolling');
or
var roll = new js_rolling(document.getElementById('rolling'));
// id�̸��� ������, ���� ����� �����ؼ� �Ѹ� Ŭ������ ��ü�� ����
roll.set_direction(4); // ������ �ٲ�. 1: top, 2:right, 3:bottom 4:left �׿��� ��� ���۾���
roll.move_gap = 1; //�����̴� �ȼ�����
roll.time_dealy = 10; //�����̴� Ÿ�ӵ�����
roll.time_dealy_pause = 5000;//�ϳ��� ����� ���� ������ �� ���ߴ� �ð�, 0 �̸� ���� ����
roll.start(); //�Ѹ� ����
roll.move_up(); //���� �ѹ� �Ѹ� (������ top,botton�϶��� ����)
roll.move_right(); //���������� �ѹ� �Ѹ�(������ right,left�϶��� ����)
roll.move_down(); //�Ʒ��� �ѹ� �Ѹ�(������ top,botton�϶��� ����)
roll.move_left(); //�������� �ѹ� �Ѹ�(������ right,left�϶��� ����)

#����
������ top,bottom�� ��� ���� �±״� block���(div)��
������ left,right�� ��� �����±״� inline���(a,span)����
���ּ���.
FF���� top,bottom�� ��� inline����� ��� offsetHeight�� �߸��˾ƿɴϴ�.
========================================*/

var js_rolling = function(bannerContainer){
	// �ð������� ms�� 1000�� 1��
	if(bannerContainer.nodeType==1){
		this.bannerContainer = bannerContainer;
	}else{
		this.bannerContainer = document.getElementById(bannerContainer);
	}
	this.is_rolling = false;
	this.direction = 1; //1:top, 2:right, 3:bottom, 4:left (�ð����) // 1���� 4���� ��
	this.children = null;
	this.move_gap = 1; //�����̴� �ȼ�����
	this.time_dealy = 20; //�����̴� Ÿ�ӵ�����
	this.time_dealy_pause = 2000;//�ϳ��� ����� ���� ������ �� ���ߴ� �ð�, 0 �̸� ���� ����
	this.time_timer=null;
	this.time_timer_pause=null;
	this.mouseover=false;
	this.user_stop=false;
	this.init();
	this.set_direction(this.direction);
}

js_rolling.prototype.init = function(){
	this.bannerContainer.style.position='relative';
	this.bannerContainer.style.overflow='hidden';
	var children = this.bannerContainer.childNodes;
	for(var i=(children.length-1);0<=i;i--){
		if(children[i].nodeType==1){
			children[i].style.position='relative';
			children[i].style.padding='0 1px';
		}else{
			this.bannerContainer.removeChild(children[i]);
		}
	}
	var bannerContainer=this;
	this.bannerContainer.onmouseover=function(){
		bannerContainer.mouseover=true;
		if(!bannerContainer.time_timer_pause){
			bannerContainer.pause();
		}
	}
	this.bannerContainer.onmouseout=function(){
		bannerContainer.mouseover=false;
		if(!bannerContainer.time_timer_pause){
			bannerContainer.resume();
		}
	}
	/*
	this.bannerContainer.onfocus=function(){
		bannerContainer.mouseover=true;
		if(!bannerContainer.time_timer_pause){
			bannerContainer.pause();
		}
	}
	this.bannerContainer.onblur=function(){
		bannerContainer.mouseover=false;
		if(!bannerContainer.time_timer_pause){
			bannerContainer.resume();
		}
	}
	*/
}

js_rolling.prototype.set_direction = function(direction){
	this.direction=direction;
	if(this.direction==2 ||this.direction==4){
		this.bannerContainer.style.whiteSpace='nowrap';
	}else{
		this.bannerContainer.style.whiteSpace='normal';
	}
	var children = this.bannerContainer.childNodes;
	for(var i=(children.length-1);0<=i;i--){
		if(this.direction==1){
			children[i].style.display='block';
		}else if(this.direction==2){
			children[i].style.textlign='right';
			children[i].style.display='inline';
		}else if(this.direction==3){
			children[i].style.display='block';
		}else if(this.direction==4){
			children[i].style.display='inline';
		}
	}
	this.init_element_children(); 
}

js_rolling.prototype.init_element_children = function(){
	var children = this.bannerContainer.childNodes;
	this.children = children;
	for(var i=(children.length-1);0<=i;i--){
		if(this.direction==1){
			children[i].style.top='0px';
		}else if(this.direction==2){
			children[i].style.left='-'+this.bannerContainer.firstChild.offsetWidth+'px';
		}else if(this.direction==3){
			children[i].style.top='-'+this.bannerContainer.firstChild.offsetHeight+'px';
		}else if(this.direction==4){
			children[i].style.left='0px';
		}
	}
}

js_rolling.prototype.act_move_up = function(){
	for(var i=0, m=this.children.length; i<m; i++){
		var child = this.children[i];
		child.style.top=(parseInt(child.style.top)-this.move_gap)+'px';
	}
	if((this.children[0].offsetHeight+parseInt(this.children[0].style.top))<=0){
		this.bannerContainer.appendChild(this.children[0]);
		this.init_element_children();
		this.pause_act();  
	}
}

js_rolling.prototype.move_up = function(){
	if(this.direction!=1&&this.direction!=3){return false;}
	this.bannerContainer.appendChild(this.children[0]);
	this.init_element_children();
	this.pause_act(); 
}

js_rolling.prototype.act_move_down = function(){
	for(var i=0, m=this.children.length; i<m; i++){
		var child = this.children[i];
		child.style.top=(parseInt(child.style.top)+this.move_gap)+'px';
	}
	if(parseInt(this.children[0].style.top)>=0){
		this.bannerContainer.insertBefore(this.bannerContainer.lastChild,this.bannerContainer.firstChild);
		this.init_element_children();
		this.pause_act(); 
	}
}

js_rolling.prototype.move_down = function(){
	if(this.direction!=1&&this.direction!=3){return false;} 
	this.bannerContainer.insertBefore(this.bannerContainer.lastChild,this.bannerContainer.firstChild);
	this.init_element_children();
	this.pause_act();
}

js_rolling.prototype.act_move_left = function(){
	for(var i = 0,m=this.children.length;i<m;i++){
		var child = this.children[i];
		child.style.left=(parseInt(child.style.left)-this.move_gap)+'px';
	}
	if((this.children[0].offsetWidth+parseInt(this.children[0].style.left))<=0){
		this.bannerContainer.appendChild(this.bannerContainer.firstChild);
		this.init_element_children();
		this.pause_act();  
	}
}

js_rolling.prototype.move_left = function(){
	if(this.direction!=2&&this.direction!=4){return false;}  
	this.bannerContainer.appendChild(this.bannerContainer.firstChild);
	this.init_element_children();
	this.pause_act();  
}

js_rolling.prototype.act_move_right = function(){
	for(var i = 0,m=this.children.length;i<m;i++){
		var child = this.children[i];
		child.style.left=(parseInt(child.style.left)+this.move_gap)+'px';
	}

	if(parseInt(this.bannerContainer.lastChild.style.left)>=0){
		this.bannerContainer.insertBefore(this.bannerContainer.lastChild,this.bannerContainer.firstChild);
		this.init_element_children();
		this.pause_act();  
	}
}

js_rolling.prototype.move_right = function(){
	if(this.direction!=2&&this.direction!=4){return false;}   
	this.bannerContainer.insertBefore(this.bannerContainer.lastChild,this.bannerContainer.firstChild);
	this.init_element_children();
	this.pause_act();
}

js_rolling.prototype.start = function(){ //�Ѹ� ����
	var bannerContainer = this;
	this.stop();
	this.is_rolling = true;
	var act = function(){
		if(bannerContainer.is_rolling){
			if(bannerContainer.direction==1){bannerContainer.act_move_up();}
			else if(bannerContainer.direction==2){bannerContainer.act_move_right();}
			else if(bannerContainer.direction==3){bannerContainer.act_move_down();}
			else if(bannerContainer.direction==4){bannerContainer.act_move_left();}
		}
	}
	this.time_timer = setInterval(act,this.time_dealy);
}

js_rolling.prototype.pause_act = function(){ //�Ͻ� ����
	if(this.time_dealy_pause){
		var bannerContainer = this;
		var act = function(){bannerContainer.resume();bannerContainer.time_timer_pause=null;}
		if(this.time_timer_pause){clearTimeout(this.time_timer_pause);}
		this.time_timer_pause = setTimeout(act,this.time_dealy_pause);
		this.pause();
	}
}

js_rolling.prototype.pause = function(){ //�Ͻ� ����
	this.is_rolling = false;
}

js_rolling.prototype.resume = function(){ //�Ͻ� ���� ����
	if(!this.mouseover && !this.user_stop){
		this.is_rolling = true;
	}
}

js_rolling.prototype.stop = function(){ //�Ѹ��� ����
	this.is_rolling = false;
	if(!this.time_timer){
		clearInterval(this.time_timer);
	}
	this.time_timer = null;
}
	
js_rolling.prototype.user_pause = function(){ //�Ͻ� ����
	this.user_stop = true;
	this.pause();
}

js_rolling.prototype.user_resume = function(){ //�Ͻ� ���� ����
	this.user_stop = false;
	this.resume();
}
//----------------------------------------------------------------------------------------------------------