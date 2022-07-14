package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
			RedirectAttributes redirectAttr) {
		try {
			log.debug("board = {}", board);
			// log.debug("application = {}", application);
			// log.debug("saveDirectory = {}", saveDirectory);
			
			String saveDirectory = application.getRealPath("/resources/upload/board");
			
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
				}
			}
			
			// int result = boardService.insertBoard(board);
			redirectAttr.addFlashAttribute("msg", "게시글을 성공적으로 등록했습니다.");
		} catch (IOException e) {
			log.error("첨부파일 저장 오류", e);
		} catch (Exception e) {
			log.error("게시글 등록 오류", e);
			throw e;
		}
		return "redirect:/board/boardList.do";
	}
	
}
