package com.withus.spring.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.withus.spring.domain.FundedDTO;
import com.withus.spring.domain.InquiryDTO;
import com.withus.spring.domain.LikesDTO;
import com.withus.spring.domain.NoticeDTO;
import com.withus.spring.domain.PagingDTO;
import com.withus.spring.domain.ProjectFundingDTO;
import com.withus.spring.domain.UserShpDTO;
import com.withus.spring.service.AdminService;
import com.withus.spring.service.FundedService;
import com.withus.spring.service.InquiryService;
import com.withus.spring.service.InquiryanswerService;
import com.withus.spring.service.PagingService;
import com.withus.spring.service.ProjectFundingService;
import com.withus.spring.service.ShippingService;
import com.withus.spring.service.UserService;

@Controller
@RequestMapping("/**")
public class FundingController {

	private FundedService fundedService;
	private ProjectFundingService projFundingService;
	private ShippingService shippingService;
	private UserService userService;
	private InquiryService InquiryService;
	private InquiryanswerService inquiryanswerService;
	private PagingService pagingService;
	private AdminService adminService;

	@Autowired
	public void setService(InquiryanswerService inquiryanswerService, FundedService fundedService,
			ProjectFundingService projFundingService, InquiryService inquiryService, UserService userService,
			ShippingService shippingService, PagingService pagingService, AdminService adminService) {
		this.inquiryanswerService = inquiryanswerService;
		this.fundedService = fundedService;
		this.projFundingService = projFundingService;
		this.InquiryService = inquiryService;
		this.userService = userService;
		this.shippingService = shippingService;
		this.pagingService=pagingService;
		this.adminService=adminService;
	}

	@RequestMapping("/main")
	public String mainPage(Model model, Principal principal) {

		String str = printEmail(principal);
		if (str != null) {
			model.addAttribute("prc", str);
			model.addAttribute("popular",projFundingService.popularArticle());
			model.addAttribute("viewcnt",projFundingService.viewcount());
			model.addAttribute("support",projFundingService.support());
			return "main/main";
		} else {
			model.addAttribute("popular",projFundingService.popularArticle());
			model.addAttribute("viewcnt",projFundingService.viewcount());
			model.addAttribute("support",projFundingService.support());
			return "main/main";
		}
	}

