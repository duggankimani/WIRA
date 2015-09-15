package com.duggan.workflow.server.dao.helper;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.duggan.workflow.server.dao.CatalogDaoImpl;
import com.duggan.workflow.server.dao.model.CatalogColumnModel;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.dao.JaxbFormExportProviderImpl;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;

public class CatalogDaoHelper {

	public static Catalog save(Catalog catalog) {

		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel model = get(catalog);
		dao.save(model);
		dao.generateTable(catalog);
		catalog.setId(model.getId());

		return catalog;
	}

	private static CatalogModel get(Catalog catalog) {
		CatalogDaoImpl dao = DB.getCatalogDao();

		CatalogModel model = new CatalogModel();
		if (catalog.getId() != null) {
			model = dao.getById(CatalogModel.class, catalog.getId());
		}

		model.setDescription(catalog.getDescription());
		model.setName(catalog.getName());

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

	private static Catalog get(CatalogModel model) {
		Catalog catalog = new Catalog();
		catalog.setId(model.getId());
		catalog.setDescription(model.getDescription());
		catalog.setName(model.getName());
		catalog.setRecordCount(DB.getCatalogDao().getCount(
				"EXT_" + model.getName()));
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
		catalog.setColumns(models);
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
		dao.delete(dao.getById(CatalogModel.class, catalogId));
	}

	public static void deleteCatalogColumn(Long columnId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		dao.delete(dao.getById(CatalogColumnModel.class, columnId));
	}

	public static List<Catalog> getAllCatalogs() {
		CatalogDaoImpl dao = DB.getCatalogDao();
		List<CatalogModel> models = dao.getCatalogs();

		List<Catalog> catalogs = new ArrayList<>();
		for (CatalogModel m : models) {
			catalogs.add(get(m));
		}

		return catalogs;
	}

	public static void saveData(Long id, List<DocumentLine> lines) {
		Catalog catalog = getCatalog(id);
		CatalogDaoImpl dao = DB.getCatalogDao();
		dao.save("EXT_" + catalog.getName(), catalog.getColumns(), lines);
	}

	public static List<DocumentLine> getTableData(Long catalogId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel catalog = dao.getById(CatalogModel.class, catalogId);

		StringBuffer fieldNames = new StringBuffer();
		int size = catalog.getColumns().size();
		int i = 0;
		for (CatalogColumnModel col : catalog.getColumns()) {
			fieldNames.append(""+col.getName()+"");
			if (i + 1 < size) {
				fieldNames.append(",");
			}
			++i;
		}

		List<CatalogColumnModel> columns = new ArrayList<>(catalog.getColumns());
		List<Object[]> row = dao.getData("EXT_" + catalog.getName(),
				fieldNames.toString());
		List<DocumentLine> lines = new ArrayList<>();
		for (Object[] line : row) {

			i = 0;
			DocumentLine docLine = new DocumentLine();
			for (Object v : line) {
				CatalogColumnModel column = columns.get(i);
				docLine.addValue(column.getName(), getValue(column, v));
				++i;
			}
			lines.add(docLine);
		}

		return lines;
	}

	private static Value getValue(CatalogColumnModel column, Object v) {
		return FormDaoHelper.getValue(null, column.getName(), v, column
				.getType().getFieldType());
	}

	public static String exportTable(Long catalogId) {
		CatalogDaoImpl dao = DB.getCatalogDao();
		CatalogModel model = dao.getById(CatalogModel.class, catalogId);
		assert model != null;

		model.getColumns();

		return exportTable(model);
	}

	public static String exportTable(CatalogModel model) {
		JAXBContext context = new JaxbFormExportProviderImpl()
				.getContext(CatalogModel.class);
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
		JAXBContext context = new JaxbFormExportProviderImpl()
				.getContext(CatalogModel.class);
		CatalogModel model = null;
		try {
			Unmarshaller marshaller = context.createUnmarshaller();
			model = (CatalogModel) marshaller.unmarshal(new StringReader(xml));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

}
