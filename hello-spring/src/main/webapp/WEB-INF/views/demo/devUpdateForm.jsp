<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Dev 수정" name="title"/>
</jsp:include>
<style>
div#demo-container{
	width:550px;
	padding:15px;
	border:1px solid lightgray;
	border-radius: 10px;
}
</style>
<div id="demo-container" class="mx-auto">
	<!-- https://getbootstrap.com/docs/4.1/components/forms/#readonly-plain-text -->
	<form id="devUpdateFrm"
		  method="post" 
		  action="${pageContext.request.contextPath}/demo/updateDev.do">
		<div class="form-group row">
		  <label for="name" class="col-sm-2 col-form-label">이름</label>
		  <div class="col-sm-10">
		    <input type="text" class="form-control" id="name" name="name" value="${dev.name}" required>
		  </div>
		</div>
		<div class="form-group row">
		  <label for="career" class="col-sm-2 col-form-label">개발경력</label>
		  <div class="col-sm-10">
		    <input type="number" class="form-control" id="career" name="career" value="${dev.career}" required>년
		  </div>
		</div>
		<div class="form-group row">
		  <label for="email" class="col-sm-2 col-form-label">이메일</label>
		  <div class="col-sm-10">
		    <input type="email" class="form-control" id="email" name="email" value="${dev.email}" required>
		  </div>
		</div>
	  	<!-- https://getbootstrap.com/docs/4.1/components/forms/#inline -->
	    <div class="form-group row">
	    	<label class="col-sm-2 col-form-label">성별</label>
	    	<div class="col-sm-10">
			    <div class="form-check form-check-inline">
				  <input class="form-check-input" type="radio" name="gender"
				  		 id="gender0" value="M" ${dev.gender eq 'M' ? 'checked' : ''}>
				  <label class="form-check-label" for="gender0">남</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="radio" name="gender" 
				  		 id="gender1" value="F" ${dev.gender eq 'F' ? 'checked' : ''}>
				  <label class="form-check-label" for="gender1">여</label>
				</div>
			</div>
		</div>
		<div class="form-group row">
			<label class="col-sm-2 col-form-label">개발언어</label>
			<div class="col-sm-10">
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Java" value="Java">
				  <label class="form-check-label" for="Java">Java</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="C" value="C">
				  <label class="form-check-label" for="C">C</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Javascript" value="Javascript">
				  <label class="form-check-label" for="Javascript">Javascript</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Python" value="Python">
				  <label class="form-check-label" for="Python">Python</label>
				</div>
			</div>
		</div>
		<!-- 중요 - 수정시 반드시 고유번호도 함께 넘겨주어야 함 -->
	  	<input type="hidden" name="no" value="${dev.no}" />
	  	<button type="submit" class="list-group-item list-group-item-action">dev 수정</button>
	</form>
</div>

<script>
window.addEventListener('load', (e) => {
	<c:forEach items="${dev.lang}" var="lang" varStatus="vs">
		<c:if test="${lang eq lang}">
			$("input:checkbox[id=${lang}]").prop("checked", true);
		</c:if>
	</c:forEach>
});

function devValidate() {
	// required 속성은 form 안에 제출 버튼이 있는 경우 유효함
 	// 폼 밖에서 제출하는 경우 별도의 validation 메소드가 필요함
 	let $name = $("#name");
	if(/^[가-힣]{2,}$/.test($name.val()) == false) {
		alert("이름을 작성하세요");
		return false;
	}
	
	// 클라이언트단 유효성검사
	let $lang = $("[name=lang]");
	if($lang.filter(":checked").length == 0) {
		alert("하나 이상의 개발언어를 선택하세요.")
		return false;
	}
	
	return true;
}
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
