package vn.noname.vaccineassistant.listener;

public interface FirebaseListener<T> {
    void onDataUpdated(T data);
    void onError(String message, int errorCode);
}
