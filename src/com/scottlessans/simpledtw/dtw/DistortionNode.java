package com.scottlessans.simpledtw.dtw;

/**
 * If i,j == 0 set bottom as initial value
 *
 * User: slessans
 * Date: 2/16/14
 * Time: 7:22 PM
 */
public class DistortionNode {

    protected int i;
    protected int j;
    protected Double left;
    protected Double bottom;
    protected Double bottomLeft;

    public DistortionNode(int i, int j) {
        this.i = i;
        this.j = j;
        this.left = null;
        this.bottom = null;
        this.bottomLeft = null;
    }

    public double getValue() {

        if (this.i == 0) {
            // should only have bottom
            assert (this.left != null);
            return this.left;
        }

        if (this.j == 0) {
            // should only have left
            assert (this.bottom != null);
            return this.bottom;
        }

        assert (this.left != null && this.bottom != null && this.bottomLeft != null);

        return Math.min(Math.min(this.left, this.bottom), this.bottomLeft);
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setBottom(double bottom) {
        this.bottom = bottom;
    }

    public void setBottomLeft(double bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        boolean empty = true;

        if (this.left != null) {
            empty = false;
            builder.append("L: ")
                    .append(this.left);
        }

        if (this.bottom != null) {
            if (!empty) builder.append(" ");
            empty = false;
            builder.append("B: ")
                    .append(this.bottom);
        }

        if (this.bottomLeft != null) {
            if (!empty) builder.append(" ");
            builder.append("BL: ")
                    .append(this.bottomLeft);
        }

        return builder.toString();
    }
}
