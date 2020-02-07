package life.ls.community.advice;
import java.io.IOException;
import	java.io.PrintWriter;

import com.alibaba.fastjson.JSON;
import life.ls.community.dto.ResultDTO;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常处理
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request,
                  Throwable ex, Model model,
                  HttpServletResponse response) {
        /**
         *  处理不同的请求头Content-Type 的application/json
         *  或者Content-Type: text/html;
         */
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO=null;
            //application/json的处理
            if (ex instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
            }

            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(resultDTO));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            //Content-Type: text/html等的处理
            if (ex instanceof CustomizeException) {
                String message = ex.getMessage();
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR);
            }
            return new ModelAndView("error");
        }

    }
}
