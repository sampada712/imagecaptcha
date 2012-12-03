package com.cts.ms.higheredu.imagecaptcha.util;

import java.util.Date;
import java.util.List;

public class CaptchaResponse {
	public List<String> getTargetImages() {
		return targetImages;
	}

	public void setTargetImages(List<String> targetImages) {
		this.targetImages = targetImages;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	private List<String> targetImages;
	private Date timestamp;
}
