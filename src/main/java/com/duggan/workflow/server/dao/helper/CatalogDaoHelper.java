package com.duggan.workflow.server.dao.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.CatalogDaoImpl;
import com.duggan.workflow.server.dao.model.ADProcessCategory;
import com.duggan.workflow.server.dao.model.CatalogColumnModel;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.server.helper.dao.JaxbFormExportProviderImpl;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.servlets.upload.GenerateCatalogueExcel;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.duggan.workflow.shared.model.catalog.CatalogType;
import com.duggan.workflow.shared.model.catalog.FieldSource;
import com.itextpdf.text.DocumentException;
import com.wira.commons.shared.models.HTUser;

public class CatalogDaoHelper {

	static Logger logger = Logger.getLogger(CatalogDaoHelper.class);

	public static Catalog save(Catalog catalog) {

		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel model = null;
		if(catalog.getRefId()!=null){
			model = dao.findByRefId(catalog.getRefId(), CatalogModel.class);
			dao.updateTable(catalog);
		}else if(catalog.getId()!=null){
			model = dao.getById(CatalogModel.class, catalog.getId());
			dao.updateTable(catalog);
		}else{
			dao.generateTable(catalog);
		}
		
		model = get(catalog);
		dao.save(model);
		
		catalog.setId(model.getId());
		catalog.setRefId(model.getRefId());

		return catalog;
	}

	private static CatalogModel get(Catalog catalog) {
		CatalogDaoImpl dao = DB.getCatalogDao();

		CatalogModel model = new CatalogModel();
		if (catalog.getId() != null) {
			model = dao.getById(CatalogModel.class, catalog.getId());
		}

		ProcessCategory category = catalog.getCategory();
		if (category != null) {
			ADProcessCategory cat = dao.getById(ADProcessCategory.class, category.getId());
			model.setCategory(cat);
		}

		model.setDescription(catalog.getDescription());
		model.setName(catalog.getName());
		model.setType(catalog.getType());
		model.setProcessDefId(catalog.getProcessDefId());
		model.setFieldSource(catalog.getFieldSource());
		model.setGridName(catalog.getGridName());

		List<CatalogColumnModel> models = new ArrayList<>();
		for (CatalogColumn cat : catalog.getColumns()) {
			models.add(get(cat));
		}
		model.setColumns(models);

		return model;
	}

	private static CatalogColumnModel get(CatalogColumn cat) {
		CatalogDaoImpl dao = DB.getCatalogDao();

		CatalogColumnModel colModel = new CatalogColumnModel();
		if (cat.getId() != null) {
			colModel = dao.getById(CatalogColumnModel.class, cat.getId());
		}

		colModel.setAutoIncrement(cat.isAutoIncrement());
		colModel.setLabel(cat.getLabel());
		colModel.setName(cat.getName());
		colModel.setNullable(cat.isNullable());
		colModel.setPrimaryKey(cat.isPrimaryKey());
		colModel.setSize(cat.getSize());
		colModel.setType(cat.getType());

		return colModel;
	}

	public static Catalog getCatalog(Long id) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel colModel = dao.getById(CatalogModel.class, id);

