package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import dao.ConsumerMybatisDao;
import model.Consumer1;

@Controller
@RequestMapping("/consumer/") // browser url project-name/consumer/*
public class ConsumerController {
	@Autowired
	ConsumerMybatisDao consumerdao;

	Model c;
	HttpSession session;
	HttpServletRequest request;

	// 초기화 작업을 한다, 객체 초기화시에 사용함
	@ModelAttribute
	void init(HttpServletRequest request, Model c) {
		this.request = request;
		this.c = c;
		session = request.getSession();
	}

	@RequestMapping("alert")
	public String alert(String id) {
		String msg = "login 하세요";
		String url = "/consumer/loginForm";

		if (id.equals("admin")) {
			msg = "자료 확인 불가합니다";
		}
		c.addAttribute("msg", msg);
		c.addAttribute("url", url);
		return "/alert";
	}

	@RequestMapping("index")
	public String index() {
		System.out.println("request ok");
		c.addAttribute("name", "/consumer/*");
		return "/index"; // web-app/index !!!!!! web-app/consumer/index--->
									// web-app/consumer/index
	}
/*
	@RequestMapping("joinForm")
	public String joinForm() {
		System.out.println("request ok");
		return "/consumer/DrinkInsert";
	}
*/
	
	@RequestMapping("joinPro")
	public String joinPro(Consumer1 consumer) throws Exception {
		System.out.println("insert start ok");

		int count = consumerdao.insertConsumer(consumer);

		String msg = "";
		String url = "";

		if (count > 0) {
			msg = consumer.getName() + "님의 가입이 완료되었습니다.";
			url = "/consumer/loginForm";
		} else {
			msg = "회원가입이 실패 했습니다.";
			url = "/consumer/joinForm";
		}

		// 자료 보내기
		c.addAttribute("msg", msg);
		c.addAttribute("url", url);

		return "/alert";
	}

	@RequestMapping("loginForm")
	public String loginForm() {
		System.out.println("loginForm ok");
		return "/consumer/loginForm";
	}

	@RequestMapping("loginPro")
	public String loginPro(String id, String pass) {
		System.out.println("loginPro ok");

		String msg = "아이디를 확인하세요";
		String url = "/consumer/loginForm";
		
		Consumer1 con = consumerdao.selectOne(id);
		System.out.println(id);
		if (con != null) {
			if (pass.equals(con.getPass())) {
				request.getSession().setAttribute("id", id);
				msg = con.getName() + "님이 로그인 하였습니다.";
				url = "/consumer/index";
			} else {
				msg = "비밀번호를 확인 하세요";
			}
		}
		c.addAttribute("msg", msg);
		c.addAttribute("url", url);
		return "/alert";

	}

	@RequestMapping("logout")
	public String logout() {
		String login = (String) session.getAttribute("id");
		System.out.println(login);
		session.invalidate(); // logout
		String msg = login + "님이 로그아웃 되었습니다.";
		String url = "/consumer/loginForm";

		c.addAttribute("msg", msg);
		c.addAttribute("url", url);
		return "/alert";
	}

	@RequestMapping("consumerInfo")
	public String consumerInfo() {
		String login = (String) session.getAttribute("id");
		System.out.println(login);
			Consumer1 con = consumerdao.selectOne(login);
			c.addAttribute("consumer", con);
			return "/consumer/consumerInfo"; // joinForm.js copy
	}

	@RequestMapping("consumerUpdateForm")
	public String consumerUpdateForm() {
		String login = (String) session.getAttribute("id");
			Consumer1 con = consumerdao.selectOne(login);
			c.addAttribute("consumer", con);
			return "/consumer/consumerUpdateForm"; // joinForm.js copy
	}

