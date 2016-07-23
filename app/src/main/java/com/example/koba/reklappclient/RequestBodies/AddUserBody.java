package com.example.koba.reklappclient.RequestBodies;

/**
 * Created by Koba on 23/07/2016.
 */
public class AddUserBody {

    private String problem;

    public AddUserBody(String problem) {
        this.problem = problem;
    }

    public String getProblem() {
        return problem;
    }

}
