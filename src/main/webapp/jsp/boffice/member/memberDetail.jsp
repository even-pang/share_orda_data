<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
Map reqMap = (Map) request.getAttribute("reqMap");
Map dbMap = (Map) request.getAttribute("dbMap");


if (reqMap == null)
	reqMap= new HashMap();
if (dbMap == null)
	dbMap = new HashMap();

String strParam = CommonUtil.getRequestQueryString(request);
String strKeykind = CommonUtil.nvl(request.getAttribute("keykind"), "");

Map userMap = (Map) SessionUtil.getSessionAttribute(request, "ADM");


%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="/boffice/include/inc_top.do" flush="false" />

<script type="text/javascript">
	var idch=false;
	function FCKeditor_OnComplete(editorInstance) {
	  // window.status = editorInstance.Description;
	} 
		
   function GetInnerHTML(vName)
   {
    // Get the editor instance that we want to interact with.
     var oEditor = FCKeditorAPI.GetInstance(vName) ;
     return oEditor.EditorDocument.body.innerHTML ;
   }


   async function fSubmit()
   {
	    var vObj = document.frmInput;
	 
    	if ( !checkEmpty(vObj.user_nm, "회원명을 입력하여 주십시오")) return;
    	if ( !checkEmpty(vObj.account,     "계좌번호를 입력하여 주십시오")) return;
    	if ( !checkEmpty(vObj.bank_nm,     "은행명을 선택해 주십시오")) return;
	    if ( vObj.telno.value.length != 11) {
	    	return alert("전화번호를 확인해주십시오");
	    }
	  
		/* 
		INSERT -> ?
			
		UPDATE -> ?
			
		*/
	 
		  	if(vObj.pwd.value.length >0 ){
		  		if ( vObj.pwd.value.length < 4) {
		 	    	return alert("비밀번호는 4자 이상을 입력하여 주십시오");
		 	    }
		  		// 비밀번호 변경
		  		vObj.pwdUpdate.value = "1"
		  	}
		  	// 비밀번호 변경 안함
			vObj.pwdUpdate.value = "0"
		
		if(vObj.pwd.value !== vObj.pwdch.value) {
			return alert("비밀번호 확인이 다릅니다")
		}
		  let temp = vObj.telno.value;
		    vObj.telno.value = temp.substring(0, 3) + "-" + temp.substring(3, 7) + "-" + temp.substring(7);
	   //////
	   	$.ajax(
	   		{
	   			url:"/boffice/memberWrite.do",
	   			type:'POST',
	   			cache:false,
	   			data:$("#frmInput").serialize(),
	   			dataType:'text',
	   			success:fSuccess,
	   			error:fError
	   		}	
	   	);
	   	
   }
   
   
   
	
   
   function fSuccess(data) {
	  if ( data.indexOf("UPDATE") >= 0 ) {
	   		alert("정상적으로 수정되었습니다.");
	   		window.location.href="/boffice/member/memberList.do";
			}
		else {
			alert("오류가 발생하여 회원가입을 실패하였습니다");
		}
	}
   
	function fError(data) {
		alert("오류가 발생하여 회원가입을 실패하였습니다.");
	}
