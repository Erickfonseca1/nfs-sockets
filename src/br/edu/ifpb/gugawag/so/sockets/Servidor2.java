package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor2 {

    private static List<Arquivo> arquivos = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor ==");

        try (ServerSocket serverSocket = new ServerSocket(7001);
            Socket socket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            while (true) {
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                String comando = dis.readUTF();
                String[] partes = comando.split(" ");

                if (socket.isConnected() && !socket.isClosed()) {
                    switch (partes[0]) {
                        case "readdir":
                        if (arquivos.isEmpty()) {
                            dos.writeUTF("Diretório vazio");
                        } else {
                            StringBuilder sb = new StringBuilder();

                            for (Arquivo arquivo : arquivos) {
                                sb.append(arquivo.getNome()).append("\n");
                            }

                            dos.writeUTF(sb.toString());
                        }
                        break;
                        case "rename":
                            for (Arquivo arquivo : arquivos) {
                                if (arquivo.getNome().equals(partes[1])) {
                                    arquivo.setNome(partes[2]);
                                    dos.writeUTF("Arquivo renomeado com sucesso");
                                } else {
                                    dos.writeUTF("Arquivo não encontrado");
                                }
                            }
                            break;
                        case "create":
                            arquivos.add(new Arquivo(partes[1]));
                            dos.writeUTF("Arquivo criado com sucesso");
                            break;
                        case "remove":
                            arquivos.removeIf(arquivo -> arquivo.getNome().equals(partes[1]));
                            dos.writeUTF("Arquivo removido com sucesso");
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }

    }
 }
