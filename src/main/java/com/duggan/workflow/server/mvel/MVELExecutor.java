package com.duggan.workflow.server.mvel;

import java.io.Serializable;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Doc;

public class MVELExecutor {

	private static Logger log = LoggerFactory.getLogger( DB.class );
	
	public void execute(ADTrigger trigger, Doc doc){
		String script = trigger.getScript();
		String imports ="com.duggan.workflow.shared.model;"
						+ "com.duggan.workflow.server.db;"
						+ "com.duggan.workflow.server.helper.session;"
						+ "java.util";
				 
		if(trigger.getImports()!=null){
			imports = imports.concat(";"+trigger.getImports()); //semicolon separated imports
		}
		String[] importsArray = null;
		
		if(imports!=null && !imports.isEmpty()){
			importsArray = imports.split(";");
		}
		
		log.info("Executing trigger "+trigger.getName()+" context document - "+doc);
				
		if(script==null || script.trim().isEmpty()){
			return;
		}
		
		ParserContext context = new ParserContext();
		if(importsArray!=null)
			for(String im: importsArray){
				if(!im.trim().isEmpty() && !context.hasImport(im)){
					context.addPackageImport(im.trim());
				}
				
			}
		
		Serializable compilexEx = MVEL.compileExpression(script,context);
		MVEL.executeExpression(compilexEx, doc);
		
	}
}