	@RequestMapping("consumerUpdatePro")
	public String consumerUpdatePro(Consumer1 consumer) throws Exception {
		String login = (String) session.getAttribute("id");
		consumer.setId(login);
		String msg = "회원 자료가 없습니다";
		String url = "/consumer/loginForm";

			Consumer1 oldconsumer = consumerdao.selectOne(login); // 저장되어있는 회원자료 읽음

			if (oldconsumer != null) {
				if (oldconsumer.getPass().equals(consumer.getPass())) {
					int num = consumerdao.updateConsumer(consumer); // 비밀번호 확인 수정
					if (num > 0) {
						msg = consumer.getName() + "님의 정보 수정이 되었습니다";
						url = "/consumer/consumerInfo";
					} else {
						msg = "정보 수정이 실패 했습니다";
						url = "/consumer/consumerUpdateForm";
					}
				} else {
					msg = "비밀번호가 틀렸습니다 ";
					url = "/consumer/consumerUpdateForm";
				}
			}

		c.addAttribute("msg", msg);
		c.addAttribute("url", url);
		return "/alert";

	} // end method

	@RequestMapping("consumerDelete")
	public String consumerDelete() {
			return "/consumer/consumerDeleteForm";
	}

	@RequestMapping("consumerDeletePro")
	public String consumerDeletePro(String pass) throws Exception {
		String login = (String) session.getAttribute("id");
		String msg = "비밀번호가 틀렸습니다";
		String url = "/consumer/consumerDelete";
		
			Consumer1 consumer = consumerdao.selectOne(login);
			if (consumer.getPass().equals(pass)) { // password 확인
				int num = consumerdao.deleteConsumer(login);
				if (num > 0) {
					msg = consumer.getName() + "님이 탈퇴 처리되었습니다";
					session.invalidate(); // logout
					url = "/consumer/loginForm";
				} else {
					msg = "회원탈퇴가 실패 했습니다";
					url = "/consumer/consumerDelete";
				}
			}

		c.addAttribute("msg", msg);
		c.addAttribute("url", url);
		return "/alert";
	}

	@RequestMapping("consumerPassUpdate")
	public String consumerPassUpdate() {
			return "/consumer/consumerPassUpdate";
	}

	@RequestMapping("consumerPassPro")
	public String consumerPassPro(String pass, String chgpass1) throws Exception {
		HttpSession session = request.getSession();
		String login = (String) session.getAttribute("id");
		String msg = "비밀번호가 틀렸습니다";
		String url = "/consumer/consumerPassUpdate";
			Consumer1 consumer = consumerdao.selectOne(login);
			if (consumer.getPass().equals(pass)) { // password 확인
				int num = consumerdao.changePass(login, chgpass1);
				if (num > 0) {
					msg = consumer.getName() + "님이 비밀번호가 변경 되었습니다";
					url = "/consumer/index";
				} else {
					msg = "비밀번호가 변경 실패 했습니다";
					url = "/consumer/consumerPassUpdate";
				}
		}

		c.addAttribute("msg", msg);
		c.addAttribute("url", url);
		return "/alert";
	}

	@RequestMapping("consumerList")
	public String consumerList() {
		
			List<Consumer1> list = consumerdao.selectAll();
			c.addAttribute("list", list);
			return "/consumer/consumerList";
	}

	 @RequestMapping("pictureimgForm") public String
	  pictureimgForm() {
		 /*1) upload folder 만든다 /webapp/member/picture
			 *2) @RequestMapping("picturePro") 이미지를 폴더에 저장함
			 *3) open window : picturePro
			 */
	  return "/consumer/pictureimgForm"; }
	  
	  @RequestMapping("picturePro") 
	  public String picturePro(@RequestParam("picture") MultipartFile multipartFile) { 
		  String path = request.getServletContext().getRealPath("/")+"view/consumer/picture/";
			System.out.println(path);
			String filename=null;
			
			if (!multipartFile.isEmpty()) {
				File file = new File(path, multipartFile.getOriginalFilename());
				filename = multipartFile.getOriginalFilename();
				try {
					multipartFile.transferTo(file);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println(filename);
			c.addAttribute("filename", filename);
			return "/consumer/picturePro";
		}

}
