/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.tasksrs.model;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Provides functionality for manipulation with tasks using the persistence context from {@link Resources}.
 *
 *
 */
@Stateful
public class TosContainerDaoImpl implements TostContainerDao {

    @Inject
    private EntityManager em;

    @Override
    public void deleteTask(TosContainerStack task) {
        if (!em.contains(task)) {
            task = em.merge(task);
        }
        em.remove(task);
    }

    private TypedQuery<TosContainerStack> querySelectAllTasksFromUser(String orderNo) {
        return em.createQuery("SELECT t FROM TosContainerStack t WHERE t.orderNo = ?1", TosContainerStack.class).setParameter(1, orderNo);

    }

	@Override
	public void createTask(TosContainerStack stack) {
		// TODO Auto-generated method stub
        em.persist(stack);
		
	}

	@Override
	public List<TosContainerStack> getAll() {
        return em.createQuery("SELECT t FROM TosContainerStack t", TosContainerStack.class)
            .getResultList();
	}

	@Override
	public List<TosContainerStack> getRange(int offset, int count) {
        TypedQuery<TosContainerStack> query =  em.createQuery("SELECT t FROM TosContainerStack t", TosContainerStack.class);
        query.setMaxResults(count);
        query.setFirstResult(offset);
        return query.getResultList();
	}

	@Override
	public TosContainerStack getForOrdeNo(String orderNo) {
        try{
            TypedQuery<TosContainerStack> query = querySelectAllTasksFromUser(orderNo);
            return query.getResultList().get(0);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
	}
}
