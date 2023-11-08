package com.example.remoteclient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler
{
    private static Socket socket;
    private static OutputStream output;
    private static InputStream input;
    private static PrintWriter writer;
    private static BufferedReader reader;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket) throws IOException {
        SocketHandler.socket = socket;
        output = socket.getOutputStream();
        input  = socket.getInputStream();
        writer = new PrintWriter(output, true);
        reader = new BufferedReader(new InputStreamReader(input));

    }
    public static void send_with_size(String data)
    {
        data = Integer.toString(data.length()) + "_" + data;
        while(data.split("_")[0].length()<6)
            data = "0" + data;
        writer.print(data);
        writer.flush();



    }

    public static String read() throws IOException {
        String line = reader.readLine();
        return line;

    }
    public static synchronized void CloseSocket() throws IOException {
        socket.close();
    }
}
