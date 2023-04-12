import sys

class Polyomino(object):
    
    def __init__(self, polystr, cellch = 'x', minimizecoords = True):
        self.cells = []
        row = 0
        col = 0
        lines = polystr.splitlines()
        # skip initial blank lines
        skip = 0
        while lines[skip] == '': skip += 1
        # scan lines for cellch
        for ln in lines[skip:]:
            col = 0
            for c in ln:
                if c == cellch:
                    self.cells.append((col,row))
                col += 1
            row += 1
        if minimizecoords:
            # shift coordinates so that the minimum x and y values are both zero
            col = lambda e: e[0]
            row = lambda e: e[1]
            minx = min(map(col, self.cells))
            miny = min(map(row, self.cells))
            if minx != 0 or miny != 0:
                self.cells = [(x-minx, y-miny) for (x,y) in self.cells]

    def __str__(self):
        return str(self.cells)

    def minmax(a, fieldfunc):
        low = fieldfunc(a[0])
        high = low
        for e in a:
            val = fieldfunc(e)
            if val < low:
                low = val
            elif val > high:
                high = val
        return (low, high)

    def polydims(self, minmax = minmax):
        col = lambda e: e[0]
        row = lambda e: e[1]
        colmin, colmax = minmax(self.cells, col)
        rowmin, rowmax = minmax(self.cells, row)
        return (colmax - colmin + 1, rowmax - rowmin + 1)

    def polyshift(self, xoffset, yoffset):
        return [(x+xoffset, y+yoffset) for (x,y) in self.cells]

    # rotate p 90 degrees clockwise
    def polyrotate90(self, minmax = minmax):
        row = lambda e: e[1]
        rowmin, rowmax = minmax(self.cells, row)
        xoffset = rowmax + rowmin
        return [(xoffset-y, x) for (x,y) in self.cells]

    # reflect p in the XY diagonal (y=x)
    def polyreflectXY(self):
        return [(y,x) for (x,y) in self.cells]

    def polyprint(self, gridwidth = None, gridheight = None, out = sys.stdout):
        cols = max(map(lambda e: e[0], self.cells)) + 1
        rows = max(map(lambda e: e[1], self.cells)) + 1
        if not gridheight:
            gridheight = rows
        if not gridwidth:
            gridwidth = cols
        for y in xrange(gridheight):
            for x in xrange(gridwidth):
                if (x,y) in self.cells:
                    out.write('x')
                else:
                    out.write('.')
            out.write('\n')

    def printallpositions(self, gridwidth, gridheight, polydims = polydims, polyprint = polyprint, polyshift = polyshift):
        cols, rows = self.polydims()
        for yoffset in range(gridheight-rows+1):
            for xoffset in range(gridwidth-cols+1):
                self.polyshift(xoffset, yoffset).polyprint(gridwidth, gridheight)
                print ' '

    def polyprintmatrixrow(self, gridwidth, gridheight, precols = [], postcols = [], out = sys.stdout):
        if precols != []:
            prestr = ' '.join(map(str, precols)) + ' '
            out.write(prestr)
        for y in xrange(gridheight):
            for x in xrange(gridwidth):
                if (x,y) in self.cells:
                    out.write('1 ')
                else:
                    out.write('0 ')
        if postcols != []:
            poststr = ' '.join(map(str, postcols))
            out.write(poststr)
        out.write('\n')

    def printallmatrixrows(self, gridwidth, gridheight, precols = [], postcols = [], out = sys.stdout, polydims = polydims, polyshift = polyshift, polyprintmatrixrow = polyprintmatrixrow):
        cols, rows = self.polydims()
        for yoffset in range(gridheight-rows+1):
            for xoffset in range(gridwidth-cols+1):
                self.polyshift(xoffset, yoffset).polyprintmatrixrow(gridwidth, gridheight, precols, postcols, out)

domino = Polyomino('xx', 'x')
v = '''
x
x
'''
vdomino = Polyomino(v, 'x')
trominostr = '''
000
11.
1..
'''
tetrominostr = '''
00002
11122
133.2
33.44
...44
'''

pentominostr = '''
IIIII

LLLL
L...

YYYY
.Y..

PPP
PP.

UUU
U.U

VVV
V..
V..

T..
TTT
T..

F..
FFF
.F.

Z..
ZZZ
..Z

.X.
XXX
.X.

W..
WW.
.WW

N..
NN.
.N.
.N.
'''

trominoes = [Polyomino(trominostr, str(d)) for d in range(2)]
tetrominoes = [Polyomino(tetrominostr, str(d)) for d in range(5)]
pentominoes = {c:Polyomino(pentominostr, c) for c in "ILYPUVTFZXWN"}

