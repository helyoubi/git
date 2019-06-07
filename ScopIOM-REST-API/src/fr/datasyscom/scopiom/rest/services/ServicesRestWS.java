package fr.datasyscom.scopiom.rest.services;

import fr.datasyscom.pome.ejbentity.Lpd;
import fr.datasyscom.pome.ejbentity.Queue;
import fr.datasyscom.pome.ejbentity.Workflow;
import fr.datasyscom.pome.ejbsession.lpd.LpdManagerLocal;
import fr.datasyscom.pome.ejbsession.queue.QueueManagerLocal;
import fr.datasyscom.pome.ejbsession.services.purge.jobs.PurgeLocal;
import fr.datasyscom.pome.ejbsession.services.purge.tmp.PurgeTmpLocal;
import fr.datasyscom.pome.ejbsession.services.scanwait.ScanWaitLocal;
import fr.datasyscom.pome.ejbsession.workflow.WorkflowManagerLocal;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.LpdDto;
import fr.datasyscom.scopiom.rest.pojo.PurgeDto;
import fr.datasyscom.scopiom.rest.pojo.PurgeTmpDto;
import fr.datasyscom.scopiom.rest.pojo.ScanWaitDto;
import fr.datasyscom.scopiom.rest.services.ServicesInfos;
import fr.datasyscom.scopiom.rest.services.ServicesRestWS;
import java.text.ParseException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.quartz.SchedulerException;

@Path("/services")
public class ServicesRestWS {
	@EJB
	private ScanWaitLocal scanWait;
	@EJB
	public PurgeLocal purgeJobs;
	@EJB
	public PurgeTmpLocal purgeTmp;
	@EJB
	private LpdManagerLocal lpd;
	@EJB
	private QueueManagerLocal queueLocal;
	@EJB
	private WorkflowManagerLocal workflowLocal;

	/**
	 * 
	 * Récupération des statuts de services
	 * 
	 * @return ok
	 */
	@GET
	@Produces({ "application/json", "application/xml" })
	public Response infos() {
		return Response.ok(new ServicesInfos(this.scanWait, this.purgeJobs, this.purgeTmp, this.lpd)).build();
	}

	/**
	 * Récupération des informations de purge
	 * 
	 * @return ok
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@GET
	@Path("/purge")
	@Produces({ "application/json", "application/xml" })
	public Response purgeInfos() throws SchedulerException, ParseException {
		PurgeDto purge = new PurgeDto();
		purge.setHour_purge(Integer.valueOf(this.purgeJobs.getHourPurge()));
		purge.setMinute_purge(Integer.valueOf(this.purgeJobs.getMinutePurge()));
		purge.setNbrJobToPurge(Long.valueOf(this.purgeJobs.getNbrJobToPurge()));
		purge.setPurgeOrphelin(Boolean.valueOf(this.purgeJobs.isPurgeOrphelin()));
		purge.setStatus(purgeJobs.isStarted() ? "started" : "stopped");
		return Response.ok(purge).build();
	}

	/**
	 * 
	 * Mise à jour du service purge
	 * 
	 * @param purgeDto
	 * @return noContent
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@PUT
	@Path("/purge")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response updatePurge(PurgeDto purgeDto) throws SchedulerException, ParseException {
		if (purgeDto == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			if (this.purgeJobs.isStarted()) {
				return Response.status(Response.Status.CONFLICT).build();
			}
			if (purgeDto.getHour_purge() != null) {
				this.purgeJobs.setHourPurge(purgeDto.getHour_purge().intValue());
			}
			if (purgeDto.getMinute_purge() != null) {
				this.purgeJobs.setMinutePurge(purgeDto.getMinute_purge().intValue());
			}
			if (purgeDto.isPurgeOrphelin() != null) {
				this.purgeJobs.setPurgeOrphelin(purgeDto.isPurgeOrphelin().booleanValue());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Démarrer le service de purge
	 * 
	 * @return ok
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@POST
	@Path("/purge/start")
	@Produces({ "application/json", "application/xml" })
	public Response startPurge() throws SchedulerException, ParseException {
		this.purgeJobs.start();
		return Response.ok().build();
	}

	/**
	 * Lance une purge manuelle
	 * 
	 * @return ok
	 * @throws SchedulerException
	 */
	@POST
	@Path("/purge/startManual")
	@Produces({ "application/json", "application/xml" })
	public Response lunchPurgeManual() throws SchedulerException {
		this.purgeJobs.doPurgeManu();
		return Response.ok().build();
	}

