# Write an input file for DLXMain that
# describes the problem of how to find all 
# ways to tile a 4x4 grid with L tetrominoes.

from polyominoes import *

fout = open('L-tetrominoes-4x4.txt', 'w')
if fout:
    # write the dimensions of the matrix
    fout.write('16 0 48\n') # 16 cols, 48 rows
    # use cell coords as the column headers
    for row in range(4):
        for col in range(4):
            fout.write('(%d,%d) ' % (row, col))
    fout.write('\n')
    # write one row for each orientation and position of an L tetromino
    b = tetrominoes[1]
    for side in range(2):
        for rotation in range(4):
            printallmatrixrows(b, 4, 4, out = fout)
            b = polyrotate90(b)
        b = polyreflectXY(b)
    fout.close()
