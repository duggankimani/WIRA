package com.duggan.workflow.test.ddlutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.itextpdf.text.DocumentException;

public class TestCatalogDao {
	
	Logger logger = Logger.getLogger(TestCatalogDao.class);

	@Before
	public void init() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}

	@Test
	public void getTableData(){
		Long catalogId = 50L;
		List<DocumentLine> data = CatalogDaoHelper.getTableData(catalogId, null);
	
		for(DocumentLine line: data){
			logger.info("Line -> "+line);
		}
	}

	@Ignore
	public void loadViews() {
		DB.getCatalogDao().getViews();
	}

	@Ignore
	public void testGetCatalogsForProcess() {
		String processId = "chasebank.finance.ExpenseClaim";

		List<Catalog> cats = CatalogDaoHelper.getCatalogsForProcess(processId);
		Assert.assertEquals(2, cats.size());
	}

	@Ignore
	public void saveData() {
		// Catalog catalog = CatalogDaoHelper.getCatalog(24L);

		/**
		 * String query = "SELECT (case when(exists(select * " + "from "
		 * +tableName+" " +whereBuffer.toString() + ")='t') then 1 else 0 end)";
		 * 
		 * logger.warn("#Exists query: "+query); int isExists =
		 * getSingleResultOrNull(em .createNativeQuery(query)
		 * .setParameter(primaryKey, val.getValue()));
		 * 
		 */

		List<DocumentLine> lines = new ArrayList<>();
		DocumentLine line = new DocumentLine();
		line.addValue("caseno", new StringValue("case-125"));
		line.addValue("administration", new DoubleValue(0.0));
		line.addValue("itemdescription", new DoubleValue(0.0));
		line.addValue("quantity", new DoubleValue(0.0));
		lines.add(line);
		CatalogDaoHelper.saveData(39L, lines, false);
	}

	@Ignore
	public void createTable() {
		Catalog catalog = new Catalog();
		catalog.setDescription("Test Table");
		catalog.setName("Test");
		catalog.setProcess("xxx");
		catalog.setRecordCount(30);

		ArrayList<CatalogColumn> columns = new ArrayList<>();
		CatalogColumn col = new CatalogColumn();
		col.setAutoIncrement(true);
		col.setLabel("ID");
		col.setName("id");
		col.setNullable(false);
		col.setPrimaryKey(true);
		col.setSize(10);
		col.setType(DBType.INTEGER);
		columns.add(col);

		col = new CatalogColumn();
		col.setLabel("Name");
		col.setName("name");
		col.setSize(100);
		col.setType(DBType.VARCHAR);
		columns.add(col);

		col = new CatalogColumn();
		col.setLabel("Description");
		col.setName("description");
		col.setSize(250);
		col.setType(DBType.VARCHAR);
		columns.add(col);

		catalog.setColumns(columns);
		CatalogDaoHelper.save(catalog);
	}

	@Ignore
	public void export() {
//		String xml = CatalogDaoHelper.exportTable(1L);
//		System.err.println(xml);
//
//		CatalogModel model = CatalogDaoHelper.importTable(xml);
//		System.err.println("Size: " + model.getColumns().size() + "\n" + model.getName() + "\n"
//				+ CatalogDaoHelper.exportTable(model));

	}

	@Ignore
	public void testGenerateCatalogue() throws IOException, SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		String refId = "KXcBJKEAIJKJxPsy";
		
		logger.debug(" +++++++++ FETCHING CATALOGUE ++++ ");
		
		Catalog cat = CatalogDaoHelper.getCatalog(refId);
		
		List<DocumentLine> documentLines = CatalogDaoHelper.getTableData(refId, null);
		
		for (CatalogColumn catCol : cat.getColumns()) {
			System.out.println(" CATALOG NAME "+catCol.getName());
		}
		
		logger.info(" CAtatlogue "+cat.getDisplayName());

		byte[] bytes = CatalogDaoHelper.printCatalogue(refId, "pdf");
//		GenerateCatalogueExcel excel = new GenerateCatalogueExcel(documentLines, "excel", cat);
//		byte[] bytes  = excel.getBytes();

		IOUtils.write(bytes, new FileOutputStream(new File("/home/wladek/Documents/cataloguepdf.pdf")));
		
		logger.debug(" PRINT DONE ++++++++++++ ");
	}

	@After
	public void destroy() {
		DB.commitTransaction();
		DB.closeSession();
	}
}
