package wlong.work.forumserve.handler;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wlong.work.forumserve.common.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//@Component
public class RedisSessionInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //无论访问的地址是不是正确的，都进行登录验证，登录成功后的访问再进行分发，404的访问自然会进入到错误控制器中
        HttpSession session = request.getSession();
        if (session.getAttribute("loginUserId") != null)
        {
            try
            {
                //验证当前请求的session是否是已登录的session
                String loginSessionId = redisTemplate.opsForValue().get("loginUser:" + (long) session.getAttribute("loginUserId"));
                if (loginSessionId != null && loginSessionId.equals(session.getId()))
                {
                    return true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {

    }

}
