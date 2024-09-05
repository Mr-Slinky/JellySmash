package com.slinky.jellysmash.model.physics.comps;

/**
 * A grid of Vector2D objects that represent points in a matrix, arranged into
 * rows and columns. Currently created for me to play around with vectors and
 * the Math involved with vectors. Not likely to be used in the game; or at
 * least no plans exist for that as of yet.
 *
 * Visualised as a collection of arrows point from the top-left (if working with
 * this Matrix in the context of screen space) or top-bottom if working in the
 * context of a normal Cartesian plane.
 *
 * @author Kheagen Haskins
 */
public class Matrix2D extends VectorCollection {

    // ============================== Static ================================ //
    // ============================== Fields ================================ //
    private final int rows;
    private final int cols;
    private final int unitSize;

    private final Vector2D[][] vectors;

    // =========================== Constructors ============================= //
    public Matrix2D(int rows, int cols, int unitSize) {
        this.rows = rows;
        this.cols = cols;
        this.unitSize = unitSize;

        this.vectors = new Vector2D[rows][cols];
        configureGrid();
    }

    public Matrix2D(int rows, int cols) {
        this(rows, cols, 1);
    }

    // ============================== Getters =============================== //
    public Vector2D[][] getVectors() {
        return vectors;
    }

    // ============================== Setters =============================== //
    // ============================ API Methods ============================= //
    public void translate(Vector2D vector) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                vectors[r][c].add(vector);
            }
        }
    }

    public void translate(int x, int y) {
        translate(new Vector2D(x, y));
    }
    
    public void scale(double scalar) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                vectors[r][c].scale(scalar);
            }
        }
    }

    // ========================== Helper Methods ============================ //
    private void configureGrid() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                vectors[r][c] = new Vector2D(r * unitSize, c * unitSize);
            }
        }

    }

}
