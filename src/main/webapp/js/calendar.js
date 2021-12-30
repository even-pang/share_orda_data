var vObj;
var vMouseY;
var vMouseX;
if( navigator.appName == "Microsoft Internet Explorer" )	document.onclick = mousedown_event;
else document.onmousedown = mousedown_event2;

function mousedown_event() {
	vMouseY = event.clientY;
	vMouseX = event.clientX;
}
 
function cal_open( vField ){
	if( navigator.appName == "Microsoft Internet Explorer" ) {
	 vMouseY = vMouseY + document.body.scrollTop;
	}
	vObj = vField;
	var targetElement = document.getElementById( "calendar" );
	targetElement.style.display = 'block';
	show_cal( vMouseX, vMouseY ); 
}
 

function show_cal(first_second,top_height){
	document.getElementById("calframe").contentWindow.location="/Common/calendar.html";
	document.getElementById( "calendar" ).style.visibility = "visible";
	//document.getElementById( "calendar" ).style.pixelTop=top_height + 15 ;
	//document.getElementById( "calendar" ).style.pixelLeft= first_second - 20;
	document.getElementById( "calendar" ).style.top=(top_height + 15) +"px";
	document.getElementById( "calendar" ).style.left= (first_second - 20) +"px";
//	document.all["calendar"].style.left= first_second - 65 ;//;259+((first_second-1)*72);
}

function onCalendarChange( y, m, d ){
	vObj.value = y + "-" + m + "-" +  d;
	vObj.focus();
}

function mousedown_event2( myEvent ) {
	var evt = myEvent ? myEvent : window.event;
	if (evt.pageX || evt.pageY) { // pageX/Y
		vMouseY = myEvent.pageY;
		vMouseX = myEvent.pageX;
	} else if (evt.clientX || evt.clientY) { //clientX/Y
		vMouseY = event.clientY;
		vMouseX = event.clientX;
	}
}