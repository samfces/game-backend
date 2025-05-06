package es.samfc.gamebackend.events.types;

public enum RestRequestType {

    AUTH_REGISTER("POST","/api/v1/auth/register"),

    AUTH_LOGIN("POST","/api/v1/auth/login"),

    AUTH_PASSWORD_CHANGE("POST","/api/v1/auth/password/change"),

    AUTH_EMAIL_CHANGE("POST","/api/v1/auth/email/change"),

    AUTH_REFRESH("POST","/api/v1/auth/refresh"),

    AUTH_LOGOUT("POST","/api/v1/auth/logout"),

    AUTH_LOG("GET","/api/v1/auth/log"),

    ECONOMY_CREATE("POST","/api/v1/economy/create"),

    ECONOMY_EDIT("PATCH","/api/v1/economy/edit"),

    ECONOMY_DELETE("DELETE","/api/v1/economy/delete"),

    BALANCE_GET("GET","/api/v1/balance"),

    BALANCE_DEPOSIT("POST","/api/v1/balance/deposit"),

    BALANCE_WITHDRAW("POST","/api/v1/balance/withdraw"),

    PERMISSIONS_GET("GET","/api/v1/permissions"),

    PERMISSIONS_ADD("POST","/api/v1/permissions/add"),

    PERMISSIONS_DELETE("DELETE","/api/v1/permissions/delete"),

    SELF_ME("GET","/api/v1/self/me");


    private String method;
    private String path;

    RestRequestType(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public static RestRequestType getByMethodAndPath(String method, String path) {
        for (RestRequestType restEventType : RestRequestType.values()) {
            if (restEventType.getMethod().equals(method) && restEventType.getPath().equals(path)) {
                return restEventType;
            }
        }
        return null;
    }
}
