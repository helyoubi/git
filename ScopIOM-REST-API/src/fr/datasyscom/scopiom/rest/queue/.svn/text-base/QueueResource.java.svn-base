package fr.datasyscom.scopiom.rest.queue;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import fr.datasyscom.pome.ejbentity.filter.QueueFilter;
import fr.datasyscom.scopiom.ws.pojo.QueueWS;
import fr.datasyscom.scopiom.ws.queue.QueueUILocal;

@Stateless(mappedName = "scopiom/ejb/stateless/QueueResource")
public class QueueResource implements QueueResourceLocal, QueueResourceRemote {

	@EJB
	private QueueUILocal qmUi;

	public QueuesSummary queues(QueueFilter filter) {
		List<QueueWS> queues = qmUi.retrieveAllQueue(filter);
		return new QueuesSummary(queues);
	}

}