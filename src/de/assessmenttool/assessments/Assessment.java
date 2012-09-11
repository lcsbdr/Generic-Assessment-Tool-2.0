package de.assessmenttool.assessments;

import java.util.ArrayList;
import java.util.Date;

public class Assessment {

	/** The database id. */
	private int id = -1;

	/** The name of this assessment. */
	private final String name;

	/** Indicates whether this assessment has 3 dimensions or 2. */
	private boolean is3D;

	/** All top categories of this assessment. */
	private ArrayList<Category> categories;

	/** All top questions of this assessment. */
	private ArrayList<Question> questions;

	/**
	 * All questions of this assessment and its categories and its categories...
	 */
	private final ArrayList<Question> allQuestions;

	/** The user name of the creator. */
	private String creator;

	/** The creation date of this assessment. */
	private Date creation;

	/** The date of the last modification of this assessment. */
	private Date lastModDate;

	/** The last modifier of this assessment. */
	private String lastModUser;

	/**
	 * Creates a new assessment object.
	 * 
	 * @param name
	 *            The name of the assessment.
	 */
	public Assessment(int id, String name) {
		// sets the name and id of this assessment
		this.id = id;
		this.name = name;
		// initiates the standard lists
		this.categories = new ArrayList<Category>();
		this.questions = new ArrayList<Question>();
		this.allQuestions = new ArrayList<Question>();
	}

	/**
	 * Sets whether this assessment has 3 dimensions or not.
	 * 
	 * @param isThreeD
	 *            <code>true</code> if this assessment has 3 dimensions,
	 *            otherwise <code>false</code>.
	 */
	public void set3D(boolean isThreeD) {
		this.is3D = isThreeD;
	}

	/**
	 * Get the name of this assessment.
	 * 
	 * @return The name of this assessment.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Indicates whether this assessment has 3 dimensions or not.
	 * 
	 * @return <code>true</code> if this assessment has 3 dimensions, otherwise
	 *         <code>false</code>.
	 */
	public boolean is3D() {
		return this.is3D;
	}

	/***
	 * Get the name of the creator of this assessment.
	 * 
	 * @return The name of the creator of this assessment.
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * Set the name of the creator of this assessment.
	 * 
	 * @param creator
	 *            The name of the creator.
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * Get the creation date of this assessment.
	 * 
	 * @return The creation date of this assessment.
	 */
	public Date getCreation() {
		return this.creation;
	}

	/**
	 * Get the database id.
	 * 
	 * @return The database id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Set the creation date of this assessment.
	 * 
	 * @param creation
	 *            The creation date of this assessment.
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * Get the last modifier of this assessment.
	 * 
	 * @return The last modifier of this assessment.
	 */
	public String getLastModifier() {
		return this.lastModUser;
	}

	/**
	 * Set the last modifier of this assessment.
	 * 
	 * @param lastModifier
	 *            The name of the last modifier of this assessment.
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModUser = lastModifier;
	}

	/**
	 * Add a new top category to this assessment.
	 * 
	 * @param cat
	 *            The new category to add.
	 */
	public void addCategory(Category cat) {
		this.categories.add(cat);
	}

	/**
	 * Add a new top question to this assessment.
	 * 
	 * @param quest
	 *            The new question to add.
	 */
	public void addQuestion(Question quest) {
		this.questions.add(quest);
	}

	/**
	 * Get all top categories of this assessment.
	 * 
	 * @return A list containing all top categories of this assessment.
	 */
	public ArrayList<Category> getCategories() {
		return this.categories;
	}

	/**
	 * Get all top questions of this assessment.
	 * 
	 * @return A list containing all top questions of this assessment.
	 */
	public ArrayList<Question> getQuestions() {
		return this.questions;
	}

	/**
	 * Get all questions of this assessment and run recursively through the
	 * category structure of this assessment for every category of this
	 * assessment to get all questions located under this assessment.
	 */
	public ArrayList<Question> getAllQuestions() {
		this.allQuestions.addAll(getQuestions());

		for (Category temp : getCategories()) {
			this.allQuestions.addAll(temp.getAllQuestions(temp));
		}
		return this.allQuestions;
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Sets the last modification date of this assessment.
	 * 
	 * @param lastMod
	 *            The last modification of this assessment.
	 */
	public void setLastModificationDate(Date lastMod) {
		this.lastModDate = lastMod;
	}

	/**
	 * Get the last modification date of this assessment.
	 * 
	 * @return The last modification date of this assessment.
	 */
	public Date getLastModificationDate() {
		return this.lastModDate;
	}

	/**
	 * Calculates and returns the completion level of this category in percent.
	 * 
	 * @return The completion level in percent.
	 */
	public float getAnswered() {
		float level = 0.0f;
		float sum = 0.0f, divider = 0.0f;

		ArrayList<Category> cats = getCategories();
		for (Category tmp : cats) {
			// calc category weight
			float cWeight = tmp.getWeight();
			if (cWeight <= 0)
				cWeight = 1.0f;

			divider += cWeight;
			sum += tmp.getAnswered() / 100.0f;
		}

		ArrayList<Question> quests = getQuestions();
		for (Question tmp : quests) {
			// calc questions weight
			float qWeight = tmp.getWeight();
			if (qWeight <= 0)
				qWeight = 1.0f;

			divider += qWeight;
			if (tmp.isAnswered()) {
				int sumParams = tmp.getCriterion().getParams().size() - 1;
				int qAnswer = tmp.getAnswerInt() - 1;
				sum += (((float) (sumParams - qAnswer) / sumParams)) * qWeight;
			}
		}

		if (divider > 0.0f) {
			level = sum / divider;
		}

		level = level * 100.0f;
		if (level > 100.0f)
			level = 100.0f;

		return level;
	}

	public void setCategories(ArrayList<Category> cats) {
		this.categories = cats;
	}

	public void setQuestions(ArrayList<Question> questsFromAssessment) {
		this.questions = questsFromAssessment;
	}
}
