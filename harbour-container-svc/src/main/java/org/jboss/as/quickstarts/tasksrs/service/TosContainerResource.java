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
package org.jboss.as.quickstarts.tasksrs.service;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.as.quickstarts.tasksrs.model.TosContainerStack;
import org.jboss.as.quickstarts.tasksrs.model.TostContainerDao;

/**
 * A JAX-RS resource for exposing REST endpoints for  manipulation
 */
@Path("/")
public class TosContainerResource {

    @Inject
    private TostContainerDao taskDao;

    @GET
    @Path("stack/orderno/{orderno}")
    // JSON: include "application/json" in the @Produces annotation to include json support
    //  @Produces({ "application/xml", "application/json" })
    @Produces({ "application/json" })
    public TosContainerStack getStackByOrderNo(@Context SecurityContext context, @PathParam("orderno") String orderNo) {
    	TosContainerStack stack = taskDao.getForOrdeNo(orderNo);
    	if (Objects.nonNull(stack)) return stack;
    	throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("stacks")
    // JSON: include "application/json" in the @Produces annotation to include json support
    //  @Produces({ "application/xml", "application/json" })
    @Produces({ "application/json" })
    public List<TosContainerStack> getContainerStack(@Context SecurityContext context) {
        return taskDao.getAll();
    }

    @POST
    @Path("stacks")
    // @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({"application/json"})
    public Response createStack(TosContainerStack stack){
        taskDao.createStack(stack);
		return Response.ok().build();
    }
   
    @POST
    @Path("stacks")
    // @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({"application/json"})
    public Response createBulkStack(List<TosContainerStack> stacks){
        for (TosContainerStack stack : stacks) {
            taskDao.createStack(stack);
        }
		return Response.ok().build();
    }
}
