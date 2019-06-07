package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Extension;
import fr.datasyscom.pome.ejbentity.Type;
import fr.datasyscom.pome.ejbentity.TypeMime;
import fr.datasyscom.pome.ejbentity.interfaces.IId;

/**
 * @author hamza
 *
 */
@XmlRootElement
public class TypeDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	/** Id du Type en base de données */
	private long id;

	/** Nom du Type */
	private String name;

	private String defaultExtension;

	private String defaultMime;

	/** Indique si le type est le type par défaut */
	private Boolean defaut;

	/** Commande de d'analyse du flux */
	private String analyseFluxCmd;

	private Integer timeoutAnalyseFlux;

	private String extractPageCmd;

	private List<String> extensions;

	private List<String> typeMimes;

	public TypeDto() {
	}

	public TypeDto(Type type) {
		this.id = type.getId();
		this.name = type.getName();
		this.defaut = type.isDefaut();
		this.analyseFluxCmd = type.getAnalayseFluxCmd();
		this.extractPageCmd = type.getExtractPageCmd();
		this.timeoutAnalyseFlux = type.getTimeoutAnalyseFlux();
		this.defaultExtension = type.getDefaultExtension().getValue();
		this.defaultMime = type.getDefaultTypeMime().getValue();
		this.extensions = new ArrayList<>();
		this.typeMimes = new ArrayList<>();
		List<Extension> extensionList = type.getExtensions();
		for (Extension extension : extensionList) {
			this.extensions.add(extension.getValue());
		}
		List<TypeMime> typeMimeList = type.getMimeTypes();
		for (TypeMime typeMime : typeMimeList) {
			this.typeMimes.add(typeMime.getValue());
		}

	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAnalyseFluxCmd() {
		return analyseFluxCmd;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDefaut(Boolean defaut) {
		this.defaut = defaut;
	}

	public void setAnalyseFluxCmd(String analyseFluxCmd) {
		this.analyseFluxCmd = analyseFluxCmd;
	}

	public String getDefaultExtension() {
		return defaultExtension;
	}

	public String getDefaultMime() {
		return defaultMime;
	}

	public Boolean getDefaut() {
		return defaut;
	}

	public void setDefaultExtension(String defaultExtension) {
		this.defaultExtension = defaultExtension;
	}

	public void setDefaultMime(String defaultMime) {
		this.defaultMime = defaultMime;
	}

	public Integer getTimeoutAnalyseFlux() {
		return timeoutAnalyseFlux;
	}

	public void setTimeoutAnalyseFlux(Integer timeoutAnalyseFlux) {
		this.timeoutAnalyseFlux = timeoutAnalyseFlux;
	}

	public String getExtractPageCmd() {
		return extractPageCmd;
	}

	public void setExtractPageCmd(String extractPageCmd) {
		this.extractPageCmd = extractPageCmd;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
	}

	public List<String> getTypeMimes() {
		return typeMimes;
	}

	public void setTypeMimes(List<String> typeMimes) {
		this.typeMimes = typeMimes;
	}

}
