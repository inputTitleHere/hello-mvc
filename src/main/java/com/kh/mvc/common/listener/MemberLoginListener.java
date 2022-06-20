package com.kh.mvc.common.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.kh.mvc.member.model.dto.Member;

/**
 * Application Lifecycle Listener implementation class MemberLoginListener
 *
 */
@WebListener
public class MemberLoginListener implements HttpSessionAttributeListener {

    /**
     * Default constructor. 
     */
    public MemberLoginListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent se)  { 
         String name = se.getName();
         Object value = se.getValue();
         //System.out.println("[attributeAdded]  : "+name+" = "+value);
         
         if("loginMember".equals(name)) {
        	 //로그인한 회원정보 처리
        	 Member loginMember=(Member)value;
        	 //System.out.println("[회원 로그인] " + loginMember.getMemberId()+"로그인!");
         }
    }

    public void attributeRemoved(HttpSessionBindingEvent se)  { 
    	String name = se.getName();
        Object value = se.getValue();
        //System.out.println("[attributeAdded]  : "+name+" = "+value);
        
        if("loginMember".equals(name)) {
       	 //로그인한 회원정보 처리
       	 Member loginMember=(Member)value;
       	 //System.out.println("[회원 로그인] " + loginMember.getMemberId()+"로그인!");
        }
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent se)  { 
         // TODO Auto-generated method stub
    }
	
}
