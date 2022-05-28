package com.zq.admin.modules.system.rest;

import com.zq.admin.modules.system.service.UserService;
import com.zq.admin.modules.system.service.dto.UserDto;
import com.zq.common.utils.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author wilmiam
 * @since 2022-05-28 16:50
 */
@Api(tags = "访问图片文件")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AccessController {

    private final UserService userService;

    // http://localhost:9888/admin/avatar/avatar-20220528044034262.png
    @ApiOperation("访问头像")
    @GetMapping(value = "/avatar/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] avatar(@PathVariable String username) throws IOException {
        UserDto userDto = userService.findByName(username);
        AssertUtils.isTrue(userDto != null, "用户不存在");
        AssertUtils.hasText(userDto.getAvatarPath(), "未设置头像");

        File file = new File(userDto.getAvatarPath());
        AssertUtils.isTrue(file.exists(), "访问图片不存在");

        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return bytes;
    }

    /*@GetMapping(value = "/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
    public BufferedImage getImage(HttpServletRequest request) throws IOException {
        try (InputStream is = new FileInputStream(request.getRequestURI())) {
            return ImageIO.read(is);
        }
    }*/

    /*@GetMapping(value = "/file/**")
    public ResponseEntity<Resource> download(HttpServletRequest request) {
        File file = new File(request.getRequestURI());
        AssertUtils.isTrue(file.exists(), "访问文件不存在");

        String contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(request.getRequestURI())
                .build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
    }*/

}
