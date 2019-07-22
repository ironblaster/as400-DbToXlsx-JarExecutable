package it.ironblaster.JarAs400DbToXlsx;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class App 
{
    public static void main( String[] args )
    {
    	//http://localhost:8080/as400-DbToXlsx/?&ip=10.5.26.95&user=SPROJECT&pass=SPROJECT&struttura=SPROJECT.ACS9170FST&file=SPROJECT.ACS9170F
    			String ip = args[0];
    			
    			String user = args[1];
    			
    			String pass = args[2];
    			
    			String struttura = args[3];
    				
    			String file = args[4];
    			
    			
    			try {
    				String path = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    			String decodedPath = URLDecoder.decode(path, "UTF-8");

    			Workbook work = App.start(ip,file, struttura,user,pass);
    			File fileparent = new File(decodedPath);
    			
    			
    			
    			
    			
    			File yourFile = new File(fileparent.getParentFile(),file+".xlsx");
    			yourFile.createNewFile();
    			
    			FileOutputStream out = new FileOutputStream(yourFile);
    		    
    		    
    	        work.write(out);
    	        out.flush();
    	        out.close();
    			 
    			}
    			catch (Exception e) {
    				e.printStackTrace();
    			}
    			 
    			 
    }
    
    
    
public static Workbook start(String ip,String fileDati,String fileStruttura,String user,String pass) throws SQLException {
		
		
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);

		Sql.Setparameter(ip,user,pass);
		ResultSet rs = Sql.GetValori(fileDati);
		Map <String,String> struttura = Sql.getStruttura(fileStruttura);
		
		ArrayList<String> colonne = Sql.GetNameOriginalColumn(rs);
		
		

     
	
	
		//System.out.println(new java.util.Date()+" sto iniziando a scrivere la tabella");
		SXSSFSheet sheet = workbook.createSheet(fileDati);
		

		
		
		//crea header su excel
		int i = 0;
		SXSSFRow rowhead = sheet.createRow(0);
		for (String colonna : colonne) {
			 
	            rowhead.createCell(i).setCellValue(struttura.get(colonna));
	            i++;
		}

		
		
		//scrivo i dati 
			int r = 1;
			while (rs.next()) {
				boolean scritto=false;
				SXSSFRow row = sheet.createRow(r);
				int v = 0;
				for (String colonna : colonne) {
					String valore = rs.getString(colonna);
					if(valore !=null) {
					if (valore.length()>32000) {
						valore=valore.substring(0, 32000);
					}
					
					SXSSFCell cell = row.createCell(v);
					
				
					if(Pattern.matches("^[0-9-]*\\.[0-9]+$",valore)) {
						scritto=true;
						
						DataFormat format = workbook.createDataFormat();
						CellStyle style = workbook.createCellStyle();
						style.setDataFormat(format.getFormat("#,##0.00"));

						cell.setCellStyle(style);
						
						
					
						cell.setCellValue(Float.parseFloat(valore));
					
						
						
					}
					
					if(Pattern.matches("^[0-9-]*$",valore)) {
						scritto=true;
						if (valore.length()==8) {
							DataFormat format = workbook.createDataFormat();
							CellStyle style = workbook.createCellStyle();
							style.setDataFormat(format.getFormat("0"));

							cell.setCellStyle(style);
							
							
						
							cell.setCellValue(Integer.parseInt(valore));
						}
						else {
							DataFormat format = workbook.createDataFormat();
							CellStyle style = workbook.createCellStyle();
							style.setDataFormat(format.getFormat("#,##0"));

							cell.setCellStyle(style);
							cell.setCellValue(Integer.parseInt(valore));
							
						}
					}
					
					
					
					if (!scritto) {
						cell.setCellValue(valore);
					}
					
					
				
					}
					v++;
				}
				r++;
			}

		
		
		//System.out.println(new java.util.Date()+" scrittura terminata");	
		


	return workbook;
	
	}
}
