# Write an input file for DLXMain that describes the problem 
# of how to find all ways to tile a 4x4 grid with dominoes.

from polyominos import *

fout = open('dominos-4x4.txt', 'w')
if fout:
    # write the dimensions of the matrix
    fout.write('16 0 24\n') # 16 cols, 24 rows
    # use cell coords as the column headers
    for row in range(4):
        for col in range(4):
            fout.write('(%d,%d) ' % (row, col))
    fout.write('\n')
    # write one row for each orientation and position of a domino
    printallmatrixrows(domino, 4, 4, out = fout)
    printallmatrixrows(vdomino, 4, 4, out = fout)
    fout.close()
