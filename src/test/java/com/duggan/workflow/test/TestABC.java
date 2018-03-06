package com.duggan.workflow.test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.duggan.workflow.server.db.DBUtil;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Document;

public class TestABC {

	public static void main(String[] args) throws IOException {
		
		String test = "	</table> <!--End of Inner Table-->\n" + 
				"			</td>\n" + 
				"		</tr>\n" + 
				"	</table>\n" + 
				"	<!-- ATTACH:PATH:/x/y/z/a "
				+ " ATTACH:PATH:ksMnix9tBbbEKMgw:/1/2/3/4 "
				+ "-->";
		
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		List<String> paths = mapper.getAttachmentPaths(test);
		System.err.println("Value = "+paths);
	}

}
