package com.team1.sgart.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminDTO extends GenericUserDTO{
	@JsonProperty("center")
	private String center;
	@JsonProperty("blocked")
	private boolean blocked;
	
	public AdminDTO() {
		super();
	}
	
	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}
	
	public boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
