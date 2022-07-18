<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="게시글 수정" name="title"/>
</jsp:include>
<style>
div#board-container{width:400px; margin:0 auto; text-align:center;}
div#board-container input{margin-bottom:15px;}
/* 부트스트랩 : 파일라벨명 정렬*/
div#board-container label.custom-file-label{text-align:left;}
</style>

<div id="board-container">
	<form 
		name="boardFrm" 
		action="${pageContext.request.contextPath}/board/boardUpdate.do" 
		method="post"
		enctype="multipart/form-data">
		<input type="text" class="form-control" placeholder="제목" name="title" id="title" value="${board.title}" required>
		<input type="text" class="form-control" name="memberId" value="${loginMember.memberId}" readonly required>
		<c:if test="${not empty board.attachments}">
			<c:forEach items="${board.attachments}" var="attach" varStatus="vs">
				<div class="btn-group-toggle p-0 mb-3" data-toggle="buttons">
					<label class="btn btn-outline-danger btn-block" title="${attach.originalFilename} 삭제">
						<input type="checkbox" id="delFile${vs.count}" name="delFile" value="${attach.no}">
						<c:if test="${fn:length(attach.originalFilename) ge 30}">
							${fn:substring(attach.originalFilename, 0, 30)}... 삭제
	                   	</c:if> 
	                   	<c:if test="${fn:length(attach.originalFilename) lt 30}">
	                   		${attach.originalFilename} 삭제
	                   	</c:if>
					</label>
                </div>
			</c:forEach>
		</c:if>
		<div class="input-group mb-3" style="padding:0px;">
		  <div class="input-group-prepend" style="padding:0px;">
		    <span class="input-group-text">첨부파일</span>
		  </div>
		  <div class="custom-file">
		    <input type="file" class="custom-file-input" name="upFile" id="upFile1" multiple>
		    <label class="custom-file-label" for="upFile1">파일을 선택하세요.</label>
		  </div>
		</div>
		
	    <textarea class="form-control" name="content" placeholder="내용" required>${board.content}</textarea>
		<br />
		<input type="hidden" name ="no" value="${board.no}" />
		<input type="submit" class="btn btn-outline-success" value="저장" >
	</form>
</div>
<script>
document.querySelectorAll("[name=upFile]").forEach((input) => {
	input.addEventListener('change', (e) => {
		const [file] = e.target.files;
		console.log(file);
		const label = e.target.nextElementSibling;
		console.log(label);
		
		if(file) {
			const {name} = file;
			label.innerText = name;
		}
		else {
			label.innerText = "파일을 선택하세요.";
		}
	});
});

/**
 * title/content 유효성 검사
 */
document.boardFrm.addEventListener('submit', (e) => {
	const title = document.querySelector("#title");
	const content = document.querySelector("#content");
	
	if(!/^.+$/.test(title.value)) {
		e.preventDefault();
		alert("제목을 작성해주세요.");
		return;
	}
	
	if(!/^(.|\n)+$/.test(content.value)) {
		e.preventDefault();
		alert("내용을 작성해주세요.");
		return;
	}
});
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
