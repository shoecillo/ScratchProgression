package com.sh.scratch.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ScratchFailsDto implements Serializable{
	
	
	private static final long serialVersionUID = -6874825812123757361L;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	private Date startTime;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	private Date endTime;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	private List<Date> fails;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		
			return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<Date> getFails() {
		if(fails != null) {
			return fails;
		} else {
			return new ArrayList<Date>();
		}
	}

	public void setFails(List<Date> fails) {
		this.fails = fails;
	}
	
	
	
	
}
