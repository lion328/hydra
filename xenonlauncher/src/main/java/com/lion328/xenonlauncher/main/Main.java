package com.lion328.xenonlauncher.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.launcher.BasicLauncher;
import com.lion328.xenonlauncher.launcher.Launcher;
import com.lion328.xenonlauncher.launcher.ui.CLILauncherUI;
import com.lion328.xenonlauncher.launcher.ui.LauncherUI;
import com.lion328.xenonlauncher.minecraft.api.authentication.MinecraftAuthenticator;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.YggdrasilMinecraftAuthenticator;
import com.lion328.xenonlauncher.minecraft.downloader.MinecraftDownloader;
import com.lion328.xenonlauncher.minecraft.downloader.Repositories;
import com.lion328.xenonlauncher.minecraft.launcher.GameLauncher;
import com.lion328.xenonlauncher.minecraft.launcher.json.JSONGameLauncher;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.type.DependencyNameTypeAdapter;
import com.lion328.xenonlauncher.patcher.FilePatcher;
import com.lion328.xenonlauncher.patcher.HttpsProtocolPatcher;
import com.lion328.xenonlauncher.proxy.HttpDataHandler;
import com.lion328.xenonlauncher.proxy.ProxyServer;
import com.lion328.xenonlauncher.proxy.StreamDataHandler;
import com.lion328.xenonlauncher.proxy.socks5.SOCKS5ProxyServer;
import com.lion328.xenonlauncher.settings.LauncherConstant;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main
{

    public static void main(String[] args)
    {
        boolean gui = true;
        boolean server = false;

        for (String s : args)
        {
            switch (s.toLowerCase())
            {
                case "nogui":
                    gui = false;
                    break;
                case "server":
                    server = true;
                    break;
            }
        }

        if (server)
        {
            gui = false;
        }

        try
        {
            test(args);
        }
        catch (Exception e)
        {
            LauncherConstant.LOGGER.catching(e);
        }
    }

    public static String getLauncherVersion()
    {
        return LauncherConstant.VERSION;
    }

    public static void test(String[] args) throws Exception
    {
        final ServerSocket server = new ServerSocket(35565);
        final ProxyServer proxy = new SOCKS5ProxyServer();
        proxy.addDataHandler(Integer.MAX_VALUE, new StreamDataHandler()
        {

            private int count = 0;

            @Override
            public boolean process(Socket a, Socket b) throws IOException
            {
                int i = count++;
                System.out.println("Streaming #" + i);
                boolean out = super.process(a, b);
                System.out.println("Disconnected #" + i);
                count--;
                return out;
            }
        });
        HttpDataHandler httpHandler = new HttpDataHandler();
        httpHandler.addHttpRequestHandler(-1, new HttpRequestHandler()
        {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException
            {
                String host = request.getHeaders("Host")[0].getValue();
                String uri = request.getRequestLine().getUri();
                String path = host + uri;
                String completeUri = "http://" + path;
                System.out.println(completeUri);
            }
        });
        httpHandler.addHttpRequestHandler(0, new HttpRequestHandler()
        {

            @Override
            public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException
            {
                String host = httpRequest.getHeaders("Host")[0].getValue();
                String uri = httpRequest.getRequestLine().getUri();
                String path = host + uri;
                String completeUri = "http://" + path;

                if (!host.equals("sessionserver.mojang.com"))
                {
                    return;
                }

                final Socket serverConnection = SSLSocketFactory.getDefault().createSocket(host, 443);
                final Socket clientConnection = (Socket) httpContext.getAttribute("client.socket");

                clientConnection.getInputStream().reset();

                System.out.println("Start streaming " + completeUri);

                System.out.println("Finish streamming " + completeUri);

                httpContext.setAttribute("response.sent", true);
                httpContext.setAttribute("pipeline.end", true);
            }
        });
        httpHandler.addHttpRequestHandler(Integer.MAX_VALUE, HttpDataHandler.STREAM_HANDLER);
        proxy.addDataHandler(50, httpHandler);
        //System.exit(0);

        MinecraftAuthenticator authenticator;
        authenticator = new YggdrasilMinecraftAuthenticator();
        //authenticator.login(System.console().readLine("User: "), System.console().readPassword("Password: "));

        File basepath = new File("/home/lion328/mc2");
        String id = "1.8.9";

        Downloader mcDownloader = new MinecraftDownloader(id, basepath, Repositories.getRepository(), null);

        File versions = new File(basepath, "versions");
        File libraries = new File(basepath, "libraries");

        File jsonFile = new File(versions, id + "/" + id + ".json");

        System.out.println(jsonFile.getAbsolutePath());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DependencyName.class, new DependencyNameTypeAdapter());
        Gson gson = gsonBuilder.create();
        GameVersion version = gson.fromJson(new FileReader(jsonFile), GameVersion.class);
        //GameVersion parent = gson.fromJson(new FileReader(new File(versions, "1.8.9/1.8.9.json")), GameVersion.class);
        //version = new MergedGameVersion(version, parent);

        GameLauncher launcher = new JSONGameLauncher(version, basepath);

        //launcher.addJVMArgument("-Dlog4j.configuration=/home/lion328/mc2/log4j.xml");
        launcher.addJVMArgument("-DsocksProxyHost=127.0.0.1");
        launcher.addJVMArgument("-DsocksProxyPort=35565");

        //FilePatcher patcher = new StringReplaceFilePatcher("https://sessionserver.mojang.com", "http://127.0.0.1/~lion328/mcwebserver/api");
        FilePatcher patcher = new HttpsProtocolPatcher("http");
        final DependencyName regex = new DependencyName("com\\.mojang:authlib:.*");
        launcher.addPatcher(regex, patcher);

        LauncherUI ui = new CLILauncherUI();
        Launcher l = new BasicLauncher(authenticator, mcDownloader, launcher);

        l.setLauncherUI(ui);
        l.start();

        //td.join();

        //System.exit(0);

        Runtime.getRuntime().addShutdownHook(new Thread()
        {

            @Override
            public void run()
            {
                proxy.stop();
            }
        });

        System.out.println();
        System.out.println("Proxy broadcast at " + server.getInetAddress().getHostAddress() + ":" + server.getLocalPort());

        proxy.start(server);
    }
}