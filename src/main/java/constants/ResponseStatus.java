package constants;

public enum ResponseStatus {

    CORRECT("00","Spelling Correct"),
    NOT_CORRECT("01","Spelling Not Correct");

    private ResponseStatus(String code, String description){
        this.code=code;
        this.message=description;
    }

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code+" "+ message;
    }
    private String code;
    private String message;





}
