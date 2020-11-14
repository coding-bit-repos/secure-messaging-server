package com.codingbit.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;

public class SMServerRun {

    private static final Logger log = LoggerFactory.getLogger(SMServerRun.class);

    public static void main(String[] args) {
        // create the server
        SMServer smServer = new SMServer(new Module[]{});

        // start the server
        smServer.start();
    }
}