	/**
	 * 
	 * Arrête le service de purge
	 * 
	 * @return ok
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@POST
	@Path("/purge/stop")
	@Produces({ "application/json", "application/xml" })
	public Response stopPurge() throws SchedulerException, ParseException {
		this.purgeJobs.stop();
		return Response.ok().build();
	}

	/**
	 * Récupération des informations de purge temp
	 * 
	 * @return ok
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@GET
	@Path("/purgetmp")
	@Produces({ "application/json", "application/xml" })
	public Response purgeTmpInfos() throws SchedulerException, ParseException {
		PurgeTmpDto purgeTmpDto = new PurgeTmpDto();
		purgeTmpDto.setPurgeDelay(Integer.valueOf(this.purgeTmp.getPurgeDelay()));
		purgeTmpDto.setPurgeOld(Integer.valueOf(this.purgeTmp.getPurgeOld()));
		purgeTmpDto.setTmpFolder(this.purgeTmp.getTmpFolder());
		purgeTmpDto.setStatus(purgeTmp.isStarted() ? "started" : "stopped");
		return Response.ok(purgeTmpDto).build();
	}

	/**
	 * Mise à jour du service purge temp
	 * 
	 * @param purgeTmpDto
	 * @return noContent
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@PUT
	@Path("/purgetmp")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response updatePurgeTmp(PurgeTmpDto purgeTmpDto) throws SchedulerException, ParseException {
		if (purgeTmpDto == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			if (this.purgeTmp.isStarted()) {
				return Response.status(Response.Status.CONFLICT).build();
			}
			if (purgeTmpDto.getPurgeDelay() != null) {
				this.purgeTmp.setPurgeDelay(purgeTmpDto.getPurgeDelay().intValue());
			}
			if (purgeTmpDto.getPurgeOld() != null) {
				this.purgeTmp.setPurgeOld(purgeTmpDto.getPurgeOld().intValue());
			}
			if (purgeTmpDto.getTmpFolder() != null) {
				this.purgeTmp.setTmpFolder(purgeTmpDto.getTmpFolder());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Démarre le service de purge temp
	 * 
	 * @return ok
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@POST
	@Path("/purgetmp/start")
	@Produces({ "application/json", "application/xml" })
	public Response startPurgeTmp() throws SchedulerException, ParseException {
		this.purgeTmp.start();
		return Response.ok().build();
	}

	/**
	 * Arrête le service de purge temp
	 * 
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@POST
	@Path("/purgetmp/stop")
	@Produces({ "application/json", "application/xml" })
	public Response stopPurgeTmp() throws SchedulerException, ParseException {
		this.purgeTmp.stop();
		return Response.ok().build();
	}

	/**
	 * Récupération des informations du LPD
	 * 
	 * @return ok
	 */
	@GET
	@Path("/lpd")
	@Produces({ "application/json", "application/xml" })
	public Response lpdInfos() {
		Lpd lpdConfig = this.lpd.getConfigurationLpd();
		LpdDto lpdInf = new LpdDto();
		if (lpdConfig.getPort() != null) {
			lpdInf.setPort(lpdConfig.getPort());
		}
		if (lpdConfig.getDefaultQueue() != null) {
			lpdInf.setDefaultQueue(lpdConfig.getDefaultQueue().getName());
		}
		if (lpdConfig.getDefaultWorkflow() != null) {
			lpdInf.setDefaultWorkflow(lpdConfig.getDefaultWorkflow().getName());
		}
		lpdInf.setStatus(lpdConfig.isActivated() ? "started" : "stopped");
		return Response.ok(lpdInf).build();
	}

