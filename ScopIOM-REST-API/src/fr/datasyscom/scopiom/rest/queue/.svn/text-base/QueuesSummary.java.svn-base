package fr.datasyscom.scopiom.rest.queue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.scopiom.ws.pojo.QueueWS;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QueuesSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public int count;

	@XmlElementWrapper(name = "queues")
	@XmlElement(name = "queue")
	public List<QueueSummary> queues;


	public QueuesSummary() {
	}

	public QueuesSummary(List<QueueWS> queueWs) {
		this.count = queueWs.size();
		this.queues = new ArrayList<QueueSummary>(queueWs.size());
		for (QueueWS queueWS : queueWs) {
			queues.add(new QueueSummary(queueWS));
		}
	}

}
