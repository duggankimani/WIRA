package com.duggan.workflow.client.ui.grid;

import java.util.ArrayList;

public abstract class DataMapper {

	public abstract <T> T getData(DataModel model);

	public abstract ArrayList<DataModel> getDataModels(ArrayList objs);
}
