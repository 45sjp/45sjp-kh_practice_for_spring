package com.kh.spring.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.demo.model.dto.Dev;
import com.kh.spring.demo.model.dto.Gender;
import com.kh.spring.demo.model.service.DemoService;

/**
 * @Controller 클래스의 handler메소드가 가질 수 있는 매개변수 타입
 * 
 * HttpServletRequest
 * HttpServletResponse
 * HttpSession
 * 
 * java.util.Locale : 요청에 대한 Locale
 * InputStream/Reader : 요청에 대한 입력스트림
 * OutputStream/Writer : 응답에 대한 출력스트림. ServletOutputStream, PrintWriter
 * 
 * 사용자입력값처리
 * 	- Command객체 : http요청 파라미터를 커맨드객체에 저장한 VO객체
 * 	- CommandMap :  HandlerMethodArgumentResolver에 의해 처리된 사용자입력값을 가진 Map객체
 * @Valid : 커맨드객체 유효성 검사객체
 * 	- Error, BindingResult : Command객체에 저장결과(Command객체 바로 다음에 위치시킬 것)
 * @PathVariable : 요청url중 일부를 매개변수로 취할 수 있음
 * @RequestParam : 사용자입력값을 자바변수에 대입처리(필수여부 설정)
 * @RequestHeader : 헤더값
 * @CookieValue : 쿠키값
 * @RequestBody : http message body에 작성된 json을 vo객체로 변환처리
 * 
 * 뷰에 전달할 모델 데이터 설정
 * 	- ModelAndView
 * 	- ModelMap 
 * 	- Model
 * 
 * @ModelAttribute : model속성에 대한 getter
 * @SessionAttribute : session속성에 대한 getter(required 여부 선택 가능)
 * @SessionAttributes : session에서 관리될 속성명을 class-level에 작성
 * SessionStatus : @SessionAttributes로 등록된 속성에 대하여 사용완료(complete)처리. 세션을 폐기하지 않고 재사용함
 * 
 * 기타
 * 	- MultipartFile : 업로드파일 처리 인터페이스. CommonsMultipartFile
 * 	- RedirectAttributes : DML처리후 요청주소 변경을 위한 redirect시 속성처리 지원
 *
 */
@Controller
@RequestMapping("/demo") // 중복된 부분을 클래스 레벨에서 RequestMapping으로 작성
public class DemoController {
	
	static Logger log = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	private DemoService demoService;
	
	/**
	 * 전송방식 : GET(기본값)
	 * @return
	 */
	@RequestMapping("/devForm.do")
	public String devForm() {
		log.info("GET /demo/devForm.do 요청");
		log.info("{}", 1234567890);
		return "demo/devForm";
	}
	
	@RequestMapping("/dev1.do")
	public String dev1(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		int career = Integer.parseInt(request.getParameter("career"));
		String email = request.getParameter("email");
		String _gender = request.getParameter("gender");
		Gender gender = _gender != null ? Gender.valueOf(_gender) : null;
		String[] langs = request.getParameterValues("lang");
		
		Dev dev = new Dev(0, name, career, email, gender, langs, LocalDateTime.now());
		log.info("dev = {}", dev);
		
		request.setAttribute("dev", dev);
		
		return "demo/devResult";
	}
	
	// method를 작성하면 한정할 수 있고, 작성하지 않으면 모두 처리할 수 있음
	@RequestMapping(value="/dev2.do", method=RequestMethod.GET)
	public String dev2(
			@RequestParam String name,
			@RequestParam int career,
			@RequestParam String email,
			// 필수가 아닌 경우 false 처리, 입력하지 않았을 경우 기본값 처리 또한 가능!
			@RequestParam(required=false, defaultValue="M") Gender gender,
			// 이름이 일치하지 않을 경우에도 설정 가능!
			@RequestParam(name="lang") String[] langs,
			Model model) {
		
		Dev dev = new Dev(0, name, career, email, gender, langs, LocalDateTime.now());
		log.info("dev = {}", dev);
		
		model.addAttribute("dev", dev); // view단에 전달할 데이터를 모델에 속성으로 등록
		
		return "demo/devResult";
	}
	
	/**
	 * 커맨드 객체
	 * 	- 전송된 사용자 입력값과 대응하는 필드에 자동으로 값 대입
	 * 	- 커맨드객체는 자동으로 model에 속성으로 등록됨
	 * @param dev
	 * @return
	 */
	@RequestMapping("/dev3.do")
	public String dev3(Dev dev) {
		log.info("dev = {}", dev);
		return "demo/devResult";
	}
	
	@RequestMapping(path="/insertDev.do", method=RequestMethod.POST)
	public String insertDev(Dev dev, RedirectAttributes redirectAttr) {
		log.info("dev = {}", dev);
		
		int result = demoService.insertDev(dev);
		log.info("result = {}", result);
		
		// redirect 후에 사용자가 확인할 수 있는 정보 저장
		redirectAttr.addFlashAttribute("msg", "Dev 등록이 정상적으로 처리되었습니다.");
		
		return "redirect:/demo/devForm.do";
	}
	
	@RequestMapping("/devList.do")
	public String devList(Model model) {
		List<Dev> list = demoService.selectDevList();
		log.info("list = {}", list);
		model.addAttribute("list", list);
		return "demo/devList";
	}
	
	@RequestMapping(path="/updateDev.do", method=RequestMethod.GET)
	public String updateDev(@RequestParam int no, Model model) {
		// 사용자가 입력한 no값으로 Dev 한 건 조회 후 view단 전달
		Dev dev = demoService.selectOneDev(no);
		log.info("dev = {}", dev);
		model.addAttribute("dev", dev);
		return "demo/devUpdateForm";
	}
	
	@RequestMapping(path="/updateDev.do", method=RequestMethod.POST)
	public String updateDev(Dev dev, Model model) {
		log.info("dev = {}", dev);
		int result = demoService.updateDev(dev);
		model.addAttribute("msg", "Dev 수정이 정상적으로 처리되었습니다.");
		return "demo/devUpdateForm";
	}
	
	@RequestMapping(path="/deleteDev.do", method=RequestMethod.POST)
	public String deleteDev(@RequestParam int no, RedirectAttributes redirectAttr) {
		int result = demoService.deleteDev(no);
		redirectAttr.addFlashAttribute("msg", "Dev 삭제가 정상적으로 처리되었습니다.");
		return "redirect:/demo/devList.do";
	}
	
}
