import java.awt.*;

/**
 * Created by alexsanchez on 2017-01-10.
 */
public class PromotionMove extends Move {

    private Piece promotionPiece;
    private PieceType promotionType = PieceType.QUEEN; //default value; needed

    public PromotionMove(Pawn pawn, Square initSquare, Square finalSquare) {
        super(pawn, initSquare, finalSquare);
    }

    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
    }
    public PieceType getPromotionType() {
        return promotionType;
    }

    public void setPromotionPiece(Piece promotionPiece) {
        this.promotionPiece = promotionPiece;
    }
    public Piece getPromotionPiece() {
        return promotionPiece;
    }
}
