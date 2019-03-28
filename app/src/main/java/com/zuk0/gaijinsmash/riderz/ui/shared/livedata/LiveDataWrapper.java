package com.zuk0.gaijinsmash.riderz.ui.shared.livedata;

import static com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper.Status.ERROR;
import static com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper.Status.LOADING;
import static com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper.Status.SUCCESS;

public class LiveDataWrapper<T> {

    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    private Status status;
    private final T data; // this is the livedata value?
    private String msg;

    private LiveDataWrapper(Status status, final T data, String msg) {
        this.data = data;
        this.status = status;
        this.msg = msg;
    }

    public static <T>LiveDataWrapper success(T data) {
        return new LiveDataWrapper(SUCCESS, data, null);
    }

    public static <T>LiveDataWrapper error(T data, String msg) {
        return new LiveDataWrapper(ERROR, data, msg);
    }

    public static <T>LiveDataWrapper loading(T data, String msg) {
        return new LiveDataWrapper(LOADING, data, msg);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}
