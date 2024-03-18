package br.lyois;

public record Teams(Long id, String camp, String teamId, String tag, String name, String region, int wins, int loses, double winPercent, double gameDuration, String urlIcon) {
}
