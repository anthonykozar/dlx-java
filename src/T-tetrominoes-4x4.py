# Write an input file for DLXMain that
# describes the problem of how to find all 
# ways to tile a 4x4 grid with T tetrominoes.

from polyominoes import *

fout = open('T-tetrominoes-4x4.txt', 'w')
if fout:
    # write the dimensions of the matrix
    fout.write('16 0 24\n') # 16 cols, 24 rows
    # use cell coords as the column headers
    for row in range(4):
        for col in range(4):
            fout.write('(%d,%d) ' % (row, col))
    fout.write('\n')
    # write one row for each orientation and position of a T tetromino
    b = tetrominoes[2]
    for i in range(4):
        printallmatrixrows(b, 4, 4, out = fout)
        b = polyrotate90(b)
    fout.close()
