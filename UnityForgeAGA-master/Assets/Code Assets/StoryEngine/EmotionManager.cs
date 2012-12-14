using UnityEngine;
using WorldEngine.Items;
using System.Collections;
using System.Collections.Generic; 
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;


public class EmotionManager : MonoBehaviour {
	List<string> charNames;
	GameObject[] minions;
	GameObject[] friends;
	GameObject villain; 
	GameObject sidekick;
	byte[] mess;
	TcpClient client;
	NetworkStream stream;
	StreamReader reader;
	StreamWriter writer;
	
	// Use this for initialization
	void Start () {
//	Debug.Log ("STARTING CLIENT");
//	client = new TcpClient("localhost",2013);
//	stream = client.GetStream();
//	reader = new StreamReader(stream);
//	writer = new StreamWriter(stream);
	}
	
	// Update is called once per frame
	void Update () {
	}
	
	string checkWorldState(){
	minions = GameObject.FindGameObjectsWithTag("Minion");
	friends = GameObject.FindGameObjectsWithTag("Friend");	
	villain = GameObject.FindGameObjectWithTag("Villain");
	sidekick = GameObject.FindGameObjectWithTag ("Sidekick");
	Debug.Log ("Minions="+minions.Length+" Friends="+friends.Length);
		string str = "";
		Debug.Log ("Minions="+minions.Length+" Friends="+friends.Length);
		for(int i = 0; i<minions.Length; i++){
			if (minions[i].GetComponent<CharacterScript>().Dead){
				str+="0";
			}
			else{
				str+="1";
			}
		}
			for(int i = 0; i<friends.Length; i++){
			if (friends[i].GetComponent<CharacterScript>().Dead){
				str+="0";
			}
			else{
				str+="1";
			}
		}
		Debug.Log ("STRING TO SEND="+str);
		return str;
	}
	
	public void UpdateEmotionalModel(){
		Debug.Log ("Algo paso y tengo que procesar el estado del mundo!");
		string s = checkWorldState()+"1";
		//writeSocket (s);
		//string toInterpret = readSocket ();
		//float[] v = StringToFloatArr(toInterpret);
		villain.GetComponent<EmotionModel>().CurrentAppraisal = GenerateRandomAppraisal();
	}
	
	public Appraisal GenerateRandomAppraisal ()
	{
		//int choice = Random.Range(0, 5);
		
		//TODO REMOVE TEST CODE
		int choice = 0;
		
		Appraisal CurrentAppraisal = new Appraisal();
		Appraisal[] AppraisalOptions = new Appraisal[] {
			new Appraisal(-0.5f, -0.5f, 0.0f, 1.0f, 0), //Anger
			new Appraisal(-0.5f, 0.0f, 0.0f, 0.5f, 0), //Fear
			new Appraisal(-0.5f, -0.5f, 0.0f, 0.5f, 0), //Anger + Fear
			new Appraisal(0.0f, -0.5f, 0.0f, 1.0f, 1), //Shame
			new Appraisal(-0.5f, 0.0f, 0.0f, 0.0f, 0)}; //Relief
		
		CurrentAppraisal = AppraisalOptions[choice];
		
		string ListofEmotions = "";
		
		foreach(Emotion e in AppraisalOptions[choice].CurrentEmotions)
		{
			ListofEmotions += e.ToString() + ", ";
		}
		
		Debug.Log ("Generated Random Appraisal:\nEmotions: " + ListofEmotions + "D: " + AppraisalOptions[choice].Desirability 
			+ ", A: " + AppraisalOptions[choice].Appealingness + ", P: " + AppraisalOptions[choice].Praiseworthiness + ", L: " + AppraisalOptions[choice].Likelihood 
			+ ", CA: " + AppraisalOptions[choice].CausalAgent);
		
		return CurrentAppraisal;
	}
	
//	public void writeSocket(string s){
//		writer.WriteLine (s);
//		writer.Flush ();
//	}
	
	public float[] StringToFloatArr(string s){
		string[] s1 = s.Split(new char[] {','});
		float[] f1 = new float[s1.Length];
		for(int i = 0; i < f1.Length; i++){
			f1[i] = float.Parse(s1[i]);
		}
		return f1;
	}
//	public string readSocket(){ 
//		
//		mess = new byte[256];
//		this.stream.Read(mess,0,256);
//		Debug.Log ("READ:"+System.Text.Encoding.UTF8.GetString(mess));
//		string message = System.Text.Encoding.UTF8.GetString(mess);
//		return message;
//	}
	
	void OnApplicationQuit(){
		//writer.WriteLine ("bye");
		//writer.Flush ();	
		//stream.Close();
		//writer.Close();
		//reader.Close ();
	}
	byte[] GetBytes(string str){
    byte[] bytes = new byte[str.Length * sizeof(char)];
    System.Buffer.BlockCopy(str.ToCharArray(), 0, bytes, 0, bytes.Length);
    return bytes;
	}

	string GetString(byte[] bytes){
    char[] chars = new char[bytes.Length / sizeof(char)];
    System.Buffer.BlockCopy(bytes, 0, chars, 0, bytes.Length);
    return new string(chars);
	}
}
