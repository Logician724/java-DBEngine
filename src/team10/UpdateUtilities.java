package team10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

public class UpdateUtilities {

	// Here I will be returning an Arraylist of two things: the Hashtable, and
	// the Primary Key column name
	public static ArrayList<Object> getColumnsAndKey(String strTableName) {
		String line = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("data/metadata.csv"));
			line = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String PKey = null;
		ArrayList<String> indexedColumns = new ArrayList<>();
		Hashtable<String, String> ColNameType = new Hashtable<>();

		while (line != null) {
			String[] content = line.split(",");

			if (content[0].equals(strTableName)) {
				ColNameType.put(content[1], content[2]);
				if ((content[3].toLowerCase()).equals("true"))
					PKey = content[1];
				if ((content[4].toLowerCase()).equals("true"))
					indexedColumns.add(content[1]);
			}
			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Object> Data = new ArrayList<>();
		Data.add(ColNameType);
		Data.add(PKey);
		Data.add(indexedColumns);

		return Data;
	}

	public static boolean checkNotUsed(String tableName, Object newValue, String PKName) {
		int pageNum = 1;
		while (true) {
			try {
				Page curPage = PageManager.deserializePage("data/" + tableName + "/" + "page_" + pageNum + ".ser");
				for (int i = 0; i < curPage.getRows().length; i++) {
					Hashtable<String, Object> curRow = curPage.getRows()[i];
					// if a matching row is found
					if (curRow.get(PKName).equals(newValue) && !((boolean) curRow.get("isDeleted"))) {
						return false;
					}
				}
			} catch (Exception e) {
				return true;
			}
			pageNum++;
		}
	}

	public static Object getTypedPKey(String keyType, String strKey) throws ParseException {
		Object keyValue = null;
		switch (keyType) {
		case "java.lang.Integer":
			keyValue = Integer.parseInt(strKey);
			break;
		case "java.lang.String":
			keyValue = strKey;
			break;
		case "java.lang.Double":
			keyValue = Double.parseDouble(strKey);
			break;
		case "java.util.Date":
			keyValue = new SimpleDateFormat("dd/MM/yyyy").parse(strKey);
			break;
		}
		return keyValue;
	}

	public static void updateDenseIndex(String strKey, Hashtable<String, Object> newValues,
			Hashtable<String, Object> oldValues, int tblPageNum, int tupleRowNum) {
		// TODO Auto-generated method stub
		// Go Through the indexed columns
		// for every column: Load the brin and Go through it with the old value
		// till you find the dense page
		// when the dense is found, use linear search to find the key, check the table and page to avoid duplicate problems
		// replace the value
		// re arrange
	}

}
