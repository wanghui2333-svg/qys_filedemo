package com.kdwh.server.jetty.filter;

import com.alibaba.fastjson.JSON;
import com.kdwh.server.entity.JSONResult;
import com.kdwh.server.utils.AESUtil;
import com.kdwh.server.utils.RSAUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

@WebFilter(filterName = "AuthorityFilter")
public class AuthorityFilter implements Filter {
    private  final String PRIVATE_KEY_STR = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCFAVZYjVY80QJVS4ApFQO4RcsI\n" +
            "DPpibg/UPn78tXNdSkDQHfGZhiET5SEUZi0Mv87YJNce2bVo6InKTXgI3idpZZeirC6XjN5yPWjh\n" +
            "mhJQ3R6sa4MbImV0IHjXmOLtzhzGlq3PysOPSbPEqbt9VDHHoaTjCpCWEEdYHnUIlAqo1jWV4Cm4\n" +
            "tap/UxkHFhd8QS5SwYx++5rgbwyMNqbaqqjtycuT0fz1+qG5azm5brDy/2kSrXDr1jKKdNmR/ol6\n" +
            "fIwWQZv29IP8ZDBhMuNE7oMs6rCZPbdINlDTglcqNuzDLhQPTu4y/jV9zEcYBV03U34hJFNbFgX8\n" +
            "NasY8SRxbf5nAgMBAAECggEAaVPMyCVtJI4z9c3reR0eLACBLyfv5KDaQi8XqxIagMc7mlQMLpLM\n" +
            "gQ/TaW8cv9rpd7t86/5mLeTwsVc19nfO6CU+tuD5qKQyyG8fLoEvfgANuWcOlR9hDlxktf9pvf6a\n" +
            "RR2wGTyuLzujc9siEbbqEPhONkn4hA3yIbNE5P0vj9GcbsoxUg3L+WmqptYN3VWe7NXHSosAhC8w\n" +
            "NeG1vz6GH0ZPgBiWL2LmbW5Cv1g/qdh5UB7F4yi4wqius+gUrCB7eVB2zdF8ikmBcIy000MIsMq+\n" +
            "0iD8LWRTiRB5bJ19D8GnxxOfU/7lwA4pS2wzC7W4sh3IsZv8EdnW7UG2gcU0sQKBgQDwSZQKOTXh\n" +
            "TiOm8RQuESiHWlfx8VDHmGQUFcDwvv5BlbpuzbPf1jG56W2Cpv7qA/OSomomsY+52QjqLiYlcOBw\n" +
            "k+TKGWb6J94WBGaTsIOuyHFHqiV4bDOKuTsWmQGTOtjOPLbSfEWfedNOTrdXjT3cEZ21HqBWGhWv\n" +
            "q40saFKg+QKBgQCNs9m5a5/57kGA+GIk/PZjglN+s7djdQe3zhvM+yaute5ojla1rniquYyMmU4s\n" +
            "//buqO3FveFpXEgh0ZA+LGYpNArKAuG99alE7RZkgRY/5o5gJ4pB6nI8ylMoQPMWBJ9nLHSSRulP\n" +
            "4hr4jTe23t12fwf5GojTzH2hNqUL7njSXwKBgETPzWVgHN340pzsTSskHsckSCsxQEDok0BtOmO3\n" +
            "TnjZuBojLm/bl0H2ZzYTPM0ndIl5mYybLN28GaF2tdXjDWMv90UpISRiZmdHu1hwJEXZgRdU0oY5\n" +
            "9kiz4wgFqT3QjGYDXRfKHuYcrTavtDGOcTikktVbf+8oaS11o2gkOAWBAoGAWdWHVnHQvdY6WNUf\n" +
            "A2wLmBJrdIvi2366IeDc5xrh7ykPt+FlP1EY1sNDQ299jDHKDouyEgbK/MywPiBpXcEwEzcXD/K7\n" +
            "wn4Xjcbwx06/GEvGP1dR43AKdFegWIi0k2Ti2bnpPUMPbsFdb+23QIwRwqnOGDKmRAAENUw7XlEU\n" +
            "7+kCgYEAs4w3Xb+ZmsHr0mD2B6PNsUsXQkutcUdFWnJG3rAOwagxu7cu/PjNiEI26Rk6sWK5JWBo\n" +
            "brHnQt6u6rM4XjNW5ljkvQB42aO1OwJjSDzUTUdP/1TDZIG3gqdINLNupygiL0FCiJ2W/MdiYPVB\n" +
            "I+qOxzsbsPfzHaDFb2/y+2kslbc=";


    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        JSONResult jsonResult = new JSONResult();
        chain.doFilter(req, resp);
        HttpServletRequest request = (HttpServletRequest) req;
        String s = request.getHeader("X-SID");
        String str = request.getHeader("X-Signature");
        try {
            String s1 = str.replace("  ","\n");
            System.out.println(str);
            System.out.println(s1);
            PrivateKey privateKey = RSAUtil.string2PrivateKey(PRIVATE_KEY_STR);
            byte[] bytes = RSAUtil.base642Byte(s1);
            byte[] privateDecrypt = RSAUtil.privateDecrypt(bytes, privateKey);
            if (privateDecrypt.equals(s)){
                req.getRequestDispatcher("/fileUpload.do").forward(req,resp);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
    }

}
