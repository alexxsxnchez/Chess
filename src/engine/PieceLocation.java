/**
 * Created by alexsanchez on 2019-02-22.
 */

package engine;
import java.util.Objects;

public class PieceLocation {
    private int x;
    private int y;

    public PieceLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) {
            return true;
        }
        if(!(other instanceof PieceLocation)) {
            return false;
        }
        PieceLocation location = (PieceLocation) other;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
