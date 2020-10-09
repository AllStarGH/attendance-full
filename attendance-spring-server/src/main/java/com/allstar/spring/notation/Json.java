package com.allstar.spring.notation;

/**
 * Notation:记号,标记法<br>
 * <em>JS对象简谱:Java Script Object Notation</em>
 * 
 * @author admin
 * @param <V>
 *
 */
public class Json<V> {
	private Integer status;
	private String message;
	private V data;

	public Json() {

	}

	public Json(Integer status) {

		this.status = status;
	}

	public Json(String message) {

		this.message = message;
	}

	public Json(V data) {

		this.data = data;
	}

	public Json(String message, V data) {

		this.message = message;
		this.data = data;
	}

	public Json(Integer status, String message) {

		this.status = status;
		this.message = message;
	}

	public Json(Integer status, V data) {
		this.status = status;
		this.data = data;
	}

	public Json(Integer status, String message, V data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public V getData() {
		return data;
	}

	public void setData(V data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JavaScriptObjectNotation [status=" + status + ", message=" + message + ", data=" + data + "]";
	}

}
