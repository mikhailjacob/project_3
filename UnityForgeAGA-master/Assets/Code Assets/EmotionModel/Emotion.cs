using System;

public class Emotion
{
	public enum EMOTIONS
	{
		JOY,
		DISTRESS,
		HOPE,
		FEAR,
		ANGER,
		PRIDE,
		ADMIRATION,
		SHAME,
		REPROACH,
		LIKING,
		DISLIKING,
		RELIEF,
		DISAPPOINTMENT,
		UNKNOWN
	};
		
	private EMOTIONS appraisedEmotion;
		
	public EMOTIONS AppraisedEmotion {
		get {
			return appraisedEmotion;
		}
			
		set {
			appraisedEmotion = value;
		}
	}
		
	private float appraisedIntensity;
		
	public float AppraisedIntensity {
		get {
			return appraisedIntensity;
		}
			
		set {
			appraisedIntensity = value;
		}
	}
		
	[Obsolete("Use constructor with proper parameters!")]
	public Emotion ()
	{
	}
		
	public Emotion (EMOTIONS emotion, float intensity)
	{
		AppraisedEmotion = emotion;
		AppraisedIntensity = intensity;
	}
		
	public override String ToString ()
	{ 
		String CurrentEmotion = "Emotion: " + AppraisedEmotion.ToString () + ", Intensity: " + AppraisedIntensity;
		return CurrentEmotion;
	}
}
