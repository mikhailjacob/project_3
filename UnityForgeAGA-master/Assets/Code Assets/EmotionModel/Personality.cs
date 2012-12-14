using System;

public class Personality
{
	private float openness;
		
	public float Openness {
		get {
			return openness;
		}
			
		set {
			openness = value;
		}
	}
		
	private float conscientiousness;
		
	public float Conscientiousness {
		get {
			return conscientiousness;
		}
			
		set {
			conscientiousness = value;
		}
	}
		
	private float extraversion;
		
	public float Extraversion {
		get {
			return extraversion;
		}
			
		set {
			extraversion = value;
		}
	}
		
	private float agreeableness;
		
	public float Agreeableness {
		get {
			return agreeableness;
		}
			
		set {
			agreeableness = value;
		}
	}
		
	private float neuroticism;
		
	public float Neuroticism {
		get {
			return neuroticism;
		}
			
		set {
			neuroticism = value;
		}
	}
		
	[Obsolete("Use constructor with proper parameters!")]
	public Personality ()
	{
	}
		
	public Personality (float Openness, float Conscientiousness, float Extraversion, float Agreeableness, float Neuroticism)
	{
		this.Openness = Openness;
		this.Conscientiousness = Conscientiousness;
		this.Extraversion = Extraversion;
		this.Agreeableness = Agreeableness;
		this.Neuroticism = Neuroticism;
	}
}
