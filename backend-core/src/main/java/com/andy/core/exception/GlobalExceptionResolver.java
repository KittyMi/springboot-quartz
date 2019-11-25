package com.andy.core.exception;


import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.andy.core.Constant.EMPTY_OBJECT;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 15:42
 * @desc 全局异常处理器
 */
@Slf4j
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        Map<String, Object> attributes = new HashMap<>(3);
        attributes.put("data", EMPTY_OBJECT);
        attributes.put("flag", false);

       if (e instanceof BindException || e instanceof MethodArgumentNotValidException) {
            // Spring Valid参数绑定验证异常
            BindingResult result = (e instanceof BindException)
                    ? ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();
            List<FieldError> lst = result.getFieldErrors();
            if (!lst.isEmpty()) {
                FieldError fe = lst.get(0);
                attributes.put("code", fe.getDefaultMessage());
                attributes.put("data", e.getLocalizedMessage());
            }
        } else {
            if (o instanceof HandlerMethod) {
                HandlerMethod method = (HandlerMethod) o;
                StringJoiner sj = new StringJoiner("|");

                sj.add("接口：[" + httpServletRequest.getRequestURI() + "]出现异常");
                sj.add("方法：" + method.getBean().getClass().getName() + "." + method.getMethod().getName());
                sj.add("异常信息：" + e.getLocalizedMessage());

                attributes.put("data", sj.toString());
            }
        }

        log.error("{}, {}, {}", httpServletResponse.getStatus(), attributes, e.getLocalizedMessage());

        ModelAndView mv = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
    }
}
