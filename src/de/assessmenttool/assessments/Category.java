package de.assessmenttool.assessments;

import java.util.ArrayList;

public class Category {

	/** The database id. */
	private int id = -1;

	/** The name of this category. */
	private String name;

	/** The note of this category. */
	private String note;

	/** All categories of this category. */
	private ArrayList<Category> categories;

	/** All questions of this category. */
	private ArrayList<Question> questions;

	/**
	 * All questions of this assessment and its categories and its categories...
	 */
	private final ArrayList<Question> allQuestions;

	/** The weight of this category. */
	private float weight;

	/**
	 * Creates a new category with the given name.
	 * 
	 * @param name
	 *            The name of this category.
	 */
	public Category(String name) {
		this.name = name;
		this.questions = new ArrayList<Question>();
		this.allQuestions = new ArrayList<Question>();
		this.categories = new ArrayList<Category>();
	}

	/**
	 * Creates a new category with the given name and note.
	 * 
	 * @param name
	 *            The name.
	 * @param note
	 *            The note.
	 */
	public Category(String name, String note) {
		this(name);
		this.note = note;
	}

	/**
	 * Creates a new category with the given name, note and weight.
	 * 
	 * @param name
	 *            The name.
	 * @param note
	 *            The note.
	 * @param weight
	 *            The weight.
	 */
	public Category(String name, String note, float weight) {
		this(name, note);
		this.weight = weight;
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
	 * Sets the weight of this category.
	 * 
	 * @param weight
	 *            The weight to set.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Get the weight of this category.
	 * 
	 * @return The weight of this category.
	 */
	public float getWeight() {
		return this.weight;
	}

	/**
	 * Add a new category to this category.
	 * 
	 * @param cat
	 *            The category to add.
	 */
	public void addCategory(Category cat) {
		this.categories.add(cat);
	}

	/**
	 * Add a new question to this category.
	 * 
	 * @param quest
	 *            The question to add.
	 */
	public void addQuestion(Question quest) {
		this.questions.add(quest);
	}

	/**
	 * Get a list containing all questions of this category.
	 * 
	 * @return All questions of this category.
	 */
	public ArrayList<Question> getQuestions() {
		return this.questions;
	}

	/**
	 * Get all questions of this category and run recursively through the
	 * category structure of the category categories to get all questions
	 * located under this category.
	 */
	public ArrayList<Question> getAllQuestions(Category cat) {
		this.allQuestions.addAll(getQuestions());

		for (Category temp : cat.getCategories()) {
			this.allQuestions.addAll(temp.getAllQuestions(temp));
		}
		return this.allQuestions;
	}

	/**
	 * Get a list containing all categories of this category.
	 * 
	 * @return All categories of this category.
	 */
	public ArrayList<Category> getCategories() {
		return this.categories;
	}

	/**
	 * Get the name of this category.
	 * 
	 * @return The name of this category.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the name of this category.
	 * 
	 * @param name
	 *            The name of this category.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the note of this category.
	 * 
	 * @return The note of this category.
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * Set the note of this category.
	 * 
	 * @param note
	 *            The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return this.name;
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

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Set the categories for this category.
	 * 
	 * @param cats
	 *            The categories to set.
	 */
	public void setCategories(ArrayList<Category> cats) {
		this.categories = cats;
	}

	public void setQuestions(ArrayList<Question> questsFromCategory) {
		this.questions = questsFromCategory;
	}
}
