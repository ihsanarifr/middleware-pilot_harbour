package com.redhat.fuse.quickstart;

import java.util.HashMap;
import java.util.List;

import javax.jws.WebService;

@WebService
public interface FrontEndAdapter {

	public List<HashMap> getContainerStack();
	
	public String createStack(List<HashMap> payload);

}
