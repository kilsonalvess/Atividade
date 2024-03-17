package com.gugawag.so.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/**
 * Time-of-day server listening to port 6013.
 *
 * Figure 3.21
 *
 * @author Silberschatz, Gagne, and Galvin. Pequenas alterações feitas por Gustavo Wagner (gugawag@gmail.com)
 * Operating System Concepts  - Ninth Edition
 * Copyright John Wiley & Sons - 2013.
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6013);
            System.out.println("=== Servidor mult iniciado ===");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Servidor recebeu comunicação do ip: " + clientSocket.getInetAddress() + "-" + clientSocket.getPort());

                // Cria uma nova thread para lidar com o cliente
                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Escreve a data atual no socket		
            out.println(new Date().toString() + "-Boa noite alunos!");

            // Lê mensagens do cliente e as imprime no console
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Cliente disse: " + line);
            }

            // fechar o socket e volta no loop para escutar novas conexões
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao lidar com o cliente: " + e.getMessage());
        }
    }
}