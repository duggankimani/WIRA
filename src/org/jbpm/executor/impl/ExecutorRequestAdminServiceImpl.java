/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.impl;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.jbpm.executor.api.ExecutorRequestAdminService;
import org.jbpm.executor.entities.ErrorInfo;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.db.DB;

/**
 *
 * @author salaboy
 */
public class ExecutorRequestAdminServiceImpl implements ExecutorRequestAdminService {

    private EntityManager em;

    public ExecutorRequestAdminServiceImpl(){
    	em = DB.getEntityManager();
    }
    
    public int clearAllRequests() {
        List<RequestInfo> requests = em.createQuery("select r from RequestInfo r").getResultList();
        for (RequestInfo r : requests) {
            em.remove(r);

        }
        return requests.size();
    }

    public int clearAllErrors() {
        List<ErrorInfo> errors = em.createQuery("select e from ErrorInfo e").getResultList();

        for (ErrorInfo e : errors) {
            em.remove(e);

        }
        return errors.size();
    }

}
