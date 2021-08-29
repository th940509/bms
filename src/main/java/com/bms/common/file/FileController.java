package com.bms.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bms.goods.dto.ImageFileDTO;

import net.coobird.thumbnailator.Thumbnails;


@Controller
public class FileController {
	
	private static final String CURR_IMAGE_REPO_PATH = "C:\\file_repo";
	String seperatorPath = "\\";	// window

	//private static final String CURR_IMAGE_REPO_PATH = "/var/lib/tomcat8/file_repo";
	//String seperatorPath = "/";		// linux
	
	
	@RequestMapping("/download")
	public void download(@RequestParam("fileName") String fileName,
		                 	@RequestParam("goodsId") String goodsId,
			                 HttpServletResponse response) throws Exception {
		// 다운로드 할 이미지 파일 이름 전달(param)
		OutputStream out = response.getOutputStream();
		                                         //파일구분자
		String filePath = CURR_IMAGE_REPO_PATH + seperatorPath + goodsId + seperatorPath + fileName;
		
		// 다운로드할 파일 객체 생성
		File image = new File(filePath);
		
		// *브라우저 캐싱 방지 (정보가 남는 것 방지)		
		response.setHeader("Cache-Control","no-cache");
		
		// content-disposition에 attachment, filename을 주는 경우 Body에서 오는 값을 다운받으라는 의미
		response.addHeader("Content-disposition", "attachment; fileName="+fileName);
		// FileInputStream 
		// 자바에서는 파일에서 바이트 단위로 입력할 수 있도록 하기 위해 이 클래스를 제공함. 객체를 생성할 때 데이터를 읽어올 파일을 지정
		FileInputStream in = new FileInputStream(image); 
		
		// 버퍼를 이용해 한번에 8kbyte씩 브라우저로 전송한다.
		byte[] buffer = new byte[1024*8];
		while (true){
			int count = in.read(buffer); 
			if (count==-1)  
				break;
			out.write(buffer,0,count);
		}
		in.close();
		out.close();
		
	}
	
	
	@RequestMapping("/thumbnails.do")
	public void thumbnails(@RequestParam("fileName") String fileName,
                              @RequestParam("goodsId") String goodsId,
			                 HttpServletResponse response) throws Exception {
		
		
		OutputStream out = response.getOutputStream();

		String filePath = CURR_IMAGE_REPO_PATH + seperatorPath + goodsId + seperatorPath + fileName;
		
		File image = new File(filePath); // 파일객체 생성

		if (image.exists()) {                     // toOutputStream (다운로드나 웹브라우저 경로가 아님 스트립으로 이미지 표현)
			Thumbnails.of(image).size(121,154).outputFormat("png").toOutputStream(out);
		}
		
		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();
		
		
	}
	
	
	
	
	public List<ImageFileDTO> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		
		List<ImageFileDTO> fileList= new ArrayList<ImageFileDTO>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		//Iterator란? 자바의 컬렉션에 저장되어 있는 요소들을 읽어오는 방법의 인터페이스. 즉 컬렉션으로부터 정보를 얻어내는 인터페이스
		while (fileNames.hasNext()) { //읽어올 요소가 남아있는지 확인하는 메소드이다. 요소가 있으면 true, 없으면 false
			
			ImageFileDTO imageFileDTO = new ImageFileDTO();				// 빈 파일 dto생성
			
			String fileName = fileNames.next();	  						// 다음데이터 반환
			imageFileDTO.setFileType(fileName);
			
			MultipartFile mFile = multipartRequest.getFile(fileName); 	// 멀티파트의 객체
			
			String originalFileName = mFile.getOriginalFilename();
			imageFileDTO.setFileName(originalFileName);
			
			fileList.add(imageFileDTO);
			
			File file = new File(CURR_IMAGE_REPO_PATH + "/" + fileName);	// 새로 생성한 파일
			if (mFile.getSize() != 0) { 									// File Null Check
				if (!file.exists()) {										// 새로생성한 파일이 존재하지 않으면
					if (file.getParentFile().mkdirs()){ 					// 부모파일의 폴더를 만듬
						System.out.println("========getParent : " + file.getParentFile());
						
						file.createNewFile(); 								// 빈파일을 만든다.
					}
				}
				// 멀티파트의 파일을 저장경로에 옮긴다. c:file_repo\temp\원본파일
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH  + seperatorPath + "temp" + seperatorPath + originalFileName)); 
			}
		}
		return fileList;		// 파일리스트를 반환
		
	}
	
	
	public void deleteFile(String fileName) {
		File file = new File(CURR_IMAGE_REPO_PATH + seperatorPath + fileName);
		try{
			file.delete();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
