/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.jbpm.executor.api.ExecutorRequestAdminService;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.dao.model.ErrorLog;
import com.duggan.workflow.server.db.DB;

/**
 *
 * @author salaboy
 */
public class ExecutorRequestAdminServiceImpl implements ExecutorRequestAdminService {

    public ExecutorRequestAdminServiceImpl(){
    }
    
    public int clearAllRequests() {
    	EntityManager em  = DB.getEntityManager();
        List<RequestInfo> requests = em.createQuery("select r from RequestInfo r").getResultList();
        for (RequestInfo r : requests) {
            em.remove(r);

        }
        return requests.size();
    }

    public int clearAllErrors() {
    	EntityManager em  = DB.getEntityManager();
        List<ErrorLog> errors = em.createQuery("select e from ErrorLog e").getResultList();

        for (ErrorLog e : errors) {
            em.remove(e);

        }
        return errors.size();
    }

}
