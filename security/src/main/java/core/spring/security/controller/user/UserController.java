package core.spring.security.controller.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import core.spring.security.domain.Account;
import core.spring.security.domain.dto.AccountDto;
import core.spring.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/mypage")
	public String myPage() {
		return "user/mypage";
	}

	@GetMapping("/users")
	public String createUser() {
		return "user/login/register";
	}

	@PostMapping("/users")
	public String createUser(AccountDto accountDto) {

		String encode = passwordEncoder.encode(accountDto.getPassword());
		accountDto.setPassword(encode);
		Account account = new Account(accountDto);
		userService.createUser(account);

		return "redirect:/";
	}
}
