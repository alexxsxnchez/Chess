/**
 * Created by alexsanchez on 2017-01-19.
 */
public enum GameType {
    HUMAN_VS_HUMAN,
    HUMAN_VS_COMPUTER,
    COMPUTER_VS_COMPUTER;

    public static GameType getGameType(PlayerType player1, PlayerType player2) {
        if(player1 != player2) {
            return HUMAN_VS_COMPUTER;
        }
        if(player1 == PlayerType.HUMAN) {
            return HUMAN_VS_HUMAN;
        }
        return COMPUTER_VS_COMPUTER;
    }

}
