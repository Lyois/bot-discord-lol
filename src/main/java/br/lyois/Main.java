package br.lyois;

import br.lyois.commands.ShowTournaments;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class Main {

    public static JDA jda;

    public static void main(String[] args) {

        //Start bot
        jda = JDABuilder.create("",
                EnumSet.allOf(GatewayIntent.class))
                .build();

        jda.addEventListener(new ShowTournaments());
    }
}