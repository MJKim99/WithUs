package com.withus.spring.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.withus.spring.GenericValidator;
import com.withus.spring.domain.InquiryDTO;
import com.withus.spring.domain.PagingDTO;
import com.withus.spring.domain.SellerDTO;
import com.withus.spring.domain.UserDTO;
import com.withus.spring.service.InquiryService;
import com.withus.spring.service.InquiryanswerService;
import com.withus.spring.service.PagingService;
import com.withus.spring.service.SellerService;
import com.withus.spring.service.UserService;

@Controller
public class UserController {

	private UserService userService;
	private SellerService sellerService;
	private InquiryService inquiryService;
	private InquiryanswerService inqAnsService;
	private PagingService pagingService;

	@Autowired
	public void setService(UserService userService, SellerService sellerService, InquiryService inquiryService,
			InquiryanswerService inqAnsService, PagingService pagingService) {
		this.userService = userService;
		this.sellerService = sellerService;
		this.inquiryService = inquiryService;
		this.inqAnsService = inqAnsService;
		this.pagingService = pagingService;

	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/join")
	public String join() {
		return "main/joinForm";
	}

	@PostMapping("/joinOk")
	public String joinOk(@ModelAttribute("w") @Valid UserDTO user, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {

		String rawPassword = user.getPw();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPw(encPassword);

		model.addAttribute("user", user);
		redirectAttributes.addFlashAttribute("w", user);
		if (result.hasErrors()) {
			if (result.getFieldError("email") != null) {
				
				if (result.getFieldError("email").getCode().equals("noValue")) {
					Map<String, Object> map = new HashMap<>();
					map.put("EMAIL", "???????????? ??????????????????.");
					redirectAttributes.addFlashAttribute("ERROR1", map);
				} else {
					Map<String, Object> map = new HashMap<>();
					map.put("DUPLEMAIL", "????????? ??????????????????.");
					redirectAttributes.addFlashAttribute("ERROR1", map);
				}
			}
			if (result.getFieldError("pw") != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("PW", "??????????????? ??????????????????.");
				redirectAttributes.addFlashAttribute("ERROR3", map);
			}
			if (result.getFieldError("name") != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("NAME", "????????? ??????????????????.");
				redirectAttributes.addFlashAttribute("ERROR4", map);
			}
			return "redirect:/join";
		}

		int cnt = userService.addUser(user);
		return "redirect:/loginForm";
	}

	@RequestMapping("/loginForm/**")
	public String login() {
		return "main/loginForm";
	}

	@RequestMapping(value = "/main/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public void logout(HttpSession session) throws Exception {
		session.invalidate();
	}

	@RequestMapping("/profileForm/**")
	public String profileForm(Model model, Principal principal) {
		String email = printEmail(principal);
		model.addAttribute("prc", email);
		return "main/profileForm";
	}

	@RequestMapping("/profileMgt/**")
	public String profileMgt(Model model, Principal principal, UserDTO user) {
		String email = printEmail(principal);
		userService.findById(email);

		model.addAttribute("prc", email);
		model.addAttribute("user", userService.selectByEmail(email));
		return "main/profileMgt";
	}

	@RequestMapping("/profileUpdateOk")
	public String profileUpdateOk(UserDTO user, HttpServletRequest request, @RequestParam(value = "pw") String pw) {
		String p1 = request.getParameter("password1");
		String p2 = request.getParameter("password2");
		if (p1.equals("") && p2.equals("")) {
			return "redirect:/profileForm";
		} else if (p1.equals(p2)) {
			pw = p1;
		}

		/* ?????? ??????????????? ????????? ????????? */
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		/* ???????????? ??????????????? ?????? user????????? ??????. */
		pw = encoder.encode(pw);
		user.setPw(pw); // DB??? ?????????????????? ????????? ??? ??????????????? user ????????? ??????
		userService.updateProfile(user);
		
		return "redirect:/profileForm";
	}

	@RequestMapping("/myInquiryList")
	public String myInpuiry(Model model, Principal principal, Integer num,
			@RequestParam(value = "nowPage", required = false) String nowPage,
			@RequestParam(value = "cntPerPage", required = false) String cntPerPage) {

		if (num == null) {
			num = 1;
		}

		String email = printEmail(principal);

		if (email != null) {
			model.addAttribute("prc", email);
		} else {
			model.addAttribute("", email);
		}

		int total = pagingService.countInquiry(email);
		if (nowPage == null && cntPerPage == null) {
			nowPage = "1";
			cntPerPage = "5";
		} else if (nowPage == null) {
			nowPage = "1";
		} else if (cntPerPage == null) {
			cntPerPage = "5";
		}
		PagingDTO dto = new PagingDTO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		model.addAttribute("paging", dto);
		List<InquiryDTO> selectBoard = pagingService.selectInquiry(email, dto.getStart(), dto.getCntPage());
		model.addAttribute("inList", selectBoard);

		return "main/myInquiryList";
	}

	@RequestMapping("/viewInquiry")
	public String viewInquiry(Model model, int innum, Principal principal) {
		String email = printEmail(principal);

		if (email != null) {
			model.addAttribute("prc", email);
		} else {
			model.addAttribute("", email);
		}

		model.addAttribute("inList", inquiryService.selectBynum(innum));
		model.addAttribute("ansList", inqAnsService.showAnswer(innum));
		return "main/viewInquiry";
	}

	@RequestMapping("/deleteUser")
	public String userDelete(Model model, Principal principal, UserDTO user) {
		String email = printEmail(principal);
		userService.findById(email);

		model.addAttribute("prc", email);
		model.addAttribute("decodepw", userService.selectByEmail(email).getPw());
		model.addAttribute("user", userService.selectByEmail(email));

		return "main/deleteUser";
	}

	@RequestMapping("/deleteUserOk")
	public String userDeleteOk(Model model, Principal principal, UserDTO user, HttpServletRequest request) {
		String email = printEmail(principal);
		userService.findById(email);
		String pw = request.getParameter("pw");

		if (passwordEncoder.matches(pw, userService.selectByEmail(email).getPw())) {
			userService.deleteUser(user);
			model.addAttribute("msg", "???????????? ???????????????. ???????????????.");
			model.addAttribute("url", "/logout");
			return "main/deleteUserOk";
		}
		return "redirect:/deleteUser";
	}

	@RequestMapping("/regSeller")
	public String regSeller(Model model, Principal principal, SellerDTO seller, Authentication authentication,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String email = printEmail(principal);
		session.setAttribute("sessionId", email);
		String id = (String) session.getAttribute("sessionId");

		if (sellerService.existEmail(id) != null) {
			return "redirect:/seller/main";
		} else {
			model.addAttribute("prc", email);
			return "main/regSeller";
		}
	}

	@RequestMapping("/regSellerOk")
	public String regSellerOk(@ModelAttribute("s") @Valid SellerDTO seller, BindingResult result,
			RedirectAttributes redirectAttributes, Principal principal, Model model, HttpSession session,
			SessionStatus status) throws Exception {

		String email = printEmail(principal);
		redirectAttributes.addFlashAttribute("s", seller);
		if (result.hasErrors()) {
			if (result.getFieldError("name") != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("NAME", "????????? ??????????????????.");
				redirectAttributes.addFlashAttribute("ERROR1", map);
			}
			if (result.getFieldError("business") != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("BUSINESS", "?????????????????? ??????????????????.");
				redirectAttributes.addFlashAttribute("ERROR2", map);
			}
			if (result.getFieldError("jumin") != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("JUMIN", "??????????????? ??????????????????.");
				redirectAttributes.addFlashAttribute("ERROR3", map);

			}
			return "redirect:/regSeller";
		}
		
		sellerService.regSeller(seller);
		userService.addSeller(email);
		model.addAttribute("msg", "????????? ????????? ?????????????????????! ?????? ?????????????????????");
		model.addAttribute("url", "/loginForm");
		session.invalidate();
		status.setComplete();
		return "main/regSellerOk";
	}

	public String printEmail(Principal principal) {
		String regex = "^[0-9]*$"; // ????????? ??????????????? ???????????? ???????????????
		String emailRegex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]" + "([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"; // ?????????
																													// ??????
																													// //
																													// ???????????????
		try {
			String prc = principal.getName();
			if (prc != null) {
				if (Pattern.matches(emailRegex, prc)) { // ?????? ????????????
					return prc;
				}
				if (Pattern.matches(regex, prc)) { // ??????, ????????? ??????????????????
					prc = principal.toString();
					prc = StringUtils.remove(prc, "has_email=true,"); // ????????? ????????? ???????????? ????????? ????????????
				}
				System.out.println("??? API : " + prc);

				String regprc = StringUtils.substringBetween(prc, "email=", ",");// email= ??? , ????????? ?????? ?????????.
				return regprc;
			}
		} catch (NullPointerException e) {

			return null;
		}

		return null;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new GenericValidator(userService, sellerService));
	}

}