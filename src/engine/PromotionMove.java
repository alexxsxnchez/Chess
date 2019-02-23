/**
 * Created by alexsanchez on 2017-01-10.
 */

package engine;

public class PromotionMove extends Move {

    private PieceType promotionType = PieceType.QUEEN; //default value; needed

    public PromotionMove(Pawn pawn, PieceLocation initLocation, PieceLocation finalLocation) {
        super(pawn, initLocation, finalLocation);
    }

    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
    }

    public PieceType getPromotionType() {
        return promotionType;
    }

}
