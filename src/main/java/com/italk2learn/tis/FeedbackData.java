package com.italk2learn.tis;

public class FeedbackData {
	
	public static int affectBoosts = 1;
	public static int nextStep=2;
	public static int problemSolving=3;
	public static int reflection=4;
	public static int mathsVocabular= 5;
	public static int talkAloud = 6;
	public static int affirmation = 7;

	public static final String[] talkAloudMessage = {"Please explain what you are doing.",
		"How do you feel about this task?", "What do you think about this task? Is it easy or hard?",
		"Please explain what you're aiming to do.", "Please explain what you're going to do next."};
	
	public static final String[] reflectiveTask = {"Have you read the task? What are you asked to do?",
		"Please read the task again. What are you asked to do?"};
	
	public static final String reflectiveForflow = "Well done, you've have worked hard on this task. Why did you use this method?";
	public static final String reflectiveForConfusion = "This is quite tricky. What is the task asking you to do?";
	public static final String reflectiveForFrustration = "When things don't work out as expected, it can be annoying! What are you trying to do?";
	
	public static final String[] affectBoostsForConfusion = {"You seem like you're trying hard. That's excellent!",
		"Have another go. If you think hard about the problem, you'll soon work out what to do. Well done!",
		"Look at the task again. If you keep working hard, you'll soon make progress. Nice one!"};
	
	public static final String[] affectBoostsForFrustration = {"It's annoying when things don't work out as you expect. Have another go and see if you can work out what to do",
		"It doesn't always makes sense straight away. Try again and see if you can move forward.",
		"Fractions can be frustrating. What can you remember about denominators and numerators?"};
	
	public static final String[] affectBoostsForBoredom = {"Are you finding this too easy? Perhaps you should quickly finish this task, so you can tackle a more challenging task.",
		"Fractions aren't always fun. Can you think where you might use fractions outside of school?",
		"Do you think fractions are a bit boring? Once you've had lots of practice, you'll probably find them easy to do.",
		"Can you think of a different way of answering this task, to make it more challenging for you?"};
	
	public static String mathsReminder = "Please explain that again using the words denominator and numerator.";
	
}
