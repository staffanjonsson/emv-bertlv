package com.willcurrie;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.willcurrie.hex.HexDumpElement;
import com.willcurrie.tlv.Tag;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class DecodedData {
	private final String rawData;
	private final String decodedData;
	private final List<DecodedData> children;
	private final int startIndex; // in bytes
	private final int endIndex; // in bytes
    private final Tag tag;
    private List<HexDumpElement> hexDump;

    public DecodedData(String rawData, String decodedData, int startIndex, int endIndex) {
		this(null, rawData, decodedData, startIndex, endIndex, Collections.<DecodedData>emptyList());
	}

    public DecodedData(String rawData, String decodedData, int startIndex, int endIndex, List<DecodedData> children) {
        this(null, rawData, decodedData, startIndex, endIndex, children);
    }

    public DecodedData(Tag tag, String rawData, String decodedData, int startIndex, int endIndex, List<DecodedData> children) {
        this.tag = tag;
        this.rawData = rawData;
        this.decodedData = decodedData;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.children = children;
    }
	
	private String trim(String decodedData) {
		return decodedData.length() >= 60 ? decodedData.substring(0, 56) + "..." + StringUtils.right(decodedData, 4) : decodedData;
	}

	public String getRawData() {
		return rawData;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}
	
	public String getDecodedData() {
		return isComposite() ? trim(decodedData) : decodedData;
	}

    public String getFullDecodedData() {
        return decodedData;
    }
    
	public List<DecodedData> getChildren() {
		return children;
	}
	
	public boolean isComposite() {
		return children != null && !children.isEmpty();
	}

    public Tag getTag() {
        return tag;
    }

    public List<HexDumpElement> getHexDump() {
        return hexDump;
    }

    public void setHexDump(List<HexDumpElement> hexDump) {
        this.hexDump = hexDump;
    }

    @Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public String toString() {
		String s = String.format("raw=[%s] decoded=[%s] indexes=[%d,%d]", rawData, decodedData, startIndex, endIndex);
		if (isComposite()) {
			for (DecodedData d : children) {
				s += "\n" + d;
			}
		}
		return s;
	}
	
}
