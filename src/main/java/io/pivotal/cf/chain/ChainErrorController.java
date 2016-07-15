package io.pivotal.cf.chain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.pivotal.cf.chain.domain.Child;
import io.pivotal.cf.chain.domain.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Based on the helpful answer at http://stackoverflow.com/q/25356781/56285,
 * with error details in response body added by
 *
 * @author Joni Karppinen
 *         <p>
 *         then more stuff added by
 * @author JaredGordon
 */
@RestController
public class ChainErrorController implements ErrorController {

    private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    ErrorJson error(HttpServletRequest request, HttpServletResponse response) {
        return new ErrorJson(response.getStatus(), getErrorAttributes(request), getError(request));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, false);
    }

    private Throwable getError(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getError(requestAttributes);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ErrorJson {

        public Integer status;

        @JsonIgnore
        public Throwable error;

        public String message;
        public String timeStamp;
        public Child child;

        public ErrorJson(int status, Map<String, Object> errorAttributes, Throwable t) {
            this.status = status;
            this.message = (String) errorAttributes.get("message");
            this.timeStamp = errorAttributes.get("timestamp").toString();

            this.error = t;

            if (t instanceof VerificationException) {
                VerificationException ve = (VerificationException) t;
                this.status = ve.getStatus().value();
                this.child = ve.getChild();
                this.message = ve.getMessage();
            }
        }
    }
}