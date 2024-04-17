import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF quf;
    private final WeightedQuickUnionUF quf2; // to solve backwash
    private final int n;
    private final int virtualTop;
    private final int virtualBottom;
    private boolean[] sites; // false--> blocked true-->open
    private boolean isTopOpen; // flag to use in percolation method
    private boolean isBottomOpen; // flag to use in percolation method
    private int count; // numberOfOpenSites

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Invalid grid size");
        this.n = n;
        this.count = 0;
        this.sites = new boolean[(n * n) + 2]; // plus two - VT & VB
        this.quf = new WeightedQuickUnionUF(sites.length);
        this.quf2 = new WeightedQuickUnionUF((n * n) + 1); // plus one - VT (backwash)
        this.virtualBottom = (n * n) + 1;
        this.virtualTop = 0;
        this.isTopOpen = false;
        this.isBottomOpen = false;
    }

    // opens the site (row, col) if it is not open already
    /*
     * abrir um site implica marcar o site como aberto, ligar aos sites adjacentes
     * que estiverem abertos e iterar o contador de sites abertos
     */
    public void open(int row, int col) {
        validateRowCol(row, col);
        if (isOpen(row, col)) // without this instruction, when it is already open it counts anyway
            return;
        int index = xyTo1D(row, col);
        sites[index] = true; // change [] index to true marks site as opened
        connectToAdjacentSites(row, col);
        count++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);
        int index = xyTo1D(row, col);
        return sites[index];
    }

    // is the site (row, col) full?
    /*
     * Um site estar full é o mesmo que dizer se dada posição está ligada ao topo
     * virtual
     */
    public boolean isFull(int row, int col) {
        validateRowCol(row, col);
        int index = xyTo1D(row, col);
        return quf2.find(virtualTop) == quf2.find(index);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        if (!isTopOpen || !isBottomOpen) /*
                                             * TIMMING & MEMORY - if the top or the bottom are not open, returns false
                                             * straight away
                                             */
            return false;
        return quf.find(virtualTop) == quf.find(virtualBottom);
    }

    private void connectToAdjacentSites(int row, int col) {
        int index = xyTo1D(row, col);
        validateRowCol(row, col);
        // top
        if (row != 1 && isOpen(row - 1, col)) {
            quf.union(index, xyTo1D(row - 1, col));
            quf2.union(index, xyTo1D(row - 1, col));
        }
        // connection to virtual top
        else if (row == 1) {
            quf.union(index, virtualTop);
            quf2.union(index, virtualTop);
            isTopOpen = true;

        }
        // bottom
        if (row != n && isOpen(row + 1, col)) {
            quf.union(index, xyTo1D(row + 1, col));
            quf2.union(index, xyTo1D(row + 1, col));
        }
        // connection to virtual bottom
        // não liga o quf2 ao VB para resolver backwash
        else if (row == n) {
            quf.union(index, virtualBottom);
            isBottomOpen = true;
        }
        // left
        if (col != 1 && isOpen(row, col - 1)) {
            quf.union(index, xyTo1D(row, col - 1));
            quf2.union(index, xyTo1D(row, col - 1));
        }
        // right
        if (col != n && isOpen(row, col + 1)) {
            quf.union(index, xyTo1D(row, col + 1));
            quf2.union(index, xyTo1D(row, col + 1));
        }
    }

    // converto os indices do vetor para a grelha n por n
    private int xyTo1D(int row, int col) {
        return (row - 1) * n + col;
    }

    private void validateRowCol(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n)
            throw new IllegalArgumentException("Invalid Arguments");
    }
}