</script>
</head>
<body>
	<jsp:include page="/boffice/include/inc_header.do" flush="false" />
	<div id="main">
		<div class="group">
			<div class="header">
				<h3>회원수정</h3>
			</div>
			<div class="body">
				<form autocomplete="off" name="frmInput" id="frmInput" method="post"
					action="/boffice/memberModify.do">
					<input name="returl" type="hidden" value="/boffice/member/memberUpdate.do" />
					<input name="iflag" type="hidden"
						value="UPDATE" />
					<input name="menu_no" type="hidden"
						value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>" />
					<input name="pwdUpdate" type="hidden" value="0" />
					<div class="board_search_wrap">
						<div class="ipt_group flex_wrap">
							<div class="flex_1">
								<p class="sch_text">아이디 :</p>
								<input type="text"  name="user_id" class="ipt" value="<%=CommonUtil.nvl(dbMap.get("USER_ID")) %>" maxlength="10" disabled/>
							</div>
							<div class="flex_1">
								<p class="sch_text">이름 :</p>
								<input type="text"  name="user_nm" class="ipt" value="<%=CommonUtil.nvl(dbMap.get("USER_NM")) %>" maxlength="20" />
							</div>
						</div>
						<div class="ipt_group flex_wrap">
							<div class="flex_1">
								<p class="sch_text">비밀번호 :</p>
								<input type="password"   name="pwd" class="ipt" value="" maxlength="20" />
							</div>
							<div class="flex_1">
								<p class="sch_text">비밀번호 확인 :</p>
								<input type="password"   name="pwdch" class="ipt" value="" maxlength="20" />
							</div>
						</div>
						<div class="ipt_group flex_wrap">
							<div class="flex_1">
								<p class="sch_text">은행명 :</p>
								<%if(CommonUtil.nvl(dbMap.get("BANK_NAME")) != null && CommonUtil.nvl(dbMap.get("BANK_NAME")) != "") {%>
											           	<select name="bank_nm" id="bank_nm" class="ipt">
									                      <option value="">은행선택ㅂㅈㄷ</option>
										                  <!-- 은행 -->
										                  <optgroup label="은행">
										                    <option value="NH농협 은행">NH농협 은행</option>
										                    <option value="KB국민 은행">KB국민 은행</option>
										                    <option value="신한 은행">신한 은행</option>
										                    <option value="우리 은행">우리 은행</option>
										                    <option value="하나 은행">하나 은행</option>
										                    <option value="IBK기업 은행">IBK기업 은행</option>
										                    <option value="외한 은행">외한 은행</option>
										                    <option value="SC제일 은행">SC제일 은행</option>
										                    <option value="KDB산업 은행">KDB산업 은행</option>
										                    <option value="새마을 은행">새마을 은행</option>
										                    <option value="대구 은행">대구 은행</option>
										                    <option value="광주 은행">광주 은행</option>
										                    <option value="우체국 은행">우체국 은행</option>
										                    <option value="신협 은행">신협 은행</option>
										                    <option value="전북 은행">전북 은행</option>
										                    <option value="경남 은행">경남 은행</option>
										                    <option value="부산 은행">부산 은행</option>
										                    <option value="수협 은행">수협 은행</option>
										                    <option value="제주 은행">제주 은행</option>
										                    <option value="저축 은행">저축 은행</option>
										                    <option value="산림조합">산림조합</option>
										                    <option value="케이뱅크">케이뱅크</option>
										                    <option value="카카오뱅크">카카오뱅크</option>
										                  </optgroup>
										                  <!-- 증권사 -->
										                  <optgroup label="증권사">
										                    <option value="키움증권">키움증권</option>
										                    <option value="KB증권">KB증권</option>
										                    <option value="미래에셋대우">미래에셋대우</option>
										                    <option value="삼성증권">삼성증권</option>
										                    <option value="NH투자증권">NH투자증권</option>
										                    <option value="유안타">유안타</option>
										                    <option value="대신증권">대신증권</option>
										                    <option value="한국투자증권">한국투자증권</option>
										                    <option value="신한금융투자">신한금융투자</option>
										                    <option value="유진투자증권">유진투자증권</option>
										                    <option value="한화투자증권">한화투자증권</option>
										                    <option value="DB금융투자">DB금융투자</option>
										                    <option value="하나금융증권">하나금융증권</option>
										                    <option value="하이투자증권">하이투자증권</option>
										                    <option value="현대차투자증권">현대차투자증권</option>
										                    <option value="신영">신영</option>
										                    <option value="이베스트">이베스트</option>
										                    <option value="교보">교보</option>
										                    <option value="메리츠종금">메리츠종금</option>
										                    <option value="KTB투자">KTB투자</option>
										                    <option value="SK증권">SK증권</option>
										                    <option value="부국">부국</option>
										                    <option value="현대증권">현대증권</option>
										                    <option value="케이프투자">케이프투자</option>
										                    <option value="펀드온라인 코리아">펀드온라인 코리아</option>
										                  </optgroup>
									                </select>
														<br />																		
													<%} else {%>
													<select name="bank_nm" id="bank_nm" class="ipt">
									                      <option value="">은행선택</option>
										                  <!-- 은행 -->
										                  <optgroup label="은행">
										                    <option value="NH농협 은행">NH농협 은행</option>
										                    <option value="KB국민 은행">KB국민 은행</option>
										                    <option value="신한 은행">신한 은행</option>
										                    <option value="우리 은행">우리 은행</option>
										                    <option value="하나 은행">하나 은행</option>
										                    <option value="IBK기업 은행">IBK기업 은행</option>
										                    <option value="외한 은행">외한 은행</option>
										                    <option value="SC제일 은행">SC제일 은행</option>
										                    <option value="KDB산업 은행">KDB산업 은행</option>
										                    <option value="새마을 은행">새마을 은행</option>
										                    <option value="대구 은행">대구 은행</option>
										                    <option value="광주 은행">광주 은행</option>
										                    <option value="우체국 은행">우체국 은행</option>
										                    <option value="신협 은행">신협 은행</option>
										                    <option value="전북 은행">전북 은행</option>
										                    <option value="경남 은행">경남 은행</option>
										                    <option value="부산 은행">부산 은행</option>
										                    <option value="수협 은행">수협 은행</option>
										                    <option value="제주 은행">제주 은행</option>
										                    <option value="저축 은행">저축 은행</option>
										                    <option value="산림조합">산림조합</option>
										                    <option value="케이뱅크">케이뱅크</option>
										                    <option value="카카오뱅크">카카오뱅크</option>
										                  </optgroup>
										                  <!-- 증권사 -->
										                  <optgroup label="증권사">
										                    <option value="키움증권">키움증권</option>
										                    <option value="KB증권">KB증권</option>
										                    <option value="미래에셋대우">미래에셋대우</option>
										                    <option value="삼성증권">삼성증권</option>
										                    <option value="NH투자증권">NH투자증권</option>
										                    <option value="유안타">유안타</option>
										                    <option value="대신증권">대신증권</option>
										                    <option value="한국투자증권">한국투자증권</option>
										                    <option value="신한금융투자">신한금융투자</option>
										                    <option value="유진투자증권">유진투자증권</option>
										                    <option value="한화투자증권">한화투자증권</option>
										                    <option value="DB금융투자">DB금융투자</option>
										                    <option value="하나금융증권">하나금융증권</option>
										                    <option value="하이투자증권">하이투자증권</option>
										                    <option value="현대차투자증권">현대차투자증권</option>
										                    <option value="신영">신영</option>
										                    <option value="이베스트">이베스트</option>
										                    <option value="교보">교보</option>
										                    <option value="메리츠종금">메리츠종금</option>
										                    <option value="KTB투자">KTB투자</option>
										                    <option value="SK증권">SK증권</option>
										                    <option value="부국">부국</option>
										                    <option value="현대증권">현대증권</option>
										                    <option value="케이프투자">케이프투자</option>
										                    <option value="펀드온라인 코리아">펀드온라인 코리아</option>
										                  </optgroup>
									                </select>
														<br />	
													<%} %>
							
							</div>
							<div class="flex_1">
								<p class="sch_text">계좌번호 :</p>
								<input type="text"   name="account" class="ipt" value="<%=CommonUtil.nvl(dbMap.get("ACCOUNT_NUM")) %>" maxlength="20" />
							</div>
						</div>
						<div class="ipt_group flex_wrap">
							<div class="flex_1">
								<p class="sch_text">전화번호 :</p>
								<input type="text" name="telno" class="ipt" value="<%=CommonUtil.nvl(dbMap.get("PHONE")).replaceAll("-","") %>" maxlength="13" />
							</div>
							<div class="flex_1">
								<p class="sch_text">추천인 :</p>
								<input type="text"   name="recommender" class="ipt" value="<%=CommonUtil.nvl(dbMap.get("RECOMMENDER")) %>" maxlength="20" disabled/>
							</div>
						</div>
						<div class="board_list_btn right">
							<button type="button"  class="btn blue_trans" onclick="fSubmit();">완료</button>
							<button type="button" class="btn blue" onclick="location.href='/boffice/member/memberList.do?<%=CommonUtil.nvl(reqMap.get("menu_no"))%>'">목록</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>



	<%-- <tr>
								<th scope="col">회원ID</th>
								<td scope="col"><input type="text" style="width: 100px"
									name="user_id" class="input-text" value="" maxlength="10" /></td>
								<th scope="col">이름</th>
								<td scope="col"><input type="text" style="width: 100px"
									name="user_nm" class="input-text" value="" maxlength="20" /></td>
							</tr>

							<tr>
								<th scope="col">비밀번호</th>
								<td scope="col" colspan="3"><input type="password"
									style="width: 100px" name="pwd" class="input-text" value=""
									maxlength="10" /></td>
							</tr>
						
							<tr>
								<th scope="col">전화번호</th>
								<td scope="col" colspan="3"><input type="text"
									style="width: 60px" name="telno1" class="input-text" value=""
									maxlength="3" /> - <input type="text" style="width: 60px"
									name="telno2" class="input-text" value="" maxlength="4" /> - <input
									type="text" style="width: 60px" name="telno3"
									class="input-text" value="" maxlength="4" /></td>
							</tr>
							<tr class="configBtnTr">
								<td colspan="4" class="buttonTd" style="text-align: center">
									<button type="button" onclick="fSubmit();">완료</button>
									<button type="button"
										onclick="location.href='/boffice/memberList.do?<%=strParam%>'">목록</button>
								</td>
							</tr> --%>
</body>
</html>