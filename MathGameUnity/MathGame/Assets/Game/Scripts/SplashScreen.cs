using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class SplashScreen : MonoBehaviour {

	// Use this for initialization
	void Start () 
    {
        StartCoroutine("Delay"); 
       
        Debug.Log("Schene loaded");
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    IEnumerator Delay()
    {
        yield return new WaitForSeconds(1);
        SceneManager.LoadScene(1);
    }
}
