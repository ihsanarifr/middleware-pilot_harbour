package com.redhat.fuse.quickstart;

import java.util.List;

import javax.jws.WebService;

import com.redhat.fuse.quickstart.model.TosContainerStack;

@WebService
public interface FrontEndAdapter {

	public List<TosContainerStack> getContainerStack();
	
	public String createStack(List<TosContainerStack> payload);

}