	// ????????? ?????? ?????????
	public String printEmail(Principal principal) {
		String regex = "^[0-9]*$"; // ????????? ??????????????? ???????????? ???????????????
		String emailRegex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]" + "([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"; // ?????????
		// ??????
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

				String regprc = StringUtils.substringBetween(prc, "email=", ",");// email= ??? , ????????? ?????? ?????????.
				return regprc;
			}
		} catch (NullPointerException e) {
			return null;
		}
		return null;
	}

	@RequestMapping("/comingsoon/**")
	public String fundExpected(Model model, Principal principal) {
		String email = printEmail(principal);
		model.addAttribute("prc", email);
		return "main/comingsoon";
	}

	@RequestMapping("/payment/**")
	public String payment() {
		return "main/paymentForm";
	}

	@PostMapping("/paymentOk")
	public String paymentOk(Model model, Principal principal, HttpServletRequest req, HttpServletResponse resp) {
		String userEmail = printEmail(principal);
		String fulladdr = req.getParameter("roadAddrPart1");
		String addrDetail = req.getParameter("addrDetail");
		String addr = fulladdr.concat(" " + addrDetail);

		model.addAttribute("update", userService.insertAddr(addr, userEmail));
		return "main/paymentOk";
	}

	@RequestMapping("/popup/jusoPopup/**")
	public String jusoPopup() {
		return "main/popup/jusoPopup";
	}

	@RequestMapping("/fund")
	public String fund(Model model, Principal principal, int prnum) {
		int like = 0;
		String userEmail = printEmail(principal);
		LikesDTO lkDto = new LikesDTO();

		if (userEmail != null) {
			lkDto.setUserEmail(userEmail);
		} else {
			lkDto.setUserEmail("");
		}
		lkDto.setPrnum(prnum);

		int check = fundedService.chkLikeCnt(lkDto);
		if (check == 0 && userEmail != null) { // user??? ???????????? ?????? ????????? ?????? ????????? ??????
			fundedService.insertLike(lkDto);
		} else if (check == 1) {
			like = fundedService.getLikeCnt(lkDto);
		}
		// ?????? ?????? ?????????
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formatedNow = now.format(formatter);

		model.addAttribute("prc", userEmail);
		model.addAttribute("like", like);
		model.addAttribute("dto", lkDto);
		model.addAttribute("now", formatedNow);
		
		model.addAttribute("page", projFundingService.viewByPrnum(prnum));
		model.addAttribute("support", projFundingService.countsupport(prnum));
		model.addAttribute("fund", projFundingService.fundedPrice(prnum));
		model.addAttribute("quiry", InquiryService.selectByPrnum(prnum));
		model.addAttribute("check", userService.checkAddr(userEmail));
		System.out.println(model.toString());
		return "main/fund";
	}

	@RequestMapping("/notice")
	public String viewNotice(Model model, Principal principal,
			PagingDTO dto, Integer num1 ,
			@RequestParam(value="nowPage", required=false)String nowPage,
			@RequestParam(value="cntPerPage", required=false)String cntPerPage
			) {
		
		if(num1 == null)
		{
			num1=1;
		}
		
		int total = pagingService.cusernotice("notice");
		if (nowPage == null && cntPerPage == null) {
			nowPage = "1";
			cntPerPage = "5";
		} else if (nowPage == null) {
			nowPage = "1";
		} else if (cntPerPage == null) { 
			cntPerPage = "5";
		}
		dto = new PagingDTO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		model.addAttribute("paging", dto);
		List<NoticeDTO> selectBoard = pagingService.userNotice("notice", dto.getStart(), dto.getCntPage()); // cntPerPage X ,notice
		model.addAttribute("list", selectBoard);

		return "main/more/notice";
	}

	@RequestMapping("/event")
	public String event(Model model, Principal principal,
			PagingDTO dto, Integer num1 ,
			@RequestParam(value="nowPage", required=false)String nowPage,
			@RequestParam(value="cntPerPage", required=false)String cntPerPage
			) {
		
		if(num1 == null)
		{
			num1=1;
		}
		
		int total = pagingService.cuserevent("event"); // cntPerPage X ,event??? ???????????????
		if (nowPage == null && cntPerPage == null) {
			nowPage = "1";
			cntPerPage = "5";
		} else if (nowPage == null) {
			nowPage = "1";
		} else if (cntPerPage == null) { 
			cntPerPage = "5";
		}
		dto = new PagingDTO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		model.addAttribute("paging", dto);
		
		String email = printEmail(principal);
		model.addAttribute("prc", email);
		model.addAttribute("list", pagingService.userEvent("event", dto.getStart(), dto.getCntPage()));
		return "main/more/event";
	}

	@RequestMapping("/viewNotice")
	public String viewNotice(int num, Model model, Principal principal) {
		String email = printEmail(principal);
		model.addAttribute("prc", email);
		model.addAttribute("list", projFundingService.selectByNum(num));
		return "main/more/viewNotice";
	}
	
	@RequestMapping("/fundpage")
	public String fundpage(Model model, Principal principal, ProjectFundingDTO dto) {
		String email = printEmail(principal);

		model.addAttribute("prc", email);
		model.addAttribute("projectList", projFundingService.opening());
		return "main/fundpage";
	}
	
	@RequestMapping("/comingsoon")
	public String notOpen(Model model, Principal principal, ProjectFundingDTO dto) {
		String prc = printEmail(principal);

		model.addAttribute("prc", prc);
		model.addAttribute("comingsoon",projFundingService.comingsoon());
		return "main/comingsoon";
	}

	@RequestMapping("/fundpage/{category}")
	public String fundpage_category(Model model, @PathVariable("category") String category) {
		model.addAttribute("category", projFundingService.selectCategory(category));
		return "main/fundpage_category";
	}

	@PostMapping("/InquiryOk")
	public String quiryOk(InquiryDTO dto, Model model) {
		model.addAttribute("result", InquiryService.writeQuiry(dto));
		model.addAttribute("last", InquiryService.selectLastNum());
		return "main/InquiryOk";
	}

	@RequestMapping("/myfundedlist")
	public String myfundedlist(Model model, Principal principal
			,PagingDTO dto, Integer prnum,
			@RequestParam(value="nowPage", required=false)String nowPage,
			@RequestParam(value="cntPerPage", required=false)String cntPerPage
			) {
		
		if(prnum == null)
		{
			prnum=1;
		}
		
		String useremail = printEmail(principal);
		List<String> prnums = fundedService.myprnumlist(useremail);
		List<UserShpDTO> deliverys = new ArrayList<UserShpDTO>();
		model.addAttribute("gettitle",fundedService.getcnt(useremail));
		List<ProjectFundingDTO> title = new ArrayList<ProjectFundingDTO>();
		
		for (int i = 0; i < prnums.size(); i++) {
			UserShpDTO shp = new UserShpDTO();
			shp = shippingService.dvnumByShp(prnums.get(i));
			deliverys.add(shp);
		}
		
		
		List<FundedDTO> user = fundedService.myfundedlist(useremail);
		int total = pagingService.countMfl(prnum);
		if (nowPage == null && cntPerPage == null) {
			nowPage = "1";
			cntPerPage = "5";
		} else if (nowPage == null) {
			nowPage = "1";
		} else if (cntPerPage == null) { 
			cntPerPage = "5";
		}
		dto = new PagingDTO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		model.addAttribute("paging", dto);
		List<FundedDTO> selectBoard = pagingService.selectMfl(useremail, dto.getStart(), dto.getCntPerPage());
		model.addAttribute("viewAll", selectBoard);
		
		
		model.addAttribute("delList", deliverys);
		model.addAttribute("myflist", fundedService.myfundedlist(useremail));
		return "main/myfundedlist";
	}

	@PostMapping("payFinished")
	public String fundPay(Model model, @RequestBody FundedDTO dto) {
		model.addAttribute("pay", fundedService.fund(dto));
		return "main/payFinished";
	}

}
