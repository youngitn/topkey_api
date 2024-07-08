package com.topkey.api.controller;

import com.topkey.api.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pass")
@Tag(name = "Demox", description = "pass token API")
public class DemoxController {

    /**
     * 要繞過token需先至securityFilterChain和JwtAuthenticationFilter設定
     * @return
     */
    @Operation(
            summary = "不受token約束的API們",
            description = "不受token約束的API們,可以直接try it out~~~~~~~!",hidden = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "拒絕存取", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "找不到資源",content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "伺服器錯誤",content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity(new StandardResponse("200", "Done", "免安全認證的API~~~~~~"), HttpStatus.OK);
    }

}