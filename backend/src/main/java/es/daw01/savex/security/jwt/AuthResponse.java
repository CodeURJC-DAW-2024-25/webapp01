package es.daw01.savex.security.jwt;

import es.daw01.savex.model.UserType;

public class AuthResponse {

	private Status status;
	private String message;
	private String error;
	private UserType role = UserType.ANONYMOUS;
	private boolean isAuthenticated = false;

	public enum Status {
		SUCCESS, FAILURE
	}

	public AuthResponse() {
	}

	public AuthResponse(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public AuthResponse(Status status, String message, String error) {
		this.status = status;
		this.message = message;
		this.error = error;
	}

	public AuthResponse(Status status, String message, String error, UserType role) {
		this.status = status;
		this.message = message;
		this.error = error;
		this.role = role;
	}

	public AuthResponse(Status status, String message, String error, UserType role, boolean isAuthenticated) {
		this.status = status;
		this.message = message;
		this.error = error;
		this.role = role;
		this.isAuthenticated = isAuthenticated;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public UserType getRole() {
		return role;
	}

	public void setRole(UserType role) {
		this.role = role;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	@Override
	public String toString() {
		return "LoginResponse [status=" + status + ", message=" + message + ", error=" + error + "]";
	}

}
