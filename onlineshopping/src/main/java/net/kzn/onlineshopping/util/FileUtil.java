package net.kzn.onlineshopping.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	private static final String ABS_PATH = "E:/JAVAApp/online-shopping/onlineshopping/src/main/webapp/assets/images/";
		
	public static boolean uploadFile(HttpServletRequest request, MultipartFile file, String code) 
	throws IOException
	{				
		// get the real server path
		String REAL_PATH = request.getSession().getServletContext().getRealPath("/assets/images/");
							
		// create the directories if it does not exist
		if(!new File(REAL_PATH).exists()) {
			new File(REAL_PATH).mkdirs();
		}
		
		if(!new File(ABS_PATH).exists()) {
			new File(ABS_PATH).mkdirs();
		}
		
		
		//transfer the file to both the location
		file.transferTo(new File(REAL_PATH + code + ".jpg"));
		file.transferTo(new File(ABS_PATH + code + ".jpg"));
				
		return true;
	}

		
	
}
