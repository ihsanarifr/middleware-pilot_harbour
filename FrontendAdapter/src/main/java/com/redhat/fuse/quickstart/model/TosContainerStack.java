package com.redhat.fuse.quickstart.model;

import java.io.Serializable;
import java.io.StringReader;

import javax.xml.bind.JAXB;

public class TosContainerStack implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3839049832431848572L;


	private Long rowId;
    

    private Long kdCabang;
    

    private String orderNo;
    
    private String voyageNo;
    
    private String containerNo;

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
