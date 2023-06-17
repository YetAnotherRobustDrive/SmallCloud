package org.mint.smallcloud.user.controller;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.ResponseDto;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.dto.*;
import org.mint.smallcloud.user.service.MemberFacadeService;
import org.mint.smallcloud.validation.UserNameValidation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final MemberFacadeService memberFacadeService;
    private final UserDetailsProvider userDetailsProvider;

    @Secured({Roles.S_ADMIN})
    @RequestMapping(
        value = "/{username}/delete",
        method = {RequestMethod.GET, RequestMethod.POST})
    public void delete(
        @UserNameValidation @PathVariable("username") String username) {
        memberFacadeService.delete(username);
    }

    @Secured({Roles.S_ADMIN})
    @PostMapping
    public void register(@Valid @RequestBody RegisterFromAdminDto registerDto) {
        memberFacadeService.register(registerDto);
    }

    @Secured({Roles.S_ADMIN})
    @PostMapping("/update-expired")
    public void setExpired(@Valid @RequestBody UpdateExpiredDto setExpiredDto) {
        memberFacadeService.setExpired(setExpiredDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @PostMapping("/{username}/update")
    public void update(
        @UserNameValidation @PathVariable("username") String username,
        @Valid @RequestBody UserProfileRequestDto userProfileDto) {
        memberFacadeService.update(username, userProfileDto);
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @GetMapping("/{username}")
    public UserProfileResponseDto profile(
        @UserNameValidation @PathVariable("username") String username) {
        return memberFacadeService.profile(username);
    }

    @GetMapping("/search")
    public ResponseDto<List<String>> search(
        @RequestParam("q") String q) {
        userDetailsProvider
            .getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        List<String> usernames = memberFacadeService.search(q);
        return ResponseDto.<List<String>>builder()
            .result(usernames)
            .build();
    }


    @Secured({Roles.S_COMMON})
    @GetMapping("/root-dir")
    public ResponseDto<Long> getRootDir() {
        return ResponseDto.<Long>builder()
            .result(memberFacadeService
                .getRootDir(userDetailsProvider.getUserDetails()
                    .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername()))
            .build();
    }

    @Secured({Roles.S_ADMIN, Roles.S_PRIVILEGE})
    @PostMapping("/profile-photo")
    public ResponseEntity<Void> updatePhoto(PhotoUpdateRequestDto req)
        throws Exception {
        UserDetails user = userDetailsProvider
            .getUserDetails()
            .orElseThrow(() -> new ServiceException
                         (ExceptionStatus.NO_PERMISSION));
        
        memberFacadeService.updatePhoto(user.getUsername(),
                                        req.getImageFile());
        return ResponseEntity.ok().build();
    }

    @Secured({Roles.S_COMMON, Roles.S_COMMON, Roles.S_PRIVILEGE})
    @GetMapping("/profile-photo")
    public ResponseEntity<Resource> downloadPhoto() {
        UserDetails user = userDetailsProvider
            .getUserDetails()
            .orElseThrow(() -> new ServiceException
                         (ExceptionStatus.NO_PERMISSION));
        
        PhotoDownloadResponseDto res =
            memberFacadeService.downloadPhoto(user.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(res.getContentLength());
        headers.setContentType(MediaType.parseMediaType(res.getContentType()));
        headers.setPragma("no-cache");
        headers.setExpires(0);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        return new ResponseEntity<Resource>(res.getPhotoResource(), headers, HttpStatus.OK);
    }
    
    @Secured({Roles.S_ADMIN})
    @GetMapping("/profile-photo/{username}")
    public ResponseEntity<Resource> downloadPhotoForSuperUser(@PathVariable String username) {
        PhotoDownloadResponseDto res =
            memberFacadeService.downloadPhoto(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(res.getContentLength());
        headers.setContentType(MediaType.parseMediaType(res.getContentType()));
        headers.setPragma("no-cache");
        headers.setExpires(0);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        return new ResponseEntity<Resource>(res.getPhotoResource(),
                                            headers,
                                            HttpStatus.OK);
        
    }

    @PostMapping("/password")
    public void updatePassword(@Valid @RequestBody PasswordUpdateRequestDto dto) {
        UserDetails user = userDetailsProvider
            .getUserDetails()
            .orElseThrow(() -> new ServiceException
                         (ExceptionStatus.NO_PERMISSION));
        memberFacadeService.updatePassword(user.getUsername(), dto);
    }

    @Secured(Roles.S_COMMON)
    @PostMapping("/password/expired")
    public PasswordChangedDateDto getPasswordChangedDate(
            @Valid @RequestBody LoginDto loginDto) {
        return memberFacadeService.getPasswordChangedDate(loginDto);
    }
}
