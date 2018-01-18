package com.walmart.ticketing.model.output;

public class ResponseMessage{

	Status status;
	String message;
	String link;
	
	public ResponseMessage(Status status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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
}
