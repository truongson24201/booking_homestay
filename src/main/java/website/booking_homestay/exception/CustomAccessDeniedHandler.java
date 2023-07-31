package website.booking_homestay.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import website.booking_homestay.utils.MessageResponse;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        BaseResponseDTO responseDTO = new BaseResponseDTO();
//        responseDTO.setMessage("You don't have permission to access this resource!");
//        responseDTO.setCode(HttpStatus.FORBIDDEN);
//
//        String json = HelperUtils.JSON_WRITE.writeValueAsString(responseDTO);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(MessageResponse.NO_PERMISSION);
    }
}
