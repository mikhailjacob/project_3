using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MoodModel
{
	public float PullRate = 0.2f;
	public float PushRate = 0.2f;
	public float DecayRate = 0.001f;
	private Mood currentMood;
		
	public Mood CurrentMood {
		get {
			return currentMood;
		}
			
		set {
			currentMood = value;
		}
	}
		
	private Mood defaultMood;
		
	public Mood DefaultMood {
		get {
			return defaultMood;
		}
			
		set {
			defaultMood = value;
		}
	}
		
	[Obsolete("Use constructor with appropriate parameters")]
	public MoodModel ()
	{
	}
		
	public MoodModel (Personality agentPersonality)
	{
		//Calculation of default mood based on personality
		float DefaultPleasure = 0.21f * agentPersonality.Extraversion + 0.59f * agentPersonality.Agreeableness + 0.19f * agentPersonality.Neuroticism;
		float DefaultArousal = 0.15f * agentPersonality.Openness + 0.30f * agentPersonality.Agreeableness - 0.57f * agentPersonality.Neuroticism;
		float DefaultDominance = 0.25f * agentPersonality.Openness + 0.17f * agentPersonality.Conscientiousness + 0.60f * agentPersonality.Extraversion - 0.32f * agentPersonality.Agreeableness;
			
		//DefaultMood = new Mood (DefaultPleasure, DefaultArousal, DefaultDominance);
			
		//CurrentMood = new Mood (DefaultPleasure, DefaultArousal, DefaultDominance);
		
		//TODO REMOVE THIS CODE - TESTING PURPOSES
		DefaultMood = new Mood (0.0f, 0.0f, 0.0f);
		CurrentMood = new Mood (0.0f, 0.0f, 0.0f);
	}
		
	public void CalculateCurrentMood (Appraisal CurrentAppraisal)
	{
		if (CurrentAppraisal == null) {
			//DecayMood ();
			
			return;
		}
			
		//Calculate Virtual Emotion Center
		Mood VirtualEmotionCenter = CalculateVirtualEmotionCenter (CurrentAppraisal.CurrentEmotions);
			
		if (Mathf.Abs (VirtualEmotionCenter.Arousal) > Mathf.Abs (this.CurrentMood.Arousal) &&
				Mathf.Abs (VirtualEmotionCenter.Pleasure) > Mathf.Abs (this.CurrentMood.Pleasure) &&
				Mathf.Abs (VirtualEmotionCenter.Dominance) > Mathf.Abs (this.CurrentMood.Dominance)) {
			//Pull Phase
			PullPhase (VirtualEmotionCenter);
			Debug.Log ("PullPhase");
		} else {
			//Push Phase
			PushPhase (VirtualEmotionCenter);
			Debug.Log ("PushPhase");
		}
			
		Debug.Log ("Mood Changed To: " + CurrentMood.ToString ());
		
		//TODO - DO SOMETHING EMOTIONAL
		//...
		//...
		//...
	}
		
	public void PullPhase (Mood VirtualEmotionCenter)
	{
		//TODO TEST
		Vector3 VectorEmotionCenter = new Vector3 (VirtualEmotionCenter.Pleasure, VirtualEmotionCenter.Arousal, VirtualEmotionCenter.Dominance);
		Vector3 VectorCurrentMood = new Vector3 (this.CurrentMood.Pleasure, this.CurrentMood.Arousal, this.CurrentMood.Dominance);
		//Vector3 VectorMoodDisplacement = VectorEmotionCenter;
		Vector3 VectorNewMood = VectorEmotionCenter * this.PullRate + VectorCurrentMood;
		
		//VectorMoodDisplacement *= this.PullRate;
		
		CurrentMood.Pleasure = Sigmoid(VectorNewMood.x);
		CurrentMood.Arousal = Sigmoid(VectorNewMood.y);
		CurrentMood.Dominance = Sigmoid(VectorNewMood.z);
		
		Mood OldMood = new Mood (VectorCurrentMood);
		Mood VirtualEmotionCenterAsMood = new Mood (VectorEmotionCenter);
		Mood NewMood = new Mood (CurrentMood.getMoodVector ());
		Mood NewMoodSigmoidless = new Mood (VectorNewMood);
		
		Debug.Log ("Old Mood: " + OldMood.ToString () + "\nNew Mood Sigmoidless: " + NewMoodSigmoidless.ToString () + "\nNew Mood: " + NewMood.ToString () 
			+ "\nVirtual Emotion Center: " + VirtualEmotionCenterAsMood.ToString ());
	}
		
	public void PushPhase (Mood VirtualEmotionCenter)
	{
		//TODO TEST
		Vector3 VectorEmotionCenter = new Vector3 (VirtualEmotionCenter.Pleasure, VirtualEmotionCenter.Arousal, VirtualEmotionCenter.Dominance);
		Vector3 VectorCurrentMood = new Vector3 (this.CurrentMood.Pleasure, this.CurrentMood.Arousal, this.CurrentMood.Dominance);
		//Vector3 VectorMoodDisplacement = VectorEmotionCenter;
		Vector3 VectorNewMood = VectorEmotionCenter * this.PushRate + VectorCurrentMood;
		
		//VectorMoodDisplacement *= this.PushRate;
		
		CurrentMood.Pleasure = Sigmoid(VectorNewMood.x);
		CurrentMood.Arousal = Sigmoid(VectorNewMood.y);
		CurrentMood.Dominance = Sigmoid(VectorNewMood.z);
		
		Mood OldMood = new Mood (VectorCurrentMood);
		Mood VirtualEmotionCenterAsMood = new Mood (VectorEmotionCenter);
		Mood NewMood = new Mood (CurrentMood.getMoodVector ());
		Mood NewMoodSigmoidless = new Mood (VectorNewMood);
		
		Debug.Log ("Old Mood: " + OldMood.ToString () + "\nNew Mood Sigmoidless: " + NewMoodSigmoidless.ToString () + "\nNew Mood: " + NewMood.ToString () 
			+ "\nVirtual Emotion Center: " + VirtualEmotionCenterAsMood.ToString ());
	}
		
	public Mood CalculateVirtualEmotionCenter (List<Emotion> CurrentEmotions)
	{
		Mood EmotionCenter = new Mood (0.0f, 0.0f, 0.0f);
		int NumberEmotions = 0;
			
		foreach (Emotion e in CurrentEmotions) {
			Mood CurrentMood = CalculateMoodFromEmotion (e);
				
			EmotionCenter.Pleasure += CurrentMood.Pleasure;
			EmotionCenter.Arousal += CurrentMood.Arousal;
			EmotionCenter.Dominance += CurrentMood.Dominance;
			
			NumberEmotions++;
		}
		
		if (NumberEmotions > 0) {
			EmotionCenter.Pleasure /= NumberEmotions;
			EmotionCenter.Arousal /= NumberEmotions;
			EmotionCenter.Dominance /= NumberEmotions;
		}
		
		return EmotionCenter;
	}
		
	public void DecayMood ()
	{
		//TODO TEST
		Vector3 VectorDefaultMood = new Vector3 (this.DefaultMood.Pleasure, this.DefaultMood.Arousal, this.DefaultMood.Dominance);
		Vector3 VectorCurrentMood = new Vector3 (this.CurrentMood.Pleasure, this.CurrentMood.Arousal, this.CurrentMood.Dominance);
		//Vector3 VectorMoodDisplacement = VectorDefaultMood - VectorCurrentMood;
		Vector3 VectorNewMood = VectorDefaultMood * this.DecayRate + VectorCurrentMood;
		
		//VectorMoodDisplacement *= this.DecayRate;
			
		CurrentMood.Pleasure += Sigmoid(VectorNewMood.x);
		CurrentMood.Arousal += Sigmoid(VectorNewMood.y);
		CurrentMood.Dominance += Sigmoid(VectorNewMood.z);
		
		Mood OldMood = new Mood (VectorCurrentMood);
		Mood VirtualEmotionCenterAsMood = new Mood (VectorDefaultMood);
		Mood NewMood = new Mood (CurrentMood.getMoodVector ());
		Mood NewMoodSigmoidless = new Mood (VectorNewMood);
		
		Debug.Log ("Old Mood: " + OldMood.ToString () + "\nNew Mood Sigmoidless: " + NewMoodSigmoidless.ToString () + "\nNew Mood: " + NewMood.ToString () 
			+ "\nVirtual Emotion Center: " + VirtualEmotionCenterAsMood.ToString ());
		
		Debug.Log ("Current Decay Mood: " + CurrentMood.ToString ());
	}
		
	public Mood CalculateMoodFromEmotion (Emotion InputEmotion)
	{
		Mood CalculatedMood = new Mood (0.0f, 0.0f, 0.0f);
			
		//Emotions: JOY, DISTRESS, HOPE, FEAR, ANGER, PRIDE, ADMIRATION, GUILT, REPROACH, LIKING, DISLIKING, UNKNOWN
		if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.JOY) {
			CalculatedMood = new Mood (0.4f, 0.2f, 0.1f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.DISTRESS) {
			CalculatedMood = new Mood (-0.4f, -0.2f, -0.5f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.HOPE) {
			CalculatedMood = new Mood (0.2f, 0.2f, -0.1f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.FEAR) {
			CalculatedMood = new Mood (-0.64f, 0.60f, -0.43f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.ANGER) {
			CalculatedMood = new Mood (-0.51f, 0.59f, 0.25f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.PRIDE) {
			CalculatedMood = new Mood (0.4f, 0.3f, 0.3f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.ADMIRATION) {
			CalculatedMood = new Mood (0.5f, 0.3f, -0.2f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.SHAME) {
			CalculatedMood = new Mood (-0.3f, 0.1f, -0.6f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.REPROACH) {
			CalculatedMood = new Mood (-0.3f, -0.1f, 0.4f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.LIKING) {
			CalculatedMood = new Mood (0.40f, 0.16f, -0.24f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.DISLIKING) {
			CalculatedMood = new Mood (-0.4f, 0.2f, 0.1f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.RELIEF) {
			CalculatedMood = new Mood (0.2f, -0.3f, 0.4f);
		} else if (InputEmotion.AppraisedEmotion == Emotion.EMOTIONS.DISAPPOINTMENT) {
			CalculatedMood = new Mood (-0.3f, 0.1f, -0.4f);
		} else {
			CalculatedMood = new Mood (0.0f, 0.0f, 0.0f);
		}
			
		return CalculatedMood;
	}
	
	public float Sigmoid(float x)
	{
		return (2.0f / (1.0f + Mathf.Exp(-2.0f * x)) - 1.0f);
	}
}
