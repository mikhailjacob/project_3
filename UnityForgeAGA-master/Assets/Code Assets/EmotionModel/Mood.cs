using System;
using UnityEngine;

public class Mood
{
	public enum MOODS
	{
		EXUBERANT,
		BORED,
		DEPENDENT,
		DISDAINFUL,
		RELAXED,
		ANXIOUS,
		DOCILE,
		HOSTILE,
		NEUTRAL
	};
		
	private float pleasure;
		
	public float Pleasure {
		get {
			return pleasure;
		}
			
		set {
			pleasure = value;
			CalculateCurrentMood ();
		}
	}
		
	private float arousal;
		
	public float Arousal {
		get {
			return arousal;
		}
			
		set {
			arousal = value;
			CalculateCurrentMood ();
		}
	}
		
	private float dominance;
		
	public float Dominance {
		get {
			return dominance;
		}
			
		set {
			dominance = value;
			CalculateCurrentMood ();
		}
	}
		
	private MOODS currentMood;
		
	public MOODS CurrentMood {
		get {
			return currentMood;
		}
			
		set {
			currentMood = value;
		}
	}
		
	[Obsolete("Use constructor with proper parameters!")]
	public Mood ()
	{
	}
		
	public Mood (float Pleasure, float Arousal, float Dominance)
	{
		this.Pleasure = Pleasure;
		this.Arousal = Arousal;
		this.Dominance = Dominance;
			
		CalculateCurrentMood ();
	}
	
	public Mood (Vector3 PAD)
	{
		this.Pleasure = PAD.x;
		this.Arousal = PAD.y;
		this.Dominance = PAD.z;
			
		CalculateCurrentMood ();
	}
		
	public void CalculateCurrentMood ()
	{
		//+P+A+D  Exuberant -P-A-D  Bored
		//+P+A-D  Dependent -P-A+D  Disdainful
		//+P-A+D  Relaxed -P+A-D  Anxious
		//+P-A-D  Docile -P+A+D  Hostile
			
		if (Pleasure > 0.0f && Arousal > 0.0f && Dominance > 0.0f) {
			//Exuberant
			CurrentMood = MOODS.EXUBERANT;
		} else if (Pleasure > 0.0f && Arousal > 0.0f && Dominance < 0.0f) {
			//Dependent
			CurrentMood = MOODS.DEPENDENT;
		} else if (Pleasure > 0.0f && Arousal < 0.0f && Dominance > 0.0f) {
			//Relaxed
			CurrentMood = MOODS.RELAXED;
		} else if (Pleasure > 0.0f && Arousal < 0.0f && Dominance < 0.0f) {
			//Docile
			CurrentMood = MOODS.DOCILE;
		} else if (Pleasure < 0.0f && Arousal < 0.0f && Dominance < 0.0f) {
			//Bored
			CurrentMood = MOODS.BORED;
		} else if (Pleasure < 0.0f && Arousal < 0.0f && Dominance > 0.0f) {
			//Disdainful
			CurrentMood = MOODS.DISDAINFUL;
		} else if (Pleasure < 0.0f && Arousal > 0.0f && Dominance < 0.0f) {
			//Anxious
			CurrentMood = MOODS.ANXIOUS;
		} else if (Pleasure < 0.0f && Arousal > 0.0f && Dominance > 0.0f) {
			//Hostile
			CurrentMood = MOODS.HOSTILE;
		} else {
			//Neutral
			CurrentMood = MOODS.NEUTRAL;
		}
	}
	
	public Vector3 getMoodVector ()
	{
		return (new Vector3 (Pleasure, Arousal, Dominance));
	}
		
	public override String ToString ()
	{
		String CurrentMood = "Pleasure: " + Pleasure + ", Arousal: " + Arousal + ", Dominance: " + Dominance + ", Mood: " + this.CurrentMood.ToString ();
			
		return CurrentMood;
	}
}

