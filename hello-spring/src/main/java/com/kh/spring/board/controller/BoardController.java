package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.model.dto.Attachment;
import com.kh.spring.board.model.dto.Board;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.common.HelloSpringUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	ServletContext application;
	
	@Autowired
	ResourceLoader resourceLoader; // Resource를 불러오는 의존
	
	@GetMapping("/boardList.do")
	public ModelAndView boardList(
			@RequestParam(defaultValue = "1") int cPage,
			ModelAndView mav,
			HttpServletRequest request) {
		try {
			// 목록 조회
			int numPerPage = 5;
			List<Board> list = boardService.selectBoardList(cPage, numPerPage);
			// log.debug("list = {}", list);
			mav.addObject("list", list);
			
			// 페이지바
			int totalContent = boardService.selectTotalContent();
			String url = request.getRequestURI();
			// log.debug("totalContent = {}", totalContent);
			String pagebar = HelloSpringUtils.getPagebar(cPage, numPerPage, totalContent, url);
			// log.debug("pagebar = {}", pagebar);
			mav.addObject("pagebar", pagebar);
			
			// viewName 설정
			mav.setViewName("board/boardList");
		} catch (Exception e) {
			log.error("게시글 목록 조회 오류", e);
		}
		return mav;
	}
	
	@GetMapping("/boardForm.do")
	public void boardForm() {}
	
	// ModelAndView와 RedirectAttributes를 함께 쓰면 충돌날 수 있음!
	@PostMapping("/boardEnroll.do")
	public String boardEnroll(
			@ModelAttribute Board board, 
			@RequestParam("upFile") MultipartFile[] upFiles, 
			RedirectAttributes redirectAttr) throws Exception {
		try {
			log.debug("board = {}", board);
			// log.debug("application = {}", application);
			
			String saveDirectory = application.getRealPath("/resources/upload/board");
			// log.debug("saveDirectory = {}", saveDirectory);
			
			// 업로드한 파일 저장
			for(MultipartFile upFile : upFiles) {
				// log.debug("upFile = {}", upFile); // org.springframework.web.multipart.commons.CommonsMultipartFile@54863bee
				// log.debug("upFile#originalFilename = {}", upFile.getOriginalFilename()); // mainpage.jpg
				// log.debug("upFile#size = {}", upFile.getSize()); // 파일이 있을 경우 : 390529, 없을 경우 : 0				
				
				if(upFile.getSize() > 0) {
					String originalFilename = upFile.getOriginalFilename();
					String renamedFilename = HelloSpringUtils.getRenamedFilename(originalFilename);
					log.debug("renamedFilename = {}", renamedFilename);
					
					File destFile = new File(saveDirectory, renamedFilename);
					upFile.transferTo(destFile); // 실제 파일 저장
					
					// Attachment객체 -> Board#attachments에 추가
					Attachment attach = new Attachment();
					attach.setOriginalFilename(originalFilename);
					attach.setRenamedFilename(renamedFilename);
					board.addAttachment(attach);
				}
			}
			
			int result = boardService.insertBoard(board);
			redirectAttr.addFlashAttribute("msg", "게시글을 성공적으로 등록했습니다.");
		} catch (IOException e) {
			log.error("첨부파일 저장 오류", e);
			throw e;
		} catch (Exception e) {
			log.error("게시글 등록 오류", e);
			throw e;
		}
		return "redirect:/board/boardDetail.do?no=" + board.getNo();
	}
	
	@GetMapping("/boardDetail.do")
	public ModelAndView boardDetail(ModelAndView mav, @RequestParam int no) {
		try {
			// 게시글 조회
			// Board board = boardService.selectOneBoard(no);
			Board board = boardService.selectOneBoardCollection(no);
			log.debug("board = {}", board);
			mav.addObject("board", board);
			
			// viewName 설정
			mav.setViewName("board/boardDetail"); // 생략 가능
		} catch (Exception e) {
			log.error("게시글 상세 조회 오류", e);
		}
		return mav;
	}
	
	@GetMapping("/boardUpdate.do")
	public void boardUpdate(@RequestParam int no, Model model) {
		try {
			Board board = boardService.selectOneBoard(no);
			log.debug("board = {}", board);
			model.addAttribute("board", board);
		} catch (Exception e) {
			log.error("게시글 수정폼 오류", e);
			throw e;
		}
	}
	
	@PostMapping("/boardUpdate.do")
    public String boardUpdate(
    		@ModelAttribute Board board, 
            @RequestParam("upFile") MultipartFile[] upFiles, 
            @RequestParam(value="delFile", required=false) int[] delFiles, 
            RedirectAttributes redirectAttr) throws Exception {
		try {
			log.debug("board = {}", board);
			String saveDirectory = application.getRealPath("/resources/upload/board");
			
			// 1. 첨부파일 삭제 (파일 삭제 + table 행 삭제)
	        // 복수 개의 delFiles 처리
			// 첨부 파일 삭제 처리
			if(delFiles != null) {
				for(int attachNo : delFiles) {
					Attachment attach = boardService.selectOneAttachment(attachNo);
								
					// a. 저장된 파일에 대한 처리 - 파일 삭제
					String renamedFilename = attach.getRenamedFilename();
					File delFile = new File(saveDirectory, renamedFilename);
					if(delFile.exists()) {
						delFile.delete(); // 기존 파일 제거
						log.debug("{}번 {}파일 삭제", attachNo, renamedFilename);
					}
								
					// b. db record에 대한 처리 - db record 삭제
					int result = boardService.deleteAttachments(attachNo);
					log.debug("{}번 Attachment 레코드 삭제!", attachNo);
				}
			}
			
			// 2. 첨부파일 등록 (파일 저장 + Attachment객체 생성 추가)
			// 업로드한 파일 저장
			for(MultipartFile upFile : upFiles) {
				if(upFile.getSize() > 0) {					
					Attachment attach = new Attachment();
					attach.setOriginalFilename(upFile.getOriginalFilename());
					attach.setRenamedFilename(HelloSpringUtils.getRenamedFilename(upFile.getOriginalFilename()));
					attach.setBoardNo(board.getNo());
					board.addAttachment(attach);
					
					File destFile = new File(saveDirectory, attach.getRenamedFilename());
					upFile.transferTo(destFile); // 실제 파일 저장
				}
			}
			
			
			// 3. 게시글 수정 (board 수정 + 복수 개의 attachment 등록)
			int result = boardService.updateBoard(board);
			redirectAttr.addFlashAttribute("msg", "게시글을 성공적으로 수정했습니다.");
		} catch (Exception e) {
			log.error("게시글 수정 오류", e);
			throw e;
		}
		return "redirect:/board/boardDetail.do?no=" + board.getNo(); // board#no
	}
	
	/**
	 * Resource : PSA
	 * 	- UrlResource
	 * 	- ClassPathResource
	 * 	- FileSystemResource : 서버 컴퓨터 지원
	 * 	- ServletContextResource : 웹 루트 상의 자원
	 * 	- InputStreamResource
	 * 	- ByteArrayResource
	 * 
	 * @ResponseBody
	 * 	- 응답메세지 바디에 핸들러 리턴 객체를 직접 출력
	 * @param no
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(path = "/fileDownload.do", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public Resource fileDownload(@RequestParam int no, HttpServletResponse response) throws Exception {
		Resource resource = null;
		try {
			// 1. 첨부파일 조회
			Attachment attach = boardService.selectOneAttachment(no);
			log.debug("attach = {}", attach);
			
			String saveDirectory = application.getRealPath("/resources/upload/board");
			String renamedFilename = attach.getRenamedFilename();
			File downFile = new File(saveDirectory, renamedFilename);
			
			// 2. Resource 객체 생성
			String location = "file:" + downFile; // File#toString이 파일 절대경로로 오버라이드되어 있음
			log.debug("location = {}", location);
			resource = resourceLoader.getResource(location);
			
			// 3. 응답헤더 작성
			String filename = URLEncoder.encode(attach.getOriginalFilename(), "utf-8");
//			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.addHeader(HttpHeaders.CONTENT_DISPOSITION, 
					"attachment; filename=\"" + filename + "\"");
		} catch (Exception e) {
			log.error("파일 다운로드 오류!", e);
			throw e;
		}
		return resource;
	}
	
}
