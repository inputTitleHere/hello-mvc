package com.kh.mvc.board.model.dto;

import java.sql.Timestamp;

public class BoardExt extends Board {

	private int attachmentCount;
	
	
	public BoardExt(int no, String title, String writer, String content, int readCount, Timestamp regDate, int attachCount) {
		super(no, title, writer, content, readCount, regDate);
		this.attachmentCount=attachCount;
	}

	public BoardExt() {
	}

	public int getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	@Override
	public String toString() {
		return "BoardExt [attachmentCount=" + attachmentCount + "]";
	}
	
	

}
