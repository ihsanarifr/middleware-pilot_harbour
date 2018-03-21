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

// JSON: uncomment to include json support (note json is not part of the JAX-RS standard)
// import com.fasterxml.jackson.annotation.JsonIgnore;


import java.io.Serializable;
import java.io.StringReader;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User's task entity which is marked up with JPA annotations and JAXB for serializing XML
 * (and JSON if required)
 *
 */
@SuppressWarnings("serial")
@Entity
@XmlRootElement(name = "containerStack")
@Table(name = "TOS_CONTAINER_LAST_STACK")
public class TosContainerStack implements Serializable {

    @Id
    // @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ROW_ID")
//    @GeneratedValue(generator="InvSeq") 
//    @SequenceGenerator(name="InvSeq",sequenceName="INV_SEQ", allocationSize=5) 
    private Long rowId;
    

    @Column(name = "KD_CABANG")
    private Long kdCabang;
    

    @Column(name = "ORDER_NO")
    private String orderNo;
    
    @Column(name = "VOYAGE_NO")
    private String voyageNo;
    
    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "TRANSACT_DATE")
    private java.sql.Date trxDate;
    
    public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public Long getKdCabang() {
		return kdCabang;
	}

	public void setKdCabang(Long kdCabang) {
		this.kdCabang = kdCabang;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public java.sql.Date getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(java.sql.Date trxDate) {
		this.trxDate = trxDate;
	}

	public TosContainerStack() {
    }

    
    public Long getId() {
        return rowId;
    }

    
    public void setId(Long id) {
        this.rowId = id;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + ((kdCabang == null) ? 0 : kdCabang.hashCode());
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
		result = prime * result + ((rowId == null) ? 0 : rowId.hashCode());
		result = prime * result + ((trxDate == null) ? 0 : trxDate.hashCode());
		result = prime * result + ((voyageNo == null) ? 0 : voyageNo.hashCode());
		return result;
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TosContainerStack other = (TosContainerStack) obj;
		if (containerNo == null) {
			if (other.containerNo != null)
				return false;
		} else if (!containerNo.equals(other.containerNo))
			return false;
		if (kdCabang == null) {
			if (other.kdCabang != null)
				return false;
		} else if (!kdCabang.equals(other.kdCabang))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		if (rowId == null) {
			if (other.rowId != null)
				return false;
		} else if (!rowId.equals(other.rowId))
			return false;
		if (trxDate == null) {
			if (other.trxDate != null)
				return false;
		} else if (!trxDate.equals(other.trxDate))
			return false;
		if (voyageNo == null) {
			if (other.voyageNo != null)
				return false;
		} else if (!voyageNo.equals(other.voyageNo))
			return false;
		return true;
	}

    public static TosContainerStack stringToTask(String content) {
        return JAXB.unmarshal(new StringReader(content), TosContainerStack.class);
    }
}
