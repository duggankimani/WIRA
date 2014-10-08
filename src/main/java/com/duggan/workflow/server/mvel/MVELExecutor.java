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
		String imports = trigger.getImports(); //semicolon separated imports
		
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
				context.addPackageImport(im);
			}
		
		Serializable compilexEx = MVEL.compileExpression(script,context);
		MVEL.executeExpression(compilexEx, doc);
		
	}
}