	/**
	 * 
	 * Mise à jour du service LPD
	 * 
	 * @param lpdDto
	 * @return noContent
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@PUT
	@Path("/lpd")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response updateLpd(LpdDto lpdDto) throws SchedulerException, ParseException {
		if (lpdDto == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			if (this.lpd.isStarted()) {
				return Response.status(Response.Status.CONFLICT).build();
			}

			if (lpdDto.getPort() != null) {
				this.lpd.changePort(lpdDto.getPort().intValue());
			}
			if (lpdDto.getDefaultQueue() != null) {
				if (lpdDto.getDefaultQueue().isEmpty()) {
					this.lpd.changeDefaultDestination(null);
				} else {
					Queue queue = this.queueLocal.retrieveQueue(lpdDto.getDefaultQueue());
					this.lpd.changeDefaultDestination(Long.valueOf(queue.getId()));
				}
			} else if (lpdDto.getDefaultWorkflow() != null) {
				if (lpdDto.getDefaultWorkflow().isEmpty()) {
					this.lpd.changeDefaultDestination(null);
				} else {
					Workflow workflow = this.workflowLocal.retrieveWF(lpdDto.getDefaultWorkflow());
					this.lpd.changeDefaultDestination(Long.valueOf(workflow.getId()));
				}

			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Démarre le service LPD
	 * 
	 * @return ok
	 */
	@POST
	@Path("/lpd/start")
	@Produces({ "application/json", "application/xml" })
	public Response startLpd() {
		try {
			this.lpd.setActive(true);
		} catch (ValidationException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 * Arrête le service LPD
	 * 
	 * @return ok
	 */
	@POST
	@Path("/lpd/stop")
	@Produces({ "application/json", "application/xml" })
	public Response stopLpd() {
		try {
			this.lpd.setActive(false);
		} catch (ValidationException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 * Récupération des informations de Scheduler
	 * 
	 * @return ok
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@GET
	@Path("/scheduler")
	@Produces({ "application/json", "application/xml" })
	public Response schedulerInfos() throws SchedulerException, ParseException {
		ScanWaitDto scanWaitDto = new ScanWaitDto();
		scanWaitDto.setDelayRun(Integer.valueOf(this.scanWait.getDelayRun()));
		scanWaitDto.setDelayScan(Integer.valueOf(this.scanWait.getDelayScan()));
		scanWaitDto.setStatus(scanWait.isStarted() ? "started" : "stopped");
		return Response.ok(scanWaitDto).build();
	}

	/**
	 * Mise à jour du service scheduler
	 * 
	 * @param scanWaitDto
	 * @return noContent
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@PUT
	@Path("/scheduler")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response updateScheduler(ScanWaitDto scanWaitDto) throws SchedulerException, ParseException {
		if (scanWaitDto == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			if (this.scanWait.isStarted()) {
				return Response.status(Response.Status.CONFLICT).build();
			}

			if (scanWaitDto.getDelayRun() != null) {
				this.scanWait.setDelayRun(scanWaitDto.getDelayRun().intValue());
			}
			if (scanWaitDto.getDelayScan() != null) {
				this.scanWait.setDelayScan(scanWaitDto.getDelayScan().intValue());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Démarre le service scheduler
	 * 
	 * @return ok
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@POST
	@Path("/scheduler/start")
	@Produces({ "application/json", "application/xml" })
	public Response startScheduler() throws SchedulerException, ParseException {
		this.scanWait.start();
		return Response.ok().build();
	}

	/**
	 * Arrête le service scheduler
	 * 
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@POST
	@Path("/scheduler/stop")
	@Produces({ "application/json", "application/xml" })
	public Response stopScheduler() throws SchedulerException, ParseException {
		this.scanWait.stop();
		return Response.ok().build();
	}
}
