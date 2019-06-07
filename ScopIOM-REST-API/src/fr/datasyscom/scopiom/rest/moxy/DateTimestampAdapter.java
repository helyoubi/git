package fr.datasyscom.scopiom.rest.moxy;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimestampAdapter extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date v) throws Exception {
		return String.valueOf(v.getTime());
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		return new Date(Long.parseLong(v));
	}

}
