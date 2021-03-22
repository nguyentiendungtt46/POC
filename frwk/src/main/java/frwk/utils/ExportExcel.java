package frwk.utils;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

@Service
public class ExportExcel {

	Logger logger = LogManager.getLogger(ExportExcel.class);

	public void export(String templateName, HttpServletResponse response, Map<String, Object> beans) {
		logger.info("BEGIN export " + templateName);
		try {
			String pathFile = File.separator + "templates" + File.separator + templateName + ".xlsx";
			InputStream tempFile = new ClassPathResource(pathFile).getInputStream();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + templateName + ".xlsx");
			XLSTransformer transformer = new XLSTransformer();
			Workbook book = transformer.transformXLS(tempFile, beans);
			ServletOutputStream out = response.getOutputStream();
			book.write(out);
			out.flush();
			out.close();
			tempFile.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		logger.info("END export " + templateName);
	}

}
