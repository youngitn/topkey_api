package com.topkey.api.controller;

import com.topkey.api.dto.AuthenticationRequest;
import com.topkey.api.dto.AuthenticationResponse;
import com.topkey.api.dto.RegisterRequest;
import com.topkey.api.service.AuthenticationService;
import com.topkey.api.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth management APIs")
public class AuthenticationController {

	private final AuthenticationService service = null;

	/**
	 * 註冊用戶
	 * 
	 * @param request
	 * @return
	 */
	@Operation(summary = "API用戶註冊", description = "API合法用戶註冊", tags = { "Auth" }, hidden = true)
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody RegisterRequest request) {
		return new ResponseEntity(new StandardResponse("200", "Done", service.register(request)), HttpStatus.OK);
	}

	/**
	 * 用戶認證後取得token
	 * 
	 * @param request
	 * @return
	 */
	@Operation(summary = "取得 API access token", description = "使用帳號密碼認證後取得 access token", tags = {
			"Auth" }, hidden = true)
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return new ResponseEntity(new StandardResponse("200", "Done", service.authenticate(request)), HttpStatus.OK);
	}

	@Operation(summary = "取得 API refresh token", description = "使用帳號密碼認證後取得refresh token", tags = {
			"Auth" }, hidden = true)
	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		service.refreshToken(request, response);
	}

}