package com.kh.mvc.board.model.dto;


/**
 * 댓글 COMMENT 
 * 답글 REPLY
 *
 */
public enum CommentLevel {
	
	COMMENT(1),REPLY(2); //Enum 생성자 호출하는 것
	
	private int value; 
	
	// enum의 생성자 -> 접근제한자가 없으며 내부에서만 호출
	CommentLevel(int value){
		this.value=value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static CommentLevel valueOf(int value) {
		switch(value){
		case 1:return COMMENT;
		case 2:return REPLY;
		default: throw new AssertionError("Unknown CommentLevel : "+value);
		}
	}
	
}
