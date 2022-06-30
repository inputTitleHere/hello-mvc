package com.kh.mvc.common;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class HelloMvcFileRenamePolicy implements FileRenamePolicy {

	/**
	 * abc.txt -> 20220628_182039829_123.txt 이런식으로 변경
	 */
	
	@Override
	public File rename(File oldFile) {
		File newFile=null;
		do {
			// 파일명 변경
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS_");
			DecimalFormat df = new DecimalFormat("000"); // 30-> 030
			
			String oldName = oldFile.getName();
			String ext = "";
			int dot = oldName.lastIndexOf(".");
			if(dot>-1) {
				ext=oldName.substring(dot); // 확장자를 추출.
			}
			String newName = sdf.format(new Date())+df.format(Math.random()*1000)+ext;
			
			// File객체 새로생성
			newFile=new File(oldFile.getParent(),newName);
			
		}while(!createNewFile(newFile)); // 파일을 생성한다.
		
		return newFile;
	}
	/**
	 * {@link File#createNewFile()}
	 * - 실제 파일이 존재하지 않는 경우, 파일 생성 후 true 리턴.
	 * - 실제 파일이 존재하는 경우, IOException 발생. -> false반환하고 끝냄.
	 * @param f
	 * @return
	 */
	private boolean createNewFile(File f) {
		try {
			return f.createNewFile();
		} catch (IOException ignored) {
			return false;
		}
	}

}
