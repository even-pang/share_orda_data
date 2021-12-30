var files = {};

function addPreview(str, str2) {
   	if(!str.includes(',')) {
      	$("#preview").append(
      	"<div class=\"preview-box\" value=\""+ str.split(".")[0] +"\">"
      	+ str 
      	+ "<a href=\"#\" class=\"input-delelte-button\" value=\""+ str +"\" onclick=\"deletePreview(this)\">"
      	+ "삭제" + "</a>" + "</div>");   
      	files1.push(str);
		files2.push(str2);
   	} else {
		str.split(',').forEach((e) => {
			files1.push(e);
		 	$("#preview").append(
		 	"<div class=\"preview-box\" value=\"" + e.split(".")[0] + "\">"
		 	+ e + "<a href=\"#\" class=\"input-delelte-button\" value=\""+ e + "\" onclick=\"deletePreview(this)\">삭제" + "</a>" + "</div>");
      	})
		str2.split(',').forEach((e) => {
         	files2.push(e);
      	})
   	}
}

function deletePreview(obj) {
	var imgNum = obj.attributes['value'].value;
	files1 = files1.filter((v) => v !== imgNum);
	files2 = files2.filter((v) => v !== imgNum);
	files_undelete = files_undelete.filter((v) => v !== imgNum);
	$("#preview .preview-box[value=" + imgNum.split(".")[0] + "]").remove();
}

var files1 = [];
var files2 = [];
var files_undelete = [];

$(document).ready(function() {
   $('#uploadInputBox').change(function() {
      const imageFormData = new FormData();
      [].forEach.call($(this)[0].files, (f) => {
         imageFormData.append('image', f);
      });
      $.ajax({
         type: "POST",
         enctype : 'multipart/form-data',
         processData : false,
         contentType : false,
         cache : false,
         url: '/boffice/uploadImage.do',
         data: imageFormData,
         dataType: "text",
         success: function(data) {
            var reg = /[\{\}\[\]\/?;:|\)*~`!^\_+<>@\#$%&\\\=\(\'\"]/gi
            var str = data.split("|")[0].replace(reg,'');
            var str2 = data.split("|")[1].replace(reg,'');
            addPreview(str, str2);
         },
         error: function() {
            console.log("에러");
         }
      });
   });
});