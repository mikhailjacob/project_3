using UnityEngine;

public class EmotionModel : MonoBehaviour
{
	private Personality agentPersonality;
		
	public Personality AgentPersonality {
		get {
			return agentPersonality;
		}
			
		set {
			agentPersonality = value;
		}
	}
		
	private MoodModel currentMoods;
		
	public MoodModel CurrentMoods {
		get {
			return currentMoods;
		}
			
		set {
			currentMoods = value;
		}
	}
		
	private Appraisal currentAppraisal;
		
	public Appraisal CurrentAppraisal {
		get {
			return currentAppraisal;
		}
			
		set {
			currentAppraisal = value;
			isCurrentAppraisalDirty = true;
		}
	}
		
	bool isCurrentAppraisalDirty;
	System.DateTime CheckTime;
	System.DateTime CheckTimeDecay;
		
	public void Start ()
	{
		this.isCurrentAppraisalDirty = false;
			
		//Get personality values. Create Personality Object
		this.AgentPersonality = new Personality (0.4f, 0.8f, 0.6f, 0.3f, 0.4f);
			
		//Create MoodModel with personality
		this.CurrentMoods = new MoodModel (AgentPersonality);
			
		CheckTime = System.DateTime.Now;
		CheckTimeDecay = System.DateTime.Now;
	}
		
	public void Update ()
	{
			
	}
		
	public void FixedUpdate ()
	{
		//Check for CurrentAppraisal Dirty
		if (isCurrentAppraisalDirty) {
			
			if (this.CurrentAppraisal == null) {
				Debug.Log ("ERROR: NULL APPRAISAL OBJECT");
			}
			
			//Update Mood Model with new Appraisal
			this.CurrentMoods.CalculateCurrentMood (this.CurrentAppraisal);
			this.isCurrentAppraisalDirty = false;
		}
			
		if ((System.DateTime.Now - CheckTime) > new System.TimeSpan (0, 0, 5)) {
			//GenerateRandomAppraisal ();
				
			CheckTime = System.DateTime.Now;
		} else if ((System.DateTime.Now - CheckTimeDecay) > new System.TimeSpan (0, 0, 1)) {
			//Else Update with null to Decay Mood
			//this.CurrentMoods.CalculateCurrentMood (null);
			
			CheckTimeDecay = System.DateTime.Now;
		}
	}
		
	public void GenerateCompletelyRandomAppraisal ()
	{
		Appraisal RandomAppraisal = new Appraisal (Random.Range (-1.0f, 1.0f), 
				Random.Range (-1.0f, 1.0f), 
				Random.Range (-1.0f, 1.0f), 
				Random.Range (0.0f, 1.01f), 
				Random.Range (0, 2));
		
		//Appraisal RandomAppraisal = new Appraisal ( -0.5f, 0.0f, 0.0f, 0.0f, 0);
			
		this.CurrentAppraisal = RandomAppraisal;
		this.isCurrentAppraisalDirty = true;
		
		string ListofEmotions = "";
		
		foreach(Emotion e in RandomAppraisal.CurrentEmotions)
		{
			ListofEmotions += e.ToString() + ", ";
		}
		
		Debug.Log ("Generated Random Appraisal:\nEmotions: " + ListofEmotions + "D: " + RandomAppraisal.Desirability 
			+ ", A: " + RandomAppraisal.Appealingness + ", P: " + RandomAppraisal.Praiseworthiness + ", L: " + RandomAppraisal.Likelihood 
			+ ", CA: " + RandomAppraisal.CausalAgent);
	}
	
	public void GenerateRandomAppraisal ()
	{
		int choice = Random.Range(0, 5);
		
		Appraisal[] AppraisalOptions = new Appraisal[] {
			new Appraisal(-0.5f, -0.5f, 0.0f, 1.0f, 0), //Anger
			new Appraisal(-0.5f, 0.0f, 0.0f, 0.5f, 0), //Fear
			new Appraisal(-0.5f, -0.5f, 0.0f, 0.5f, 0), //Anger + Fear
			new Appraisal(0.0f, -0.5f, 0.0f, 1.0f, 1), //Shame
			new Appraisal(-0.5f, 0.0f, 0.0f, 0.0f, 0)}; //Relief
		
		this.CurrentAppraisal = AppraisalOptions[choice];
		this.isCurrentAppraisalDirty = true;
		
		string ListofEmotions = "";
		
		foreach(Emotion e in AppraisalOptions[choice].CurrentEmotions)
		{
			ListofEmotions += e.ToString() + ", ";
		}
		
		Debug.Log ("Generated Random Appraisal:\nEmotions: " + ListofEmotions + "D: " + AppraisalOptions[choice].Desirability 
			+ ", A: " + AppraisalOptions[choice].Appealingness + ", P: " + AppraisalOptions[choice].Praiseworthiness + ", L: " + AppraisalOptions[choice].Likelihood 
			+ ", CA: " + AppraisalOptions[choice].CausalAgent);
	}
}
