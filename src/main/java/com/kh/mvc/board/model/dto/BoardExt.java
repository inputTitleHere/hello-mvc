package com.kh.mvc.board.model.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BoardExt extends Board {

	private int attachmentCount;
	private List<Attachment> attachments = new ArrayList<>();
	private int commentCount;
	
	public BoardExt(int no, String title, String writer, String content, int readCount, Timestamp regDate, int commentCount) {
		super(no, title, writer, content, readCount, regDate);
		this.commentCount=commentCount;
	}
	

	public int getCommentCount() {
		return commentCount;
	}


	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}


	public BoardExt() {
	}



	public int getAttachmentCount() {
		return attachmentCount;
	}
	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	
	public List<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	public void addAttachment(Attachment attach) {
		this.attachments.add(attach);
	}


	@Override
	public String toString() {
		return "BoardExt [attachmentCount=" + attachmentCount + ", attachments=" + attachments + ", toString()="
				+ super.toString() + "]";
	}



}
