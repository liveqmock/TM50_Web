/**
 * 이 소스는 Spring 프레임워크 워크북에서 사용한 배포용 소스이다.
 *  
 */

package web.common.util;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * ServletRequest객체에 인코딩을 설정하는 Filter클래스.
 * 
 * @author 박재성(자바지기, javajigi@gmail.com)
 */
public class EncodingFilter implements Filter {

    private String encoding = null;

    protected FilterConfig filterConfig = null;

    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;

    }

    /**
     * ServletRequest객체에 web.xml에서 전달된 인코딩을 설정하는 메써드.
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (request.getCharacterEncoding() == null) {
            if (encoding != null) {
                request.setCharacterEncoding(encoding);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * web.xml에서 전달된 인코딩 값을 초기화하는 메써드.
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig cfg) {
        filterConfig = cfg;
    }
}

