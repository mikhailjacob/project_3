using System;
using System.Collections;
using System.Collections.Generic;

using UnityEngine;

public class Appraisal
{
	//Local Appraisal Variables = desirability (D), praiseworthiness (P), appealingness (A) and likelihood (L), Causal Agent (CA)
		
	private float desirability;
		
	public float Desirability {
		get {
			return desirability;
		}
			
		set {
			desirability = value;
		}
	}
		
	private float praiseworthiness;
		
	public float Praiseworthiness {
		get {
			return praiseworthiness;
		}
			
		set {
			praiseworthiness = value;
		}
	}
		
	private float appealingness;
		
	public float Appealingness {
		get {
			return appealingness;
		}
			
		set {
			appealingness = value;
		}
	}
		
	private float likelihood;
		
	public float Likelihood {
		get {
			return likelihood;
		}
			
		set {
			likelihood = value;
		}
	}
		
	private String causalAgent;
		
	public String CausalAgent {
		get {
			return causalAgent;
		}
			
		set {
			causalAgent = value;
		}
	}
		
	public List<Emotion> CurrentEmotions;
		
	[Obsolete("Initialise Appraisal object with the requisite parameters!")]
	public Appraisal ()
	{
	}
		
	public Appraisal (float d, float p, float a, float l, String ca)
	{
		Desirability = d;
		Praiseworthiness = p;
		Appealingness = a;
		Likelihood = l;
		CausalAgent = ca;
			
		CurrentEmotions = new List<Emotion> ();
			
		CalculateEmotion ();
	}
		
	public Appraisal (float d, float p, float a, float l, bool ca) : this(d, p, a, l, (ca) ? "SELF" : "OTHER")
	{
	}
		
	public Appraisal (float d, float p, float a, float l, int ca) : this(d, p, a, l, (ca == 1) ? "SELF" : "OTHER")
	{
	}
		
	public void CalculateEmotion ()
	{
//		if (Desirability > 0.0f && Likelihood < 1.0f && Likelihood > 0.0f) {
//			//HOPE
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.HOPE, Mathf.Abs (Desirability * Likelihood)));
//		}
//		if (Desirability > 0.0f && Likelihood == 1.0f) {
//			//JOY
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.JOY, Mathf.Abs (Desirability * Likelihood)));
//		}
		if (Desirability < 0.0f && Likelihood < 1.0f && Likelihood > 0.0f) {
			//FEAR
			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.FEAR, Mathf.Abs (Desirability * Likelihood)));
		}
//		if (Desirability < 0.0f && Likelihood == 1.0f) {
//			//DISTRESS
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.DISTRESS, Mathf.Abs (Desirability * Likelihood)));
//		}
		if (Desirability < 0.0f && Praiseworthiness < 0.0f && Likelihood > 0.0f && CausalAgent != "SELF") {
			//ANGER
			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.ANGER, Mathf.Abs (Desirability * Likelihood)));
		}
//		if (Praiseworthiness > 0.0f && Likelihood == 1.0f && CausalAgent == "SELF") {
//			//PRIDE
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.PRIDE, Mathf.Abs (Desirability * Likelihood)));
//		}
//		if (Praiseworthiness > 0.0f && Likelihood == 1.0f && CausalAgent != "SELF") {
//			//ADMIRATION
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.ADMIRATION, Mathf.Abs (Desirability * Likelihood)));
//		}
		if (Praiseworthiness < 0.0f && Likelihood == 1.0f && CausalAgent == "SELF") {
			//GUILT/SHAME - NB: GUILT != SHAME But Here Equated
			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.SHAME, Mathf.Abs (Desirability * Likelihood)));
		}
//		if (Praiseworthiness < 0.0f && Likelihood == 1.0f && CausalAgent != "SELF") {
//			//REPROACH
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.REPROACH, Mathf.Abs (Desirability * Likelihood)));
//		}
//		if (Appealingness > 0.0f) {
//			//LIKE
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.LIKING, Mathf.Abs (Desirability * Likelihood)));
//		}
//		if (Appealingness < 0.0f) {
//			//DISLIKE
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.DISLIKING, Mathf.Abs (Desirability * Likelihood)));
//		}
		if (Desirability < 0.0f && Likelihood == 0.0f) {
			//RELIEF
			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.RELIEF, Mathf.Abs (Desirability)));
		}
//		if (Desirability > 0.0f && Likelihood == 0.0f) {
//			//DISAPPOINTMENT
//			CurrentEmotions.Add (new Emotion (Emotion.EMOTIONS.DISAPPOINTMENT, Mathf.Abs (Desirability)));
//		}
	}
}