		return get(colModel);
	}

	public static Catalog getCatalog(String catalogRefId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel colModel = dao.findByRefId(catalogRefId, CatalogModel.class);

		return get(colModel);
	}

	public static List<Catalog> getCatalogsForProcess(String processId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		List<CatalogModel> catModels = dao.getReportTablesByProcessId(processId);

		List<Catalog> catalogs = new ArrayList<>();
		for (CatalogModel model : catModels) {
			catalogs.add(get(model));
		}
		return catalogs;
	}

	private static Catalog get(CatalogModel model) {
		Catalog catalog = new Catalog();

		catalog.setId(model.getId());
		catalog.setRefId(model.getRefId());
		catalog.setDescription(model.getDescription());
		catalog.setName(model.getName());
		String catalogName = "EXT_" + model.getName();
		if (model.getType() == CatalogType.REPORTVIEW) {
			catalogName = model.getName();
		}
		catalog.setRecordCount(DB.getCatalogDao().getCount(catalogName));

		catalog.setType(model.getType());
		catalog.setProcessDefId(model.getProcessDefId());
		catalog.setFieldSource(model.getFieldSource());
		catalog.setGridName(model.getGridName());
		if (model.getCategory() != null) {
			catalog.setCategory(ProcessDaoHelper.get(model.getCategory()));
		}

		List<CatalogColumn> models = new ArrayList<>();
		for (CatalogColumnModel cat : model.getColumns()) {
			models.add(get(cat));
		}

		Collections.sort(models, new Comparator<CatalogColumn>() {
			@Override
			public int compare(CatalogColumn o1, CatalogColumn o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		catalog.getColumns().addAll(models);
		return catalog;
	}

	private static CatalogColumn get(CatalogColumnModel colModel) {
		CatalogColumn cat = new CatalogColumn();
		cat.setId(colModel.getId());
		cat.setAutoIncrement(colModel.isAutoIncrement());
		cat.setLabel(colModel.getLabel());
		cat.setName(colModel.getName());
		cat.setNullable(colModel.isNullable());
		cat.setPrimaryKey(colModel.isPrimaryKey());
		cat.setSize(colModel.getSize());
		cat.setType(colModel.getType());

		return cat;
	}

	public static void deleteCatalog(Long catalogId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel model = dao.getById(CatalogModel.class, catalogId);
		dao.delete(model);
	}

	public static void deleteCatalogColumn(Long columnId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		dao.delete(dao.getById(CatalogColumnModel.class, columnId));
	}

	public static List<Catalog> getAllCatalogs(String searchTerm) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		List<CatalogModel> models = dao.getCatalogs(searchTerm);

		List<Catalog> catalogs = new ArrayList<>();
		for (CatalogModel m : models) {
			catalogs.add(get(m));
		}

		return catalogs;
	}

	public static void saveData(Long id, List<DocumentLine> lines, boolean isClearExisting) {
		Catalog catalog = getCatalog(id);
		saveData(catalog, lines, isClearExisting);
	}

	private static void saveData(Catalog catalog, List<DocumentLine> lines, boolean isClearExisting) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		dao.save("EXT_" + catalog.getName(), catalog.getColumns(), lines, isClearExisting);
	}

	public static List<DocumentLine> getTableData(String catalogRefId, String searchTerm) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel catalog = dao.findByRefId(catalogRefId, CatalogModel.class);
		return getTableData(catalog, searchTerm);
	}

	public static List<DocumentLine> getTableData(Long catalogId, String searchTerm) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel catalog = dao.getById(CatalogModel.class, catalogId);

		return getTableData(catalog, searchTerm);
	}

	public static List<DocumentLine> getTableData(CatalogModel catalog, String searchTerm) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		String catalogName = "EXT_" + catalog.getName();
		List<String> searchFields = new ArrayList<String>();

		if (catalog.getType() == CatalogType.REPORTVIEW) {
			catalogName = catalog.getName();
		}

		StringBuffer fieldNames = new StringBuffer();
		int size = catalog.getColumns().size();
		int i = 0;
		for (CatalogColumnModel col : catalog.getColumns()) {
			fieldNames.append("" + col.getName() + "");
			if (i + 1 < size) {
				fieldNames.append(",");
			}

			// Search field
			if (col.getType() == DBType.VARCHAR || col.getType() == DBType.LONGVARCHAR) {
				searchFields.add(col.getName());
			}
			++i;
		}

		List<CatalogColumnModel> columns = new ArrayList<>(catalog.getColumns());
		List<Object[]> row = dao.getData(catalogName, fieldNames.toString(), searchTerm, searchFields);

		List<DocumentLine> lines = new ArrayList<>();
		for (Object[] line : row) {

			i = 0;
			DocumentLine docLine = new DocumentLine();
			for (Object v : line) {
				CatalogColumnModel column = columns.get(i);
				Value value = getValue(column, v);
				if (value == null) {
					continue;
				}
				docLine.addValue(column.getName(), value);
				++i;
			}
			lines.add(docLine);
		}

		return lines;
	}

	private static Value getValue(CatalogColumnModel column, Object v) {
		return FormDaoHelper.getValue(null, column.getName(), v, column.getType().getFieldType());
	}

	public static String exportTable(Long catalogId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel model = dao.getById(CatalogModel.class, catalogId);
		assert model != null;

		model.getColumns();

		return exportTable(model);
	}

	public static String exportTable(CatalogModel model) {
		JAXBContext context = new JaxbFormExportProviderImpl().getContext(CatalogModel.class);
		String out = null;
		try {
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter writer = new StringWriter();
			marshaller.marshal(model, writer);

			out = writer.toString();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}

	public static CatalogModel importTable(String xml) {
		JAXBContext context = new JaxbFormExportProviderImpl().getContext(CatalogModel.class);
		CatalogModel model = null;
		try {
			Unmarshaller marshaller = context.createUnmarshaller();
			model = (CatalogModel) marshaller.unmarshal(new StringReader(xml));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	public static void mapAndSaveFormData(Catalog catalog, Doc doc) {
		List<DocumentLine> documentLines = new ArrayList<>();
		// Case Insensitive Map
		Map<String, Value> formValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		formValues.putAll(doc.getValues());

		// Form
		if (catalog.getFieldSource() == FieldSource.FORM) {

			DocumentLine row = new DocumentLine();
			for (CatalogColumn col : catalog.getColumns()) {
				Value value = formValues.get(col.getName());
				if (value != null) {
					row.addValue(col.getName(), value);
				}
			}
			documentLines.add(row);
		} else {
			// Grid
			List<DocumentLine> details = doc.getDetails().get(catalog.getGridName());
			if (details == null) {
				String message = "CatalogDaoHelper.ReportTable Error- Catalog " + catalog.getName() + ": Grid '"
						+ catalog.getGridName() + "' Not Found";
				logger.warn(message);
				throw new RuntimeException(message);
			}

			for (DocumentLine detail : details) {
				DocumentLine row = new DocumentLine();
				// Case Insensitive Map
				Map<String, Value> gridValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
				gridValues.putAll(detail.getValues());

				for (CatalogColumn col : catalog.getColumns()) {
					Value value = gridValues.get(col.getName());
					if (value == null) {
						value = formValues.get(col.getName());
						if (value != null) {
							logger.warn("#Report Table Mapping for " + doc.getCaseNo() + " - #Grid '"
									+ catalog.getGridName() + "' uses form value for '" + col.getName() + "'");
						}
					}

					if (value != null) {
						row.addValue(col.getName(), value);
					}
				}
				documentLines.add(row);
			}
		}

		saveData(catalog, documentLines, false);
	}

	public static List<Catalog> getAllViews() {
		return DB.getCatalogDao().getViews();
	}

	public static byte[] printCatalogue(String refId, String docType) throws IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError, DocumentException {

		Catalog catalog = getCatalog(refId);

		List<DocumentLine> documentLines = getTableData(refId, null);

		logger.warn("dOCUMENT LINES SIZE +++++++++++++++++++++++++++ " + documentLines.size());

		if (docType.equals("xlsx")) {
			return toExcel(catalog, documentLines);
		}

		if (docType.equals("csv")) {
			return toCsv(catalog, documentLines);
		}

		/*
		 * Logic for pdf
		 */

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = CatalogDaoHelper.class.getClassLoader().getResourceAsStream("catalogue.html");
		String html = IOUtils.toString(is);

		HTUser loggedIn = SessionHelper.getCurrentUser();

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		StringBuffer headerbuffer = new StringBuffer(html);
		headerbuffer.append("<div><h3>" + catalog.getDisplayName() + "</h3>     <h5>Generated by : "
				+ loggedIn.getDisplayName() + "</h5>   <h3>Date : " + format.format(new Date())
				+ "</h3></div>");
		headerbuffer.append(
				"<table width='100%' height='300px' cellpadding='0' style='padding: 20px;' bgcolor='#DFDFDF' class='table table-bordered table-condensed'>");

		Document document = new Document();

		document.setDetails("bodyDetails", documentLines);

		StringBuffer bodybuffer = new StringBuffer("<tbody><!--@>bodyDetails--><tr>");
		headerbuffer.append("<theader>");
		for (CatalogColumn catCol : catalog.getColumns()) {

			headerbuffer.append("<th>" + catCol.getLabel() + "</th>");

			String body = "<td>@#" + catCol.getName() + "</td>";
			bodybuffer.append(body);
		}

		bodybuffer.append("</tr><!--@<bodyDetails--></tbody><tfooter><tr><div>FOOTER</div></tr></tfooter>"
				+ "</table><!--End of main content area --></body></html>");

		headerbuffer.append("</theader>");

		String table = headerbuffer.append(bodybuffer.toString()).toString();
		
//		IOUtils.write(table.getBytes(), new FileOutputStream(new File("/home/wladek/Documents/cataloguehtml.html")));

		return convertor.convert(document, table);
	}

	public static byte[] toCsv(Catalog catalog, List<DocumentLine> documentLines) {

		StringBuffer stringBuffer = new StringBuffer();

		int size = catalog.getColumns().size();

		for (CatalogColumn catCol : catalog.getColumns()) {
			size = size - 1;
			stringBuffer.append(catCol.getLabel());

			if (size > 0)
				stringBuffer.append(",");

		}

		stringBuffer.append("\n");
		size = catalog.getColumns().size();
		for (DocumentLine line : documentLines) {

			for (CatalogColumn catCol : catalog.getColumns()) {

				Value value = line.getValue(catCol.getName());

				stringBuffer.append("" + value.getValue());

				stringBuffer.append(",");

				size--;

			}

			stringBuffer.append("\n");
		}

		return stringBuffer.toString().getBytes();
	}

	public static byte[] toExcel(Catalog catalog, List<DocumentLine> documentLines) throws IOException {
		GenerateCatalogueExcel excel = new GenerateCatalogueExcel(documentLines, catalog.getDisplayName(), catalog);
		byte[] bytes = excel.getBytes();
		return bytes;

	}

	public static Catalog save(String fileName, String format, byte[] bites) throws IOException {
		
		Reader in = new InputStreamReader(new ByteArrayInputStream(bites));
		Iterable<CSVRecord> records  = CSVFormat.valueOf(format.toUpperCase()).parse(in);

		String catalogName = format(fileName.substring(0, fileName.lastIndexOf(".")));
		
		catalogName = renameIfExists(catalogName);
		
		Catalog catalog = new Catalog();
		catalog.setName(catalogName);
		catalog.setDescription(catalogName);
		catalog.setType(CatalogType.DATATABLE);
		
		ArrayList<CatalogColumn> columns = new ArrayList<CatalogColumn>();
		ArrayList<DocumentLine> documents  = new ArrayList<DocumentLine>();
		int row=0;
		for(CSVRecord rec: records){
			if(row==0){
				columns = createCatalogColumns(rec);
				catalog.setColumns(columns);
				catalog = save(catalog);
			}else{
				//Data Column
				documents.add(createRow(columns,rec));
			}
			++row;
		}
		
		saveData(catalog, documents, true);
		return catalog;
	}
	
	private static String renameIfExists(String catalogName) {
		return renameIfExists(catalogName, 0);
	}

	private static String renameIfExists(String catalogName, int i) {

		boolean isExists = DB.getCatalogDao().checkExists(catalogName);
		if(isExists){
			return renameIfExists(catalogName+"_"+(i+1),(i+1));
		}
		
		return catalogName;
	}

	private static DocumentLine createRow(ArrayList<CatalogColumn> columns,CSVRecord rec) {
		int recCount = rec.size();
		DocumentLine line = new DocumentLine();
		for(int i=0; i<recCount; i++){
			String value = rec.get(i);
			String col = columns.get(i).getName();
			line._s(col, value);
		}
		return line;
	}

	private static String format(String substring) {
		substring = substring.replaceAll("\\s", "_");
		substring = substring.replaceAll("\\.", "_");
		substring = substring.replaceAll("[^A-Za-z0-9_]", "");
		
		return substring;
	}

	private static ArrayList<CatalogColumn> createCatalogColumns(CSVRecord rec) {
		ArrayList<CatalogColumn> lines = new ArrayList<CatalogColumn>();
		int recCount = rec.size();
		for(int i=0; i<recCount; i++){
			String label = rec.get(i);
			String name = format(label);
					
			CatalogColumn col = new CatalogColumn();
			col.setPrimaryKey(false);
			col.setAutoIncrement(false);
			col.setId(null);
			col.setNullable(false);
			col.setSize(null);
			col.setName(name);
			col.setLabel(label);
			col.setType(DBType.VARCHAR);
			col.setSize(255);
			lines.add(col);
		}
		
		return lines;
	}

	private CatalogColumn createLine(String[] lineValues) {
		CatalogColumn col = new CatalogColumn();
		col.setPrimaryKey(false);
		col.setAutoIncrement(false);
		col.setId(null);
		col.setNullable(false);
		col.setSize(null);
		for (int i = 0; i < lineValues.length; i++) {
			String value = lineValues[i];
			if (value != null && !value.isEmpty()) {
				// remove quotes, and further trim to take care of values like
				// "" 24"
				value = value.replaceAll("\"", "");
				value = value.trim();
			}

			if (i == 0) {
				col.setName(value);
			} else if (i == 1) {
				col.setLabel(value);
			} else if (i == 2) {
				try {
					col.setType(DBType.valueOf(value));
				} catch (Exception e) {
				}
			} else if (i == 3) {
				try {
					// Col Size
					col.setSize(Integer.parseInt(value));
				} catch (Exception e) {
				}

			}

		}

		return col;
	}

}
