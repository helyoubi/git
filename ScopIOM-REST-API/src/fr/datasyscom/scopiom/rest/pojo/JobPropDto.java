package fr.datasyscom.scopiom.rest.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.datasyscom.pome.ejbentity.Job;
import fr.datasyscom.pome.ejbentity.JobProperty;

public class JobPropDto extends JobDto {

	private static final long serialVersionUID = 1L;

	private Map<String, String> properties;

	public static List<JobPropDto> jobPropWsListfromRows(List<Object[]> rows) {
		List<JobPropDto> jobs = new ArrayList<JobPropDto>(rows.size());
		for (Object[] row : rows) {
			jobs.add(new JobPropDto(row));
		}
		
		return jobs;
	}
	
	public JobPropDto() {
		super();
		this.properties = new HashMap<String, String>();
	}
	
	public JobPropDto(Object[] rows) {
		super(rows);
		this.properties = new HashMap<String, String>();
	}

	public JobPropDto(Job job, boolean initJobProperties) {
		super(job);
		if (initJobProperties) {
			List<JobProperty> jobProperties = job.getProperties();
			this.properties = new HashMap<String, String>(jobProperties.size());
			for (JobProperty property : jobProperties) {
				properties.put(property.getName(), property.getValue());
			}
		}
	}

	public JobPropDto(Job job) {
		this(job, true);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}
