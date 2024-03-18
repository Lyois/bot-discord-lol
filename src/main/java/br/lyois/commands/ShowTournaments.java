package br.lyois.commands;

import br.lyois.Teams;
import br.lyois.Tournaments;
import br.lyois.config.HttpConnection;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowTournaments extends ListenerAdapter {

    private static final String lolIcon = "http://www.rw-designer.com/icon-image/21516-256x256x32.png";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        var tama = args.length;
        var textChannel = event.getChannel();

        if (args[0].equalsIgnoreCase("!" + "lol")){
            if(tama == 1){
                textChannel.sendMessageEmbeds(listTournaments().build()).queue();
            } else if (tama == 2) {
                textChannel.sendMessageEmbeds(listTeams(args[1]).build()).queue();
            } else if (tama == 3) {
                textChannel.sendMessageEmbeds(viewerTeam(args[1], args[2]).build()).queue();
            }
        }
    }

    public static EmbedBuilder listTournaments(){
        var connection = HttpConnection.connection("http://localhost:8080/tournaments");
        var gson = new GsonBuilder().create();
        var jsonArray = gson.fromJson(connection.body(), JsonArray.class);
        List<String> listTournaments = new ArrayList<String>();
        List<Tournaments> tournamets = new ArrayList<Tournaments>();
        for (JsonElement element : jsonArray) {
            tournamets.add(gson.fromJson(element, Tournaments.class));
        }
        for (Tournaments tournamet : tournamets) {
            listTournaments.add(String.format("**%s**", tournamet.name()) + " - " + tournamet.tournamentId());
        }
        var listTournamentsFinal = String.join("\n", listTournaments);

        var eb = new EmbedBuilder();
        eb.setAuthor("Campeonatos de League of Legends");
        eb.addField("Exemplos de comando", "!lol (tag do campeonato escolhido)", false);
        eb.addField("Campeonatos", listTournamentsFinal, false);
        eb.setThumbnail(lolIcon);
        eb.setFooter("© League of Legends", lolIcon);
        return eb;
    }

    public static EmbedBuilder listTeams (String tournamentId){
        var connectionTeams = HttpConnection.connection("http://localhost:8080/teams/" + tournamentId);
        var connectionTournament = HttpConnection.connection("http://localhost:8080/tournaments/" + tournamentId);
        var gson = new GsonBuilder().create();
        var jsonArray = gson.fromJson(connectionTeams.body(), JsonArray.class);
        var tournament = gson.fromJson(connectionTournament.body(), Tournaments.class);
        List<String> listTeams = new ArrayList<>();
        List<Teams> teams = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            teams.add(gson.fromJson(element, Teams.class));
        }
        for (Teams team : teams) {
            listTeams.add(String.format("**%s**", team.name()) + " - " + team.tag());
        }
        var listTeamsFinal = String.join("\n", listTeams);

        var eb = new EmbedBuilder();
        eb.setAuthor(tournament.name());
        eb.addField("Exemplos de comando", "!lol !" + tournament.tournamentId().toLowerCase() + " {tag do time escolhido}", false);
        eb.addField("Times", listTeamsFinal, false);
        eb.setThumbnail(tournament.iconUrl());
        eb.setFooter("© " + tournament.name(), tournament.iconUrl());
        return eb;
    }

    public static EmbedBuilder viewerTeam(String tournamentId, String teamTag){
        var connetionTeam = HttpConnection.connection("http://localhost:8080/teams/" + tournamentId + "/" + teamTag);
        var gson = new GsonBuilder().create();
        var team = gson.fromJson(connetionTeam.body(), Teams.class);

        var eb = new EmbedBuilder();
        eb.setAuthor(team.name());
        eb.addField("Wins", String.valueOf(team.wins()), true);
        eb.addField("Lose", String.valueOf(team.loses()), true);
        eb.addField("Win rate", String.valueOf(team.winPercent()) + "%", true);
        System.out.println(team.urlIcon());
        eb.setThumbnail(team.urlIcon());
        eb.setFooter("© " + team.name(), lolIcon);
        return eb;
    }
}
