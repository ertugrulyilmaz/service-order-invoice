package com.eriks.service.interceptor.stat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {

  private static final String REQ_PARAM_TIMING = "timing";

  @Override
  public boolean preHandle(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception {
    req.setAttribute(REQ_PARAM_TIMING, System.currentTimeMillis());

    return true;
  }

  @Override
  public void postHandle(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final Object handler,
                         final ModelAndView modelAndView) throws Exception {
    response.setHeader("X-Powered-By", "Bundle Network");

    super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler,
                              @Nullable Exception ex) throws Exception {
    final Long timingAttr = (Long) request.getAttribute(REQ_PARAM_TIMING);
    final long requestTime = System.currentTimeMillis() - timingAttr;
    String handlerLabel = handler.toString();

    if (handler instanceof HandlerMethod) {
      final Method method = ((HandlerMethod) handler).getMethod();
      handlerLabel = method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }

    final String value = String.format("%s:%s:%s:%s", request.getMethod(), handlerLabel, Integer.toString(response.getStatus()), requestTime);
    final String attribute = request.getRequestURL().toString();

    log.info("{} - {}", attribute, value);

    request.setAttribute(attribute, value);

    super.afterCompletion(request, response, handler, ex);
  }
}
