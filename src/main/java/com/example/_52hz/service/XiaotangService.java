package com.example._52hz.service;
import com.example._52hz.util.APIResponse;

import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface XiaotangService {
    APIResponse getAllConfession();
    APIResponse getMyConfession(HttpSession session);
    APIResponse deleteThisConfession(Integer id);
    APIResponse addConfession(HttpSession session, String msg) throws IOException;
}
