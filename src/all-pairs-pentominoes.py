# Write DLXMain input files for each pair of
# pentominoes to solve for all ways to tile
# an NxN grid with those pentominoes.

import argparse
from polyominoes import *

parser = argparse.ArgumentParser(description = "Outputs DLX matrices for tiling problems involving pairs of pentominoes.")
parser.add_argument("width", type=int)
parser.add_argument("height", type=int)
parser.add_argument("firstcount", type=int, help = "How many of the first pentomino to use.")
parser.add_argument("secondcount", type=int, help = "How many of the second pentomino to use.")
parser.add_argument("-p", "--stdout", help = "Print to stdout instead of files", action = "store_true")
args = parser.parse_args()

gridwidth = args.width
gridheight = args.height
firstcount = args.firstcount
secondcount = args.secondcount

if gridwidth * gridheight != 5 * (firstcount + secondcount):
    sys.stderr.write("Warning: the size of the grid does not match the number of pentominoes.\n")

names = "FILNPTUVWXYZ"
for firstpent in names:
    for secondpent in names:
        if secondpent == firstpent: continue
        if args.stdout:
            fout = sys.stdout
        else:
            filename = '%d%s-%d%s-%dx%d.dlx' % (firstcount, firstpent, secondcount, secondpent, gridwidth, gridheight)
            fout = open(filename, 'w')
        if fout:
            # write the dimensions of the matrix
            numcols = gridwidth * gridheight + secondcount
            numrows = 1
            fout.write('%d 0 %d\n' % (numcols, numrows))
            # use cell coords as the column headers
            for row in range(gridheight):
                for col in range(gridwidth):
                    fout.write('(%d,%d) ' % (row, col))
            # use the name of the second pentomino as the header for its extra columns
            fout.write((secondpent + ' ') * secondcount)
            fout.write('\n')
            # write one row for each orientation and position of the first pentomino
            b = pentominoes[firstpent]
            post = [0] * secondcount
            for side in range(2):
                for rotation in range(4):
                    printallmatrixrows(b, gridwidth, gridheight, out = fout, postcols = post)
                    b = polyrotate90(b)
                b = polyreflectXY(b)
            # write secondcount rows for each orientation and position of the second pentomino, each with a unique column '1'
            # this is a clunky way of requiring the solution to have secondcount copies of the second pentomino
            # (is not necessary to do the same for the first because the correct number of copies will be selected by filling the grid)
            b = pentominoes[secondpent]
            for side in range(2):
                for rotation in range(4):
                    for i in range(secondcount):
                        post = [0] * secondcount
                        post[i] = 1
                        printallmatrixrows(b, gridwidth, gridheight, out = fout, postcols = post)
                    b = polyrotate90(b)
                b = polyreflectXY(b)
            if not args.stdout:
                fout.close()
