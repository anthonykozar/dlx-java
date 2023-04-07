# Write one or more input files for DLXMain 
# that describe the problems of how to find 
# all ways to tile an NxN grid with a single 
# pentomino.

import argparse
from polyominos import *

parser = argparse.ArgumentParser(description = "Outputs DLX matrices for one or more tiling problems involving a single pentomino.")
parser.add_argument("width", type=int)
parser.add_argument("height", type=int)
parser.add_argument("pentominoes")
parser.add_argument("-a", "--all", help = "Use all 12 pentominoes", action = "store_true")
parser.add_argument("-p", "--stdout", help = "Print to stdout instead of files", action = "store_true")
args = parser.parse_args()

gridwidth = args.width
gridheight = args.height
if args.all:
    names = "FILNPTUVWXYZ"
else:
    names = args.pentominoes

for name in names:
    if args.stdout:
        fout = sys.stdout
    else:
        filename = name + '-pentominoes-%dx%d.txt' % (gridwidth, gridheight)
        fout = open(filename, 'w')
    if fout:
        # write the dimensions of the matrix
        numcols = gridwidth * gridheight
        numrows = 10
        fout.write('%d 0 %d\n' % (numcols, numrows))
        # use cell coords as the column headers
        for row in range(gridheight):
            for col in range(gridwidth):
                fout.write('(%d,%d) ' % (row, col))
        fout.write('\n')
        # write one row for each orientation and position of an L tetromino
        b = pentominoes[name]
        for side in range(2):
            for rotation in range(4):
                printallmatrixrows(b, gridwidth, gridheight, out = fout)
                b = polyrotate90(b)
            b = polyreflectXY(b)
        if not args.stdout:
            fout.close()
