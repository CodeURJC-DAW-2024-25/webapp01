package es.daw01.savex.security.jwt;

import es.daw01.savex.DTOs.users.PublicUserDTO;

public class AuthResponse {

	private Status status;
	private String message;
	private String error;
	private PublicUserDTO user = null;
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

	public AuthResponse(Status status, String message, String error, PublicUserDTO user) {
		this.status = status;
		this.message = message;
		this.error = error;
		this.user = user;
	}

	public AuthResponse(Status status, String message, String error, PublicUserDTO user, boolean isAuthenticated) {
		this.status = status;
		this.message = message;
		this.error = error;
		this.user = user;
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

	public PublicUserDTO getUser() {
		return user;
	}

	public void setUser(PublicUserDTO user) {
		this.user = user;
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
