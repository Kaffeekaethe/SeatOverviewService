package com.db.systel.bachelorproject2016.seatoverviewservice.domainmodel.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeatLock {

	private Integer seatID;
	private Integer lockEventID;
	private Double price;
	
	public SeatLock(@JsonProperty("seatID") Integer seatID,
			@JsonProperty("lockEventID")Integer lockEventID,
			@JsonProperty("price")Double price) {
		this.setLockEventID(lockEventID);
		this.setPrice(price);
		this.setSeatID(seatID);
	}
	
	public Integer getSeatID() {
		return seatID;
	}
	@JsonProperty("seatID")
	public void setSeatID(Integer seatID) {
		this.seatID = seatID;
	}
	public Integer getLockEventID() {
		return lockEventID;
	}
	@JsonProperty("lockEventID")
	public void setLockEventID(Integer lockEventID) {
		this.lockEventID = lockEventID;
	}
	public Double getPrice() {
		return price;
	}
	@JsonProperty("price")
	public void setPrice(Double price) {
		this.price = price;
	}

}
