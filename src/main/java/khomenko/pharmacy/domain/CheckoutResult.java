package khomenko.pharmacy.domain;

import lombok.ToString;

@ToString(of = {"code", "message"})
public class CheckoutResult {

    private String message;
    private Integer code;

    public CheckoutResult(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
