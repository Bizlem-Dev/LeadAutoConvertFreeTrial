package leadconverter.servlet;

import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

@SlingFilter(label = "Cross-Origin Request Filter",
        metatype = false,
        scope = SlingFilterScope.REQUEST,
        order = -100)
public class CORSFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CORSFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (response instanceof SlingHttpServletResponse) {
            SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
            slingResponse.setHeader("Access-Control-Allow-Origin", "*");
            slingResponse.setHeader("Access-Control-Allow-Credentials", "true");
            slingResponse.setHeader("Access-Control-Allow-Headers", "*");
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Initialising CORS Filter");
    }

    public void destroy() {
        log.debug("Destroying CORS Filter");
    }
}

