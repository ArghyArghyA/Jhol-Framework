package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dataProviders.Configurations;
import dataProviders.Configurations.OutPutFields;

public class DataManager {

	public static List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

	public static List<HashMap<String, String>> read() throws IOException, InvalidFormatException {
		OPCPackage pkg = OPCPackage.open(Configurations.DataSheetPath);
		Workbook workbook = new XSSFWorkbook(pkg);
		Sheet masterSheet = workbook.getSheet("MasterSheet");
		for (Row masterRow : masterSheet) {
			if (masterRow.getCell(1).getStringCellValue().equalsIgnoreCase("Yes")) {
				Sheet sheet = workbook.getSheet(masterRow.getCell(0).getStringCellValue());
				String browser = masterRow.getCell(2).getStringCellValue();
				List<String> header = new ArrayList<String>();
				for (Cell cell : sheet.getRow(1)) {
					header.add(cell.getStringCellValue().trim());
				}

				for (Row row : sheet) {
					HashMap<String, String> data = new HashMap<String, String>();
					if (row.getRowNum() < 2)
						continue; // don't read first 2 rows since they are headers

					for (Cell cell : row) {
						String text = cell.getStringCellValue().trim();
						if (!text.trim().equalsIgnoreCase(""))
							data.put(header.get(cell.getColumnIndex()), text);
					}
					if (row.getCell(0).getStringCellValue().toUpperCase().equalsIgnoreCase("YES")) {
						data.put("index", Integer.toString(row.getRowNum()));// zero indexed row number
						data.put("sheet", sheet.getSheetName());
						if(browser.isEmpty())
							data.put("browser", "Chrome");
						else
							data.put("browser", browser);
						dataList.add(data);
					}
				}
			}
		}
		workbook.close();
		return dataList;
	}

	public static void write(HashMap<String, String> dictionary) {
		Workbook workbook = null;
		String sheet = dictionary.get("sheet");
		int index = Integer.parseInt(dictionary.get("index"));
		try {
			FileInputStream input = new FileInputStream(new File(Configurations.DataSheetPath));
			workbook = new XSSFWorkbook(input);
			Sheet worksheet = workbook.getSheet(sheet);

			// if some last moment data manipulation is required or some additional columns
			// needs to be updated/added it can be done here.

			// update the excel file
			for (OutPutFields o : OutPutFields.values()) {
				worksheet.getRow(index).getCell(o.columnIndex)
						.setCellValue(dictionary.get(o.columnHeader));
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			try {
				FileOutputStream output = new FileOutputStream(new File(Configurations.DataSheetPath));
				workbook.write(output);
				workbook.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

}
