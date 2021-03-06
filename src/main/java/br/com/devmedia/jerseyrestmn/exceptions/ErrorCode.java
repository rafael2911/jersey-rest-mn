package br.com.devmedia.jerseyrestmn.exceptions;

public enum ErrorCode {
	BAD_REQUEST(400), NOT_FOUND(404), SERVER_ERROR(500), CONFLICT(409);
	
	private Integer code;
	
	private ErrorCode(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}
}
