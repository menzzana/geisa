package se.kirc.geisa.data.store;

import java.io.Serializable;
import java.util.List;

import se.kirc.geisa.data.plink.AffectionStatus;
import se.kirc.geisa.data.plink.Sex;

/**
 * 
 * @author danuve
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class IndividualEntry implements Serializable {
	private static final long serialVersionUID = 7732581060310015168L;
	private String id;
	private List<Float> covariate;
	private int interactionVariable;
	private AffectionStatus affectionStatus;
	private Sex sex;

	public IndividualEntry(String id) {
		this.id = id;
		sex=Sex.UNKNOWN;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Float> getCovariate() {
		return covariate;
	}

	public void setCovariate(List<Float> list) {
		this.covariate = list;
	}

	public int getInteractionVariable() {
		return interactionVariable;
	}

	public void setInteractionVariable(int interactionVariable) {
		this.interactionVariable = interactionVariable;
	}

	public AffectionStatus getAffectionStatus() {
		return affectionStatus;
	}

	public void setAffectionStatus(int affectionStatus) {
		this.affectionStatus = AffectionStatus.getByValue(affectionStatus);
	}

	public void setAffectionStatus(AffectionStatus affectionStatus) {
		this.affectionStatus = affectionStatus;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	public Sex getSex() {
		return sex;
	}
}
