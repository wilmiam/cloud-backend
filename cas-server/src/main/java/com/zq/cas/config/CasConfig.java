package com.zq.cas.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CasConfig {

    @Autowired
    private SpringCasAutoconfig autoconfig;

    private static boolean casEnabled = true;

    public CasConfig() {
    }

    @Bean
    public SpringCasAutoconfig getSpringCasAutoconfig() {
        return new SpringCasAutoconfig();
    }

    /**
     * 添加监听器
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
        listener.setEnabled(casEnabled);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    /**
     * 登出过滤器
     * (这个过滤器不知道有什么作用）
     */
//   没用到security 可以尝试这样写，或者使用自己项目的安全LogoutFilter
//    @Bean
//    public FilterRegistrationBean filterSingleRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new SingleSignOutFilter());
//        // 设定匹配的路径
//        registration.addUrlPatterns("/logout");
//        Map<String,String> initParameters = new HashMap<String, String>();
//        initParameters.put("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
//        registration.setInitParameters(initParameters);
//        // 设定加载的顺序
//        registration.setOrder(2);
//        return registration;
//    }
    @Bean
    public FilterRegistrationBean<LogoutFilter> logOutFilter() {
        FilterRegistrationBean<LogoutFilter> filterRegistration = new FilterRegistrationBean<>();
        LogoutFilter logoutFilter = new LogoutFilter(autoconfig.getCasServerUrlPrefix() + "/logout?service=" + autoconfig.getServerName(), new SecurityContextLogoutHandler());
        filterRegistration.setFilter(logoutFilter);
        filterRegistration.setEnabled(casEnabled);
        if (autoconfig.getSignOutFilters().size() > 0)
            filterRegistration.setUrlPatterns(autoconfig.getSignOutFilters());
        else
            filterRegistration.addUrlPatterns("/logout");
        filterRegistration.addInitParameter("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
        filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
        filterRegistration.setOrder(2);
        return filterRegistration;
    }

    /**
     * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
     */
    @Bean
    public FilterRegistrationBean<SingleSignOutFilter> singleSignOutFilter() {
        FilterRegistrationBean<SingleSignOutFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new SingleSignOutFilter());
        filterRegistration.setEnabled(casEnabled);
        if (autoconfig.getSignOutFilters().size() > 0)
            filterRegistration.setUrlPatterns(autoconfig.getSignOutFilters());
        else
            filterRegistration.addUrlPatterns("/*");
        filterRegistration.addInitParameter("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
        filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
        filterRegistration.setOrder(3);
        return filterRegistration;
    }

    /**
     * 授权过滤器，用于登录
     * 该过滤器负责用户的认证工作
     */
    @Bean
    public FilterRegistrationBean<MyAuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<MyAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new MyAuthenticationFilter());

        filterRegistration.setEnabled(casEnabled);
        if (autoconfig.getAuthFilters().size() > 0)
            filterRegistration.setUrlPatterns(autoconfig.getAuthFilters());
        else
            filterRegistration.addUrlPatterns("/*");

        //casServerLoginUrl:cas服务的登陆url
        filterRegistration.addInitParameter("casServerLoginUrl", autoconfig.getCasServerLoginUrl());
        //本项目登录ip+port
        filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
        filterRegistration.addInitParameter("useSession", autoconfig.isUseSession() ? "true" : "false");
        filterRegistration.addInitParameter("redirectAfterValidation", autoconfig.isRedirectAfterValidation() ? "true" : "false");
        filterRegistration.setOrder(4);
        return filterRegistration;
    }

    /**
     * ticke验证器
     * 该过滤器负责对Ticket的校验工作
     */
    @Bean
    public FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> cas20ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> filterRegistration = new FilterRegistrationBean<>();
        Cas20ProxyReceivingTicketValidationFilter cas20ProxyReceivingTicketValidationFilter = new Cas20ProxyReceivingTicketValidationFilter();
        cas20ProxyReceivingTicketValidationFilter.setServerName(autoconfig.getServerName());
        filterRegistration.setFilter(cas20ProxyReceivingTicketValidationFilter);
        filterRegistration.setEnabled(casEnabled);
        if (autoconfig.getValidateFilters().size() > 0)
            filterRegistration.setUrlPatterns(autoconfig.getValidateFilters());
        else
            filterRegistration.addUrlPatterns("/*");
        filterRegistration.addInitParameter("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
        filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
        filterRegistration.setOrder(5);
        return filterRegistration;
    }


    /**
     * wraper过滤器
     * 该过滤器对HttpServletRequest请求包装， 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
     */
    @Bean
    public FilterRegistrationBean<HttpServletRequestWrapperFilter> httpServletRequestWrapperFilter() {
        FilterRegistrationBean<HttpServletRequestWrapperFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
        filterRegistration.setEnabled(true);
        if (autoconfig.getRequestWrapperFilters().size() > 0)
            filterRegistration.setUrlPatterns(autoconfig.getRequestWrapperFilters());
        else
            filterRegistration.addUrlPatterns("/login");
        filterRegistration.setOrder(6);
        return filterRegistration;
    }

    /**
     * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
     * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
     * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
     */
    @Bean
    public FilterRegistrationBean<AssertionThreadLocalFilter> assertionThreadLocalFilter() {
        FilterRegistrationBean<AssertionThreadLocalFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new AssertionThreadLocalFilter());
        filterRegistration.setEnabled(true);
        if (autoconfig.getAssertionFilters().size() > 0)
            filterRegistration.setUrlPatterns(autoconfig.getAssertionFilters());
        else
            filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(7);
        return filterRegistration;
    }

}
