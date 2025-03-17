package es.daw01.savex.DTOs;

import org.springframework.http.ResponseEntity;

import es.daw01.savex.components.ApiError;


/**
 * ApiResponseDTO
 * 
 * Represents a response from the API:
 * 
 * <hr/>
 * <b>Successful responses:</b>
 * <ul>
 *  <li><pre>ApiResponseDTO.ok(Object data)</pre> - 200 OK</li>
 *  <li><pre>ApiResponseDTO.ok(Object data, int code)</pre> - Custom code</li>
 * </ul>
 * 
 * <b>Error responses:</b>
 * <ul>
 *  <li><pre>ApiResponseDTO.error(String error)</pre> - 500 Internal Server Error</li>
 *  <li><pre>ApiResponseDTO.error(String error, int code)</pre> - Custom code</li>
 *  <li><pre>ApiResponseDTO.error(String error, Object data)</pre> - 500 Internal Server Error with data</li>
 *  <li><pre>ApiResponseDTO.error(String error, int code, Object data)</pre> - Custom code with data</li>
 * </ul>
 * 
 * <hr/>
 * 
 * @param <T> The type of the data
 */
public class ApiResponseDTO<T> {
    private int code;
    private boolean ok;
    private T data;
    private ApiError error;

    // ====================[ Default Constructors ]====================

    public ApiResponseDTO() {
        this.code = 0;
        this.ok = true;
        this.data = null;
        this.error = null;
    }

    // ====================[ Data constructors ]====================

    public ApiResponseDTO(T data) {
        this.code = 200;
        this.ok = true;
        this.data = data;
        this.error = null;
    }

    public ApiResponseDTO(T data, int code) {
        this.code = code;
        this.ok = code < 400;
        this.data = data;
        this.error = null;
    }

    // ====================[ Error constructors ]====================
    public ApiResponseDTO(ApiError error) {
        this.code = 500;
        this.ok = false;
        this.data = null;
        this.error = error;
    }

    public ApiResponseDTO(ApiError error, int code) {
        this.code = code;
        this.ok = code < 400;
        this.data = null;
        this.error = error;
    }

    public ApiResponseDTO(ApiError error, T data) {
        this.code = 500;
        this.ok = false;
        this.data = data;
        this.error = error;
    }

    public ApiResponseDTO(ApiError error, int code, T data) {
        this.code = code;
        this.ok = code < 400;
        this.data = data;
        this.error = error;
    }

    // ====================[ Methods ]====================

    public static ResponseEntity<Object> ok(Object data) {
        return ResponseEntity.ok(new ApiResponseDTO<Object>(data));
    }

    public static ResponseEntity<Object> ok(Object data, int code) {
        return ResponseEntity.status(code).body(new ApiResponseDTO<Object>(data, code));
    }

    public static ResponseEntity<Object> error(String error) {
        return ResponseEntity.status(500).body(new ApiResponseDTO<Object>(new ApiError(error)));
    }

    public static ResponseEntity<Object> error(String error, int code) {
        return ResponseEntity.status(code).body(new ApiResponseDTO<Object>(new ApiError(error), code));
    }

    public static ResponseEntity<Object> error(String error, Object data) {
        return ResponseEntity.status(500).body(new ApiResponseDTO<Object>(new ApiError(error), data));
    }

    public static ResponseEntity<Object> error(String error, int code, Object data) {
        return ResponseEntity.status(code).body(new ApiResponseDTO<Object>(new ApiError(error), code, data));
    }

    // ====================[ Getters ]====================

    public int getCode() {
        return code;
    }

    public boolean isOk() {
        return ok;
    }

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    // ====================[ Setters ]====================

    public void setCode(int code) {
        this.code = code;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
