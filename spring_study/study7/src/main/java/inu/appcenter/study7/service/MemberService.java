package inu.appcenter.study7.service;


import inu.appcenter.study7.model.MemberCreateRequest;
import inu.appcenter.study7.model.MemberResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {

    MemberResponse create(MultipartFile image, MemberCreateRequest request);
}
