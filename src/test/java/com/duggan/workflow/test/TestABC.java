package com.duggan.workflow.test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.duggan.workflow.server.db.DBUtil;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Document;

public class TestABC {

	public static void main(String[] args) throws IOException {
		
		Integer a = (Integer) null;
		
		Document doc = null;
		String email = "";

		
/*		
String userId = SessionHelper.getCurrentUser().getUserId();
System.out.println("##### CurrentUser ID = "+userId);

List documentLines = details.?leaveDetailsGrid;
for(DocumentLine line: documentLines){
	  String leaveCategory = line.values.?leaveCategory.?value;

	  if(leaveCategory!=null){
      List list = DBUtil.getValues(
	"select * from fun_LeaveBalance('"+userId+"', '"+leaveCategory+"')", 
	"kamdb");

      Integer balance = list.get(0)[0]; 
      line._s('balance', balance);
  }
}
*/
	}

}
